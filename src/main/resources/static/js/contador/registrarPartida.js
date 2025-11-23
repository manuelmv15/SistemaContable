//------------ FUNCIONALIDAD DEL BOTON DE CANCELAR --------------------
const botonCancelar = document.getElementById("cancelar");
const formPartida = document.querySelector('.entry-form');

//función auxiliar para limpiar un contenedor específico
//dejará solo el primer elemento con clase '.account-entries' y borrará el resto
function limpiarContenedor(contenedorId) {

    const contenedor = document.getElementById(contenedorId);
    if (!contenedor) return;

    const filas = contenedor.querySelectorAll('.account-entries');

    for (let i = filas.length - 1; i > 0; i--) {
        filas[i].remove();
    }

    const primerSelect = filas[0].querySelector('select');
    if (primerSelect) primerSelect.selectedIndex = 0;
}

botonCancelar.addEventListener("click", () => {

  formPartida.reset();

  limpiarContenedor('contenedorDebe');
  limpiarContenedor('contenedorHaber');

  console.log("formulario reseteado a su estado inicial.");
});

//-------------------------- FUNCIONALIDAD DE LOS BOTONES DE AGREGAR----------------------------
let contenedorDebe = document.getElementById("contenedorDebe");
let contenedorHaber = document.getElementById("contenedorHaber");

document.addEventListener('click', (e) => {

  if (e.target.classList.contains("add-btn")){

    const boton = e.target; // el botón que disparó el evento
    console.log(boton);
    const divPadre = boton.closest("div.entry-item"); // busca el div padre con clase entry-item
    const idPadre = divPadre.id; // obtiene el id del div padre

    console.log("id del div padre:", idPadre);

    const selectOriginal = divPadre.querySelector('select');
    const opcionesHTML = selectOriginal.innerHTML;

    if (idPadre === "contenedorDebe") {

      let contenedorNuevo = document.createElement("div");
      contenedorNuevo.classList.add("account-entries", "mb-3");

      contenedorNuevo.innerHTML = `
      <div class="account-row mb-3 item-contenedor-debe">
                    
        <!-- DEBE -->
        <div class="form-group">
          <label for="selectDebe">Código y nombre de la cuenta</label>
          <select name="selectDebe" required>
            ${opcionesHTML}
          </select>
        </div>
                    
        <div class="form-group">
          <label for="monto">Monto</label>
          <input type="number" name="monto-cuenta-debe" step="0.01" placeholder="0.00" min="1" required/>
        </div>
        <button type="button" class="remove-btn">x</button>
      </div>
      `;
      contenedorDebe.appendChild(contenedorNuevo);
    }
    else {
      
      let contenedorNuevo = document.createElement("div");
      contenedorNuevo.classList.add("account-entries");

      contenedorNuevo.innerHTML = `
      <div class="account-row mb-3 item-contenedor-haber">
                    
        <!-- HABER -->
        <div class="form-group">
          <label for="selectHaber">Código y nombre de la cuenta</label>
          <select name="selectHaber" required>
            ${opcionesHTML}
          </select>
        </div>
                    
        <div class="form-group">
          <label for="monto">Monto</label>
          <input type="number" name="monto-cuenta-haber" step="0.01" placeholder="0.00" min="1" required/>
        </div>
        <button type="button" class="remove-btn">x</button>
      </div>
      `;
      contenedorHaber.appendChild(contenedorNuevo);
    }
  } 

  //eliminar una fila
  else if (e.target.classList.contains("remove-btn")) {

    const bloquesDebe = contenedorDebe.querySelectorAll(".account-entries");
    const bloquesHaber = contenedorHaber.querySelectorAll(".account-entries");
    
    console.log(bloquesDebe.length, bloquesHaber.length);

    const fila = e.target.closest(".account-entries"); //encontrar la fila a quiere eliminar

    if (!fila) return;

    //determinar a qué contenedor padre pertenece esta fila
    const contenedorPadre = fila.closest(".entry-item"); //busca hacia arriba el div contenedor principal

    if (!contenedorPadre) return;

    const idContenedorPadre = contenedorPadre.id;

    let puedeEliminar = false; // validar según el lado específico

    if (idContenedorPadre === "contenedorDebe") {
      const cantidadActualDebe = contenedorPadre.querySelectorAll(".account-entries").length;
      puedeEliminar = cantidadActualDebe > 1;
    }
    else if (idContenedorPadre === "contenedorHaber") {
      const cantidadActualHaber = contenedorPadre.querySelectorAll(".account-entries").length; //recalcular las filas del debe
      puedeEliminar = cantidadActualHaber > 1; //calcular si solo hay 1
    }

    //eliminar fila
    if (puedeEliminar) {
      fila.remove();
      console.log("Fila eliminada del " + idContenedorPadre.replace("contenedor", ""));
    } else {
      alert("Debe mantener al menos una cuenta en cada lado.");
    }
  }
});

