/**
 * 通过 BlbbContextController 接口创建上下文测试数据
 * 用于在左侧栏目中显示
 */

const http = require('http');

const BASE_URL = 'localhost';
const PORT = 8080;
const API_PATH = '/system/context/insertData';
const GET_BY_PATH_PATH = '/system/context/getContextByPath';
const LOGIN_PATH = '/api/blbb/auth/login';

// 从命令行参数获取用户名和密码，如果没有提供则使用默认值
const USERNAME = process.argv[2] || 'admin';
const PASSWORD = process.argv[3] || 'admin';

let sessionCookie = ''; // 存储会话Cookie

// 定义测试数据 - 树形结构
const testData = [
    // 根节点：系统管理
    {
        parentId: null,
        contextPath: '/system',
        nodeName: 'system',
        nodeLevel: 0,
        description: '系统管理根节点',
        hasConfig: false
    },
    // 一级节点：用户管理
    {
        parentId: null, // 需要先创建根节点，然后更新为根节点的ID
        contextPath: '/system/user',
        nodeName: 'user',
        nodeLevel: 1,
        description: '用户管理模块',
        hasConfig: true
    },
    // 一级节点：订单管理
    {
        parentId: null,
        contextPath: '/system/order',
        nodeName: 'order',
        nodeLevel: 1,
        description: '订单管理模块',
        hasConfig: true
    },
    // 一级节点：商品管理
    {
        parentId: null,
        contextPath: '/system/product',
        nodeName: 'product',
        nodeLevel: 1,
        description: '商品管理模块',
        hasConfig: true
    },
    // 二级节点：用户权限
    {
        parentId: null,
        contextPath: '/system/user/permission',
        nodeName: 'permission',
        nodeLevel: 2,
        description: '用户权限配置',
        hasConfig: true
    },
    // 二级节点：用户角色
    {
        parentId: null,
        contextPath: '/system/user/role',
        nodeName: 'role',
        nodeLevel: 2,
        description: '用户角色配置',
        hasConfig: true
    },
    // 二级节点：订单列表
    {
        parentId: null,
        contextPath: '/system/order/list',
        nodeName: 'list',
        nodeLevel: 2,
        description: '订单列表配置',
        hasConfig: true
    },
    // 二级节点：订单详情
    {
        parentId: null,
        contextPath: '/system/order/detail',
        nodeName: 'detail',
        nodeLevel: 2,
        description: '订单详情配置',
        hasConfig: true
    },
    // 二级节点：商品分类
    {
        parentId: null,
        contextPath: '/system/product/category',
        nodeName: 'category',
        nodeLevel: 2,
        description: '商品分类配置',
        hasConfig: true
    },
    // 二级节点：商品库存
    {
        parentId: null,
        contextPath: '/system/product/inventory',
        nodeName: 'inventory',
        nodeLevel: 2,
        description: '商品库存配置',
        hasConfig: true
    },
    // 根节点：业务管理
    {
        parentId: null,
        contextPath: '/business',
        nodeName: 'business',
        nodeLevel: 0,
        description: '业务管理根节点',
        hasConfig: false
    },
    // 一级节点：消费升级
    {
        parentId: null,
        contextPath: '/business/spend',
        nodeName: 'spend',
        nodeLevel: 1,
        description: '消费升级管理',
        hasConfig: true
    },
    // 二级节点：航空消费
    {
        parentId: null,
        contextPath: '/business/spend/air',
        nodeName: 'air',
        nodeLevel: 2,
        description: '航空消费升级规则配置',
        hasConfig: true
    },
    // 二级节点：酒店消费
    {
        parentId: null,
        contextPath: '/business/spend/hotel',
        nodeName: 'hotel',
        nodeLevel: 2,
        description: '酒店消费升级规则配置',
        hasConfig: true
    }
];

// 登录函数
function login() {
    return new Promise((resolve, reject) => {
        const postData = `username=${encodeURIComponent(USERNAME)}&password=${encodeURIComponent(PASSWORD)}`;
        
        const options = {
            hostname: BASE_URL,
            port: PORT,
            path: LOGIN_PATH,
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Content-Length': Buffer.byteLength(postData)
            }
        };

        const req = http.request(options, (res) => {
            let responseData = '';

            res.on('data', (chunk) => {
                responseData += chunk;
            });

            res.on('end', () => {
                // 从响应头中提取Cookie
                const cookies = res.headers['set-cookie'];
                if (cookies) {
                    // 提取SESSION或JSESSIONID等Cookie，只取名称和值部分
                    sessionCookie = cookies.map(cookie => {
                        // 格式: SESSION=xxxxx; Path=/; HttpOnly
                        return cookie.split(';')[0].trim();
                    }).join('; ');
                    console.log(`获取到Session Cookie: ${sessionCookie.substring(0, 50)}...`);
                } else {
                    console.warn('警告: 未从响应中获取到Cookie');
                }
                
                if (res.statusCode === 200 && responseData.includes('登录成功')) {
                    console.log(`✓ 登录成功: ${USERNAME}`);
                    resolve(true);
                } else {
                    console.error(`✗ 登录失败: ${responseData}`);
                    reject(new Error(`登录失败: ${responseData}`));
                }
            });
        });

        req.on('error', (e) => {
            reject(e);
        });

        req.write(postData);
        req.end();
    });
}

