package com.example.demo.clasificacionDocumentos;

import com.example.demo.documentoFuente.documentoFuenteModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clasificaciondocumento")
public class clasificacionDocumentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clasificacion")
    private Long idClasificacion;

    @NotBlank
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "clasificacion", fetch = FetchType.LAZY)
    private List<documentoFuenteModel> documentos = new ArrayList<>();


    // Getters/Setters
    public Long getIdClasificacion() { return idClasificacion; }
    public void setIdClasificacion(Long idClasificacion) { this.idClasificacion = idClasificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<documentoFuenteModel> getDocumentos() { return documentos; }
    public void setDocumentos(List<documentoFuenteModel> documentos) { this.documentos = documentos; }
}
