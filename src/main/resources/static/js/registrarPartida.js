document.addEventListener("DOMContentLoaded", () => {
  // selectors
  const selectDebe = document.getElementById("selectDebe");
  const selectHaber = document.getElementById("selectHaber");
  const btnAgregarDebe = document.getElementById("agregarCuentaDebe");
  const btnAgregarHaber = document.getElementById("agregarCuentaHaber");
  const form = document.querySelector(".entry-form");

  // fetch cuentas y rellenar selects iniciales
  async function cargarCuentas() {
    try {
      const res = await fetch("/api/cuentas/todas");
      if (!res.ok) throw new Error("Error al cargar cuentas");
      const cuentas = await res.json();

      function rellenar(select) {
        select.innerHTML = `<option value="">Seleccione...</option>`;
        cuentas.forEach(c => {
          // adapt to your CuentaContable fields (id_cuenta or idCuenta)
          const id = c.idCuenta !== undefined ? c.idCuenta : c.id_cuenta || c.id;
          const codigo = c.codigo || "";
          const nombre = c.nombre || "";
          const opt = document.createElement("option");
          opt.value = id;
          opt.textContent = `${codigo} - ${nombre}`;
          select.appendChild(opt);
        });
      }

      rellenar(selectDebe);
      rellenar(selectHaber);
    } catch (e) {
      console.error(e);
    }
  }

  // clonar fila base para debe/haber
  function agregarFila(contenedorId, selectName) {
    const cont = document.querySelector(`#${contenedorId} .account-entries`);
    const fila = document.createElement("div");
    fila.className = "account-row";

    fila.innerHTML = `
      <div class="form-group">
        <label for="${selectName}">Cuenta</label>
        <select name="${selectName}" class="${selectName}"></select>
      </div>
      <div class="form-group">
        <label>Monto</label>
        <input type="number" step="0.01" name="monto" placeholder="0.00" />
      </div>
      <button type="button" class="remove-btn">x</button>
    `;

    cont.appendChild(fila);

    // rellenar el select creado con las cuentas
    cargarCuentas().then(() => {
      // si hay selects dentro de la fila, rellenarlos con las mismas opciones (última lista)
      const lastSelect = fila.querySelector("select");
      if (lastSelect) {
        // copiar opciones desde el primer select del contenedor si ya existe
        const source = cont.querySelector("select");
        if (source) lastSelect.innerHTML = source.innerHTML;
      }
    });

    fila.querySelector(".remove-btn").addEventListener("click", () => {
      fila.remove();
      calcularTotales();
    });
    calcularTotales();
  }

  btnAgregarDebe.addEventListener("click", () => agregarFila("contenedorDebe", "selectDebe"));
  btnAgregarHaber.addEventListener("click", () => agregarFila("contenedorHaber", "selectHaber"));

  function calcularTotales() {
    let totalDebe = 0;
    document.querySelectorAll("#contenedorDebe input[name='monto']").forEach(i => totalDebe += Number(i.value) || 0);
    let totalHaber = 0;
    document.querySelectorAll("#contenedorHaber input[name='monto']").forEach(i => totalHaber += Number(i.value) || 0);

    document.getElementById("totalDebe").innerText = `$${totalDebe.toFixed(2)}`;
    document.getElementById("totalHaber").innerText = `$${totalHaber.toFixed(2)}`;
    document.getElementById("diferencia").innerText = `$${(totalDebe - totalHaber).toFixed(2)}`;
  }

  document.addEventListener("input", () => calcularTotales());

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // construir detalles
    const detalles = [];

    document.querySelectorAll("#contenedorDebe .account-row").forEach(row => {
      const select = row.querySelector("select");
      const cuentaId = select ? select.value : null;
      const monto = Number(row.querySelector("input[name='monto']").value) || 0;
      if (cuentaId && monto > 0) detalles.push({ cuentaId: Number(cuentaId), debe: monto, haber: 0 });
    });

    document.querySelectorAll("#contenedorHaber .account-row").forEach(row => {
      const select = row.querySelector("select");
      const cuentaId = select ? select.value : null;
      const monto = Number(row.querySelector("input[name='monto']").value) || 0;
      if (cuentaId && monto > 0) detalles.push({ cuentaId: Number(cuentaId), debe: 0, haber: monto });
    });

    // validación: debe = haber
    const totalDebe = detalles.reduce((s, d) => s + (d.debe || 0), 0);
    const totalHaber = detalles.reduce((s, d) => s + (d.haber || 0), 0);
    if (Math.abs(totalDebe - totalHaber) > 0.009) {
      alert("La partida no está cuadrada. Verifica los montos.");
      return;
    }

    // usuarioId: intenta sacar del DOM si lo tienes (ej.: Thymeleaf model), si no, usa 1 temporal
    const usuarioId = window.USUARIO_ID || (sessionStorage.getItem("idUsuario") ? Number(sessionStorage.getItem("idUsuario")) : 1);

    const payload = {
      fecha: document.getElementById("fecha_partida").value,
      concepto: document.getElementById("descripcion_partida").value,
      usuarioId,
      detalles
    };

    try {
      const res = await fetch("/api/partidas/registrar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Error al registrar");
      }
      const data = await res.json();
      alert("Partida registrada. ID: " + (data.idPartida || data.id_partida || data.id));
      location.reload();
    } catch (err) {
      console.error(err);
      alert("Error: " + err.message);
    }
  });

  // inicial
  cargarCuentas().then(() => {
    // si quieres una fila extra al inicio, puedes crearla: agregarFila("contenedorHaber","selectHaber");
    calcularTotales();
  });
});
