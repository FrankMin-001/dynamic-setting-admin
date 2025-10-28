package com.smalldragon.yml.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.params.XTrimParams;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.smalldragon.yml.constants.CommonConstants.*;
import static com.smalldragon.yml.constants.CommonConstants.ROOM_END;


/**
 * Redis工具类
 */
@Slf4j
@Component
public class RedisUtil {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JedisPool jedisPool;

    //=============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return boolean
     */
    public boolean expire(String key, long time) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (time > 0) {
                jedis.expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回 -1 代表为永久有效
     */
    public long getExpire(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存（支持单个或多个key）
     * @param keys 可以传一个值或多个
     */
    public void del(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            // Jedis 的 del 方法原生支持可变参数，无需区分单key和多key情况
            jedis.del(keys);
        } catch (Exception e) {
            // 实际项目中建议使用日志记录而不是打印堆栈
            e.printStackTrace();
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param seconds  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (seconds > 0) {
                jedis.setex(key, seconds, value);
            } else {
                jedis.set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间
     * @param timeUnit 时间单位
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long time, TimeUnit timeUnit) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (time > 0) {
                long seconds = timeUnit.toSeconds(time);
                jedis.setex(key, seconds, value);
            } else {
                jedis.set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incrBy(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.decrBy(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public String hget(String key, Object item) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, JSONUtil.toJsonStr(item));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, String> hmget(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, String> map) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param seconds 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, String> map, long seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hmset(key, map);
            if (seconds > 0) {
                jedis.expire(key, seconds);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, Object item, Object value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(JSONUtil.toJsonStr(key), JSONUtil.toJsonStr(item), JSONUtil.toJsonStr(value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param seconds  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, Object item, Object value, long seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(JSONUtil.toJsonStr(key), JSONUtil.toJsonStr(item), JSONUtil.toJsonStr(value));
            if (seconds > 0) {
                jedis.expire(key, seconds);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param items 项 可以使多个 不能为null
     */
    public long hdel(String key, String... items) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hdel(key, items);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hexists(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param increment   要增加几(大于0)
     * @return double
     */
    public double hincr(String key, String item, double increment) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hincrByFloat(key, item, increment);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param decrement   要减少记(小于0)
     * @return double
     */
    public double hdecr(String key, String item, double decrement) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hincrByFloat(key, item, -decrement);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set
     */
    public Set<String> sGet(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存并设置过期时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, int time, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long count = jedis.sadd(key, values);
            if (time > 0) {
                jedis.expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return long
     */
    public long sGetSetSize(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.scard(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.srem(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================

    public List<String> lGet(String key, long start, long end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long lGetListSize(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.llen(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String lGetIndex(String key, long index) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lindex(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean lSet(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, String value, int time) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, value);
            if (time > 0) {
                jedis.expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<String> value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, value.toArray(new String[0]));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<String> value, int time) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, value.toArray(new String[0]));
            if (time > 0) {
                jedis.expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lUpdateIndex(String key, long index, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lset(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long lRemove(String key, long count, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrem(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("redis list 执行 lRemove 操作发生错误!");
        }
    }

    //===============================Stream=================================

    /**
     * 添加消息到Stream
     *
     * @param streamKey Stream键
     * @param message   消息内容
     * @return 消息ID（格式："时间戳-序列号"），失败返回null
     */
    public StreamEntryID xAdd(String streamKey, Map<String, String> message) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xadd(streamKey, StreamEntryID.NEW_ENTRY, message);
        }catch (Exception e) {
            // 实际项目中应使用日志记录
            e.printStackTrace();
            throw new RuntimeException("往"+streamKey+"插入消息发生错误!");
        }
    }

    /**
     * 添加消息到Stream（带参数）
     * @param streamKey Stream键
     * @param params 添加参数（可设置MAXLEN、NOMKSTREAM等）
     * @param message 消息内容
     * @return 消息ID（格式："时间戳-序列号"），失败返回null
     */
    public void xAdd(String streamKey, StreamEntryID messageId, Map<String, String> message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.xadd(streamKey, messageId, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Stream消息数量
     * @param streamKey Stream键
     * @return 消息数量（long），失败返回0
     */
    public long xLen(String streamKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xlen(streamKey);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 范围读取消息（正序）
     * @param streamKey Stream键
     * @param startId 起始ID（"-"表示最小ID）
     * @param endId 结束ID（"+"表示最大ID）
     * @param count 数量限制（0表示不限制）
     * @return 消息列表（List<StreamEntry>），失败返回null
     */
    public List<StreamEntry> xRange(String streamKey, String startId, String endId, int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (count > 0) {
                return jedis.xrange(streamKey, new StreamEntryID(startId), new StreamEntryID(endId), count);
            }
            return jedis.xrange(streamKey, new StreamEntryID(startId), new StreamEntryID(endId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 范围读取消息（倒序）
     * @param streamKey Stream键
     * @param startId 起始ID（"+"表示最大ID）
     * @param endId 结束ID（"-"表示最小ID）
     * @param count 数量限制（0表示不限制）
     * @return 消息列表（List<StreamEntry>），失败返回null
     */
    public List<StreamEntry> xRevRange(String streamKey, String startId, String endId, int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (count > 0) {
                return jedis.xrevrange(streamKey, new StreamEntryID(startId), new StreamEntryID(endId), count);
            }
            return jedis.xrevrange(streamKey, new StreamEntryID(startId), new StreamEntryID(endId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除消息
     * @param streamKey Stream键
     * @param ids 要删除的消息ID数组
     * @return 实际删除的消息数量（long），失败返回0
     */
    /**
     * 删除Stream中的指定消息
     * @param streamKey Stream键
     * @param ids 要删除的消息ID数组（格式："时间戳-序列号"）
     * @return 实际删除的消息数量（long），失败返回0
     */
    public long xDel(String streamKey, String... ids) {
        if (ids == null || ids.length == 0) {
            return 0L;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            StreamEntryID[] streamIds = new StreamEntryID[ids.length];
            for (int i = 0; i < ids.length; i++) {
                streamIds[i] = new StreamEntryID(ids[i]);
            }
            return jedis.xdel(streamKey, streamIds);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 创建消费者组
     * @param streamKey Stream键
     * @param groupName 组名
     * @param id 起始ID（"$"表示只接收新消息）
     * @param makeStream 如果流不存在是否创建
     * @return 创建成功返回true，失败返回false
     */
    public boolean xGroupCreate(String streamKey, String groupName, String id, boolean makeStream) {
        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.xgroupCreate(streamKey, groupName,
                    new StreamEntryID(id), makeStream);
            return "OK".equals(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建Stream消费组异常,请检查Stream数据类型是否被创建!");
        }
    }

    /**
     * 消费者组读取消息
     * @param groupName 组名
     * @param consumerName 消费者名
     * @param count 读取数量
     * @param blockMs 阻塞时间（毫秒，0表示无限阻塞）
     * @param streamKey Stream键
     * @param id 起始ID（">"表示从未传递给消费者的消息）
     * @return 消息列表（List<Map.Entry<String, List<StreamEntry>>>），失败返回null
     */
    public List<Map.Entry<String, List<StreamEntry>>> xReadGroup(String groupName, String consumerName,
                                                                 int count, long blockMs,
                                                                 String streamKey, StreamEntryID id) {
        try (Jedis jedis = jedisPool.getResource()) {
            XReadGroupParams params = XReadGroupParams.xReadGroupParams()
                    .count(count)
                    .block(Math.toIntExact(blockMs));
            return jedis.xreadGroup(groupName, consumerName, params,
                    Collections.singletonMap(streamKey, id != null ? id : StreamEntryID.UNRECEIVED_ENTRY));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 确认消息已处理
     * @param streamKey Stream键
     * @param groupName 组名
     * @param ids 消息ID数组
     * @return 确认的消息数量（long），失败返回0
     */
    /**
     * 确认消息已处理
     * @param streamKey Stream键
     * @param groupName 消费者组名
     * @param ids 要确认的消息ID数组（格式："时间戳-序列号"）
     * @return 成功确认的消息数量（long），失败返回0
     */
    public long xAck(String streamKey, String groupName, String... ids) {
        if (ids == null || ids.length == 0) {
            return 0L;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            StreamEntryID[] streamIds = new StreamEntryID[ids.length];
            for (int i = 0; i < ids.length; i++) {
                streamIds[i] = new StreamEntryID(ids[i]);
            }
            return jedis.xack(streamKey, groupName, streamIds);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 裁剪Stream
     * @param streamKey Stream键
     * @param maxLen 保留的最大消息数
     * @param approximate 是否使用近似裁剪（性能更好）
     * @return 删除的消息数量（long），失败返回0
     */
    public long xTrim(String streamKey, long maxLen, boolean approximate) {
        try (Jedis jedis = jedisPool.getResource()) {
            XTrimParams params = new XTrimParams().maxLen(maxLen);
            if (approximate) {
                params.approximateTrimming();
            }
            return jedis.xtrim(streamKey, params);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取Stream信息
     *
     * @param streamKey Stream键
     * @return Stream信息（Map<String, Object>），失败返回null
     */
    public StreamInfo xInfoStream(String streamKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xinfoStream(streamKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取消费者组信息
     * @param streamKey Stream键
     * @return 消费者组信息列表（List<StreamGroupInfo>），失败返回null
     */
    public List<StreamGroupInfo> xInfoGroups(String streamKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xinfoGroup(streamKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取消费者信息
     *
     * @param streamKey Stream键
     * @param groupName 组名
     * @return 消费者信息列表（List<StreamConsumerInfo>），失败返回null
     */
    public List<StreamConsumersInfo> xInfoConsumers(String streamKey, String groupName) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xinfoConsumers(streamKey, groupName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取待处理消息概要
     * @param streamKey Stream键
     * @param groupName 组名
     * @return 待处理消息概要（StreamPendingSummary），失败返回null
     */
    public StreamPendingSummary xPending(String streamKey, String groupName) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xpending(streamKey, groupName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //=============================== Stream (聊天室专用) =================================
    /**
     * @Description 为聊天室添加消息/创建聊天室
     * @Author YML
     * @Date 2025/4/3
     */
    public void addMessageToRoom(String streamKey, Map<String, String> message) {
        String roomKey = streamKey + ROOM_END;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.xadd(roomKey, StreamEntryID.NEW_ENTRY, message);
            expire(roomKey, TWO_HOURS);
        }catch (Exception e) {
            // 实际项目中应使用日志记录
            e.printStackTrace();
        }
    }

    /**
     * @Description 判断聊天室是否存在
     * @Author YML
     * @Date 2025/4/3
     */
    public Boolean hasRoom(String streamKey) {
        String roomKey = streamKey + ROOM_END;
        return hasKey(roomKey);
    }

    /**
     * @Description 关闭聊天室
     * @Author YML
     * @Date 2025/4/3
     */
    public Boolean closeRoom(String streamKey) {
        String roomKey = streamKey + ROOM_END;
        del(roomKey);
        return true;
    }

    /**
     * @Description 查询聊天室消息(最近10条)
     * @Author YML
     * @Date 2025/4/3
     */
    public Map<String, String> queryRoomMessage(String streamKey,Integer count) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        String roomKey = streamKey + ROOM_END;

        try (Jedis jedis = jedisPool.getResource()) {
            // 逆序获取最新消息（新→旧）
            List<StreamEntry> entries = jedis.xrevrange(roomKey, null, null, count);

            // 提取每条消息的唯一键值对
            for (StreamEntry entry : entries) {
                Map<String, String> fields = entry.getFields();
                if (!fields.isEmpty()) {
                    // 取第一个（也是唯一一个）键值对
                    Map.Entry<String, String> firstEntry = fields.entrySet().iterator().next();
                    resultMap.put(firstEntry.getKey(), firstEntry.getValue());
                }
            }
        } catch (Exception e) {
            // 实际项目中应使用日志记录
            e.printStackTrace();
        }

        return resultMap;
    }




}

