package com.uam.guiapracticas4.controllers;

import com.uam.guiapracticas4.dto.ResultadoWeillDTO;
import com.uam.guiapracticas4.services.ResultadoWeillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resultados-weill")
public class ResultadoWeillController {

    private final ResultadoWeillService servicio;

    public ResultadoWeillController(ResultadoWeillService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<ResultadoWeillDTO>> listar() {
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> respuestaNoEncontrado());
    }

    @PostMapping
    public ResponseEntity<ResultadoWeillDTO> registrar(
            @Valid @RequestBody ResultadoWeillDTO resultado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.guardar(resultado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResultadoWeillDTO resultado) {
        return servicio.actualizar(id, resultado)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> respuestaNoEncontrado());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!servicio.eliminar(id)) {
            return respuestaNoEncontrado();
        }
        return ResponseEntity.ok(Map.of("mensaje", "Resultado Weill eliminado correctamente"));
    }

    private ResponseEntity<Map<String, String>> respuestaNoEncontrado() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", "Resultado Weill no encontrado"));
    }
}
