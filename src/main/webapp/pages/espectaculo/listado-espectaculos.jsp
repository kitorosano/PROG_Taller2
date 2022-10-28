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
<%@ page import="main.java.taller1.Logica.Clases.Categoria" %>
<%@ page import="java.util.HashMap" %>
<% String nickname = request.getParameter("nickname") instanceof String ? request.getParameter("nickname") : ""; %>
<!DOCTYPE html>
<html>
<head>
  <style><%@ include file="/pages/global.css" %></style>
  <style><%@ include file="/pages/espectaculo/listado-espectaculos.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="/pages/header.jsp" %>

  <section>
    <%@ include file="/pages/sidebar.jsp" %>
    <div class="main-container">
      <%--                AGREGAR COMPONENTES ACA--%>
        <div class="plataformas-categorias-container" style="display: flex; flex-direction: row;">
          <form method="GET" action="listado-espectaculos" id="formEspectaculos">
            <label for="plataforma">Selecciona una plataforma:</label>
            <select name="plataforma" id="plataforma">
              <option value="Todas" selected>Todas</option>
            </select>
            <label for="categoria">Selecciona una categoria:</label>
            <select name="categoria" id="categoria">
              <option value="Todas" selected>Todas</option>
              <!-- FALTA HACER EL SELECT POR CATEGORIAS  -->
            </select>
            <button type="submit">Buscar</button>
          </form>

          <form method="GET" action="listado-espectaculos" id="resetEspectaculos">
            <button type="submit">Resetear</button>
          </form>


        </div>

        <div class="busqueda">
        <br/>
        <label for="txtBuscar">Buscar espectaculo</label>
          <input type="text" name="buscarEspectaculo" id="txtBuscar" value="Espectaculo...">
        </div>

        <div>
          <h2>Espectaculos</h2>
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

    //CUANDO EL DOCUMENTO ESTE LISTO
    $(document).ready(function(){
      cargarPlataformas();
      cargarCategorias();
      <%if (request.getAttribute("totalEspectaculos") != null){%>
      crearTablaTotal();
    <%} else {%>
      crearTablaConsulta();
      <% } %>
    });

    //SI EL ATRIBUTO DEL REQUEST "totalEspectaculos" NO SE ENCUENTRA NULO
    <% if (request.getAttribute("totalEspectaculos") != null){ %>
      function crearTablaTotal(){
        <%Map<String, Espectaculo> totalEspectaculos = (Map<String, Espectaculo>) request.getAttribute("totalEspectaculos");%>

        let tabla = document.getElementById("cuerpoTabla");
        let nuevaFila = document.createElement("tr");
        let celdaEspectaculo = document.createElement("td");
        let celdaArtista = document.createElement("td");

        <%for (Espectaculo elem : totalEspectaculos.values()) {%>
        nuevaFila = tabla.insertRow(-1);
        celdaEspectaculo = nuevaFila.insertCell(0);
        celdaArtista = nuevaFila.insertCell(1);

        celdaEspectaculo.innerHTML = "<%=elem.getNombre()%>";
        celdaArtista.innerHTML = "<%=elem.getArtista().getNickname()%>";

        celdaEspectaculo.setAttribute('onClick',"location.href='detalle-espectaculo?nombre=<%=elem.getNombre()%>&plataforma=<%=elem.getPlataforma().getNombre()%>'");

        <% } %>
        }
    <% } else { %>
      function crearTablaConsulta(){

        <%Map<String, Espectaculo> espectaculos = new HashMap<>();

        if (request.getAttribute("espectaculosPlataformaCategoria") != null){
          espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculosPlataformaCategoria");
        } else if (request.getAttribute("espectaculosDePlataforma") != null){
          espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculosDePlataforma");
        } else {
          espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculosDeCategoria");
        } %>

        let tabla = document.getElementById("cuerpoTabla");
        let nuevaFila = document.createElement("tr");
        let celdaEspectaculo = document.createElement("td");
        let celdaArtista = document.createElement("td");
        <%for (Espectaculo elem : espectaculos.values()) {%>
        nuevaFila = tabla.insertRow(-1);
        celdaEspectaculo = nuevaFila.insertCell(0);
        celdaArtista = nuevaFila.insertCell(1);

        celdaEspectaculo.innerHTML = "<%=elem.getNombre()%>";
        celdaArtista.innerHTML = "<%=elem.getArtista().getNickname()%>";

        celdaEspectaculo.setAttribute('onClick',"location.href='detalle-espectaculo?nombre=<%=elem.getNombre()%>&plataforma=<%=elem.getPlataforma().getNombre()%>'");
        //celdaEspectaculo.setAttribute('onClick','window.location.href = \'detalle-espectaculo.jsp\';');
        <% } %>
        }
    <% } %>



    function cargarPlataformas(){
      <%Map<String, Plataforma> plataformas = (Map<String, Plataforma>) request.getAttribute("plataformas");%>
      let select = document.getElementById("plataforma");
      let opt;
      <%for (Plataforma elem : plataformas.values()) {%>
        opt = document.createElement('option');
        opt.value = "<%=elem.getNombre()%>";
        <%
        String s2 = elem.getNombre();
        String s1 = request.getParameter("plataforma") instanceof String ? request.getParameter("plataforma") : "";
        if (s1.equals(s2)){%>
          opt.selected="selected"
        <%}%>
        opt.innerHTML = "<%=elem.getNombre()%>";
        select.appendChild(opt);
      <% } %>
    }

    function cargarCategorias(){
      <%Map<String, Categoria> categorias = (Map<String, Categoria>) request.getAttribute("categorias");%>
      let select = document.getElementById("categoria");
      let opt;
      <%for (Categoria elem : categorias.values()) {%>
      opt = document.createElement('option');
      opt.value = "<%=elem.getNombre()%>";
      <%
      String s2 = elem.getNombre();
      String s1 = request.getParameter("categoria") instanceof String ? request.getParameter("categoria") : "";
      if (s1.equals(s2)){%>
      opt.selected="selected"
      <%}%>
      opt.innerHTML = "<%=elem.getNombre()%>";
      select.appendChild(opt);
      <% } %>
    }

    //FUNCION PARA BUSCAR POR ESPECTACULOS EN TIEMPO REAL
    $("#txtBuscar").on("keyup", function() {
      var keyword = this.value;
      keyword = keyword.toUpperCase();
      var table_1 = document.getElementById("tabla");
      var all_tr = table_1.getElementsByTagName("tr");
      for(var i=0; i<all_tr.length; i++){
        var name_column = all_tr[i].getElementsByTagName("td")[0];
        if(name_column){
          var name_value = name_column.textContent || name_column.innerText;
          name_value = name_value.toUpperCase();
          if(name_value.indexOf(keyword) > -1){
            all_tr[i].style.display = ""; // show
          }else{
            all_tr[i].style.display = "none"; // hide
          }
        }
      }
    });
    //limpiar textbox
    $("#txtBuscar").click(function(){
      $("#txtBuscar").val("");
    });

  </script>
</body>
</html>

