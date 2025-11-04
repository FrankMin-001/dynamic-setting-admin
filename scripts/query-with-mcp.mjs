import { Client } from "@modelcontextprotocol/sdk/client";
import { StdioClientTransport } from "@modelcontextprotocol/sdk/client/stdio.js";
import fs from "node:fs";
import path from "node:path";

async function main() {
  const configPath = path.resolve(process.cwd(), "mysql-mcp-config.json");
  if (!fs.existsSync(configPath)) {
    console.error("未找到配置文件:", configPath);
    process.exit(1);
  }

  const cfg = JSON.parse(fs.readFileSync(configPath, 'utf-8'));
  const dbCfg = cfg.database || {};
  const transport = new StdioClientTransport({
    command: "mcp-mysql",
    args: ["--config", configPath],
    env: {
      MYSQL_HOST: dbCfg.host,
      MYSQL_PORT: String(dbCfg.port),
      MYSQL_USER: dbCfg.username,
      MYSQL_PASSWORD: dbCfg.password,
      MYSQL_DATABASE: dbCfg.database,
    }
  });

  const client = new Client({
    name: "local-mcp-client",
    version: "1.0.0",
  }, {
    capabilities: { tools: {} },
  });

  await client.connect(transport);

  // 列出服务器提供的工具
  const tools = await client.listTools();
  console.log("可用工具:", tools.tools.map(t => t.name));

  // 如果服务器已根据环境变量自动连接，可直接查询

  // 优先尝试 'query' 工具
  const queryTool = tools.tools.find(t => t.name.toLowerCase().includes("query"));
  if (!queryTool) {
    console.error("未找到 query 工具，工具列表:", tools.tools);
    process.exit(1);
  }

  // 通过 MCP 执行 SHOW DATABASES 查询
  const result = await client.callTool({
    name: queryTool.name,
    arguments: { sql: "SELECT schema_name AS Database FROM information_schema.schemata" },
  });

  // 打印结果
  console.log("查询结果:");
  console.log(JSON.stringify(result, null, 2));

  await client.close();
}

main().catch(err => {
  console.error("MCP 查询失败:", err?.message || err);
  process.exit(1);
});
