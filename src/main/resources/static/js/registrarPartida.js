//Borrar el contenido de los inputs
let botonCancelar = document.getElementById("cancelar");

botonCancelar.addEventListener("click", () => {
  let campos = document.querySelectorAll("input, textarea");

  campos.forEach((campo) => {
    campo.value = "";
  });
});

//Funcionalidad de los botones agregar cuenta y eliminar

//Agregar cuenta DEBE
let btnAddCuentaDebe = document.getElementById("agregarCuentaDebe");
let contenedorDebe = document.getElementById("contenedorDebe");

btnAddCuentaDebe.addEventListener("click", () => {
  let contenedorNuevo = document.createElement("div");
  contenedorNuevo.classList.add("account-entries");

  contenedorNuevo.innerHTML = `
          <div class="account-row mt-3">
            
            <div class="form-group">
              <label for="nombre">Código y nombre de la cuenta</label>
              <select name="selectDebe" id="selectDebe"></select>
            </div>

            <div class="form-group">
              <label>Monto</label>
              <input
                type="number"
                id="monto-cuenta-debe"
                step="0.01"
                placeholder="0.00"/>
            </div>
            <button type="button" class="remove-btn">x</button>
          </div>
        `;

  contenedorDebe.appendChild(contenedorNuevo);

  //Cargar el select de Debe
  cargarCuentasEnSelect("selectDebe");
});

//Agregar cuenta HABER
let btnAddCuentaHaber = document.getElementById("agregarCuentaHaber");
let contenedorHaber = document.getElementById("contenedorHaber");

btnAddCuentaHaber.addEventListener("click", () => {
  let contenedorNuevo = document.createElement("div");
  contenedorNuevo.classList.add("account-entries");

  contenedorNuevo.innerHTML = `
            <div class="account-row mt-3">
            
            <div class="form-group">
              <label for="nombre">Código y nombre de la cuenta</label>
              <select name="selectHaber" id="selectHaber"></select>
            </div>

            <div class="form-group">
              <label>Monto</label>
              <input
                type="number"
                id="monto-cuenta-haber"
                step="0.01"
                placeholder="0.00"
              />
            </div>
            <button type="button" class="remove-btn">x</button>
          </div>
          `;

  contenedorHaber.appendChild(contenedorNuevo);

  //Cargar el select de Haber
  cargarCuentasEnSelect("selectHaber");
});

//Eliminar un div de una cuenta
document.addEventListener("click", function (event) {
  const bloquesDebe = contenedorDebe.querySelectorAll(".account-entries");
  const bloquesHaber = contenedorHaber.querySelectorAll(".account-entries");

  console.log(bloquesDebe.length, bloquesHaber.length);

  if (bloquesDebe.length > 0 || bloquesHaber.length > 0) {
    if (event.target.classList.contains("remove-btn")) {
      const fila = event.target.closest(".account-entries");

      if (fila) {
        fila.remove();
      }
    }
  }
});

document.addEventListener("DOMContentLoaded", function () {
  // Cargar cuentas en ambos selects
  cargarCuentasEnSelect("selectDebe");
  cargarCuentasEnSelect("selectHaber");
});

function cargarCuentasEnSelect(selectId) {
  fetch("/api/cuentas")
    .then((response) => response.json())
    .then((cuentas) => {
      let selects = document.querySelectorAll(`#${selectId}`);

      console.log(selects.length);

      selects.forEach((select) => {
        select.innerHTML = '<option value="">Seleccione una cuenta</option>';

        cuentas.forEach((cuenta) => {
          let option = document.createElement("option");

          option.value = cuenta.idCuenta;
          option.textContent = `${cuenta.codigo} - ${cuenta.nombre}`;
          option.dataset.codigo = cuenta.codigo;
          option.dataset.nombre = cuenta.nombre;

          select.appendChild(option);
        });
      });
    })
    .catch((error) => console.error("Error:", error));
}
let btnCalcularTotal = document.getElementById("btnTotales");

btnCalcularTotal.addEventListener("click", (e) => {
  e.preventDefault();

  let montosDebe = document.querySelectorAll('input[name="monto-cuenta-debe"]');
  let montosHaber = document.querySelectorAll(
    'input[name="monto-cuenta-haber"]'
  );

  let totalDebe = 0;
  let totalHaber = 0;

  montosDebe.forEach((input) => {
    totalDebe += parseFloat(input.value) || 0;
  });

  montosHaber.forEach((input) => {
    totalHaber += parseFloat(input.value) || 0;
  });

  document.getElementById("totalDebe").textContent = "$" + totalDebe.toFixed(2);
  document.getElementById("totalHaber").textContent =
    "$" + totalHaber.toFixed(2);

  let diferencia = Math.abs(totalDebe - totalHaber);
  document.getElementById("diferencia").textContent =
    "$" + diferencia.toFixed(2);

  console.log(totalDebe, totalHaber, diferencia);
});