// 发送HTTP POST请求
function sendRequest(data) {
    return new Promise((resolve, reject) => {
        const postData = JSON.stringify(data);
        
        const headers = {
            'Content-Type': 'application/json',
            'Content-Length': Buffer.byteLength(postData)
        };
        
        // 如果有会话Cookie，添加到请求头
        if (sessionCookie) {
            headers['Cookie'] = sessionCookie;
        }
        
        const options = {
            hostname: BASE_URL,
            port: PORT,
            path: API_PATH,
            method: 'POST',
            headers: headers
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
                    resolve({ code: res.statusCode, raw: responseData });
                }
            });
        });

        req.on('error', (e) => {
            reject(e);
        });

        req.write(postData);
        req.end();
    });
}

// 通过路径获取上下文ID
function getContextByPath(contextPath) {
    return new Promise((resolve, reject) => {
        const encodedPath = encodeURIComponent(contextPath);
        const headers = {};
        
        // 如果有会话Cookie，添加到请求头
        if (sessionCookie) {
            headers['Cookie'] = sessionCookie;
        }
        
        const options = {
            hostname: BASE_URL,
            port: PORT,
            path: `${GET_BY_PATH_PATH}?contextPath=${encodedPath}`,
            method: 'GET',
            headers: headers
        };

        const req = http.request(options, (res) => {
            let responseData = '';

            res.on('data', (chunk) => {
                responseData += chunk;
            });

            res.on('end', () => {
                try {
                    const result = JSON.parse(responseData);
                    if (result.code === 2000 && result.data) {
                        resolve(result.data.id);
                    } else {
                        resolve(null);
                    }
                } catch (e) {
                    resolve(null);
                }
            });
        });

        req.on('error', (e) => {
            reject(e);
        });

        req.end();
    });
}

