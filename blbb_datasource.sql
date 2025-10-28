/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.4.5 : Database - blbb_datasource
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`blbb_datasource` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `blbb_datasource`;

/*Table structure for table `blbb_config_data` */

DROP TABLE IF EXISTS `blbb_config_data`;

CREATE TABLE `blbb_config_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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
  CONSTRAINT `blbb_config_data_ibfk_1` FOREIGN KEY (`template_type`) REFERENCES `blbb_template` (`template_type`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `blbb_config_data` */

/*Table structure for table `blbb_context` */

DROP TABLE IF EXISTS `blbb_context`;

CREATE TABLE `blbb_context` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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

/*Data for the table `blbb_context` */

/*Table structure for table `blbb_dict_data` */

DROP TABLE IF EXISTS `blbb_dict_data`;

CREATE TABLE `blbb_dict_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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

/*Data for the table `blbb_dict_data` */

/*Table structure for table `blbb_dict_history` */

DROP TABLE IF EXISTS `blbb_dict_history`;

CREATE TABLE `blbb_dict_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型：ADD/UPDATE/DELETE',
  `old_data` json DEFAULT NULL COMMENT '旧数据',
  `new_data` json DEFAULT NULL COMMENT '新数据',
  `operated_by` varchar(100) DEFAULT NULL COMMENT '操作人',
  `operated_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `blbb_dict_history` */

/*Table structure for table `blbb_dict_type` */

DROP TABLE IF EXISTS `blbb_dict_type`;

CREATE TABLE `blbb_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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

/*Data for the table `blbb_dict_type` */

/*Table structure for table `blbb_template` */

DROP TABLE IF EXISTS `blbb_template`;

CREATE TABLE `blbb_template` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `template_type` varchar(200) NOT NULL COMMENT '模版类型',
  `template_name` varchar(200) NOT NULL COMMENT '模板名称',
  `column_definitions` json NOT NULL COMMENT '列定义，JSON格式',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_name` (`template_name`),
  UNIQUE KEY `uk_template_type` (`template_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `blbb_template` */

/*Table structure for table `blbb_user_account` */

DROP TABLE IF EXISTS `blbb_user_account`;

CREATE TABLE `blbb_user_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（加密存储）',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户账号表';

/*Data for the table `blbb_user_account` */

/*Table structure for table `blbb_version_history` */

DROP TABLE IF EXISTS `blbb_version_history`;

CREATE TABLE `blbb_version_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
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

/*Data for the table `blbb_version_history` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