//------------ CALCULAR LOS TOTALES DEL DEBE Y HABER --------------------
let reg_partida = document.getElementById("reg_partida");

reg_partida.addEventListener("click", (e) => {

  //e.preventDefault();

  let fecha = document.getElementById('fecha_partida').value;
  let descripcion = document.getElementById('descripcion_partida').value;

  if(!fecha || !descripcion){
    alert('Hay campos vacios');
  }

  let cuentasDebe = contenedorDebe.querySelectorAll(".item-contenedor-debe");
  let cuentasHaber = contenedorHaber.querySelectorAll(".item-contenedor-haber");

  let isFullDebe = true;
  let isFullHaber = true;

  console.log(cuentasDebe.length, cuentasHaber.length);

  if (cuentasDebe.length === 0) isFullDebe = false;
  if (cuentasHaber.length === 0) isFullHaber = false;

  cuentasDebe.forEach((cuenta) => {

    let select = cuenta.querySelector('select[name=selectDebe]');
    let monto = cuenta.querySelector('input[name=monto-cuenta-debe]');
    
    console.debug(select.options.selectedIndex);

    if(select.options.selectedIndex === 0 || !monto.value > 0 || parseFloat(monto.value) <= 0){
      isFullDebe = false;

      //indicar al usuario cual falta
      select.style.borderColor = "red"; 
      monto.style.borderColor = "red";
    }

    else{
      select.style.borderColor = ""; 
      monto.style.borderColor = "";
    }
  });

  cuentasHaber.forEach((cuenta) => {

    let select = cuenta.querySelector('select[name=selectHaber');
    let monto = cuenta.querySelector('input[name=monto-cuenta-haber]');

    console.debug(select.selectedIndex);
      
    if(select.options.selectedIndex === 0 || !monto.value > 0 || parseFloat(monto.value) <= 0){
      isFullHaber = false;

      //indicar al usuario cual falta
      select.style.borderColor = "red";
      monto.style.borderColor = "red";
    }

    else{
    select.style.borderColor = ""; 
    monto.style.borderColor = "";
    }
  });

  if (!isFullDebe || !isFullHaber) {
    alert("Rellene todos los campos");
  } 
  else {
    console.debug("todo esta bien");

    let montosDebe = contenedorDebe.querySelectorAll(
      'input[name="monto-cuenta-debe"]'
    );
    let montosHaber = contenedorHaber.querySelectorAll(
      'input[name="monto-cuenta-haber"]'
    );
    let tbody = document.getElementById("detalle_partida");

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
    let tdFecha = document.createElement("td");
    tdFecha.innerText = fecha;

    //capturar el texto de la descripcion
    let tdDescripcion = document.createElement("td");
    tdDescripcion.innerText = descripcion;

    let selectCuentasDebe = contenedorDebe.querySelectorAll(
      "select[name=selectDebe]"
    );
    let montosFilaDebe = contenedorDebe.querySelectorAll(
      "input[name=monto-cuenta-debe]"
    );

    for (let i = 0; i < selectCuentasDebe.length; i++) {
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

    let selectCuentasHaber = contenedorHaber.querySelectorAll(
      "select[name=selectHaber]"
    );
    let montosFilaHaber = contenedorHaber.querySelectorAll(
      "input[name=monto-cuenta-haber]"
    );

    for (let i = 0; i < selectCuentasHaber.length; i++) {
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

    let trTotal = document.createElement("tr");

    trTotal.innerHTML = `
        <td><strong>Total</strong></td>
        <td></td>
        <td></td>
        <td><strong>$${totalDebe}</strong></td>
        <td><strong>$${totalHaber}</strong></td>
  `;

    tbody.appendChild(trTotal);

    console.log("Total Debe:", totalDebe, "Total Haber:", totalHaber);

    formPartida.reset();
    limpiarContenedor("contenedorDebe");
    limpiarContenedor("contenedorHaber");
  }
});