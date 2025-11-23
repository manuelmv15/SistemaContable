// registrarPartida.js
document.addEventListener("DOMContentLoaded", () => {

  // --- Helpers de CSRF (Spring Security) ---
  // Intenta leer token desde meta o cookie XSRF-TOKEN
  function getCsrfToken() {
    // 1) meta tags (thymeleaf común)
    const meta = document.querySelector('meta[name="_csrf"]');
    if (meta) return { header: (document.querySelector('meta[name="_csrf_header"]') || {}).content || 'X-CSRF-TOKEN', token: meta.content };

    // 2) cookie XSRF-TOKEN (if your setup sets this cookie)
    const name = 'XSRF-TOKEN=';
    const c = document.cookie.split('; ').find(row => row.startsWith(name));
    if (c) return { header: 'X-XSRF-TOKEN', token: decodeURIComponent(c.split('=')[1]) };

    // 3) fallback: no csrf token found
    return null;
  }

  // --- Elementos DOM (según tu HTML) ---
  const form = document.querySelector(".entry-form");
  const contenedorDebe = document.getElementById("contenedorDebe");
  const contenedorHaber = document.getElementById("contenedorHaber");
  const btnAgregarDebe = document.getElementById("agregarCuentaDebe");
  const btnAgregarHaber = document.getElementById("agregarCuentaHaber");
  const totalDebeEl = document.getElementById("totalDebe");
  const totalHaberEl = document.getElementById("totalHaber");
  const difEl = document.getElementById("diferencia");
  const fechaInput = document.getElementById("fecha_partida");
  const descripcionInput = document.getElementById("descripcion_partida");
  const submitBtn = document.getElementById("btnTotales");

  // endpoint para cuentas y partidas
  const CUENTAS_URL = "/apis/cuentas/todas";
  const REGISTRAR_URL = "/apis/partidas/registrar";

  // --- Cargar cuentas y rellenar selects existentes ---
  async function fetchCuentas() {
    const res = await fetch(CUENTAS_URL);
    if (!res.ok) throw new Error("No se pudieron cargar cuentas");
    return await res.json();
  }

  async function rellenarSelect(select, cuentas) {
    select.innerHTML = `<option value="">Seleccione...</option>`;
    cuentas.forEach(c => {
      // Detecta id devuelto: idCuenta / id_cuenta / id
      const id = (c.idCuenta !== undefined) ? c.idCuenta : (c.id_cuenta !== undefined ? c.id_cuenta : c.id);
      const codigo = c.codigo || "";
      const nombre = c.nombre || "";
      const opt = document.createElement("option");
      opt.value = id;
      opt.textContent = `${codigo} - ${nombre}`;
      select.appendChild(opt);
    });
  }

  async function cargarCuentasEnPagina() {
    try {
      const cuentas = await fetchCuentas();
      // Rellenar selects que ya existan (selectDebe, selectHaber y cualquier select dinámico)
      document.querySelectorAll("#selectDebe, #selectHaber").forEach(s => rellenarSelect(s, cuentas));
      // Si hay selects dinámicos (clonados), también rellenarlos
      document.querySelectorAll(".account-entries select").forEach(s => {
        if (s.options.length <= 1) rellenarSelect(s, cuentas);
      });
    } catch (err) {
      console.error(err);
      alert("Error al cargar cuentas. Revisa la consola.");
    }
  }

  // --- Manejo filas dinámicas ---
  function crearAccountRow() {
    const row = document.createElement("div");
    row.className = "account-row";
    row.innerHTML = `
      <div class="form-group">
        <label>Cuenta</label>
        <select class="select-cuenta"></select>
      </div>
      <div class="form-group">
        <label>Monto</label>
        <input type="number" step="0.01" class="monto" placeholder="0.00" />
      </div>
      <button type="button" class="remove-btn">x</button>
    `;
    // remove listener
    row.querySelector(".remove-btn").addEventListener("click", () => {
      row.remove();
      calcularTotales();
    });
    return row;
  }

  // agrega fila a contenedor (usado por botones Agregar Cuenta)
  async function agregarFila(contenedor) {
    const cont = contenedor.querySelector(".account-entries");
    const row = crearAccountRow();
    cont.appendChild(row);
    // rellenar select con las cuentas ya cargadas en la página (copiar del primer select si existe)
    const source = cont.querySelector("select");
    const newSelect = row.querySelector("select");
    if (source && source.options.length > 0) {
      newSelect.innerHTML = source.innerHTML;
    } else {
      // si por algún motivo aún no hay cuentas, cargar desde endpoint
      try {
        const cuentas = await fetchCuentas();
        await rellenarSelect(newSelect, cuentas);
      } catch (err) {
        console.error("No se pudieron cargar cuentas para la nueva fila", err);
      }
    }
    calcularTotales();
  }

  btnAgregarDebe.addEventListener("click", () => agregarFila(contenedorDebe));
  btnAgregarHaber.addEventListener("click", () => agregarFila(contenedorHaber));

  // --- Cálculo de totales (tiene en cuenta filas dinámicas) ---
  function calcularTotales() {
    let totalDebe = 0;
    contenedorDebe.querySelectorAll(".account-row").forEach(row => {
      const monto = Number(row.querySelector("input.monto").value) || 0;
      totalDebe += monto;
    });

    let totalHaber = 0;
    contenedorHaber.querySelectorAll(".account-row").forEach(row => {
      const monto = Number(row.querySelector("input.monto").value) || 0;
      totalHaber += monto;
    });

    totalDebeEl.innerText = `$${totalDebe.toFixed(2)}`;
    totalHaberEl.innerText = `$${totalHaber.toFixed(2)}`;
    difEl.innerText = `$${(totalDebe - totalHaber).toFixed(2)}`;
    return { totalDebe, totalHaber };
  }

  // recalcular cuando cambian montos / selects
  document.addEventListener("input", (e) => {
    if (e.target.matches("input.monto") || e.target.matches("select")) calcularTotales();
  });

  // --- Construir payload (lee DOM actual y mapea a detallepartida) ---
  function construirDetalles() {
    const detalles = [];

    // DEBE
    contenedorDebe.querySelectorAll(".account-row").forEach(row => {
      const select = row.querySelector("select");
      const monto = Number(row.querySelector("input.monto").value) || 0;
      const cuentaId = select ? select.value : null;
      if (cuentaId && monto > 0) detalles.push({ cuentaId: Number(cuentaId), debe: monto, haber: 0 });
    });

    // HABER
    contenedorHaber.querySelectorAll(".account-row").forEach(row => {
      const select = row.querySelector("select");
      const monto = Number(row.querySelector("input.monto").value) || 0;
      const cuentaId = select ? select.value : null;
      if (cuentaId && monto > 0) detalles.push({ cuentaId: Number(cuentaId), debe: 0, haber: monto });
    });

    return detalles;
  }

  // --- Envío del formulario (intercepta submit del form) ---
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // bloqueo visual del botón
    submitBtn.disabled = true;
    const originalText = submitBtn.innerText;
    submitBtn.innerText = "Registrando...";

    try {
      const { totalDebe, totalHaber } = calcularTotales();
      if (Math.abs(totalDebe - totalHaber) > 0.009) {
        alert("La partida no está cuadrada. Debe ser igual a Haber.");
        submitBtn.disabled = false;
        submitBtn.innerText = originalText;
        return;
      }

      const detalles = construirDetalles();
      if (detalles.length === 0) {
        alert("Ingrese al menos un detalle en Debe o Haber.");
        submitBtn.disabled = false;
        submitBtn.innerText = originalText;
        return;
      }

      const payload = {
        fecha: fechaInput.value || new Date().toISOString().slice(0,10),
        concepto: descripcionInput.value || "",
        detalles: detalles
      };

      // CSRF
      const csrf = getCsrfToken();
      const headers = { "Content-Type": "application/json" };
      if (csrf && csrf.token) headers[csrf.header || 'X-CSRF-TOKEN'] = csrf.token;

      const res = await fetch(REGISTRAR_URL, {
        method: "POST",
        headers,
        body: JSON.stringify(payload),
        credentials: "same-origin"
      });

      if (!res.ok) {
        const txt = await res.text();
        throw new Error(txt || `HTTP ${res.status}`);
      }

      const json = await res.json();
      const id = json.idPartida || json.id_partida || json.id || (json.success ? json.idPartida : null);
      alert("Partida registrada. ID: " + id);
      // opcional: redirigir o recargar
      location.reload();

    } catch (err) {
      console.error("Error al registrar partida:", err);
      alert("Error al registrar partida: " + (err.message || err));
      submitBtn.disabled = false;
      submitBtn.innerText = originalText;
    }
  });

  // --- Inicialización: cargar cuentas y asegurarse de al menos una fila ---
  (async function init() {
    await cargarCuentasEnPagina();

    // si no hay más de una fila en cada contenedor, agregamos una adicional para facilitar entrada
    if (contenedorDebe.querySelectorAll(".account-row").length < 1) agregarFila(contenedorDebe);
    if (contenedorHaber.querySelectorAll(".account-row").length < 1) agregarFila(contenedorHaber);

    calcularTotales();
  })();

});
