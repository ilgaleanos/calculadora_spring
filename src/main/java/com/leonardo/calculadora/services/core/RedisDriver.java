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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Duration;

/**
 * Envolvente para redis usando la libreria Jedis, se debe pasar al configurador de la aplicacion
 * pero de todas formas esta aproximación funciona correctamente
 */
@Service
public class RedisDriver {

    private static JedisPool pool = null;
    private static boolean iniciado = false;
    private final Logger logger = LoggerFactory.getLogger(RedisDriver.class);
    private static String REDIS_PASS = "";

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

        File file = new File(System.getenv("REDIS_PASS"));

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            REDIS_PASS = br.readLine();
            // agegamos 'algo' de seguridad al eliminar el archivo del contenedor
            file.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pool = new JedisPool(poolConfig,
                System.getenv("REDIS_HOST"),
                6379,
                3000,
                REDIS_PASS,
                false
        );

        iniciado = true;
        logger.info("+-------------------------------+");
        logger.info("|       Connected to Redis      |");
        logger.info("+-------------------------------+");
    }


    /**
     * Cuando la conexion se ha iniciado ya no vuelve a la creacion sino que pasa directamente a entregar ua conexion
     *
     * @return una conexion del pool de redis
     */
    public Jedis getConn() {
        if (!iniciado) iniciar();
        return pool.getResource();
    }
}
