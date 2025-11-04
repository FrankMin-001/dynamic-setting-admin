# 测试计划：titleId 字段改造测试

## 测试目标
验证将配置数据查询从 `templateType` 改为 `titleId` 后的功能是否正常工作。

## 测试环境
- 应用端口: 8080
- 数据库: MySQL (blbb_datasource)

## 测试步骤

### 1. 后端API测试

#### 1.1 测试新接口：根据 titleId 查询配置数据
**接口地址**: `GET /system/configData/getConfigDataByTitleId?titleId={titleId}`

**测试用例**:
- ✅ 正常情况：传入有效的 titleId，应该返回该标题下的所有配置数据
- ✅ 边界情况：传入不存在的 titleId，应该返回空数组
- ✅ 异常情况：不传 titleId 参数，应该返回错误信息

**预期结果**:
```json
{
  "code": 2000,
  "msg": "操作成功",
  "data": [
    {
      "id": "xxx",
      "titleId": "xxx",
      "templateType": "xxx",
      "rowData": "...",
      ...
    }
  ]
}
```

### 2. 前端功能测试

#### 2.1 数据加载测试
**测试场景**: 打开编辑器页面，选择一个标题（title）

**验证点**:
- ✅ 页面应该正确调用 `/system/configData/getConfigDataByTitleId` 接口
- ✅ 请求参数应该包含正确的 `titleId`
- ✅ 数据应该正确渲染到表格中

#### 2.2 数据保存测试
**测试场景**: 在表格中新增或编辑一行数据

**验证点**:
- ✅ 保存请求应该包含 `titleId` 字段
- ✅ 保存成功后，数据应该重新加载并显示

#### 2.3 数据删除测试
**测试场景**: 删除表格中的一行数据

**验证点**:
- ✅ 删除成功后，数据应该重新加载
- ✅ 删除的数据应该不再显示

### 3. 数据库验证

#### 3.1 数据一致性检查
**验证点**:
- ✅ 新保存的数据应该正确关联到对应的 titleId
- ✅ 查询时只返回指定 titleId 的数据
- ✅ 不同 titleId 的数据应该互不干扰

## 测试命令示例

### 使用 curl 测试 API
```bash
# 测试根据 titleId 查询（需要先获取一个有效的 titleId）
curl "http://localhost:8080/system/configData/getConfigDataByTitleId?titleId=1"

# 测试不传参数（应该返回错误）
curl "http://localhost:8080/system/configData/getConfigDataByTitleId"
```

### 使用浏览器测试
1. 访问: `http://localhost:8080/blbb-editor`
2. 打开浏览器开发者工具（F12）
3. 切换到 Network 标签
4. 选择一个标题（title）查看数据
5. 观察网络请求，验证：
   - 请求 URL 是否为 `/system/configData/getConfigDataByTitleId`
   - 请求参数是否包含 `titleId`
   - 响应数据是否正确

## 注意事项

1. **数据库准备**: 确保数据库中存在测试数据
   - 至少有一个 context
   - 至少有一个 title
   - 至少有一些配置数据关联到该 title

2. **日志查看**: 启动应用后，注意查看控制台日志
   - 检查是否有编译错误
   - 检查是否有运行时错误
   - 查看 MyBatis Plus 的 SQL 日志，确认查询语句正确

3. **前端控制台**: 打开浏览器开发者工具，查看控制台日志
   - 查看是否有 JavaScript 错误
   - 查看 `loadConfigData` 相关的日志输出

