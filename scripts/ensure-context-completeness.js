/**
 * 确保上下文数据完整性脚本
 * 根据现有节点，自动补充缺失的父级节点
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
    const allData = [];
    let pageNo = 1;
    const pageSize = 100;
    
    while (true) {
        const requestData = {
            pageNo: pageNo,
            pageSize: pageSize
        };
        
        const result = await sendRequest('/system/context/pageList', 'POST', requestData);
        
        if (result.code !== 2000) {
            throw new Error(`查询失败: ${result.msg}`);
        }
        
        const page = result.data;
        const records = page.records || [];
        allData.push(...records);
        
        if (records.length < pageSize) {
            break;
        }
        
        pageNo++;
    }
    
    return allData;
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
            return { success: true, message: '创建成功' };
        } else {
            // 如果是因为路径已存在，也算成功
            if (result.msg && result.msg.includes('已存在')) {
                return { success: true, message: '节点已存在', exists: true };
            }
            return { success: false, message: result.msg || '创建失败' };
        }
    } catch (error) {
        // 检查是否是路径已存在的错误
        if (error.message && error.message.includes('已存在')) {
            return { success: true, message: '节点已存在', exists: true };
        }
        return { success: false, message: error.message };
    }
}

/**
 * 根据路径获取节点ID
 */
function getNodeIdByPath(allData, contextPath) {
    const node = allData.find(item => item.contextPath === contextPath);
    return node ? node.id : null;
}

/**
 * 确保路径的所有父节点都存在
 */
async function ensurePathComplete(allData, targetPath, nodeName, nodeLevel, description, hasConfig) {
    const pathMap = new Map();
    allData.forEach(item => {
        pathMap.set(item.contextPath, item);
    });
    
    const parts = targetPath.split('/').filter(p => p);
    const missingNodes = [];
    
    // 检查路径的每一级
    for (let i = 0; i < parts.length; i++) {
        const path = '/' + parts.slice(0, i + 1).join('/');
        const name = parts[i];
        const level = i;
        
        if (!pathMap.has(path)) {
            // 计算parentId
            let parentId = 0;
            if (i > 0) {
                const parentPath = '/' + parts.slice(0, i).join('/');
                const parentIdInMap = getNodeIdByPath(allData, parentPath);
                if (parentIdInMap) {
                    parentId = parentIdInMap;
                } else {
                    // 如果父节点在missingNodes中，需要在创建时更新
                    const parentMissing = missingNodes.find(n => n.contextPath === parentPath);
                    if (parentMissing && parentMissing.id) {
                        parentId = parentMissing.id;
                    } else {
                        parentId = 0; // 暂时设为0，创建后会更新
                    }
                }
            }
            
            missingNodes.push({
                contextPath: path,
                nodeName: name,
                nodeLevel: level,
                parentId: parentId,
                description: i === parts.length - 1 ? description : `${name}节点`,
                hasConfig: i === parts.length - 1 ? (hasConfig || false) : false,
                id: null // 创建后会填充
            });
        }
    }
    
    // 按层级排序，确保先创建父节点
    missingNodes.sort((a, b) => a.nodeLevel - b.nodeLevel);
    
    // 创建缺失的节点
    const createdNodes = [];
    for (const node of missingNodes) {
        // 更新parentId（如果父节点刚创建）
        if (node.nodeLevel > 0) {
            const parts = node.contextPath.split('/').filter(p => p);
            const parentPath = '/' + parts.slice(0, parts.length - 1).join('/');
            
            // 先查已有数据
            let parentId = getNodeIdByPath(allData, parentPath);
            
            // 再查刚创建的
            if (!parentId) {
                const createdParent = createdNodes.find(n => n.contextPath === parentPath);
                if (createdParent && createdParent.id) {
                    parentId = createdParent.id;
                }
            }
            
            node.parentId = parentId || 0;
        }
        
        console.log(`创建节点: ${node.contextPath} (nodeName: ${node.nodeName}, nodeLevel: ${node.nodeLevel}, parentId: ${node.parentId})`);
        const result = await createContextNode(node);
        
        if (result.success) {
            if (result.exists) {
                console.log(`  ⊙ 节点已存在\n`);
            } else {
                console.log(`  ✓ 创建成功\n`);
                // 创建成功后，查询获取ID
                await new Promise(resolve => setTimeout(resolve, 300));
                const updatedData = await getAllContextData();
                const newId = getNodeIdByPath(updatedData, node.contextPath);
                if (newId) {
                    node.id = newId;
                    createdNodes.push({ ...node });
                    allData.push({
                        id: newId,
                        contextPath: node.contextPath,
                        nodeName: node.nodeName,
                        nodeLevel: node.nodeLevel,
                        parentId: node.parentId
                    });
                }
            }
        } else {
            console.log(`  ✗ 创建失败: ${result.message}\n`);
        }
    }
    
    return missingNodes.length;
}

