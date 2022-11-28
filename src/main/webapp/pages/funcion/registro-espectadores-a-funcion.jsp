<%@ page import="main.java.taller1.Logica.DTOs.FuncionDTO" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Paquete" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
  Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
  
  String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
  String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
  
  
  //Traer datos precargados del request anterior
  FuncionDTO funcion = (FuncionDTO) request.getAttribute("funcion");
  Map<String, EspectadorRegistradoAFuncion> registros = (Map<String,EspectadorRegistradoAFuncion>) request.getAttribute("registros");
  Map<String, Paquete> paquetes= (Map<String, Paquete>) request.getAttribute("paquetes");
%>
<html>
<head>
  <style><%@ include file="../global.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>CoronaTicketsUY</title>
  <style>
    #datos-principales{
      width: 100%;
      top: 50%;
      display: grid;
      grid-template-columns: 20% 40% 40%;
      justify-content: space-between;
    }
    #canjeos-container{
      width: 100%;
      display: grid;
      grid-template-columns: 50% 50%;
      justify-content: space-between;
      margin-bottom: 5%;
    }
    .canjeos-containers-selects{
      display: grid;
      grid-template-rows: 25% 70% 25%;
    }
    .subtitulos{
      width: 40%;
      margin-right: 5%;
    }
    .info-container{
      padding-bottom: 20px;
      width: 100%;
      display: flex;
      -webkit-box-pack: justify;
      -webkit-box-align: center;
      align-items: center;
      margin-bottom: 15px;
    }
  </style>
