//------------ FUNCIONALIDAD DEL BOTON DE CANCELAR --------------------
let botonCancelar = document.getElementById("cancelar");

let selectHaber = document.querySelectorAll('select[name=selectHaber]');
let selectDebe = document.querySelectorAll('select[name=selectDebe]');

botonCancelar.addEventListener("click", () => {

  let campos = document.querySelectorAll("input, textarea");

  campos.forEach((campo) => {
    campo.value = "";
  });

  contenedorDebe.innerHTML = `
          <div id="contenedorDebe" class="entry-item col mb-0">
                  
                  <div class="entry-header">
                    <h3>Debe</h3>
                    <button
                      type="button"
                      id="agregarCuentaDebe"
                      class="add-btn"
                    >
                      Agregar Cuenta
                    </button>
                  </div>

                  <!-- CAMPOS DEL DEBE  -->
                  <div class="account-entries">
                    <div class="account-row item-contenedor-debe">

                      <div class="form-group">
                        <label for="nombre">Código y nombre de la cuenta</label>
                        <select name="selectDebe"></select>
                      </div>

                      <div class="form-group">
                        <label>Monto</label>
                        <input
                          type="number"
                          name="monto-cuenta-debe"
                          step="0.01"
                          placeholder="0.00"
                        />
                      </div>

                      <button type="button" class="remove-btn">x</button>
                    </div>
                  </div>
                </div>
        `;

    contenedorHaber.innerHTML = `
          <div id="contenedorHaber" class="entry-item col mb-0">

                  <!-- CAMPOS DEL HABER -->
                  <div class="entry-header">
                    <h3>Haber</h3>
                    <button
                      type="button"
                      id="agregarCuentaHaber"
                      class="add-btn"
                    >
                      Agregar Cuenta
                    </button>
                  </div>

                  <div class="account-entries">
                    <div class="account-row item-contenedor-haber">

                      <div class="form-group">
                        <label for="nombre">Código y nombre de la cuenta</label>
                        <select name="selectHaber"></select>
                      </div>

                      <div class="form-group">
                        <label>Monto</label>
                        <input
                          type="number"
                          name="monto-cuenta-haber"
                          step="0.01"
                          placeholder="0.00"
                        />
                      </div>
                      <button type="button" class="remove-btn">x</button>
                    </div>
                  </div>
                </div>
        `;

    let selectLimpioDebe = contenedorDebe.querySelector('select[name=selectDebe]');
    let selectLimpioHaber = contenedorHaber.querySelector('select[name=selectHaber]');

    cargarCuentasEnSelect(selectLimpioDebe);
    cargarCuentasEnSelect(selectLimpioHaber);
});

//------------ FUNCIONALIDAD DEL BOTON DE AGREGAR EN EL DEBE --------------------
let btnAddCuentaDebe = document.getElementById("agregarCuentaDebe");
let contenedorDebe = document.getElementById("contenedorDebe");

btnAddCuentaDebe.addEventListener("click", () => {

  let contenedorNuevo = document.createElement("div");
  contenedorNuevo.classList.add("account-entries");

  contenedorNuevo.innerHTML = `
          <div class="account-row mt-3 item-contenedor-debe">
            
            <div class="form-group">
              <label for="nombre">Código y nombre de la cuenta</label>
              <select name="selectDebe"></select>
            </div>

            <div class="form-group">
              <label>Monto</label>
              <input
                type="number"
                name="monto-cuenta-debe"
                step="0.01"
                placeholder="0.00"/>
            </div>
            <button type="button" class="remove-btn">x</button>
          </div>
        `;

  contenedorDebe.appendChild(contenedorNuevo);
  let nuevoSelect = contenedorNuevo.querySelector('select[name=selectDebe]');

  //Cargar el select de Debe
  cargarCuentasEnSelect(nuevoSelect);
});

//------------ FUNCIONALIDAD DEL BOTON DE AGREGAR EN EL HABER --------------------
let aggCuenta = document.getElementById("agregarCuentaHaber");
let contenedorHaber = document.getElementById("contenedorHaber");

