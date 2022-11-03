<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.*" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    Usuario usuario = (Usuario) request.getAttribute("datos");
    Boolean esPerfilPropio = request.getAttribute("esPerfilPropio") != null ? (Boolean) request.getAttribute("esPerfilPropio") : false;
	Map<String, Espectaculo> espectaculos = request.getAttribute("espectaculos") != null ? (Map<String, Espectaculo>) request.getAttribute("espectaculos") : new HashMap<>();
	Map<String, EspectadorRegistradoAFuncion> funciones = request.getAttribute("funciones") != null ? (Map<String, EspectadorRegistradoAFuncion>) request.getAttribute("funciones") : new HashMap<>();
	Map<String, EspectadorPaquete> paquetes = request.getAttribute("paquetes") != null ? (Map<String, EspectadorPaquete>) request.getAttribute("paquetes") : new HashMap<>();
	String json = new Gson().toJson(usuario);
    
    Map<String, Espectaculo> espectaculosAceptados = espectaculos.values()
                                                                 .stream()
                                                                 .filter(e -> e.getEstado().equals(E_EstadoEspectaculo.ACEPTADO))
                                                                 .collect(Collectors.toMap(Espectaculo::getNombre, e -> e));

%>
<html>
<head>
	<title>Perfil Usuario</title>
	<style>
		<%@ include file="/pages/global.css" %>
	</style>
	<style>
		<%@ include file="/pages/detalles.css" %>
	</style>
</head>
<body>
    <%@ include file="/pages/header.jsp" %>
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <div class="grid-container">
            <%-- AGREGAR COMPONENTES ABAJO--%>
            <h1 class="title">Detalle de <%= usuario instanceof Artista ? "artista" : "espectador" %></h1>
            
            <img src="<%=usuario.getImagen()%>" alt="Foto de perfil" class="img_perfil">
            <div class="first-data">
                <h2><%=usuario.getNombre() + " " + usuario.getApellido()%></h2>
                <h4><%=usuario.getNickname() + " / " + usuario.getCorreo()%></h4>
                <% if(esPerfilPropio) {%>
                    <a class="btn" href="modificar-usuario?nickname=<%=usuario.getNickname()%>">Modificar usuario</a>
                <% } %>
            </div>
            <div class="tabs">
                <div class="menu">
                    <p data-target="#datos_generales" class="active">Datos Generales</p>
                    <% if (usuario instanceof Artista) { %>
                        <p data-target="#datos_artista">Datos Artista</p>
                        <p data-target="#espectaculos">Espectaculos</p>
                    <% } else { %>
                        <p data-target="#funciones">Funciones</p>
                        <% if (esPerfilPropio) { %>
                            <p data-target="#paquetes">Paquetes adquiridos</p>
                        <% }
                    } %>
                </div>
                
                <div class="content">
                    <div data-content id="datos_generales" class="active">
                        <h4>Nombre:<%=usuario.getNombre()%></h4>
                        <h4>Apellido:<%=usuario.getApellido()%></h4>
                        <h4>Nickname:<%=usuario.getNickname()%></h4>
                        <h4>Correo:<%=usuario.getCorreo()%></h4>
                        <h4>Fecha de Nacimiento:<%=usuario.getFechaNacimiento()%></h4>
                    </div>
                    
                    <% if (usuario instanceof Artista) { %>
                        <div data-content id="datos_artista">
                            <h4><%=((Artista) usuario).getDescripcion()%></h4>
                            <h4><%=((Artista) usuario).getBiografia()%></h4>
                            <h4>Sitio Web:<%=((Artista) usuario).getSitioWeb()%></h4>
                        </div>
                        
                        <div data-content id="espectaculos">
                            <table>
                                <tbody>
                                <% if (esPerfilPropio ? espectaculos.size() == 0 : espectaculosAceptados.size() == 0) { %>
                                    <tr>
                                        <th>
                                            <h4>
                                                <%= esPerfilPropio ? "No tienes espectaculos" : "No tiene espectaculos aceptados"%>
                                            </h4>
                                        </th>
                                    </tr>
                                <% } else {
                                        for (Espectaculo elem : espectaculos.values()) {
                                            if (elem.getEstado() == E_EstadoEspectaculo.ACEPTADO || esPerfilPropio) { %>
                                                <tr>
                                                    <th>
                                                        <a href="detalle-espectaculo?nombre=<%=elem.getNombre()%>&plataforma=<%=elem.getPlataforma().getNombre()%>"><%=elem.getNombre()%>
                                                        </a>
                                                    </th>
                                                </tr>
                                <%          }
                                        }
                                    }%>
                                </tbody>
                            </table>
                        </div>
                    <% } else { %>
                        <div data-content id="funciones">
                            <table class="table">
                                <tbody>
                                <% if (funciones.size() == 0) { %>
                                    <tr>
                                        <th>
                                            <h4>
                                                <%= esPerfilPropio ? "No tienes funciones" : "No tiene funciones"%>
                                            </h4>
                                        </th>
                                    </tr>
                                <% } else {
                                        for (EspectadorRegistradoAFuncion elem : funciones.values()) { %>
                                            <tr>
                                                <th>
                                                    <a href="detalle-funcion?nombre=<%=elem.getFuncion().getNombre()%>&espectaculo=<%=elem.getFuncion().getEspectaculo().getNombre()%>&plataforma=<%=elem.getFuncion().getEspectaculo().getPlataforma().getNombre()%>"><%=elem.getFuncion().getNombre()%>
                                                    </a>
                                                </th>
                                            </tr>
                                <%      }
                                    }%>
                                </tbody>
                            </table>
                        </div>
                        
                        <div data-content id="paquetes">
                            <table class="table">
                                <tbody>
                                <% if (paquetes.size() == 0) { %>
                                    <tr>
                                        <th>
                                            <h4>No tienes paquetes comprados</h4>
                                        </th>
                                    </tr>
                                <% } else {
                                        for (EspectadorPaquete paquete : paquetes.values()) { %>
                                            <tr>
                                                <th>
                                                    <a href="detalle-paquete?nombre=<%=paquete.getPaquete().getNombre()%>"><%=paquete.getPaquete().getNombre()%>
                                                    </a>
                                                </th>
                                            </tr>
                                <%      }
                                    } %>
                                </tbody>
                            </table>
                        </div>
                    <% } %>
                </div>
            </div>
    
            <button class="volver" onclick="history.back()">Volver</button>
            <%-- AGREGAR COMPONENTES ARRIBA--%>
        </div>
    </section>
    
    <%--    Javascript--%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
    
        $(document).ready(function () {
            let TABS = document.querySelectorAll('[data-target]');
            let CONTENTS = document.querySelectorAll('[data-content]');
    
            // Agregar evento click a cada tab
            TABS.forEach(TAB => {
                TAB.addEventListener('click', (e) => {
                    // Remover la clase active de todos los tabs y contents para luego agregarla al tab y content seleccionado
                    CONTENTS.forEach(content => {
                        content.classList.remove('active');
                    })
                    TABS.forEach(tab => {
                        tab.classList.remove('active');
                    })
    
                    let selectedTab = e.target;
                    selectedTab.classList.add('active');
                    let selectedContent = document.querySelector(selectedTab.dataset.target);
                    selectedContent.classList.add('active');
                })
            })
        });
    
    </script>
</body>
</html>
