package com.example.demo.documentoFuente;

import com.example.demo.clasificacionDocumentos.clasificacionDocumentoModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "documentofuente")
public class documentoFuenteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;

    @Column(name = "partida_id")
    private Long partidaId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "clasificacion_id",
            referencedColumnName = "id_clasificacion",
            foreignKey = @ForeignKey(name = "fk_documento_clasificacion"),
            nullable = false
    )
    private clasificacionDocumentoModel clasificacion;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "ruta_pdf", length = 255)
    private String rutaPdf;

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }

    public Long getPartidaId() { return partidaId; }
    public void setPartidaId(Long partidaId) { this.partidaId = partidaId; }

    public clasificacionDocumentoModel getClasificacion() { return clasificacion; }
    public void setClasificacion(clasificacionDocumentoModel clasificacion) { this.clasificacion = clasificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRutaPdf() { return rutaPdf; }
    public void setRutaPdf(String rutaPdf) { this.rutaPdf = rutaPdf; }
}
