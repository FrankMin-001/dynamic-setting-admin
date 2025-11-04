/**
 * 验证 business/spend/air 节点查询脚本
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
 * 根据ID查询节点
 */
async function getNodeById(id) {
    try {
        const result = await sendRequest(`/system/context/getInfoById?id=${id}`, 'GET');
        if (result.code === 2000) {
            return result.data;
        } else {
            throw new Error(result.msg || '查询失败');
        }
    } catch (error) {
        throw error;
    }
}

/**
 * 主函数
 */
async function main() {
    console.log('='.repeat(60));
    console.log('验证 /business/spend/air 路径节点');
    console.log('='.repeat(60) + '\n');
    
    // 1. 查询所有数据
    console.log('1. 查询所有上下文数据:');
    const allResult = await sendRequest('/system/context/pageList', 'POST', {
        pageNo: 1,
        pageSize: 100
    });
    
    if (allResult.code !== 2000) {
        console.error('查询失败:', allResult.msg);
        return;
    }
    
    const allData = allResult.data.records || [];
    console.log(`   获取到 ${allData.length} 条数据\n`);
    
    // 2. 查找三个节点
    console.log('2. 查找 business/spend/air 路径的节点:\n');
    
    const businessNode = allData.find(item => item.contextPath === '/business');
    const spendNode = allData.find(item => item.contextPath === '/business/spend');
    const airNode = allData.find(item => item.contextPath === '/business/spend/air');
    
    if (businessNode) {
        console.log(`   ✓ /business`);
        console.log(`      ID: ${businessNode.id}`);
        console.log(`      nodeName: ${businessNode.nodeName}`);
        console.log(`      nodeLevel: ${businessNode.nodeLevel}`);
        console.log(`      parentId: ${businessNode.parentId}`);
        console.log(`      description: ${businessNode.description || '无'}`);
        console.log(`      hasConfig: ${businessNode.hasConfig}`);
        console.log(`      createdBy: ${businessNode.createdBy || '无'}\n`);
    } else {
        console.log(`   ✗ /business - 未找到\n`);
    }
    
    if (spendNode) {
        console.log(`   ✓ /business/spend`);
        console.log(`      ID: ${spendNode.id}`);
        console.log(`      nodeName: ${spendNode.nodeName}`);
        console.log(`      nodeLevel: ${spendNode.nodeLevel}`);
        console.log(`      parentId: ${spendNode.parentId}`);
        console.log(`      description: ${spendNode.description || '无'}`);
        console.log(`      hasConfig: ${spendNode.hasConfig}`);
        console.log(`      createdBy: ${spendNode.createdBy || '无'}\n`);
    } else {
        console.log(`   ✗ /business/spend - 未找到\n`);
    }
    
    if (airNode) {
        console.log(`   ✓ /business/spend/air`);
        console.log(`      ID: ${airNode.id}`);
        console.log(`      nodeName: ${airNode.nodeName}`);
        console.log(`      nodeLevel: ${airNode.nodeLevel}`);
        console.log(`      parentId: ${airNode.parentId}`);
        console.log(`      description: ${airNode.description || '无'}`);
        console.log(`      hasConfig: ${airNode.hasConfig}`);
        console.log(`      createdBy: ${airNode.createdBy || '无'}\n`);
    } else {
        console.log(`   ✗ /business/spend/air - 未找到\n`);
    }
    
    // 3. 验证层级关系
    console.log('3. 验证层级关系:\n');
    if (businessNode && spendNode && airNode) {
        console.log(`   business (ID: ${businessNode.id}, nodeLevel: ${businessNode.nodeLevel})`);
        console.log(`   └─ spend (ID: ${spendNode.id}, nodeLevel: ${spendNode.nodeLevel}, parentId: ${spendNode.parentId})`);
        if (spendNode.parentId === businessNode.id) {
            console.log(`      ✓ spend 的 parentId 正确指向 business\n`);
        } else {
            console.log(`      ✗ spend 的 parentId 错误，应该是 ${businessNode.id}，实际是 ${spendNode.parentId}\n`);
        }
        
        console.log(`   spend (ID: ${spendNode.id}, nodeLevel: ${spendNode.nodeLevel})`);
        console.log(`   └─ air (ID: ${airNode.id}, nodeLevel: ${airNode.nodeLevel}, parentId: ${airNode.parentId})`);
        if (airNode.parentId === spendNode.id) {
            console.log(`      ✓ air 的 parentId 正确指向 spend\n`);
        } else {
            console.log(`      ✗ air 的 parentId 错误，应该是 ${spendNode.id}，实际是 ${airNode.parentId}\n`);
        }
    }
    
    // 4. 总结
    console.log('='.repeat(60));
    if (businessNode && spendNode && airNode) {
        console.log('✓ 所有节点都存在且层级关系正确！');
    } else {
        console.log('✗ 部分节点缺失，需要补充！');
        if (!businessNode) {
            console.log('  - 缺少 /business 节点');
        }
        if (!spendNode) {
            console.log('  - 缺少 /business/spend 节点');
        }
        if (!airNode) {
            console.log('  - 缺少 /business/spend/air 节点');
        }
    }
    console.log('='.repeat(60));
}

main().catch(error => {
    console.error('错误:', error.message);
    process.exit(1);
});

