/**
 * 测试分页查询脚本
 * 测试不同的查询条件，找出为什么某些节点查询不到
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
 * 测试不同的查询条件
 */
async function testPageQueries() {
    console.log('='.repeat(60));
    console.log('测试分页查询接口');
    console.log('='.repeat(60) + '\n');
    
    // 测试1: 无任何条件，查询所有
    console.log('测试1: 查询所有数据（无任何条件）');
    console.log('-'.repeat(60));
    try {
        const result1 = await sendRequest('/system/context/pageList', 'POST', {
            pageNo: 1,
            pageSize: 100
        });
        if (result1.code === 2000) {
            const records = result1.data.records || [];
            console.log(`返回 ${records.length} 条记录:`);
            records.forEach(item => {
                console.log(`  - ${item.contextPath} (nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel})`);
            });
        } else {
            console.log(`查询失败: ${result1.msg}`);
        }
    } catch (error) {
        console.log(`查询失败: ${error.message}`);
    }
    
    // 测试2: 按 nodeName 搜索 "air"
    console.log('\n测试2: 按关键词搜索 "air"');
    console.log('-'.repeat(60));
    try {
        const result2 = await sendRequest('/system/context/pageList', 'POST', {
            pageNo: 1,
            pageSize: 100,
            keywords: 'air'
        });
        if (result2.code === 2000) {
            const records = result2.data.records || [];
            console.log(`返回 ${records.length} 条记录:`);
            records.forEach(item => {
                console.log(`  - ${item.contextPath} (nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel})`);
            });
        } else {
            console.log(`查询失败: ${result2.msg}`);
        }
    } catch (error) {
        console.log(`查询失败: ${error.message}`);
    }
    
    // 测试3: 按 nodeName 搜索 "spend"
    console.log('\n测试3: 按关键词搜索 "spend"');
    console.log('-'.repeat(60));
    try {
        const result3 = await sendRequest('/system/context/pageList', 'POST', {
            pageNo: 1,
            pageSize: 100,
            keywords: 'spend'
        });
        if (result3.code === 2000) {
            const records = result3.data.records || [];
            console.log(`返回 ${records.length} 条记录:`);
            records.forEach(item => {
                console.log(`  - ${item.contextPath} (nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel})`);
            });
        } else {
            console.log(`查询失败: ${result3.msg}`);
        }
    } catch (error) {
        console.log(`查询失败: ${error.message}`);
    }
    
    // 测试4: 按 nodeName 搜索 "business"
    console.log('\n测试4: 按关键词搜索 "business"');
    console.log('-'.repeat(60));
    try {
        const result4 = await sendRequest('/system/context/pageList', 'POST', {
            pageNo: 1,
            pageSize: 100,
            keywords: 'business'
        });
        if (result4.code === 2000) {
            const records = result4.data.records || [];
            console.log(`返回 ${records.length} 条记录:`);
            records.forEach(item => {
                console.log(`  - ${item.contextPath} (nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel})`);
            });
        } else {
            console.log(`查询失败: ${result4.msg}`);
        }
    } catch (error) {
        console.log(`查询失败: ${error.message}`);
    }
    
    // 测试5: 按 nodeLevel 查询
    console.log('\n测试5: 查询 nodeLevel = 0 (根节点)');
    console.log('-'.repeat(60));
    try {
        const result5 = await sendRequest('/system/context/pageList', 'POST', {
            pageNo: 1,
            pageSize: 100,
            nodeLevel: 0
        });
        if (result5.code === 2000) {
            const records = result5.data.records || [];
            console.log(`返回 ${records.length} 条记录:`);
            records.forEach(item => {
                console.log(`  - ${item.contextPath} (nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel})`);
            });
        } else {
            console.log(`查询失败: ${result5.msg}`);
        }
    } catch (error) {
        console.log(`查询失败: ${error.message}`);
    }
    
    // 测试6: 按 parentId 查询
    console.log('\n测试6: 查询 parentId = 2 (business的子节点)');
    console.log('-'.repeat(60));
    try {
        const result6 = await sendRequest('/system/context/pageList', 'POST', {
            pageNo: 1,
            pageSize: 100,
            parentId: 2
        });
        if (result6.code === 2000) {
            const records = result6.data.records || [];
            console.log(`返回 ${records.length} 条记录:`);
            records.forEach(item => {
                console.log(`  - ${item.contextPath} (nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel})`);
            });
        } else {
            console.log(`查询失败: ${result6.msg}`);
        }
    } catch (error) {
        console.log(`查询失败: ${error.message}`);
    }
    
    // 测试7: 组合查询 - 搜索 business/spend/air 路径
    console.log('\n测试7: 搜索路径包含 "business/spend/air"');
    console.log('-'.repeat(60));
    try {
        const result7 = await sendRequest('/system/context/pageList', 'POST', {
            pageNo: 1,
            pageSize: 100,
            keywords: 'business/spend/air'
        });
        if (result7.code === 2000) {
            const records = result7.data.records || [];
            console.log(`返回 ${records.length} 条记录:`);
            records.forEach(item => {
                console.log(`  - ${item.contextPath} (nodeName: ${item.nodeName}, nodeLevel: ${item.nodeLevel})`);
            });
        } else {
            console.log(`查询失败: ${result7.msg}`);
        }
    } catch (error) {
        console.log(`查询失败: ${error.message}`);
    }
    
    console.log('\n' + '='.repeat(60));
    console.log('测试完成');
    console.log('='.repeat(60));
}

// 运行测试
testPageQueries().catch(error => {
    console.error('错误:', error.message);
    process.exit(1);
});

