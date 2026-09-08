package com.uam.guiapracticas4.services;

import com.uam.guiapracticas4.dto.ResultadoWeillDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ResultadoWeillService {

    private final List<ResultadoWeillDTO> resultados = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    public List<ResultadoWeillDTO> listar() {
        return resultados;
    }

    public Optional<ResultadoWeillDTO> buscarPorId(Long id) {
        return resultados.stream()
                .filter(resultado -> resultado.getId().equals(id))
                .findFirst();
    }

    public ResultadoWeillDTO guardar(ResultadoWeillDTO resultado) {
        resultado.setId(secuencia.incrementAndGet());
        resultados.add(resultado);
        return resultado;
    }

    public Optional<ResultadoWeillDTO> actualizar(Long id, ResultadoWeillDTO datos) {
        return buscarPorId(id).map(resultado -> {
            resultado.setNombreParticipante(datos.getNombreParticipante());
            resultado.setEdad(datos.getEdad());
            resultado.setPuntaje(datos.getPuntaje());
            resultado.setClasificacion(datos.getClasificacion());
            resultado.setFechaRealizacion(datos.getFechaRealizacion());
            return resultado;
        });
    }

    public boolean eliminar(Long id) {
        return resultados.removeIf(resultado -> resultado.getId().equals(id));
    }
}
