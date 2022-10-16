<%@ page import="java.time.LocalDate" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <style><%@ include file="../global.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="../header.jsp" %>

<section>
  <%@ include file="../sidebar.jsp" %>
  <div class="main-container">
    <%--                AGREGAR COMPONENTES ACA--%>
      <h1>Registro de funcion de espectaculo</h1>
      <form id="idform" name="formEspectaculo" method="POST" action="alta-funcion">
        <div style="display: flex; flex-direction: column; align-items: flex-start">
          <div id="camposComunes" style="display: flex; flex-direction: column; align-items: flex-start">
            Espectaculo<select name="espectaculo" >
            <%
              Map<String, Espectaculo> espectaculos= (Map<String, Espectaculo>) request.getAttribute("espectaculos");
              for (Espectaculo elem : espectaculos.values()) {
            %>
            <option value="<%= elem.getNombre()%>"><%=elem.getNombre()%></option>
            <%
              }
            %>
          </select>
            <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30">
            <input type="date" name="fechaInicio" min="<%= LocalDate.now().toString() %>">
            <input type="time" name="horaInicio">
            <input type="file" name="imagen">
          </div>
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

          let formularioValido = true;

          // Validar campos vacios
          if (espectaculo === "" || nombre === "" || fecha === "" || hora === "") {
            alert("Complete todos los campos obligatorios");
            formularioValido = false;
          }

          //Enviar formulario con jquery
          if (formularioValido) {
            document.getElementById("idform").submit();
          } else {
            alert("EL FORMULARIO NO SE ENVIO POR INVALIDO")
          }
        }
      </script>
    <%--                AGREGAR COMPONENTES ACA--%>
  </div>
</section>
</body>
</html>
