/**
 * Copyright 2019 Team Sourcing. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leonardo.calculadora.services.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * Envolvente para redis
 */
@Service
public class RedisDriver {

    private static JedisPool pool = null;
    private static boolean iniciado = false;
    private final Logger logger = LoggerFactory.getLogger(RedisDriver.class);

    /**
     * iniciar el pool
     */
    private synchronized void iniciar() {
        if (iniciado) return;

        final JedisPoolConfig poolConfig = new JedisPoolConfig();

        // los valores de esta configuración depende mucho de la carga
        poolConfig.setMaxTotal(64);
        poolConfig.setMaxIdle(32);
        poolConfig.setMinIdle(2);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        // redis://pass@host:6379/0
        pool = new JedisPool(poolConfig, "localhost");
        iniciado = true;

        logger.info("+-------------------------------+");
        logger.info("|       Connected to Redis      |");
        logger.info("+-------------------------------+");
    }


    /**
     * @return conexion
     */
    public Jedis getConn() {
        if (!iniciado) iniciar();
        return pool.getResource();
    }
}
