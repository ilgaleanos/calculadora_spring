package com.leonardo.calculadora.services.operaciones;

import com.leonardo.calculadora.logic.operaciones.Operador;
import com.leonardo.calculadora.logic.operaciones.OperadorFabrica;
import com.leonardo.calculadora.repositories.operaciones.CalculadoraRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class CalculadoraServiceTest {


    @MockBean
    private CalculadoraRepository calculadoraRepository;
    @Autowired
    private CalculadoraService calculadoraService;

    @Test
    void acumular() {
        String sesion = UUID.randomUUID().toString();
        int expected = 1;

        given(this.calculadoraRepository.agregarMemoria(sesion, BigDecimal.TEN)).willReturn(expected);

        int success = this.calculadoraService.agregar(sesion, BigDecimal.TEN);
        assertEquals("acumular", expected, success);
    }


    @Test
    void operar() {
        int bloque = 20;
        String operacion = "+";
        String sesion = UUID.randomUUID().toString();
        Operador operador = OperadorFabrica.obtenerInstancia(operacion);
        BigDecimal expected = BigDecimal.TEN;

        given(this.calculadoraRepository.calcularAcumulado(sesion, operador, bloque)).willReturn(expected);

        BigDecimal success = this.calculadoraService.operar(sesion, operador);
        assertEquals("operar", expected, success);
    }


    @Test
    void eliminar() {
        String sesion = UUID.randomUUID().toString();
        int expected = 1;

        given(this.calculadoraRepository.limpiarMemoria(sesion)).willReturn(expected);

        int success = this.calculadoraService.eliminar(sesion);
        assertEquals("operar", expected, success);
    }
}