package com.jellyfish.sell.db.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.*;
import java.util.Map.Entry;

public class RedisBean {

    public static final int DEFAULT = 0;
    public static final int KEY_EXPIRED_LISTEN=10;
//    public static final int KEY_EXPIRED_LISTEN = 1;
//    public static final int LOTTO = 2;
//    public static final int SHARE_KEY_EXPIRED_LISTEN_ = 3;
//    public static final int LOTTO_NUMBER = 4;

    private StringRedisTemplate redisTemplate;

    public RedisBean(StringRedisTemplate redisTemplate) {
        // TODO Auto-generated constructor stub
        this.redisTemplate = redisTemplate;
    }

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    private StringRedisTemplate initRedis(int indexDb, StringRedisTemplate redisTemplate) {
        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate
                .getConnectionFactory();
        jedisConnectionFactory.setDatabase(indexDb);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        jedisConnectionFactory.resetConnection();
        return redisTemplate;
    }

    public boolean getbit(final String key, final Long offset, int db) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.getBit(serKey, offset);
            }
        });
        return result;
    }

    public Long incr(final String key, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.incr(serKey);
            }
        });
        return result;
    }


    public Long decr(final String key, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.decr(serKey);
            }
        });
        return result;
    }

    public Long incrBy(final String key, final Long value, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.incrBy(serKey, value);
            }
        });
        return result;
    }

    public Long incrBy(final String key, final Long value, final Long seconds, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                Long result = connection.incrBy(serKey, value);
                connection.expire(serKey, seconds);
                return result;
            }
        });
        return result;
    }

    public Double incrBy(final String key, final Double value, final Long seconds, int db) {
        Double result = redisTemplate.execute(new RedisCallback<Double>() {
            @Override
            public Double doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                Double result = connection.incrBy(serKey, value);
                connection.expire(serKey, seconds);
                return result;
            }
        });
        return result;
    }

    public Long hIncrBy(final String key, final String field, final Long delta, final Long seconds, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                Long result = connection.hIncrBy(serKey, serField, delta);
                connection.expire(serKey, seconds);
                return result;
            }
        });
        return result;
    }

    public Long hIncrBy(final String key, final String field, final Long delta, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                return connection.hIncrBy(serKey, serField, delta);
            }
        });
        return result;
    }

    public Long getCountByKey(final String key, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.get(serKey);
                if (serValue == null || serValue.length == 0) {
                    return 0L;
                }
                String value = serializer.deserialize(serValue);
                return Long.valueOf(value);
            }
        });
        return result;
    }

    public Long getDBSize(int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    public Long delByKey(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.del(serKey);
            }
        });
    }

    public boolean hset(final String key, final String field, final String value, int db) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                byte[] serValue = serializer.serialize(value);
                return connection.hSet(serKey, serField, serValue);
            }
        });
        return result;
    }

    public Map<String, String> hgetAll(final String key, int db) {
        Map<String, String> map = redisTemplate.execute(new RedisCallback<Map<String, String>>() {
            @Override
            public Map<String, String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key1 = serializer.serialize(key);
                Map<byte[], byte[]> map = connection.hGetAll(key1);
                if (map == null) {
                    return null;
                }
                Map<String, String> mapStat = new HashMap<>(32);
                Iterator<Entry<byte[], byte[]>> iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<byte[], byte[]> entry = iter.next();
                    String nKey = serializer.deserialize(entry.getKey());
                    String nvalue = serializer.deserialize(entry.getValue());
                    mapStat.put(nKey, nvalue);
                }
                return mapStat;
            }
        });
        return map;
    }

    public Long sAdd(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                return connection.sAdd(serKey, serValue);

            }
        });
    }

    public Boolean sMove(final String key, final String value, final String otherKey, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] otherSerKey = serializer.serialize(otherKey);
                byte[] serValue = serializer.serialize(value);
                return connection.sMove(serKey, otherSerKey, serValue);

            }
        });
    }

    public Boolean exists(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.exists(serKey);
            }
        });
    }

    public String sPop(final String key, int db) {
        String value = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.sPop(serKey);
                if (serValue != null && serValue.length > 0) {
                    return serializer.deserialize(serValue);
                } else {
                    return null;
                }
            }
        });
        return value;
    }

    public String rPop(final String key, int db) {
        String value = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.rPop(serKey);
                if (serValue != null && serValue.length > 0) {
                    return serializer.deserialize(serValue);
                } else {
                    return null;
                }
            }
        });
        return value;
    }

    public Boolean hSetNx(final String key, final String field, String value, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                byte[] serValue = serializer.serialize(value);
                boolean re = connection.hSetNX(serKey, serField, serValue);
                return re;
            }
        });
    }

    public Double hIncrBy(final String key, final String field, Double delta, final Long seconds, int db) {
        return redisTemplate.execute(new RedisCallback<Double>() {
            @Override
            public Double doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                Double re = connection.hIncrBy(serKey, serField, delta);
                connection.expire(serField, seconds);
                return re;
            }
        });
    }

    public Double hIncrBy(final String key, final String field, Double delta, int db) {
        return redisTemplate.execute(new RedisCallback<Double>() {
            @Override
            public Double doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                Double re = connection.hIncrBy(serKey, serField, delta);
                return re;
            }
        });
    }

    public Set<String> sMember(final String key, int db) {
        Set<String> set = redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                Set<byte[]> set = connection.sMembers(serKey);
                Set<String> sets = new HashSet<String>();
                Iterator<byte[]> iter = set.iterator();
                while (iter.hasNext()) {
                    String setValue = serializer.deserialize(iter.next());
                    sets.add(setValue);
                }
                return sets;
            }
        });
        return set;
    }

    /**
     * 从set中随机获取一个值
     *
     * @param key
     * @param db
     * @return
     */
    public String sRandomMember(final String key, int db) {
        String set = redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] setValue = connection.sRandMember(serKey);
                return serializer.deserialize(setValue);
            }
        });
        return set;
    }

    public Integer size(final String key, int db) {
        Integer set = redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                Set<byte[]> set = connection.sMembers(serKey);
                if (null == set) {
                    return 0;
                } else {
                    return set.size();
                }
            }
        });
        return set;
    }


    public boolean setNX(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                boolean re = connection.setNX(serKey, serValue);
                return re;
            }
        });
    }

    public String hget(final String key, final String field, int db) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                byte[] serValue = connection.hGet(serKey, serField);
                if (serValue == null || serValue.length == 0) {
                    return null;
                }
                String value = serializer.deserialize(serValue);
                return value;
            }
        });
    }

    public boolean setBit(final String key, final Long offset, final Boolean value, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                boolean re = connection.setBit(serKey, offset, value);
                return re;

            }
        });
    }

    public long redisAutoInCrement(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                Long re = connection.incr(serKey);
                return re;

            }
        });
    }

    public boolean setBit(final String key, final Long offset, final Boolean value, final Long seconds, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                boolean re = connection.setBit(serKey, offset, value);
                if (re) {
                    connection.expire(serKey, seconds);
                }
                return re;

            }
        });
    }

    public boolean expire(final String key, final Long seconds, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                boolean re = connection.expire(serKey, seconds);
                return re;
            }
        });
    }

    public Long ttl(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                long re = connection.ttl(serKey);
                if (re > 0L) {
                    return re;
                }
                return 0L;
            }
        });
    }

    public void baseRedisDao(final String key, final Long seconds, final String value, int db) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                connection.setEx(serKey, seconds, serValue);
                return null;
            }
        });
    }

    public String set(final byte[] key, final byte[] value, int db) {
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.set(key, value);
                    return "OK";
                }
            });
        } catch (Exception ex) {
            return null;
        }
        return "OK";
    }

    public String get(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.get(serKey);
                if (serValue == null || serValue.length == 0) {
                    return null;
                }
                return serializer.deserialize(serValue);
            }
        });
    }

    public Long lPush(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                return connection.lPush(serKey, serValue);
            }
        });
    }

    public Long rPush(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                return connection.rPush(serKey, serValue);
            }
        });
    }

    public String lPop(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.lPop(serKey);
                if (serValue == null || serValue.length == 0) {
                    return null;
                } else {
                    return serializer.deserialize(serValue);
                }
            }
        });
    }

    public String lIndex(final String key, int db, final Long index) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.lIndex(serKey, index);
                if (serValue == null || serValue.length == 0) {
                    return null;
                } else {
                    return serializer.deserialize(serValue);
                }
            }
        });
    }

    public byte[] getObject(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.get(serKey);
                if (serValue == null || serValue.length == 0) {
                    return null;
                }
                return serValue;
            }
        });
    }

    public Long lLen(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.lLen(serKey);
            }
        });
    }

    public Boolean addStringTime(final String key, final String field, Long time, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                return connection.setEx(serKey, time, serField);
            }
        });
    }

    public Boolean setNXStringTime(final String key, final String field, Long time, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                Boolean result = connection.setNX(serKey, serField);
                if (result) {
                    connection.expire(serKey, time);
                }
                return result;
            }
        });
    }

    public Boolean setStringTime(final String key, final String field, Long time, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                Boolean result = connection.set(serKey, serField);
                if (result) {
                    connection.expire(serKey, time);
                }
                return result;
            }
        });
    }
    public Long getListLen(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.lLen(serKey);
            }
        });
    }

    public Long ttlKey(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.ttl(serKey);
            }
        });
    }

    public void setString(final String key, final String value, int db) {
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(value);
                connection.set(serKey, serField);
                return null;
            }
        });
    }

    public Long HDelByKeyField(final String key, final String field, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                return connection.hDel(serKey, serField);
            }
        });
        return result;
    }

    public String rPopLpush(final String key, int db) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.rPopLPush(serKey, serKey);
                if (serValue == null || serValue.length == 0) {
                    return null;
                } else {
                    return serializer.deserialize(serValue);
                }
            }
        });
        return result;
    }

    public Set<byte[]> sdiff(int db, final byte[]... keys) {
        Set<byte[]> result = redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                if (keys.length == 0) {
                    return null;
                }
                Set<byte[]> set = connection.sDiff(keys);
                return set;
            }
        });
        return result;
    }

    public Boolean sIsMember(final String key,final String value, int db) {
        Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(value);
                return connection.sIsMember(serKey,serField);
            }
        });
        return result;
    }
}
