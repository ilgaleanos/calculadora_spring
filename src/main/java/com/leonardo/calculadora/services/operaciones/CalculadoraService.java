package com.leonardo.calculadora.services.operaciones;

import com.leonardo.calculadora.logic.operaciones.Operador;
import com.leonardo.calculadora.logic.operaciones.OperadorFabrica;
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


    public int acumular(String sesion, BigDecimal valor) {
        return this.calculadoraRepository.agregarMemoria(sesion, valor);
    }


    public BigDecimal operar(String sesionId, String operacion) {
        Operador op = OperadorFabrica.obtenerInstancia(operacion.trim());
        return this.calculadoraRepository.calcularAcumulado(sesionId, op);
    }


    public int eliminar(String sesionId) {
        return this.calculadoraRepository.limpiarMemoria(sesionId);
    }
}
