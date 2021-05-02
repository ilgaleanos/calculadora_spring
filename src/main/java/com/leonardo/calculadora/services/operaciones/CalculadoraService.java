package com.leonardo.calculadora.services.operaciones;

import com.leonardo.calculadora.logic.operaciones.Operador;
import com.leonardo.calculadora.repositories.operaciones.CalculadoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculadoraService {

    private final CalculadoraRepository calculadoraRepository;


    @Autowired
    CalculadoraService(CalculadoraRepository calculadoraRepository) {
        this.calculadoraRepository = calculadoraRepository;
    }


    /**
     * Agrega un operando a una sesión, si la sesión no existe la crea
     *
     * @param sesionId cadena de indetificacion única de la sesión
     * @param valor    valor a agregar a los operandos
     * @return bandera que nos indica si pudo o no agregar el operando
     */
    public int agregar(String sesionId, BigDecimal valor) {
        return this.calculadoraRepository.agregarMemoria(sesionId, valor);
    }


    /**
     * Agrega un operando a una sesión, si la sesión no existe la crea
     *
     * @param sesionId cadena de indetificacion unica de la sesión
     * @param operador implementacion de un operador
     * @return resultado de ejecutar la operación
     */
    public BigDecimal operar(String sesionId, Operador operador) {
        return this.calculadoraRepository.calcularAcumulado(sesionId, operador, 20);
    }


    /**
     * Elimina una sesión dada
     *
     * @param sesionId cadena de indetificacion unica de la sesión
     * @return resultado de ejecutar la operación
     */
    public int eliminar(String sesionId) {
        return this.calculadoraRepository.limpiarMemoria(sesionId);
    }
}
