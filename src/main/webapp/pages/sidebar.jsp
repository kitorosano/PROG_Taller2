<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="main.java.taller1.Logica.Clases.Paquete" %>
<%@ page import="java.util.*" %>
<%@ page import="main.java.taller1.Logica.Clases.Categoria" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% //CARGAR ALGUN@S ESPECTACULOS / FUNCIONES / PAQUETES / ARTISTA
    Map<String, Plataforma> plataformasMap = request.getAttribute("todasPlataformas") != null ? (Map<String, Plataforma>) request.getAttribute("todasPlataformas") : new HashMap<>();
    Map<String, Categoria> categoriasMap = request.getAttribute("todasCategorias") != null ? (Map<String, Categoria>) request.getAttribute("todasCategorias") : new HashMap<>();
    Map<String, Paquete> paquetesMap = request.getAttribute("todosPaquetes") != null ? (Map<String, Paquete>) request.getAttribute("todosPaquetes") : new HashMap<>();
    
    Map<String, Espectaculo> espectaculosMap = request.getAttribute("todosEspectaculos") != null ? (Map<String, Espectaculo>) request.getAttribute("todosEspectaculos") : new HashMap<>();
    
    // shuffle plataforma and filter up to 5
    List<String> todasPlataformas = new ArrayList<String>(plataformasMap.keySet());
    Collections.shuffle(todasPlataformas);
    todasPlataformas = todasPlataformas.subList(0, Math.min(5, todasPlataformas.size()));
    
    // shuffle categorias and filter up to 5
    List<String> todasCategorias = new ArrayList<String>(categoriasMap.keySet());
    Collections.shuffle(todasCategorias);
    todasCategorias = todasCategorias.subList(0, Math.min(5, todasCategorias.size()));
    
    // shuffle paquetes and filter up to 5
    List<String> todosPaquetes = new ArrayList<String>(paquetesMap.keySet());
    Collections.shuffle(todosPaquetes);
    todosPaquetes = todosPaquetes.subList(0, Math.min(5, todosPaquetes.size()));
%>
<div class="sidebar">
    <div class="sidebar__item">
        <h4>Plataformas</h4>
        <ul>
            <% for (String pl : todasPlataformas) { %>
            <%-- TODO: LLEVAR A LISTADO DE ESPECTACULOS POR PLATAFORMA --%>
                <li><a href="listado-espectaculos?filtroPlataforma=<%=pl%>"><%=pl%></a></li>
                <hr>
            <% } %>
        </ul>
    </div>
    <div class="sidebar__item">
        <h4>Paquetes</h4>
        <ul>
            <% for (String paq : todosPaquetes) { %>
            <%-- TODO: LLEVAR A LISTADO DE ESPECTACULOS POR CATEGORIA --%>
                <li><a href="detalle-paquete?nombre=<%=paq%>"><%=paq%></a></li>
                <hr>
            <% } %>
        </ul>
    </div>
</div>