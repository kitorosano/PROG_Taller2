<%--
  Created by IntelliJ IDEA.
  User: sebas
  Date: 12/10/2022
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.Collection" %>
<%@ page import="main.java.taller1.Logica.DTOs.*" %>
<%  // Cargamos el usuarioLogueado en cada pantalla
    main.java.taller1.Logica.DTOs.UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
  String filtroPlataforma = request.getParameter("filtroPlataforma") != null ? request.getParameter("filtroPlataforma") : "";
  String filtroCategoria = request.getParameter("filtroCategoria") != null ? request.getParameter("filtroCategoria") : "";
  Map<String, Espectaculo> espectaculosFiltrados = request.getAttribute("espectaculosFiltrados") != null ? (Map<String, Espectaculo>) request.getAttribute("espectaculosFiltrados") : new HashMap<>();
  Map<String, Map<String, CategoriaDTO>> categoriasEspectaculosFiltrados = request.getAttribute("categoriasEspectaculosFiltrados") != null ? (Map<String, Map<String, CategoriaDTO>>) request.getAttribute("categoriasEspectaculosFiltrados") : new HashMap<>();
%>

<!DOCTYPE html>
<html>
<head>
  <style><%@ include file="/pages/global.css" %></style>
  <style><%@ include file="/pages/espectaculo/listado-espectaculos.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>CoronaTicketsUY</title>
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3>Listado de espectaculos</h3>
            </div>
            <section>
                <%@ include file="/pages/sidebar.jsp" %>
                <div class="main-container">
                <%-- AGREGAR COMPONENTES ABAJO--%>
                <div class="plataformas-categorias-container" style="display: flex; flex-direction: row;">
                    <form method="GET" action="listado-espectaculos" id="formEspectaculos" style="display: flex; gap: 20px">
                        <div>
                            <label for="filtroPlataforma">Selecciona una plataforma:</label>
                            <select name="filtroPlataforma" id="filtroPlataforma">
                                <option value="">Todas</option>
                                <% for (PlataformaDTO plataforma : plataformasMap.values()) { %>
                                    <option value="<%= plataforma.getNombre() %>"><%= plataforma.getNombre() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div>
                            <label for="filtroCategoria">Selecciona una categoria:</label>
                            <select name="filtroCategoria" id="filtroCategoria">
                                <option value="">Todas</option>
                                <% for (CategoriaDTO categoria : categoriasMap.values()) { %>
                                    <option value="<%= categoria.getNombre() %>"><%= categoria.getNombre() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div>
                            <button type="submit">Buscar</button>
                            <button onclick="resetForm()">Resetear</button>
                        </div>
                    </form>
                </div>
            
                <div class="busqueda">
                    <label for="txtBuscar">Filtrar espectaculo: </label>
                    <input type="text" name="buscarEspectaculo" id="txtBuscar" value="Espectaculo...">
                </div>
            
                <div>
                    <table class="tablaEspectaculos" id="tabla">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Categorias</th>
                                <th>Plataforma</th>
                                <th>Artista</th>
                            </tr>
                        </thead>
                        <tbody id="cuerpoTabla">
                        </tbody>
                    </table>
                </div>
                <%-- AGREGAR COMPONENTES ARRIBA--%>
              </div>
            </section>
        </main>
    </div>
    
    <%--Javascript--%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        // Declaramos elementos del DOM
        const TABLA = document.getElementById("cuerpoTabla");
        const FORM = document.getElementById("formEspectaculos");
        const SELECT_PLATAFORMA = document.getElementById("filtroPlataforma");
        const SELECT_CATEGORIA = document.getElementById("filtroCategoria");
        const TXT_BUSCAR = $("#txtBuscar");
      
        //CUANDO EL DOCUMENTO ESTE LISTO
        $(document).ready(function(){
            seleccionarFiltrosAnteriores();
            crearTabla();
        });
      
        function resetForm() {
            FORM.reset();
        }
      
        function seleccionarFiltrosAnteriores() {
            SELECT_PLATAFORMA.childNodes.forEach((option) => {
                if (option.value == "<%= filtroPlataforma %>") {
                    option.selected = true;
                }
            });
            SELECT_CATEGORIA.childNodes.forEach((option) => {
                if (option.value === "<%= filtroCategoria %>") {
                    option.selected = true;
                }
            });
        }
      
      
        function crearTabla(){
            let nuevaFila;
            let celdaEspectaculo;
            let celdaCategorias;
            let celdaPlataforma;
            let celdaArtista;
        
            <%for (Espectaculo elem : espectaculosFiltrados.values()) {%>
                nuevaFila = TABLA.insertRow(-1);
                celdaEspectaculo = nuevaFila.insertCell(0);
                celdaCategorias = nuevaFila.insertCell(1);
                celdaPlataforma = nuevaFila.insertCell(2);
                celdaArtista = nuevaFila.insertCell(3);
          
                celdaEspectaculo.innerHTML = "<%=elem.getNombre()%>";
                celdaCategorias.innerHTML = "";
                <%
                Collection<CategoriaDTO> categoriasEspectaculoFiltrado = categoriasEspectaculosFiltrados.get(elem.getNombre()).values();
                for (CategoriaDTO categoria : categoriasEspectaculoFiltrado) { %>
                    celdaCategorias.innerHTML += "<%=categoria.getNombre()%> ";
                <% } %>
                    celdaPlataforma.innerHTML = "<%=elem.getPlataforma().getNombre()%>";
                    celdaArtista.innerHTML = "<%=elem.getArtista().getNickname()%>";
            
                    nuevaFila.addEventListener("click", function(){
                        window.location.href = "detalle-espectaculo?nombre=<%=elem.getNombre()%>&plataforma=<%=elem.getPlataforma().getNombre()%>";
                    });
            <% } %>
        }
      
        //FUNCION PARA BUSCAR POR ESPECTACULOS EN TIEMPO REAL
        TXT_BUSCAR.on("keyup", function() {
            let keyword = this.value;
            keyword = keyword.toUpperCase();
            let table_1 = document.getElementById("tabla");
            let all_tr = table_1.getElementsByTagName("tr");
            for(let i=0; i<all_tr.length; i++){
                let name_column = all_tr[i].getElementsByTagName("td")[0];
                if(name_column){
                    let name_value = name_column.textContent || name_column.innerText;
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
        TXT_BUSCAR.click(function(){
            TXT_BUSCAR.val("");
        });
    
    </script>
</body>
</html>

