/**
 * 修复上下文缺失节点脚本
 * 根据现有数据，自动补充缺失的父级节点
 */

const http = require('http');

const BASE_URL = 'localhost';
const PORT = 8080;
const API_BASE = `http://${BASE_URL}:${PORT}`;

/**
 * 发送HTTP请求
 */
function sendRequest(path, method = 'GET', data = null) {
    return new Promise((resolve, reject) => {
        const url = new URL(path, API_BASE);
        const options = {
            hostname: url.hostname,
            port: url.port,
            path: url.pathname,
            method: method,
            headers: {
                'Content-Type': 'application/json',
            }
        };

        const req = http.request(options, (res) => {
            let responseData = '';
            res.on('data', (chunk) => {
                responseData += chunk;
            });
            res.on('end', () => {
                try {
                    const result = JSON.parse(responseData);
                    resolve(result);
                } catch (e) {
                    reject(new Error(`解析响应失败: ${e.message}, 响应: ${responseData}`));
                }
            });
        });

        req.on('error', (e) => {
            reject(new Error(`请求失败: ${e.message}`));
        });

        if (data) {
            req.write(JSON.stringify(data));
        }
        req.end();
    });
}

/**
 * 分页查询所有上下文数据
 */
async function getAllContextData() {
    console.log('开始查询所有上下文数据...\n');
    const allData = [];
    let pageNo = 1;
    const pageSize = 100; // 每页100条
    
    while (true) {
        const requestData = {
            pageNo: pageNo,
            pageSize: pageSize
        };
        
        try {
            const result = await sendRequest('/system/context/pageList', 'POST', requestData);
            
            if (result.code !== 2000) {
                throw new Error(`查询失败: ${result.msg}`);
            }
            
            const page = result.data;
            const records = page.records || [];
            
            console.log(`第 ${pageNo} 页: 获取到 ${records.length} 条数据`);
            allData.push(...records);
            
            // 如果当前页数据少于pageSize，说明已经是最后一页
            if (records.length < pageSize) {
                break;
            }
            
            pageNo++;
        } catch (error) {
            console.error(`查询第 ${pageNo} 页失败:`, error.message);
            throw error;
        }
    }
    
    console.log(`\n总共获取到 ${allData.length} 条数据\n`);
    return allData;
}

/**
 * 根据路径获取上下文ID
 */
function getContextIdByPath(allData, contextPath) {
    const context = allData.find(item => item.contextPath === contextPath);
    return context ? context.id : null;
}

/**
 * 解析路径，生成需要创建的节点列表
 */
function parsePathToNodes(contextPath, nodeName, nodeLevel) {
    const nodes = [];
    const parts = contextPath.split('/').filter(p => p); // 过滤掉空字符串
    
    // 从根节点开始构建
    for (let i = 0; i < parts.length && i <= nodeLevel; i++) {
        const path = '/' + parts.slice(0, i + 1).join('/');
        const name = parts[i];
        nodes.push({
            contextPath: path,
            nodeName: name,
            nodeLevel: i,
            parentId: null // 稍后会设置
        });
    }
    
    return nodes;
}

/**
 * 找出缺失的节点
 */
function findMissingNodes(allData, targetContext) {
    const missingNodes = [];
    const pathMap = new Map(); // 用于快速查找现有路径
    
    // 建立路径索引
    allData.forEach(item => {
        pathMap.set(item.contextPath, item);
    });
    
    // 解析目标路径，生成所有应该存在的节点
    const parts = targetContext.contextPath.split('/').filter(p => p);
    const expectedNodes = [];
    
    for (let i = 0; i < parts.length; i++) {
        const path = '/' + parts.slice(0, i + 1).join('/');
        const name = parts[i];
        
        if (!pathMap.has(path)) {
            // 计算parentId
            let parentId = null;
            if (i > 0) {
                const parentPath = '/' + parts.slice(0, i).join('/');
                const parentNode = pathMap.get(parentPath);
                if (parentNode) {
                    parentId = parentNode.id;
                } else {
                    // 如果父节点也不存在，先添加到expectedNodes中，后续再处理
                    parentId = null; // 临时设置为null，后续会递归处理
                }
            } else {
                parentId = 0; // 根节点的parentId为0
            }
            
            expectedNodes.push({
                contextPath: path,
                nodeName: name,
                nodeLevel: i,
                parentId: parentId,
                description: i === parts.length - 1 ? targetContext.description : null,
                hasConfig: i === parts.length - 1 ? targetContext.hasConfig : false
            });
        }
    }
    
    // 按层级排序，确保先创建父节点
    expectedNodes.sort((a, b) => a.nodeLevel - b.nodeLevel);
    
    // 重新计算parentId（因为创建后ID会变化）
    // 我们需要按顺序创建，每次创建后更新pathMap
    const toCreate = [];
    const updatedPathMap = new Map(pathMap);
    
    for (const node of expectedNodes) {
        // 重新计算parentId
        if (node.nodeLevel > 0) {
            const parts = node.contextPath.split('/').filter(p => p);
            if (parts.length > 1) {
                const parentPath = '/' + parts.slice(0, parts.length - 1).join('/');
                const parentNode = updatedPathMap.get(parentPath);
                if (parentNode) {
                    node.parentId = parentNode.id;
                } else {
                    node.parentId = 0;
                }
            } else {
                node.parentId = 0;
            }
        } else {
            node.parentId = 0;
        }
        
        toCreate.push(node);
    }
    
    return toCreate;
}

/**
 * 创建上下文节点
 */
