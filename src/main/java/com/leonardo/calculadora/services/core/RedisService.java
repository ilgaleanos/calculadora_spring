package com.leonardo.calculadora.services.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class RedisService {

    private final RedisDriver redisDriver;


    @Autowired
    public RedisService(
            RedisDriver redisDriver
    ) {
        this.redisDriver = redisDriver;
    }

    /**
     * Obenter datos de una key de Redis
     *
     * @param key: clave en redis
     * @return
     */
    public byte[] getRedis(String key) {
        try (Jedis jedis = redisDriver.getConn()) {
            return jedis.get(key.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * fijar datos en redis
     *
     * @param key:   clave de los datos
     * @param datos: contenido serializado
     * @param ttl:   ttl de los datos
     * @return
     */
    public String setRedis(String key, byte[] datos, long ttl) {
        String resp = null;
        try (Jedis jedis = redisDriver.getConn()) {
            byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            resp = jedis.set(byteKey, datos);
            jedis.expire(byteKey, ttl);
        }

        return resp;
    }

    /**
     * adicionador de datos a derecha en una lista en redis
     *
     * @param key:   nombre de la lista
     * @param datos: contenido serializado del elemento
     * @param ttl:   ttl de los datos
     * @return
     */
    public int rPushRedis(String key, byte[] datos, long ttl) {
        long resp;
        try (Jedis jedis = redisDriver.getConn()) {
            byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            resp = jedis.rpush(byteKey, datos);
            jedis.expire(byteKey, ttl);
        }

        return resp > 0 ? 1 : 0;
    }

    /**
     * Funcion para solicitar un fragmento de la lista en redis
     *
     * @param key:    clave de los datos
     * @param inicio: posicion de inicio de los datos solicitados
     * @param fin:    posicion de finalizacion de los datos solicitados
     * @return una lista de bytes con los datos de la lista en esas posiciones
     */
    public List<byte[]> lRangeRedis(String key, int inicio, int fin) {
        try (Jedis jedis = redisDriver.getConn()) {
            return jedis.lrange(key.getBytes(StandardCharsets.UTF_8), inicio, fin);
        }
    }

    /**
     * Funcion apra eliminar los datos de una clave
     *
     * @param key: clave de los datos
     * @return bandera si pudo o no eliminar los datos
     */
    public int delRedis(String key) {
        long resp = 0;
        try (Jedis jedis = redisDriver.getConn()) {
            resp = jedis.del(key);
        }

        return resp > 0 ? 1 : 0;
    }
}
