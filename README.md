# Dragon-DynamicSettingAdmin

简介: 此项目基于静态配置繁琐,想要把一系列规则变成线上动态的配置管理后台,因为是我自己设计的,所以就这个名字了;

## 部署与启动说明

本项目为 Spring Boot 应用，推荐以 Docker 镜像在 K3s 集群中部署。以下为从源码到部署的完整流程与依赖说明。

### 运行环境依赖
- Java `17`
- Maven `3.8+`
- Docker（可用来本地构建镜像与导出 tar）
- K3s 集群及 `kubectl`
- K3s 节点安装的 `nerdctl` 或 `ctr`（用于导入本地镜像 tar）
- MySQL 数据库（默认库名：`blbb_datasource`）

### 构建应用与镜像
1. 在项目根目录打包：
   - `mvn clean package -DskipTests`
   - 应用主 JAR 输出：`yml-server/target/dragon-dynamic-setting.jar`
2. 构建 Docker 镜像（镜像名与标签）：
   - `docker build -t dragon-dynamic-setting:20251103 .`
3. 导出镜像为 tar（便于在 K3s 节点导入）：
   - `docker save -o dragon-dynamic-setting_20251103.tar dragon-dynamic-setting:20251103`

### 导入镜像到 K3s 节点
将 `dragon-dynamic-setting_20251103.tar` 拷贝到 K3s 主节点，任选其一导入：
- 使用 `nerdctl`：
  - `nerdctl --namespace k8s.io load -i dragon-dynamic-setting_20251103.tar`
- 使用 `ctr`：
  - `ctr -n k8s.io images import dragon-dynamic-setting_20251103.tar`

导入后可验证镜像：
- `nerdctl --namespace k8s.io images | grep dragon-dynamic-setting`

### 数据库配置
部署清单中通过环境变量配置数据库连接（可按需改为 ConfigMap/Secret）：
- `SPRING_DATASOURCE_URL`（示例：`jdbc:mysql://mysql-service.infra-dev.svc.cluster.local:3306/blbb_datasource?useSSL=false&serverTimezone=UTC&characterEncoding=utf8`）
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

如需直接使用外部数据库，可将 URL 改为外网地址（示例：`jdbc:mysql://<db-host>:<port>/blbb_datasource?...`）。

### K3s 部署
已在根目录提供 `Dragon-DynamicSettingAdmin.yaml` 清单，命名空间为 `infra-dev`，NodePort 为 `30082`，容器端口为 `8080`，内存限制为 `128Mi`。直接执行：
- `kubectl apply -f Dragon-DynamicSettingAdmin.yaml -n infra-dev`

部署要点：
- 应用端口：`8080`（Service 将 NodePort 映射到 `30082`）
- JVM 内存限制通过 `JAVA_OPTS`：`-Xms64m -Xmx128m -XX:MaxMetaspaceSize=64m`
- 容器资源限制：`limits.memory=128Mi`，与 JVM 堆配置匹配，避免 OOM
- 就绪/存活探针：`/blbb/login`（可根据实际页面与接口调整）

访问方式：
- `http://<NodeIP>:30082/` 或 `http://<NodeIP>:30082/blbb/login`

### 前端依赖与构建
- 前端采用 Spring Boot 的 `Thymeleaf` 模板与静态资源，随 JAR 一并打包，无需额外在集群中执行 `npm install`。
- 模板路径：`yml-server/src/main/resources/templates`；静态资源路径：`yml-server/src/main/resources/static`。
- 本地开发时已启用 `spring-boot-devtools`：修改模板或静态资源会触发自动重启/热加载，无需单独前端构建流程。
- 根目录下 `package.json` 仅用于开发脚本（如 `scripts/` 下工具），与后端运行无关；如需使用这些脚本：
  - `npm ci`（推荐）或 `npm install`
  - 推荐 Node 版本：`>=18`
  - 运行示例：`node scripts/list-databases.js`

### 本地运行（不使用容器）
直接运行 JAR 并配置数据库：
- `java -Xms64m -Xmx128m -XX:MaxMetaspaceSize=64m -jar yml-server/target/dragon-dynamic-setting.jar --server.port=8080 --spring.datasource.url="jdbc:mysql://<db-host>:<port>/blbb_datasource?useSSL=false&serverTimezone=UTC" --spring.datasource.username=<username> --spring.datasource.password=<password>`

### 常见问题排查
- 端口不通：确认 NodePort `30082` 未被占用，且 Service/Deployment 端口为 `8080`
- 数据库连通性：K3s 节点是否能访问数据库地址；账号密码是否正确
- 内存限制：确认容器限制为 `128Mi`，JVM 堆为 `-Xmx128m`
- 镜像拉取策略：本地导入镜像时建议 `imagePullPolicy=IfNotPresent`

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
