# Dragon-DynamicSettingAdmin

简介: 此项目基于静态配置繁琐,想要把一系列规则变成线上动态的配置管理后台,因为是我自己设计的,所以就这个名字了;

## 使用方式

暂定好要配置的模版路径,以及模版内的动态配置规则以及sheet页

![img](D:\YML\typeIMage\1761620618100-1b758bf3-bd4a-401f-bb58-494d4bad623a.png)



模版是固定好的,每一列能填写的内容是固定的,且每修改一次后都会记录历史数据,一个路径下可能存在多个栏目,配置后,后端可以通过代码来进行直接获取到;



代码中的使用方法:

```sql
// Step1.获取根据路径获取该路径下所有的titles
BlBBContext context = DynamicBLBBByYeMingLongUtils.getModelByPath("/yml/air/spend/upgradeRule");

// Step2.获取某一个titles下的所有数据的title
DynamicTitle title = context.getTitleByName("这里输入具体的title名称");

// Step3.获取该title下对应的模版集合
UpgradeModels dataList = title.getList();

// Step4.即可根据自己动态配置的规则进行编写业务,或者进一步编写工具类
.............................
```



## 数据库设计

### **1. 上下文表 (blbb_context)**

sql

```plsql
CREATE TABLE blbb_context (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  parent_id BIGINT DEFAULT NULL COMMENT '父级上下文ID，用于构建层级结构',
  context_path VARCHAR(500) NOT NULL COMMENT '完整路径，如：/admin/spend/air',
  node_name VARCHAR(100) NOT NULL COMMENT '节点名称，如：admin, spend, air',
  node_level INT NOT NULL COMMENT '节点层级，根节点为0，逐级递增',
  description TEXT COMMENT '描述信息',
  has_config BOOLEAN DEFAULT FALSE COMMENT '该节点是否有具体配置',
  created_by VARCHAR(100) COMMENT '创建人',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### **2. 标题表 (blbb_title)**

一个标题只能选择一个

sql

```plsql
CREATE TABLE blbb_title (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  context_id BIGINT NOT NULL COMMENT '关联的上下文ID',
  title_name VARCHAR(200) NOT NULL COMMENT '标题名称，如：Booking Classes',
  title_key VARCHAR(200) NOT NULL COMMENT '标题键名，用于代码中获取',
  display_order INT DEFAULT 0 COMMENT '显示顺序',
  template_id VARCHAR(100) NOT NULL COMMENT '模版在blbb_template提前创建好',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (context_id) REFERENCES blbb_context(id) ON DELETE CASCADE,
  FOREIGN KEY (template_id) REFERENCES blbb_template(id) ON DELETE CASCADE
);
```

### **3. 模板定义表 (blbb_template)**

sql

```sql
CREATE TABLE blbb_template (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  template_type VARCHAR(200) NOT NULL COMMENT '模版类型',
  template_name VARCHAR(200) NOT NULL COMMENT '模板名称',
  column_definitions JSON NOT NULL COMMENT '列定义，JSON格式',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_template_name (template_name),
  UNIQUE KEY uk_template_type (template_type)
);
```

### **4. 配置数据表 (blbb_config_data)**

sql

```plsql
CREATE TABLE blbb_config_data (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  template_type VARCHAR(200) NOT NULL COMMENT '模版类型',
  row_data JSON NOT NULL COMMENT '行数据，按照模板定义的格式存储',
  display_order INT DEFAULT 0 COMMENT '显示顺序',
  is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
  version INT DEFAULT 1 COMMENT '数据版本',
  created_by VARCHAR(100) COMMENT '创建人',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (template_type) REFERENCES blbb_template(template_type) ON DELETE CASCADE
);
```

### **5. 版本历史表 (blbb_version_history)**

sql

```plsql
CREATE TABLE blbb_version_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  config_data_id BIGINT NOT NULL COMMENT '模版下',
  old_version VARCHAR(50) COMMENT '旧版本号',
  new_version VARCHAR(50) NOT NULL COMMENT '新版本号',
  change_type VARCHAR(50) NOT NULL COMMENT '变更类型：CREATE/UPDATE/DELETE',
  change_description TEXT COMMENT '变更描述',
  change_data JSON COMMENT '变更数据快照(记录所有的内容)',
  operated_by VARCHAR(100) COMMENT '操作人',
  operated_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (config_data_id) REFERENCES blbb_config_data(id) ON DELETE CASCADE
);
```

**总共五张表,即可实现对应的动态配置功能,并且实现了历史功能;**

### 6.字典类型表(blbb_dict_type)

```plsql
CREATE TABLE blbb_dict_type (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  dict_type VARCHAR(100) NOT NULL COMMENT '字典类型，如：booking_class, service_class, discount_type',
  dict_name VARCHAR(200) NOT NULL COMMENT '字典名称，如：预订舱位类型、服务等级类型、折扣类型',
  description TEXT COMMENT '字典描述',
  status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  created_by VARCHAR(100) COMMENT '创建人',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_dict_type (dict_type)
);
```

### 7.**字典数据表 (blbb_dict_data)**

```sql
CREATE TABLE blbb_dict_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    dict_code VARCHAR(100) NOT NULL COMMENT '字典编码，如：C, D, I, J',
    dict_value VARCHAR(200) NOT NULL COMMENT '字典值，如：C舱, D舱, I舱',
    display_order INT DEFAULT 0 COMMENT '显示顺序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    extend_props JSON COMMENT '扩展属性',
    created_by VARCHAR(100) COMMENT '创建人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dict_type) REFERENCES blbb_dict_type(dict_type) ON DELETE CASCADE,
    UNIQUE KEY uk_type_code (dict_type, dict_code)
);
```

### 8.字典历史表(**blbb_dict_history**)

```sql
CREATE TABLE blbb_dict_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型：ADD/UPDATE/DELETE',
    old_data JSON COMMENT '旧数据',
    new_data JSON COMMENT '新数据',
    operated_by VARCHAR(100) COMMENT '操作人',
    operated_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

以上8张表搞定所有的功能,我属实有些小膨胀,数据库设计出来了,思路清晰逻辑也清晰,剩下的代码部分任何一位合格的开发拿到我的设计稿就能开始写了~   

### 9.账号表(blbb_account)

```sql
CREATE TABLE blbb_user_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='用户账号表';
```

极致简短的设计,就只是做一个后台管理系统登录使用;