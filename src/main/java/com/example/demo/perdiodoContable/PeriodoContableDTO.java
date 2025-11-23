package com.example.demo.perdiodoContable; // ← si puedes, corrige el typo del paquete

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

//clase que transporta datos desde el front al back (caja)
//form -> db
@Getter @Setter
public class PeriodoContableDTO {

    private Long id;

    @NotNull(message = "Debes seleccionar una fecha de inicio")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaInicio;

    @NotNull(message = "Debes seleccionar una fecha final")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaFin;
}