/**
 * 主函数
 */
async function main() {
    try {
        console.log('='.repeat(60));
        console.log('确保上下文数据完整性');
        console.log('='.repeat(60) + '\n');
        
        // 1. 获取所有现有数据
        console.log('步骤1: 查询所有现有数据...');
        const allData = await getAllContextData();
        console.log(`获取到 ${allData.length} 条数据\n`);
        
        // 2. 显示现有数据
        console.log('现有数据列表:');
        allData.forEach(item => {
            console.log(`  ${item.contextPath} (ID: ${item.id}, nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel}, parentId: ${item.parentId})`);
        });
        
        // 3. 检查所有路径的完整性
        console.log('\n步骤2: 检查所有路径的完整性...\n');
        const pathsToCheck = new Set();
        
        // 收集所有需要检查的路径
        allData.forEach(item => {
            const parts = item.contextPath.split('/').filter(p => p);
            for (let i = 0; i < parts.length; i++) {
                const path = '/' + parts.slice(0, i + 1).join('/');
                pathsToCheck.add(path);
            }
        });
        
        const pathMap = new Map();
        allData.forEach(item => {
            pathMap.set(item.contextPath, item);
        });
        
        const missingPaths = [];
        pathsToCheck.forEach(path => {
            if (!pathMap.has(path)) {
                missingPaths.push(path);
            }
        });
        
        if (missingPaths.length === 0) {
            console.log('✓ 所有路径的节点都已存在，数据完整！\n');
        } else {
            console.log(`发现 ${missingPaths.length} 个缺失的路径，需要补充:\n`);
            missingPaths.forEach(path => {
                console.log(`  ✗ ${path}`);
            });
            
            // 为每个现有节点补充缺失的父节点
            console.log('\n步骤3: 开始补充缺失节点...\n');
            let totalCreated = 0;
            
            for (const item of allData) {
                const parts = item.contextPath.split('/').filter(p => p);
                for (let i = 0; i < parts.length; i++) {
                    const path = '/' + parts.slice(0, i + 1).join('/');
                    if (missingPaths.includes(path)) {
                        const created = await ensurePathComplete(
                            allData,
                            path,
                            parts[i],
                            i,
                            i === parts.length - 1 ? item.description : `${parts[i]}节点`,
                            i === parts.length - 1 ? item.hasConfig : false
                        );
                        totalCreated += created;
                        // 更新allData
                        const updated = await getAllContextData();
                        allData.length = 0;
                        allData.push(...updated);
                        pathMap.clear();
                        allData.forEach(item => {
                            pathMap.set(item.contextPath, item);
                        });
                    }
                }
            }
            
            console.log(`\n共补充了 ${totalCreated} 个缺失节点\n`);
        }
        
        // 4. 最终验证
        console.log('步骤4: 最终验证...\n');
        const finalData = await getAllContextData();
        console.log(`最终数据总数: ${finalData.length} 条\n`);
        
        // 特别验证 /business/spend/air 路径
        console.log('验证 /business/spend/air 路径的完整性:');
        const businessPaths = ['/business', '/business/spend', '/business/spend/air'];
        let allExist = true;
        businessPaths.forEach(path => {
            const node = finalData.find(item => item.contextPath === path);
            if (node) {
                console.log(`  ✓ ${path} - ID: ${node.id}, nodeName: ${node.nodeName}, nodeLevel: ${node.nodeLevel}, parentId: ${node.parentId}`);
            } else {
                console.log(`  ✗ ${path} - 缺失`);
                allExist = false;
            }
        });
        
        if (allExist) {
            console.log('\n✓ 所有节点都已存在，数据完整！');
        } else {
            console.log('\n✗ 仍有节点缺失，请检查！');
        }
        
    } catch (error) {
        console.error('\n错误:', error.message);
        process.exit(1);
    }
}

// 运行主函数
main();

