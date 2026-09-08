package com.uam.guiapracticas4.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ResultadoWeillDTO {

    private Long id;

    @NotBlank(message = "El nombre del participante es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombreParticipante;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 6, message = "La edad debe ser mayor o igual a 6 años")
    @Max(value = 100, message = "La edad debe ser menor o igual a 100 años")
    private Integer edad;

    @NotNull(message = "El puntaje es obligatorio")
    @Min(value = 0, message = "El puntaje no puede ser negativo")
    @Max(value = 100, message = "El puntaje no puede ser mayor que 100")
    private Integer puntaje;

    @NotBlank(message = "La clasificación es obligatoria")
    @Size(max = 60, message = "La clasificación no puede superar los 60 caracteres")
    private String clasificacion;

    @NotNull(message = "La fecha de realización es obligatoria")
    @PastOrPresent(message = "La fecha de realización no puede estar en el futuro")
    private LocalDate fechaRealizacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreParticipante() {
        return nombreParticipante;
    }

    public void setNombreParticipante(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public LocalDate getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDate fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }
}
