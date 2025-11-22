//------------ FUNCIONALIDAD DEL BOTON DE CANCELAR --------------------
let botonCancelar = document.getElementById("cancelar");

let selectHaber = document.querySelectorAll('select[name=selectHaber]');
let selectDebe = document.querySelectorAll('select[name=selectDebe]');

botonCancelar.addEventListener("click", () => {
  let campos = document.querySelectorAll("input, textarea");

  campos.forEach((campo) => {
    campo.value = "";
  });

  // Dejar solo el primero y vaciarlo
  selectDebe.forEach((select, index) => {
    if (index === 0) {
      select.innerHTML = ""; // limpia las opciones
      cargarCuentasEnSelect(select); // recarga las cuentas
    } else {
      select.parentElement.parentElement.remove(); // elimina el bloque extra
    }
  });

  selectHaber.forEach((select, index) => {
    if (index === 0) {
      select.innerHTML = "";
      cargarCuentasEnSelect(select);
    } else {
      select.parentElement.parentElement.remove();
    }
  });
});

//-------------------------- FUNCIONALIDAD DE LOS BOTONES DE AGREGAR----------------------------
let contenedorDebe = document.getElementById("contenedorDebe");
let contenedorHaber = document.getElementById("contenedorHaber");

document.addEventListener('click', (e) => {

  console.log(bloquesDebe.length, bloquesHaber.length);

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
                    
        <!-- HABER -->
        <div class="form-group">
          <label for="selectHaber">Código y nombre de la cuenta</label>
          <select name="selectHaber" class="">
            ${opcionesHTML}
          </select>
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
          <select name="selectHaber" class="">
            ${opcionesHTML}
          </select>
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
    }
  } 

  //eliminar una fila
  else if (e.target.classList.contains("remove-btn")) {

    const bloquesDebe = contenedorDebe.querySelectorAll(".account-entries");
    const bloquesHaber = contenedorHaber.querySelectorAll(".account-entries");
    
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

  e.preventDefault();

  let fecha = document.getElementById('fecha_partida').value;
  let descripcion = document.getElementById('descripcion_partida').value;

  let cuentasDebe = contenedorDebe.querySelectorAll(".item-contenedor-debe");
  let cuentasHaber = contenedorHaber.querySelectorAll(".item-contenedor-haber");
  let montosDebe = contenedorDebe.querySelectorAll('input[name="monto-cuenta-debe"]');
  let montosHaber = contenedorHaber.querySelectorAll('input[name="monto-cuenta-haber"]');
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
  let tdFecha = document.createElement('td');
  tdFecha.innerText = fecha;

  //capturar el texto de la descripcion
  let tdDescripcion = document.createElement('td');
  tdDescripcion.innerText = descripcion;

  let selectCuentasDebe = contenedorDebe.querySelectorAll('select[name=selectDebe]');
  let montosFilaDebe = contenedorDebe.querySelectorAll('input[name=monto-cuenta-debe]');

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

let selectCuentasHaber = contenedorHaber.querySelectorAll('select[name=selectHaber]');
let montosFilaHaber = contenedorHaber.querySelectorAll('input[name=monto-cuenta-haber]');

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