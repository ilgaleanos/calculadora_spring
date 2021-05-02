package com.leonardo.calculadora.services.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class RedisService {

    private final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final RedisDriver redisDriver;


    @Autowired
    public RedisService(
            RedisDriver redisDriver
    ) {
        this.redisDriver = redisDriver;
    }

    public byte[] getRedis(String key) {
        try (Jedis jedis = redisDriver.getConn()) {
            return jedis.get(key.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String setRedis(String key, byte[] datos, long timeout) {
        String resp = null;
        try (Jedis jedis = redisDriver.getConn()) {
            byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            resp = jedis.set(byteKey, datos);
            jedis.expire(byteKey, timeout);
        }

        return resp;
    }

    public int rPushRedis(String key, byte[] datos, long timeout) {
        long resp;
        try (Jedis jedis = redisDriver.getConn()) {
            byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            resp = jedis.rpush(byteKey, datos);
            jedis.expire(byteKey, timeout);
        }

        return resp > 0 ? 1 : 0;
    }

    public List<byte[]> lRangeRedis(String key, int inicio, int fin) {
        try (Jedis jedis = redisDriver.getConn()) {
            return jedis.lrange(key.getBytes(StandardCharsets.UTF_8), inicio, fin);
        }
    }

    public int delRedis(String key) {
        long resp = 0;
        try (Jedis jedis = redisDriver.getConn()) {
            resp = jedis.del(key);
        }

        return resp > 0 ? 1 : 0;
    }
}
