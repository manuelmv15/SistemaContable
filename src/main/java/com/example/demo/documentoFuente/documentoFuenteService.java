package com.example.demo.documentoFuente;

import com.example.demo.clasificacionDocumentos.clasificacionDocumentoRepository;
import com.example.demo.clasificacionDocumentos.clasificacionDocumentoModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class documentoFuenteService {

    private final documentoFuenteRepository repo;
    private final clasificacionDocumentoRepository clasificacionRepo;

    public documentoFuenteService(documentoFuenteRepository repo,
                                  clasificacionDocumentoRepository clasificacionRepo) {
        this.repo = repo;
        this.clasificacionRepo = clasificacionRepo;
    }

    public List<documentoFuenteModel> findAll() {
        return repo.findAll();
    }

    public List<documentoFuenteModel> findByClasificacion(Long idClasificacion) {
        return repo.findByClasificacion_IdClasificacion(idClasificacion);
    }

    @Transactional
    public documentoFuenteModel crear(Long clasificacionId, Long partidaId,
                                      String nombre, String descripcion, String rutaPdf) {
        clasificacionDocumentoModel c = clasificacionRepo.findById(clasificacionId)
                .orElseThrow(() -> new IllegalArgumentException("Clasificación inválida"));
        var d = new documentoFuenteModel();
        d.setClasificacion(c);
        d.setPartidaId(partidaId);
        d.setNombre(nombre);
        d.setDescripcion(descripcion);
        d.setRutaPdf(rutaPdf);
        return repo.save(d);
    }

    @Transactional
    public documentoFuenteModel editar(Long idDoc, Long clasificacionId, Long partidaId,
                                       String nombre, String descripcion, String rutaPdf) {
        var d = repo.findById(idDoc)
                .orElseThrow(() -> new IllegalArgumentException("No existe el documento"));

        if (clasificacionId != null) {
            var c = clasificacionRepo.findById(clasificacionId)
                    .orElseThrow(() -> new IllegalArgumentException("Clasificación inválida"));
            d.setClasificacion(c);
        }
        d.setPartidaId(partidaId);
        d.setNombre(nombre);
        d.setDescripcion(descripcion);
        d.setRutaPdf(rutaPdf);
        return d; // flush por @Transactional
    }
}