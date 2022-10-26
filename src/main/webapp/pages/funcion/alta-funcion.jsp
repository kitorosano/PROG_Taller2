<%@ page import="java.time.LocalDate" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  //Traer datos precargados del request anterior
  String nombrespectaculo = request.getParameter("espectaculo");
  String nombrefuncion= request.getParameter("nombre") instanceof String ? request.getParameter("nombre") : "";
  String fecha = request.getParameter("fechaInicio") instanceof String ? request.getParameter("fechaInicio") : "";
  String hora = request.getParameter("horaInicio") instanceof String ? request.getParameter("horaInicio") : "";
  String imagen = request.getParameter("imagen") instanceof String ? request.getParameter("imagen") : "";
  String[] artistasInvitados = request.getParameterValues("artInvitado");
  Map<String, Espectaculo> espectaculos= (Map<String, Espectaculo>) request.getAttribute("espectaculos");
  List<String> artistas= (List<String>) request.getAttribute("artistas");
%>

<!DOCTYPE html>
<html>
<head>
  <style><%@ include file="../global.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>JSP - Hello World</title>
  <style>

    #idform {
      top: 50%;
      padding: 30px;
    }
    .subtitulos{
      width: 30%;
    }
    #idform input {
      width: 50%;
      float: left;
      text-align: left;
      line-height: 30px;
    }
    #idform textarea {
      width: 50%;
      float: left;
      text-align: left;
      line-height: 60px;
    }
    .input-container{
      border-bottom: 1px solid #f2f2f2;
      padding-bottom: 20px;
      width: 100%;
      display: flex;
      -webkit-box-pack: justify;
      /*justify-content: space-between;*/
      -webkit-box-align: center;
      align-items: center;
      margin-bottom: 15px;
    }
  </style>
</head>
<body>
<%@ include file="../header.jsp" %>

<section>
  <%@ include file="../sidebar.jsp" %>
  <div class="main-container">
    <%--                AGREGAR COMPONENTES ACA--%>
      <h1>Registro de funcion de espectaculo</h1>
      <form id="idform" name="formEspectaculo" method="POST" action="alta-funcion" enctype="multipart/form-data">
        <div style="display: flex; flex-direction: column; align-items: flex-start">
          <div id="camposBasicos" style="display: flex; flex-direction: column; align-items: flex-start; width: 100%">
            <div class="input-container">
                <label class="subtitulos">Espectaculo</label>
                <select name="espectaculo" >
                <%
                  for (Espectaculo elem : espectaculos.values()) {
                %>
                <option value="<%= elem.getNombre()+"-"+elem.getPlataforma().getNombre()%>"><%=elem.getNombre()%></option>
                <%
                  }
                %>
                </select>
            </div>
            <div class="input-container">
              <label class="subtitulos">Nombre</label>
              <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%=nombrefuncion%>">
            </div>
            <div class="input-container">
              <label class="subtitulos">Fecha de inicio</label>
              <input type="date" name="fechaInicio" min="<%= LocalDate.now().toString() %>" value="<%=fecha%>">
            </div>
            <div class="input-container">
              <label class="subtitulos">Hora de inicio</label>
              <input type="time" name="horaInicio" value="<%=hora%>">
            </div>
            <div class="input-container">
              <label class="subtitulos">Imagen</label>
              <input type="file" accept="image/*" name="imagen">
            </div>
          </div>
          <div id="artistas-list">
            <p>Artistas a invitar</p>
            <select multiple name="listArtistas" id="listArtistas">
              <%
                for(String artista: artistas){
              %>
              <option value="<%=artista%>"><%=artista%></option>
              <%
                }
              %>
            </select>
            <button type="button" onclick="agregarArtista()">Invitar artista</button>
          </div>


          <select multiple name="artistasInvitados" id="artistasInvitados">
            <%
              if(artistasInvitados!=null){
                for(String nombInvitado:artistasInvitados){
            %>
            <option value="<%=nombInvitado%>"><%=nombInvitado%></option>
            <%
                }
              }
            %>
          </select>
          <button type="button" onclick="eliminarArtista()">Quitar artista</button>
          <button type="button" onclick="enviarForm()">Registrar!</button>
        </div>
      </form>

      <%
        String error = (String) request.getAttribute("error");
        if (error != "") {
      %>
      <div role="alert">
        ${error}
      </div>
      <%
        }
      %>

      <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>

      <script>

        function enviarForm() {
          //Obtener inputs con jquery
          let espectaculo = $("input[name='espectaculo']").val();
          let nombre = $("input[name='nombre']").val();
          let fecha = $("input[name='fechaInicio']").val();
          let hora = $("input[name='horaInicio']").val();
          let imagen = $("input[name='imagen']").val();
          let invitadosSelector = document.getElementById("artistasInvitados");

          let formularioValido = true;

          // Validar campos vacios
          if (espectaculo === "" || nombre === "" || fecha === "" || hora === "") {
            alert("Complete todos los campos obligatorios");
            formularioValido = false;
          }

          //Agrego un input invisible para cada elemento del select de invitados
          $("#artistasInvitados option").each(function() {
            let inputEnviar=document.createElement("input");
            inputEnviar.type="hidden";
            inputEnviar.value=this.value;
            inputEnviar.name="artInvitado";
            this.appendChild(inputEnviar);
          });

          //Enviar formulario con jquery
          if (formularioValido) {
            document.getElementById("idform").submit();
          } else {
            alert("EL FORMULARIO NO SE ENVIO POR INVALIDO")
          }
        }

        function agregarArtista(){
            //obtengo el valor del selector
            let artista = $("select[name='listArtistas']").val();
            //obtengo el objeto lista
            let artistasInvitados= document.getElementById("artistasInvitados");
            let invitado= document.createElement("option");
            invitado.text=artista;
            invitado.value=artista;
            artistasInvitados.appendChild(invitado);
            //Borro la categoria del select
            $('#listArtistas option[value="'+artista+'"]').remove();
        }

        function eliminarArtista(){
          let artista = $("select[name='artistasInvitados']").val();
          let artistas=document.getElementById("listArtistas");
          let invitado=document.createElement("option");
          invitado.text=artista;
          invitado.value=artista;
          artistas.appendChild(invitado);
          $('#artistasInvitados option[value="'+artista+'"]').remove();
        }
      </script>
    <%--                AGREGAR COMPONENTES ACA--%>
  </div>
</section>
</body>
</html>
