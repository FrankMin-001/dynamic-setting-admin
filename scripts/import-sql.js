import fs from 'fs';
import path from 'path';
import mysql from 'mysql2/promise';

async function main() {
  try {
    const sqlFileArg = process.argv[2] || 'd:/YML/Products/dragon-dynamic-setting/blbb_datasource.sql';
    const configPathArg = process.argv[3] || path.join(process.cwd(), 'mysql-mcp-config.json');

    if (!fs.existsSync(sqlFileArg)) {
      console.error(`找不到 SQL 文件: ${sqlFileArg}`);
      process.exit(1);
    }
    if (!fs.existsSync(configPathArg)) {
      console.error(`找不到数据库配置文件: ${configPathArg}`);
      process.exit(1);
    }

    const cfgRaw = fs.readFileSync(configPathArg, 'utf8');
    const cfg = JSON.parse(cfgRaw);
    const dbCfg = cfg.database || cfg;
    const host = dbCfg.host;
    const port = dbCfg.port;
    const user = dbCfg.username || dbCfg.user;
    const password = dbCfg.password;
    const database = dbCfg.database;

    const sql = fs.readFileSync(sqlFileArg, 'utf8');

    const connection = await mysql.createConnection({
      host,
      port,
      user,
      password,
      multipleStatements: true,
      // 不指定 database，允许脚本内的 CREATE DATABASE/USE 生效
      // ssl: false, // 如需禁用 SSL，可取消注释
      // connectTimeout: 15000,
    });

    console.log(`开始导入: ${sqlFileArg} -> ${host}:${port}`);
    const start = Date.now();
    await connection.query(sql);
    const duration = ((Date.now() - start) / 1000).toFixed(2);
    console.log(`导入完成，用时 ${duration}s`);

    // 验证：列出目标库的表
    const targetDb = database || 'blbb_datasource';
    try {
      const [rows] = await connection.query('SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?', [targetDb]);
      const tables = rows.map(r => r.TABLE_NAME);
      console.log(`验证结果：数据库 \`${targetDb}\` 中共 ${tables.length} 张表`);
      if (tables.length) {
        console.log(tables.join(', '));
      }
    } catch (e) {
      console.warn(`验证列表表失败：${e.message}`);
    }

    await connection.end();
  } catch (err) {
    console.error('导入失败：', err.message || err);
    if (err && err.stack) {
      console.error(err.stack);
    }
    process.exit(1);
  }
}

main();
