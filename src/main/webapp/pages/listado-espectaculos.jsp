<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %><%--
  Created by IntelliJ IDEA.
  User: sebas
  Date: 12/10/2022
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<% String nickname = request.getParameter("nickname") instanceof String ? request.getParameter("nickname") : ""; %>
<!DOCTYPE html>
<html>
<head>
  <style><%@ include file="./global.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="header.jsp" %>

  <section>
    <%@ include file="sidebar.jsp" %>
    <div class="main-container">
      <%--                AGREGAR COMPONENTES ACA--%>
        <div class="plataformas-categorias-container" style="display: flex; flex-direction: row;">
          <form method="POST" action="listado-espectaculos" id="formEspectaculos">
            <label for="plataforma">Selecciona una plataforma:</label>
            <select name="plataforma" id="plataforma">
              <%
                Map<String, Plataforma> plataformas= (Map<String, Plataforma>) request.getAttribute("plataformas");
                for (Plataforma elem : plataformas.values()) {
              %>
              <option value="<%=elem.getNombre()%>"><%=elem.getNombre()%></option>
              <% } %>
            </select>

            <label for="categorias">Selecciona una categoria:</label>
            <select name="categorias" id="categorias">
            </select>
            <button type="button" onclick="enviarForm()">Buscar</button>
          </form>

        </div>
        <div>
          <table class="tablaEspectaculos" id="tabla">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Artista</th>
              </tr>
            </thead>
            <tbody id="cuerpoTabla">
            </tbody>
          </table>
        </div>
      <%--                AGREGAR COMPONENTES ACA--%>
    </div>
  </section>

  <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
  <script>
    $(document).ready(function(){
      crearTabla();
    });
    <% if (request.getAttribute("espectaculos") != null){
    Map<String, Espectaculo> espectaculos= (Map<String, Espectaculo>) request.getAttribute("espectaculos");
    System.out.println(espectaculos.size());
    %>
      function crearTabla(){
        let tabla = document.getElementById("cuerpoTabla");
        <%//Map<String, Espectaculo> espectaculos= (Map<String, Espectaculo>) request.getAttribute("espectaculos");
        for (Espectaculo elem : espectaculos.values()) { %>
        let nuevaFila = tabla.insertRow(-1);
        let celdaEspectaculo = nuevaFila.insertCell(0);
        let celdaArtista = nuevaFila.insertCell(1);

        celdaEspectaculo.innerHTML = "<%=elem.getNombre()%>";
        celdaArtista.innerHTML = "<%=elem.getArtista().getNickname()%>";

        celdaEspectaculo.setAttribute('onClick','window.location.href = \'detalle-espectaculo.jsp\';');
        <% } %>
      }
    <%} %>


      function enviarForm(){
        $("#formEspectaculos").first().submit();
      }


  </script>
</body>
</html>

