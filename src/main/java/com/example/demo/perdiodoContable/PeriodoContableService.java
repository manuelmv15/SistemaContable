package com.example.demo.perdiodoContable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PeriodoContableService {

    private final PeriodoContableRepository repo;

    public PeriodoContableService(PeriodoContableRepository repo) {
        this.repo = repo;
    }

    public  List<PeriodoContable> listar() {
        return repo.findAll();
    }

    public PeriodoContable buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public PeriodoContable crear(PeriodoContableDTO dto) {
        validarRango(dto);
        validarTraslapes(dto, null);

        PeriodoContable p = new PeriodoContable();
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        return repo.save(p);
    }

    @Transactional
    public PeriodoContable actualizar(Long id, PeriodoContableDTO dto) {
        validarRango(dto);
        validarTraslapes(dto, id);

        PeriodoContable p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Periodo no encontrado"));
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        return repo.save(p);
    }

    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    private void validarRango(PeriodoContableDTO dto) {
        if (dto.getFechaInicio() != null && dto.getFechaFin() != null &&
                !dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser mayor que la fecha de inicio.");
        }
    }

    private void validarTraslapes(PeriodoContableDTO dto, Long idEdicion) {
        var inicio = dto.getFechaInicio();
        var fin = dto.getFechaFin();

        var overlaps = (idEdicion == null)
                ? repo.findOverlaps(inicio, fin)
                : repo.findOverlapsExcludingId(idEdicion, inicio, fin);

        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("El rango indicado se traslapa con otro período existente.");
        }
    }



    public void crear(LocalDate ini, LocalDate fin) {
        var p = new PeriodoContable();
        p.setFechaInicio(ini);
        p.setFechaFin(fin);
        repo.save(p);
    }

    public void actualizar(Long id, LocalDate ini, LocalDate fin) {
        var p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe id " + id));
        p.setFechaInicio(ini);
        p.setFechaFin(fin);
        repo.save(p);
    }
}
