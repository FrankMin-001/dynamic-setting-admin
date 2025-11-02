-- blbb_datasource.blbb_context definition

CREATE TABLE `blbb_context` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父级上下文ID，用于构建层级结构',
  `context_path` varchar(500) NOT NULL COMMENT '完整路径，如：/admin/spend/air',
  `node_name` varchar(100) NOT NULL COMMENT '节点名称，如：admin, spend, air',
  `node_level` int NOT NULL COMMENT '节点层级，根节点为0，逐级递增',
  `description` text COMMENT '描述信息',
  `has_config` tinyint(1) DEFAULT '0' COMMENT '该节点是否有具体配置',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- blbb_datasource.blbb_dict_history definition

CREATE TABLE `blbb_dict_history` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型：ADD/UPDATE/DELETE',
  `old_data` json DEFAULT NULL COMMENT '旧数据',
  `new_data` json DEFAULT NULL COMMENT '新数据',
  `operated_by` varchar(100) DEFAULT NULL COMMENT '操作人',
  `operated_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- blbb_datasource.blbb_dict_type definition

CREATE TABLE `blbb_dict_type` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型，如：booking_class, service_class, discount_type',
  `dict_name` varchar(200) NOT NULL COMMENT '字典名称，如：预订舱位类型、服务等级类型、折扣类型',
  `description` text COMMENT '字典描述',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- blbb_datasource.blbb_template definition

CREATE TABLE `blbb_template` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `template_type` varchar(200) NOT NULL COMMENT '模版类型',
  `template_name` varchar(200) NOT NULL COMMENT '模板名称',
  `column_definitions` json NOT NULL COMMENT '列定义，JSON格式',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_name` (`template_name`),
  UNIQUE KEY `uk_template_type` (`template_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- blbb_datasource.blbb_user_account definition

CREATE TABLE `blbb_user_account` (
  `id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（加密存储）',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户账号表';

-- blbb_datasource.blbb_dict_data definition

CREATE TABLE `blbb_dict_data` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `dict_code` varchar(100) NOT NULL COMMENT '字典编码，如：C, D, I, J',
  `dict_value` varchar(200) NOT NULL COMMENT '字典值，如：C舱, D舱, I舱',
  `display_order` int DEFAULT '0' COMMENT '显示顺序',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `extend_props` json DEFAULT NULL COMMENT '扩展属性',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_code` (`dict_type`,`dict_code`),
  CONSTRAINT `blbb_dict_data_ibfk_1` FOREIGN KEY (`dict_type`) REFERENCES `blbb_dict_type` (`dict_type`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- blbb_datasource.blbb_title definition

CREATE TABLE `blbb_title` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `context_id` bigint NOT NULL COMMENT '关联的上下文ID',
  `title_name` varchar(200) NOT NULL COMMENT '标题名称，如：Booking Classes',
  `title_key` varchar(200) NOT NULL COMMENT '标题键名，用于代码中获取',
  `display_order` int DEFAULT '0' COMMENT '显示顺序',
  `template_id` bigint NOT NULL COMMENT '模版在blbb_template提前创建好',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `context_id` (`context_id`),
  KEY `template_id` (`template_id`),
  CONSTRAINT `blbb_title_ibfk_1` FOREIGN KEY (`context_id`) REFERENCES `blbb_context` (`id`) ON DELETE CASCADE,
  CONSTRAINT `blbb_title_ibfk_2` FOREIGN KEY (`template_id`) REFERENCES `blbb_template` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- blbb_datasource.blbb_config_data definition

CREATE TABLE `blbb_config_data` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `title_id` bigint DEFAULT NULL COMMENT '关联的标题ID',
  `template_type` varchar(200) NOT NULL COMMENT '模版类型',
  `row_data` json NOT NULL COMMENT '行数据，按照模板定义的格式存储',
  `display_order` int DEFAULT '0' COMMENT '显示顺序',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否激活',
  `version` int DEFAULT '1' COMMENT '数据版本',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `template_type` (`template_type`),
  KEY `idx_title_id` (`title_id`),
  CONSTRAINT `blbb_config_data_ibfk_1` FOREIGN KEY (`template_type`) REFERENCES `blbb_template` (`template_type`) ON DELETE CASCADE,
  CONSTRAINT `fk_config_data_title` FOREIGN KEY (`title_id`) REFERENCES `blbb_title` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- blbb_datasource.blbb_version_history definition

CREATE TABLE `blbb_version_history` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `config_data_id` bigint NOT NULL COMMENT '模版下',
  `old_version` varchar(50) DEFAULT NULL COMMENT '旧版本号',
  `new_version` varchar(50) NOT NULL COMMENT '新版本号',
  `change_type` varchar(50) NOT NULL COMMENT '变更类型：CREATE/UPDATE/DELETE',
  `change_description` text COMMENT '变更描述',
  `change_data` json DEFAULT NULL COMMENT '变更数据快照(记录所有的内容)',
  `operated_by` varchar(100) DEFAULT NULL COMMENT '操作人',
  `operated_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `config_data_id` (`config_data_id`),
  CONSTRAINT `blbb_version_history_ibfk_1` FOREIGN KEY (`config_data_id`) REFERENCES `blbb_config_data` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