aggCuenta.addEventListener("click", () => {

  let contenedorNuevo = document.createElement("div");
  contenedorNuevo.classList.add("account-entries");

  contenedorNuevo.innerHTML = `
            <div class="account-row mt-3 item-contenedor-haber">
            
            <div class="form-group">
              <label for="nombre">Código y nombre de la cuenta</label>
              <select name="selectHaber"></select>
            </div>

            <div class="form-group">
              <label>Monto</label>
              <input
                type="number"
                name="monto-cuenta-haber"
                step="0.01"
                placeholder="0.00"
              />
            </div>
            <button type="button" class="remove-btn">x</button>
          </div>
          `;

  contenedorHaber.appendChild(contenedorNuevo);
  let nuevoSelect = contenedorNuevo.querySelector('select[name=selectHaber]');

  //Cargar el select de Haber
  cargarCuentasEnSelect(nuevoSelect);

});

//------------ FUNCIONALIDAD DEL BOTON DE ELIMINAR DE CADA DIV --------------------
document.addEventListener("click", function (event) {
  const bloquesDebe = contenedorDebe.querySelectorAll(".account-entries");
  const bloquesHaber = contenedorHaber.querySelectorAll(".account-entries");

  if (bloquesDebe.length > 0 || bloquesHaber.length > 0) {
    if (event.target.classList.contains("remove-btn")) {
      const fila = event.target.closest(".account-entries");

      if (fila) {
        fila.remove();
      }
    }
  }
});

//------------ CARGAR SELECTS DE LAS CUENTAS CUANDO CARGUE EL DOM --------------------
document.addEventListener("DOMContentLoaded", function () {
  // Cargar cuentas en ambos selects

  let selectHaberInicio = document.querySelector("select[name=selectHaber]");
  let selectDebeinicio = document.querySelector("select[name=selectDebe]");

  cargarCuentasEnSelect(selectHaberInicio);
  cargarCuentasEnSelect(selectDebeinicio);
});

function cargarCuentasEnSelect(select) {

  fetch("/api/cuentas")
    .then((response) => response.json())
    .then((cuentas) => {

      select.innerHTML = '<option value="">Seleccione una cuenta</option>';

        cuentas.forEach((cuenta) => {
          let option = document.createElement("option");

          option.value = cuenta.idCuenta;
          option.textContent = `${cuenta.codigo} - ${cuenta.nombre}`;
          option.dataset.codigo = cuenta.codigo;
          option.dataset.nombre = cuenta.nombre;

          select.appendChild(option);
        });
    })
    .catch((error) => console.error("Error:", error));
}



//------------ CALCULAR LOS TOTALES DEL DEBE Y HABER --------------------
let reg_partida = document.getElementById("reg_partida");