async function createContextNode(node) {
    const createData = {
        parentId: node.parentId,
        contextPath: node.contextPath,
        nodeName: node.nodeName,
        nodeLevel: node.nodeLevel,
        description: node.description || `${node.nodeName}节点`,
        hasConfig: node.hasConfig || false
    };
    
    try {
        const result = await sendRequest('/system/context/insertData', 'POST', createData);
        
        if (result.code === 2000) {
            console.log(`✓ 成功创建: ${node.contextPath} (nodeName: ${node.nodeName}, nodeLevel: ${node.nodeLevel})`);
            return true;
        } else {
            // 如果是因为路径已存在，也算成功
            if (result.msg && result.msg.includes('已存在')) {
                console.log(`⊙ 节点已存在: ${node.contextPath}`);
                return true;
            }
            throw new Error(result.msg || '创建失败');
        }
    } catch (error) {
        // 检查是否是路径已存在的错误
        if (error.message && error.message.includes('已存在')) {
            console.log(`⊙ 节点已存在: ${node.contextPath}`);
            return true;
        }
        throw error;
    }
}

/**
 * 主函数
 */
async function main() {
    try {
        console.log('='.repeat(60));
        console.log('开始修复上下文缺失节点');
        console.log('='.repeat(60) + '\n');
        
        // 1. 获取所有现有数据
        const allData = await getAllContextData();
        
        // 2. 找出所有需要补充父节点的记录
        const nodesToFix = [];
        const pathSet = new Set(allData.map(item => item.contextPath));
        
        allData.forEach(item => {
            // 检查该节点的所有父节点是否存在
            const parts = item.contextPath.split('/').filter(p => p);
            for (let i = 0; i < parts.length; i++) {
                const path = '/' + parts.slice(0, i + 1).join('/');
                if (!pathSet.has(path) && !nodesToFix.find(n => n.contextPath === path)) {
                    const missingNode = {
                        contextPath: path,
                        nodeName: parts[i],
                        nodeLevel: i,
                        parentId: null,
                        description: i === parts.length - 1 ? item.description : `${parts[i]}节点`,
                        hasConfig: i === parts.length - 1 ? item.hasConfig : false
                    };
                    nodesToFix.push(missingNode);
                }
            }
        });
        
        // 显示所有现有节点
        console.log('\n现有节点列表:');
        allData.forEach(item => {
            console.log(`  - ${item.contextPath} (ID: ${item.id}, nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel}, parentId: ${item.parentId})`);
        });
        
        // 特别检查 business/spend/air 路径
        console.log('\n检查 /business/spend/air 路径相关节点:');
        const businessPaths = ['/business', '/business/spend', '/business/spend/air'];
        businessPaths.forEach(path => {
            const node = allData.find(item => item.contextPath === path);
            if (node) {
                console.log(`  ✓ ${path} - ID: ${node.id}, nodeName: ${node.nodeName}, nodeLevel: ${node.nodeLevel}`);
            } else {
                console.log(`  ✗ ${path} - 缺失`);
            }
        });
        
        if (nodesToFix.length === 0) {
            console.log('\n✓ 所有节点都已存在，无需补充\n');
            return;
        }
        
        // 按层级排序
        nodesToFix.sort((a, b) => a.nodeLevel - b.nodeLevel);
        
        console.log(`\n发现 ${nodesToFix.length} 个缺失节点，开始补充:\n`);
        
        // 3. 按顺序创建缺失的节点
        const createdIds = new Map(); // 用于存储路径到ID的映射
        
        for (const node of nodesToFix) {
            // 计算parentId
            if (node.nodeLevel > 0) {
                const parts = node.contextPath.split('/').filter(p => p);
                const parentPath = '/' + parts.slice(0, parts.length - 1).join('/');
                
                // 先查已存在的数据
                let parentId = getContextIdByPath(allData, parentPath);
                
                // 如果不存在，查刚创建的
                if (!parentId && createdIds.has(parentPath)) {
                    parentId = createdIds.get(parentPath);
                }
                
                node.parentId = parentId || 0;
            } else {
                node.parentId = 0;
            }
            
            // 创建节点
            const success = await createContextNode(node);
            
            if (success) {
                // 创建成功后，查询新创建的节点ID
                await new Promise(resolve => setTimeout(resolve, 200)); // 等待200ms
                const allDataAfter = await getAllContextData();
                const newId = getContextIdByPath(allDataAfter, node.contextPath);
                if (newId) {
                    createdIds.set(node.contextPath, newId);
                    allData.push({
                        id: newId,
                        contextPath: node.contextPath,
                        nodeName: node.nodeName,
                        nodeLevel: node.nodeLevel,
                        parentId: node.parentId
                    });
                }
            }
        }
        
        console.log('\n' + '='.repeat(60));
        console.log('节点补充完成！');
        console.log('='.repeat(60) + '\n');
        
        // 4. 再次查询，验证结果
        console.log('验证结果，查询所有数据:\n');
        const finalData = await getAllContextData();
        console.log(`最终数据总数: ${finalData.length} 条\n`);
        
        // 显示 business/spend/air 路径的节点
        console.log('检查 /business/spend/air 路径的节点:');
        const paths = ['/business', '/business/spend', '/business/spend/air'];
        paths.forEach(path => {
            const node = finalData.find(item => item.contextPath === path);
            if (node) {
                console.log(`  ✓ ${path} - ID: ${node.id}, nodeName: ${node.nodeName}, nodeLevel: ${node.nodeLevel}`);
            } else {
                console.log(`  ✗ ${path} - 未找到`);
            }
        });
        
    } catch (error) {
        console.error('\n错误:', error.message);
        process.exit(1);
    }
}

// 运行主函数
main();

