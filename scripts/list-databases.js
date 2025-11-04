const fs = require('fs');
const path = require('path');
const mysql = require('mysql2/promise');

async function main() {
  try {
    const configPath = path.resolve(__dirname, '..', 'mysql-mcp-config.json');
    const raw = fs.readFileSync(configPath, 'utf-8');
    const cfg = JSON.parse(raw);

    const dbCfg = cfg.database || {};
    const host = dbCfg.host || 'localhost';
    const port = dbCfg.port || 3306;
    const user = dbCfg.username || 'root';
    const password = dbCfg.password || '';

    const conn = await mysql.createConnection({ host, port, user, password });
    const [rows] = await conn.query('SHOW DATABASES');
    await conn.end();

    const names = rows.map(r => r.Database).filter(Boolean);
    console.log('数据库数量:', names.length);
    console.log('数据库列表:');
    names.forEach(n => console.log(' -', n));
    process.exit(0);
  } catch (err) {
    console.error('查询数据库列表失败:');
    console.error(err && err.message ? err.message : err);
    process.exit(1);
  }
}

main();