</head>
<body>
  <div class="background_container">
    <div id="message" class="hidden <%=messageType%>" role="alert">
      <%=message%>
    </div>
    
    <main class="coronaTicketsUY">
      <%@ include file="/pages/header.jsp" %>
      <div class="page-title">
        <h3>Registrarse a Funcion</h3>
      </div>
    <section>
      <%@ include file="/pages/sidebar.jsp" %>
      <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
        <div id="datos-principales">
          <img src="<%=funcion.getImagen()%>" alt="Foto de perfil" class="img_perfil" style="width: 150px; height: 150px">
          <div>
            <div class="info-container">
            <label class="subtitulos">Nombre</label>
            <p id="nombreFuncion"><%=funcion.getNombre()%></p>
            </div>
            <div class="info-container">
              <label class="subtitulos">Costo</label>
              <p><%=funcion.getEspectaculo().getCosto()%></p>
            </div>
            <div class="info-container">
              <label class="subtitulos">Duracion</label>
              <p><%=funcion.getEspectaculo().getDuracion()%></p>
            </div>
          </div>
          <div>
            <div class="info-container">
              <label class="subtitulos">Nombre de espectaculo</label>
              <p id="nombreEspectaculo"><%=funcion.getEspectaculo().getNombre()%></p>
            </div>
            <div class="info-container">
              <label class="subtitulos">Nombre de plataforma</label>
              <p id="nombrePlataforma"><%=funcion.getEspectaculo().getPlataforma().getNombre()%></p>
            </div>
          </div>
        </div>
        <div id="canjeos-container">
          <div class="canjeos-containers-selects">
            <label>Espectaculos a canjear</label>
            <select multiple name="EspectACanjear" id="EspectACanjear">
              <%  if(registros!=null){
                    for(EspectadorRegistradoAFuncion registro: registros.values()){
                      if(!registro.isCanjeado()){ %>
                        <option value="<%=registro.getFuncion().getNombre()+"-"+registro.getFuncion().getEspectaculo().getNombre()+"-"+registro.getFuncion().getEspectaculo().getPlataforma().getNombre()%>"><%=registro.getFuncion().getNombre()%></option>
              <%      }
                    }
                  }  %>
            </select>
            <button type="button" onclick="agregarEspectaculo()">Seleccionar espectaculo</button>
          </div>
          <div class="canjeos-containers-selects">
            <label>Espectaculos seleccionados</label>
            <select multiple name="EspectCanjeados" id="EspectCanjeados">
            </select>
            <button type="button" onclick="eliminarEspectaculo()">Quitar espectaculo</button>
          </div>
        </div>
        <div id="paquetes-container">
          <div class="canjeos-containers-selects">
            <label>Paquetes que incluyen el espectaculo</label>
            <select multiple name="PaquetesACanjear" id="PaquetesACanjear">
              <%  if(paquetes!=null){
                    for(Paquete paquete: paquetes.values()){  %>
                      <option value="<%=paquete.getNombre()%>"><%=paquete.getNombre()%></option>
              <%    }
                  } %>
            </select>
          </div>
        </div>
        <button id="submitBtn" type="button" onclick="enviarForm()">Confirmar</button>
      <%--                AGREGAR COMPONENTES ACA--%>
      </div>
    </section>
    </main>
  </div>

  <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
  <script>
    $(document).ready(function () {
      const MESSAGE = $("#message");
    
      if (MESSAGE.text().trim() != "") {
        MESSAGE.removeClass("hidden");
        setTimeout(() => {
          MESSAGE.text("");
          MESSAGE.addClass("hidden");
        }, 5000);
      } else {
        MESSAGE.addClass("hidden");
      }
    });
  
    function mensaje(msg) {
      const SUBMITBUTTON = $("#submitBtn");
      const MESSAGE = $("#message");
      MESSAGE.text(msg);
      MESSAGE.addClass("error");
      MESSAGE.removeClass("hidden");
    
      setTimeout(() => {
        MESSAGE.text("");
        MESSAGE.addClass("hidden");
        MESSAGE.removeClass("error");
      }, 5000);
  
      SUBMITBUTTON.prop("disabled", false);
    }
   //TODO: HOMOLOGAR MENSAJES DE VALICACION
    
    function agregarEspectaculo(){
      //obtengo el valor del selector
      let espectaculo = $("select[name='EspectACanjear']").val();
      //obtengo el objeto lista
      let EspectCanjeados= document.getElementById("EspectCanjeados");
      let canjeado= document.createElement("option");
      canjeado.text=espectaculo;
      canjeado.value=espectaculo;
      EspectCanjeados.appendChild(canjeado);
      //Borro la categoria del select
      $('#EspectACanjear option[value="'+espectaculo+'"]').remove();
    }
    function eliminarEspectaculo(){
      let espectaculo = $("select[name='EspectCanjeados']").val();
      let espectaculos=document.getElementById("EspectACanjear");
      let canjeado=document.createElement("option");
      canjeado.text=espectaculo;
      canjeado.value=espectaculo;
      espectaculos.appendChild(canjeado);
      $('#EspectCanjeados option[value="'+espectaculo+'"]').remove();
    }
    
    function enviarForm(){
      let form=document.createElement("form");
      let values=document.getElementById("EspectCanjeados").options;
      let paquete=$("#PaquetesACanjear option:selected").val();
      let espectaculo=document.createElement("input");
      let plataforma=document.createElement("input");
      let funcion=document.createElement("input");
      espectaculo.name="espectaculo";
      espectaculo.value=$("#nombreEspectaculo").text();
      espectaculo.type="hidden";
      plataforma.name="plataforma";
      plataforma.value=$("#nombrePlataforma").text();
      plataforma.type="hidden";
      funcion.name="funcion";
      funcion.value=$("#nombreFuncion").text();
      funcion.type="hidden";
      form.method="post";
      form.action="registro-espectadores-a-funcion";
      form.appendChild(espectaculo);
      form.appendChild(funcion);
      form.appendChild(plataforma);
      document.body.appendChild(form);
      if(values.length!==0){
        if(values.length===3){
          for (let option of values) {
            let canjeado=document.createElement("input");
            canjeado.type="hidden";
            canjeado.value=option.value;
            canjeado.name="registrosCanjeados";
            form.appendChild(canjeado);
          }
        }else{
          alert("Los registros deben ser tres (3) para que el costo del nuevo sea 0");
          return;
        }
      }
      if(paquete!==null){
        let paqCanjeado=document.createElement("input");
        paqCanjeado.type="hidden";
        paqCanjeado.name="paquete";
        paqCanjeado.value=paquete;
        form.appendChild(paqCanjeado);
      }
      form.submit();
    }
  </script>
</body>
</html>