// 递归创建数据，处理父子关系
async function createContextData() {
    const createdIds = new Map(); // 存储 path -> id 的映射
    
    console.log(`使用账号登录: ${USERNAME}\n`);
    
    // 先登录
    try {
        await login();
        await new Promise(resolve => setTimeout(resolve, 500)); // 等待一下确保会话建立
    } catch (error) {
        console.error('登录失败，无法继续创建数据。');
        console.error('提示: 请检查用户名和密码，或使用命令: node scripts/create-context-data.js <username> <password>');
        return;
    }
    
    console.log('\n开始创建上下文数据...\n');

    // 第一遍：创建所有根节点（nodeLevel === 0）
    for (const item of testData) {
        if (item.nodeLevel === 0) {
            try {
                // 先检查是否已存在
                let existingId = await getContextByPath(item.contextPath);
                
                if (existingId) {
                    console.log(`⊙ 根节点已存在: ${item.contextPath} (ID: ${existingId})`);
                    createdIds.set(item.contextPath, existingId);
                } else {
                    const data = { ...item };
                    data.parentId = 0; // 根节点父ID为0
                    console.log(`创建根节点: ${data.contextPath}`);
                    if (sessionCookie) {
                        console.log(`  使用Cookie: ${sessionCookie.substring(0, 30)}...`);
                    }
                    const result = await sendRequest(data);
                    
                    if (result.code === 2000) {
                        // 通过路径查询获取ID
                        await new Promise(resolve => setTimeout(resolve, 200));
                        existingId = await getContextByPath(data.contextPath);
                        if (existingId) {
                            createdIds.set(data.contextPath, existingId);
                            console.log(`✓ 成功创建: ${data.contextPath} (ID: ${existingId})\n`);
                        } else {
                            console.log(`✓ 创建成功但未获取到ID: ${data.contextPath}\n`);
                        }
                    } else {
                        // 检查是否是已存在的错误
                        if (result.msg && result.msg.includes('已存在')) {
                            existingId = await getContextByPath(data.contextPath);
                            if (existingId) {
                                createdIds.set(data.contextPath, existingId);
                                console.log(`⊙ 根节点已存在: ${data.contextPath} (ID: ${existingId})\n`);
                            }
                        } else {
                            console.log(`✗ 创建失败: ${data.contextPath}, 响应: ${JSON.stringify(result)}\n`);
                        }
                    }
                }
            } catch (error) {
                console.error(`✗ 创建失败: ${item.contextPath}, 错误: ${error.message}\n`);
            }
            // 等待一小段时间，避免请求过快
            await new Promise(resolve => setTimeout(resolve, 100));
        }
    }

    // 第二遍：创建一级节点（nodeLevel === 1）
    for (const item of testData) {
        if (item.nodeLevel === 1) {
            try {
                // 先检查是否已存在
                let existingId = await getContextByPath(item.contextPath);
                
                if (existingId) {
                    console.log(`⊙ 一级节点已存在: ${item.contextPath} (ID: ${existingId})`);
                    createdIds.set(item.contextPath, existingId);
                } else {
                    const data = { ...item };
                    // 从路径中提取父路径
                    const parentPath = data.contextPath.substring(0, data.contextPath.lastIndexOf('/'));
                    const parentId = createdIds.get(parentPath);
                    
                    if (!parentId) {
                        console.log(`⚠ 跳过 ${data.contextPath}: 父节点 ${parentPath} 不存在\n`);
                        continue;
                    }
                    
                    data.parentId = parentId;
                    console.log(`创建一级节点: ${data.contextPath} (父节点: ${parentPath}, 父ID: ${parentId})`);
                    const result = await sendRequest(data);
                    
                    if (result.code === 2000) {
                        // 通过路径查询获取ID
                        await new Promise(resolve => setTimeout(resolve, 200));
                        existingId = await getContextByPath(data.contextPath);
                        if (existingId) {
                            createdIds.set(data.contextPath, existingId);
                            console.log(`✓ 成功创建: ${data.contextPath} (ID: ${existingId})\n`);
                        } else {
                            console.log(`✓ 创建成功但未获取到ID: ${data.contextPath}\n`);
                        }
                    } else {
                        // 检查是否是已存在的错误
                        if (result.msg && result.msg.includes('已存在')) {
                            existingId = await getContextByPath(data.contextPath);
                            if (existingId) {
                                createdIds.set(data.contextPath, existingId);
                                console.log(`⊙ 一级节点已存在: ${data.contextPath} (ID: ${existingId})\n`);
                            }
                        } else {
                            console.log(`✗ 创建失败: ${data.contextPath}, 响应: ${JSON.stringify(result)}\n`);
                        }
                    }
                }
            } catch (error) {
                console.error(`✗ 创建失败: ${item.contextPath}, 错误: ${error.message}\n`);
            }
            await new Promise(resolve => setTimeout(resolve, 100));
        }
    }

    // 第三遍：创建二级节点（nodeLevel === 2）
    for (const item of testData) {
        if (item.nodeLevel === 2) {
            try {
                // 先检查是否已存在
                let existingId = await getContextByPath(item.contextPath);
                
                if (existingId) {
                    console.log(`⊙ 二级节点已存在: ${item.contextPath} (ID: ${existingId})`);
                    createdIds.set(item.contextPath, existingId);
                } else {
                    const data = { ...item };
                    // 从路径中提取父路径
                    const parentPath = data.contextPath.substring(0, data.contextPath.lastIndexOf('/'));
                    const parentId = createdIds.get(parentPath);
                    
                    if (!parentId) {
                        console.log(`⚠ 跳过 ${data.contextPath}: 父节点 ${parentPath} 不存在\n`);
                        continue;
                    }
                    
                    data.parentId = parentId;
                    console.log(`创建二级节点: ${data.contextPath} (父节点: ${parentPath}, 父ID: ${parentId})`);
                    const result = await sendRequest(data);
                    
                    if (result.code === 2000) {
                        // 通过路径查询获取ID
                        await new Promise(resolve => setTimeout(resolve, 200));
                        existingId = await getContextByPath(data.contextPath);
                        if (existingId) {
                            createdIds.set(data.contextPath, existingId);
                            console.log(`✓ 成功创建: ${data.contextPath} (ID: ${existingId})\n`);
                        } else {
                            console.log(`✓ 创建成功但未获取到ID: ${data.contextPath}\n`);
                        }
                    } else {
                        // 检查是否是已存在的错误
                        if (result.msg && result.msg.includes('已存在')) {
                            existingId = await getContextByPath(data.contextPath);
                            if (existingId) {
                                createdIds.set(data.contextPath, existingId);
                                console.log(`⊙ 二级节点已存在: ${data.contextPath} (ID: ${existingId})\n`);
                            }
                        } else {
                            console.log(`✗ 创建失败: ${data.contextPath}, 响应: ${JSON.stringify(result)}\n`);
                        }
                    }
                }
            } catch (error) {
                console.error(`✗ 创建失败: ${item.contextPath}, 错误: ${error.message}\n`);
            }
            await new Promise(resolve => setTimeout(resolve, 100));
        }
    }

    console.log('\n所有数据创建完成！');
    console.log('请在浏览器中刷新页面查看左侧栏目。');
}

// 执行创建
createContextData().catch(console.error);

