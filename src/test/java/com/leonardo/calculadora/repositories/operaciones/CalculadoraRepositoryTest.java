package com.leonardo.calculadora.repositories.operaciones;

import com.leonardo.calculadora.logic.operaciones.OperadorFabrica;
import com.leonardo.calculadora.services.core.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class CalculadoraRepositoryTest {

    @Autowired
    CalculadoraRepository calculadoraRepository;
    @MockBean
    private RedisService redisService;

    @Test
    void agregarMemoria() {
        String sesion = UUID.randomUUID().toString();
        BigDecimal bigDecimal = BigDecimal.TEN;
        byte[] array = this.calculadoraRepository.toByteArray(bigDecimal);
        int expected = 1;

        given(this.redisService.rPushRedis(
                sesion,
                array,
                3600)
        ).willReturn(expected);

        int success = this.calculadoraRepository.agregarMemoria(sesion, bigDecimal);
        assertEquals("agregarMemoria", expected, success);
    }

    @Test
    void calcularAcumulado() {
        int agregados = 41;

        int bloque = 5;
        String sesion = UUID.randomUUID().toString();
        BigDecimal base = BigDecimal.valueOf(100 * Math.random());
        byte[] baseArray = this.calculadoraRepository.toByteArray(base);

        List<byte[]> memoria = new ArrayList<>(agregados);
        for (int i = 0; i < agregados; i++) {
            memoria.add(baseArray);
        }

        BigDecimal expected = base.multiply(BigDecimal.valueOf(agregados));
        given(this.redisService.lRangeRedis(
                sesion,
                0,
                bloque)
        ).willReturn(memoria);

        BigDecimal success = this.calculadoraRepository.calcularAcumulado(sesion, OperadorFabrica.obtenerInstancia("+"), bloque);
        assertEquals("agregarMemoria", expected, success);
    }

    @Test
    void limpiarMemoria() {
    }
}