reg_partida.addEventListener("click", (e) => {

  e.preventDefault();

  let fecha = document.getElementById('fecha_partida').value;
  let descripcion = document.getElementById('descripcion_partida').value;

  let cuentasDebe = contenedorDebe.querySelectorAll(".item-contenedor-debe");
  let cuentasHaber = contenedorHaber.querySelectorAll(".item-contenedor-haber");
  let montosDebe = contenedorDebe.querySelectorAll('input[name="monto-cuenta-debe"]');
  let montosHaber = contenedorHaber.querySelectorAll('input[name="monto-cuenta-haber"]');
  let tbody = document.getElementById("detalle_partida");

  console.log(contenedorDebe.length, contenedorHaber.length);

  //calculo para los totales de los montos
  let totalDebe = 0;
  let totalHaber = 0;

  montosDebe.forEach((input) => {
    totalDebe += parseFloat(input.value) || 0;
  });

  montosHaber.forEach((input) => {
    totalHaber += parseFloat(input.value) || 0;
  });

  //capturar la fecha
  let tdFecha = document.createElement('td');
  tdFecha.innerText = fecha;

  //capturar el texto de la descripcion
  let tdDescripcion = document.createElement('td');
  tdDescripcion.innerText = descripcion;

  console.log(cuentasDebe.length, cuentasHaber.length);

  // tr.appendChild(tdFecha);
  // tr.appendChild(tdDescripcion);

  let selectCuentasDebe = contenedorDebe.querySelectorAll('select[name=selectDebe]');
  let montosFilaDebe = contenedorDebe.querySelectorAll('input[name=monto-cuenta-debe]');

  console.log("selectCuentasDebe:", selectCuentasDebe);
  console.log("montosFilaDebe:", montosFilaDebe);

  selectCuentasDebe.forEach((el, i) => console.log("SELECT", i, el));
  montosFilaDebe.forEach((el, i) => console.log("INPUT", i, el));

  for (let i = 0; i < selectCuentasDebe.length; i++) {

    let select = selectCuentasDebe[i];
    let input = montosFilaDebe[i];

    if (!select || !input) {
      console.warn("Faltan elementos en la fila", i);
      continue;
    }
  
    let tr = document.createElement("tr");
  
    // celda fecha
    tr.appendChild(tdFecha.cloneNode(true));
    // celda descripción
    tr.appendChild(tdDescripcion.cloneNode(true));

    // celda cuenta
    let tdCuenta = document.createElement("td");
    tdCuenta.textContent =
      selectCuentasDebe[i].options[selectCuentasDebe[i].selectedIndex].text;
    tr.appendChild(tdCuenta);

    // celda monto debe
    let tdMontoDebe = document.createElement("td");
    tdMontoDebe.textContent = `$${montosFilaDebe[i].value}`;
    tr.appendChild(tdMontoDebe);

    // celda monto haber
    let tdMontoHaber = document.createElement("td");
    tdMontoHaber.textContent = "$0.00";
    tr.appendChild(tdMontoHaber);

    // agregar fila al tbody
    tbody.appendChild(tr);
  }

  let selectCuentasHaber = contenedorHaber.querySelectorAll('select[name=selectHaber]');
  let montosFilaHaber = contenedorHaber.querySelectorAll('input[name=monto-cuenta-haber]');

  console.log("selectCuentashaber:", selectCuentasHaber);
console.log("montosFilaHaber:", montosFilaHaber);

selectCuentasHaber.forEach((el, i) => console.log("SELECT", i, el));
montosFilaHaber.forEach((el, i) => console.log("INPUT", i, el));

  for (let i = 0; i < selectCuentasHaber.length; i++) {

    let select = selectCuentasDebe[i];
    let input = montosFilaHaber[i];

    if (!select || !input) {
      console.warn("Faltan elementos en la fila", i);
      continue;
    }

    let tr = document.createElement("tr");
    // celda fecha
    tr.appendChild(tdFecha.cloneNode(true));
    // celda descripción
    tr.appendChild(tdDescripcion.cloneNode(true));

    // celda cuenta
    let tdCuenta = document.createElement("td");
    tdCuenta.textContent =
      selectCuentasHaber[i].options[selectCuentasHaber[i].selectedIndex].text;
    tr.appendChild(tdCuenta);
    
    // celda monto debe
    let tdMontoHaber = document.createElement("td");
    tdMontoHaber.textContent = "$0.00";
    tr.appendChild(tdMontoHaber);

    // celda monto haber
    let tdMontoDebe = document.createElement("td");
    tdMontoDebe.textContent = `$${montosFilaHaber[i].value}`;
    tr.appendChild(tdMontoDebe);

    // agregar fila al tbody
    tbody.appendChild(tr);
  }

  let trTotal = document.createElement('tr');

  trTotal.innerHTML = `
        <td><strong>Total</strong></td>
        <td></td>
        <td></td>
        <td><strong>$${totalDebe}</strong></td>
        <td><strong>$${totalHaber}</strong></td>
  `;

  tbody.appendChild(trTotal);

  console.log("Total Debe:", totalDebe, "Total Haber:", totalHaber);
});