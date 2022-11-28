<%@ page import="java.util.*" %>
<%@ page import="main.java.taller1.Logica.Clases.*" %>
<%@ page import="main.java.taller1.Logica.DTOs.PaqueteDTO" %>
<%@ page import="main.java.taller1.Logica.DTOs.PlataformaDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% //CARGAR ALGUN@S ESPECTACULOS / FUNCIONES / PAQUETES / ARTISTA
    Map<String, PlataformaDTO> plataformasMap = request.getAttribute("todasPlataformas") != null ? (Map<String, PlataformaDTO>) request.getAttribute("todasPlataformas") : new HashMap<>();
    Map<String, Categoria> categoriasMap = request.getAttribute("todasCategorias") != null ? (Map<String, Categoria>) request.getAttribute("todasCategorias") : new HashMap<>();
    Map<String, PaqueteDTO> paquetesMap = request.getAttribute("todosPaquetes") != null ? (Map<String, PaqueteDTO>) request.getAttribute("todosPaquetes") : new HashMap<>();
    Map<String, Usuario> usuarioMap = request.getAttribute("todosUsuarios") != null ? (Map<String, Usuario>) request.getAttribute("todosUsuarios") : new HashMap<>();
    Map<String, Espectaculo> espectaculosMap = request.getAttribute("todosEspectaculos") != null ? (Map<String, Espectaculo>) request.getAttribute("todosEspectaculos") : new HashMap<>();
    
    // shuffle plataforma and filter up to 5
    List<String> todasPlataformas = new ArrayList<>(plataformasMap.keySet());
    Collections.shuffle(todasPlataformas);
    todasPlataformas = todasPlataformas.subList(0, Math.min(5, todasPlataformas.size()));
    
    // shuffle categorias and filter up to 5
    List<String> todasCategorias = new ArrayList<>(categoriasMap.keySet());
    Collections.shuffle(todasCategorias);
    todasCategorias = todasCategorias.subList(0, Math.min(5, todasCategorias.size()));
    
    // shuffle usuarios and filter up to 5
    List<String> todosUsuarios = new ArrayList<>(usuarioMap.keySet());
    Collections.shuffle(todosUsuarios);
    todosUsuarios = todosUsuarios.subList(0, Math.min(5, todosUsuarios.size()));
    
    // shuffle paquetes and filter up to 5
    List<PaqueteDTO> todosPaquetes = new ArrayList<>(paquetesMap.values());
    Collections.shuffle(todosPaquetes);
    todosPaquetes = todosPaquetes.subList(0, Math.min(3, todosPaquetes.size()));
    
    
    // shuffle espectaculos and filter up to 5
    List<Espectaculo> todosEspectaculos = new ArrayList<>(espectaculosMap.values());
    Collections.shuffle(todosEspectaculos);
    todosEspectaculos = todosEspectaculos.subList(0, Math.min(3, todosEspectaculos.size()));
%>
<div class="sidebars">
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
    </div>
    <div class="sidebar">
        <div class="sidebar__item">
            <h4>Categorias</h4>
            <ul>
                <% for (String cat : todasCategorias) { %>
                <%-- TODO: LLEVAR A LISTADO DE ESPECTACULOS POR PLATAFORMA --%>
                <li><a href="listado-espectaculos?filtroCategoria=<%=cat%>"><%=cat%></a></li>
                <hr>
                <% } %>
            </ul>
        </div>
    </div>
    <div class="sidebar">
        <div class="sidebar__item">
            <h4>Usuarios</h4>
            <ul>
                <% for (String usu : todosUsuarios) { %>
                <%-- TODO: LLEVAR A LISTADO DE ESPECTACULOS POR CATEGORIA --%>
                    <li><a href="perfil?nickname=<%=usu%>"><%=usu%></a></li>
                    <hr>
                <% } %>
            </ul>
        </div>
    </div>
</div>