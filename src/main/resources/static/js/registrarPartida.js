document.addEventListener("DOMContentLoaded", function () {
    
    const form = document.querySelector(".entry-form");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const fecha = document.getElementById("fecha_partida").value;
        const concepto = document.getElementById("descripcion_partida").value;

        let detalles = [];

        // Recorrer Debe
        document.querySelectorAll("#contenedorDebe .account-row").forEach(row => {
            const cuentaId = row.querySelector("select").value;
            const monto = row.querySelector('input[type="number"]').value;

            if (monto > 0)
                detalles.push({
                    cuentaId: parseInt(cuentaId),
                    debe: parseFloat(monto),
                    haber: 0
                });
        });

        // Recorrer Haber
        document.querySelectorAll("#contenedorHaber .account-row").forEach(row => {
            const cuentaId = row.querySelector("select").value;
            const monto = row.querySelector('input[type="number"]').value;

            if (monto > 0)
                detalles.push({
                    cuentaId: parseInt(cuentaId),
                    debe: 0,
                    haber: parseFloat(monto)
                });
        });

        const payload = {
            fecha: fecha,
            concepto: concepto,
            usuarioId: 1, // POR AHORA fijo, luego usar sesión
            detalles: detalles
        };

        const response = await fetch("/contador/registrar-partida", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const result = await response.text();
        alert(result);
    });
});
