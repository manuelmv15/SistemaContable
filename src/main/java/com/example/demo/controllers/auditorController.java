package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // <-- si tu paquete se llama distinto, corrígelo aquí
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.cuentacontable.CuentaContableService;
import com.example.demo.perdiodoContable.PeriodoContableService;

@Controller
@RequestMapping("/auditor")
public class auditorController {

    private final CuentaContableService cuentaService;
    private final PeriodoContableService periodosService;

    public auditorController(CuentaContableService cuentaService,
                             PeriodoContableService periodosService) {
        this.cuentaService = cuentaService;
        this.periodosService = periodosService;
    }

    // /auditor   o   /auditor/dashboard
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("titulo", "Panel principal");
        return "auditor/dashboard";
    }

    // /auditor/generar-reportes
    @GetMapping("/generar-reportes")
    public String generarReportes(@RequestParam(required = false) Long periodoId,
                                  @RequestParam(required = false) String desde,
                                  @RequestParam(required = false) String hasta,
                                  Model model) {

        model.addAttribute("titulo", "Generar reportes");
        model.addAttribute("periodos", periodosService.listar());
        model.addAttribute("periodoId", periodoId);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        // Devuelve List<CuentaContable>
        var reportes = cuentaService.buscar(periodoId, desde, hasta);
        model.addAttribute("reportes", reportes);

        return "auditor/generarReportes";
    }
    @GetMapping("/reportes/export")
    public ResponseEntity<byte[]> exportarReportes(@RequestParam String tipo,
                                                   @RequestParam(required = false) Long periodoId,
                                                   @RequestParam(required = false) String desde,
                                                   @RequestParam(required = false) String hasta) {

        // 1) Obtiene los datos que ya usas en la tabla
        var reportes = cuentaService.buscar(periodoId, desde, hasta); // List<CuentaContable>

        // 2) Rama por tipo
        switch (tipo.toLowerCase()) {
            case "csv": {
                // Construye CSV simple: encabezados + filas
                StringBuilder sb = new StringBuilder();
                sb.append("Codigo,Nombre,Tipo\n");
                for (var r : reportes) {
                    String tipoNombre = (r.getTipo() != null) ? r.getTipo().getNombre() : "";
                    // Escapa comas con comillas
                    sb.append('"').append(nullToEmpty(r.getCodigo())).append('"').append(',');
                    sb.append('"').append(nullToEmpty(r.getNombre())).append('"').append(',');
                    sb.append('"').append(nullToEmpty(tipoNombre)).append('"').append('\n');
                }
                byte[] bytes = sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);

                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=reportes.csv")
                        .header("Content-Type", "text/csv; charset=UTF-8")
                        .body(bytes);
            }

            case "pdf": {
                try (var baos = new java.io.ByteArrayOutputStream()) {
                    com.lowagie.text.Document doc = new com.lowagie.text.Document();
                    com.lowagie.text.pdf.PdfWriter.getInstance(doc, baos);
                    doc.open();

                    var titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.BOLD);
                    doc.add(new com.lowagie.text.Paragraph("Reporte de Cuentas", titleFont));
                    doc.add(new com.lowagie.text.Paragraph(" ")); // espacio

                    com.lowagie.text.Table table = new com.lowagie.text.Table(3);
                    table.addCell("Código");
                    table.addCell("Nombre");
                    table.addCell("Tipo");

                    for (var r : reportes) {
                        table.addCell(nullToEmpty(r.getCodigo()));
                        table.addCell(nullToEmpty(r.getNombre()));
                        String tipoNombre = (r.getTipo() != null) ? r.getTipo().getNombre() : "";
                        table.addCell(nullToEmpty(tipoNombre));
                    }

                    doc.add(table);
                    doc.close();

                    byte[] pdf = baos.toByteArray();
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=reportes.pdf")
                            .header("Content-Type", "application/pdf")
                            .body(pdf);
                } catch (Exception e) {
                    return ResponseEntity.internalServerError()
                            .body(("Error generando PDF: " + e.getMessage()).getBytes());
                }
            }


            default:
                return ResponseEntity.badRequest()
                        .body(("Tipo no soportado: " + tipo).getBytes());
        }
    }

    private static String nullToEmpty(String s) { return (s == null) ? "" : s; }






    // /auditor/libro-diario  (placeholder mientras no tengas el servicio real)
    @GetMapping("/libro-diario")
    public String libroDiario(@RequestParam(required = false) Long periodoId,
                              @RequestParam(required = false) String desde,
                              @RequestParam(required = false) String hasta,
                              Model model) {

        model.addAttribute("titulo", "Libro diario");
        model.addAttribute("periodos", periodosService.listar());
        model.addAttribute("periodoId", periodoId);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        model.addAttribute("movimientos", java.util.List.of()); // vacío por ahora
        return "auditor/libroDiario";
    }

    // /auditor/libro-mayor  (placeholder)
    @GetMapping("/libro-mayor")
    public String libroMayor(@RequestParam(required = false) Long periodoId,
                             @RequestParam(required = false) Long cuentaId,
                             Model model) {

        model.addAttribute("titulo", "Libro mayor");
        model.addAttribute("periodos", periodosService.listar());
        model.addAttribute("periodoId", periodoId);
        model.addAttribute("cuentaId", cuentaId);

        model.addAttribute("mayores", java.util.List.of()); // vacío por ahora
        return "auditor/libroMayor";
    }

    // /auditor/auditoria  (placeholder)
    @GetMapping("/auditoria")
    public String auditoria(@RequestParam(required = false) Long periodoId,
                            Model model) {

        model.addAttribute("titulo", "Auditoría");
        model.addAttribute("periodos", periodosService.listar());
        model.addAttribute("periodoId", periodoId);

        model.addAttribute("eventos", java.util.List.of()); // vacío por ahora
        return "auditor/auditoria";
    }
}
