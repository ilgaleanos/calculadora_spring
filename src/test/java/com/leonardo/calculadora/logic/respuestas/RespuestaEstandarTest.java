package com.leonardo.calculadora.logic.respuestas;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class RespuestaEstandarTest {

    @Autowired
    private RespuestaEstandar respuestaEstandar;
    private String data;
    private int code;

    @Before
    public void setUp() {
        this.code = (int) (1000 * Math.random());
        this.data = UUID.randomUUID().toString();
    }


    @Test
    public void constructorCodeData() {
        RespuestaEstandar rs = new RespuestaEstandar(code, data);
        assertEquals("code", code, rs.getCode());
        assertEquals("data", data, rs.getData());
    }


    @Test
    public void constructorCode() {
        RespuestaEstandar rs = new RespuestaEstandar(code);
        rs.setData(data);

        assertEquals("code", code, rs.getCode());
        assertEquals("data", data, rs.getData());
    }

    @Test
    public void parametros() {
        RespuestaEstandar rs = new RespuestaEstandar();
        rs.setCode(code);
        rs.setData(data);

        assertEquals("code", code, rs.getCode());
        assertEquals("data", data, rs.getData());
    }
}