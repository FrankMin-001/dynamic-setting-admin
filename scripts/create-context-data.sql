-- 通过SQL脚本直接创建上下文测试数据
-- 用于在左侧栏目中显示

-- 使用数据库
USE blbb_datasource;

-- 注意：如果数据已存在，会报错，请先删除旧数据或使用INSERT IGNORE

-- 先删除可能存在的旧数据（可选，根据需要取消注释）
-- DELETE FROM blbb_context WHERE context_path LIKE '/system%' OR context_path LIKE '/business%';

-- 插入根节点
INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
VALUES 
(0, '/system', 'system', 0, '系统管理根节点', 0, 'admin'),
(0, '/business', 'business', 0, '业务管理根节点', 0, 'admin');

-- 插入一级节点（需要先获取父节点ID，这里使用子查询）
INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system' LIMIT 1) as parent_id,
    '/system/user' as context_path,
    'user' as node_name,
    1 as node_level,
    '用户管理模块' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/user');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system' LIMIT 1) as parent_id,
    '/system/order' as context_path,
    'order' as node_name,
    1 as node_level,
    '订单管理模块' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/order');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system' LIMIT 1) as parent_id,
    '/system/product' as context_path,
    'product' as node_name,
    1 as node_level,
    '商品管理模块' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/product');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/business' LIMIT 1) as parent_id,
    '/business/spend' as context_path,
    'spend' as node_name,
    1 as node_level,
    '消费升级管理' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/business/spend');

-- 插入二级节点
INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system/user' LIMIT 1) as parent_id,
    '/system/user/permission' as context_path,
    'permission' as node_name,
    2 as node_level,
    '用户权限配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/user/permission');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system/user' LIMIT 1) as parent_id,
    '/system/user/role' as context_path,
    'role' as node_name,
    2 as node_level,
    '用户角色配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/user/role');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system/order' LIMIT 1) as parent_id,
    '/system/order/list' as context_path,
    'list' as node_name,
    2 as node_level,
    '订单列表配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/order/list');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system/order' LIMIT 1) as parent_id,
    '/system/order/detail' as context_path,
    'detail' as node_name,
    2 as node_level,
    '订单详情配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/order/detail');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system/product' LIMIT 1) as parent_id,
    '/system/product/category' as context_path,
    'category' as node_name,
    2 as node_level,
    '商品分类配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/product/category');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/system/product' LIMIT 1) as parent_id,
    '/system/product/inventory' as context_path,
    'inventory' as node_name,
    2 as node_level,
    '商品库存配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/system/product/inventory');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/business/spend' LIMIT 1) as parent_id,
    '/business/spend/air' as context_path,
    'air' as node_name,
    2 as node_level,
    '航空消费升级规则配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/business/spend/air');

INSERT INTO blbb_context (parent_id, context_path, node_name, node_level, description, has_config, created_by)
SELECT 
    (SELECT id FROM blbb_context WHERE context_path = '/business/spend' LIMIT 1) as parent_id,
    '/business/spend/hotel' as context_path,
    'hotel' as node_name,
    2 as node_level,
    '酒店消费升级规则配置' as description,
    1 as has_config,
    'admin' as created_by
WHERE NOT EXISTS (SELECT 1 FROM blbb_context WHERE context_path = '/business/spend/hotel');

-- 查询创建的数据
SELECT id, parent_id, context_path, node_name, node_level, description, has_config, created_by 
FROM blbb_context 
WHERE context_path LIKE '/system%' OR context_path LIKE '/business%'
ORDER BY node_level, context_path;

