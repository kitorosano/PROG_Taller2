<%@ page import="java.util.Map" %>

<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="taller2.DTOs.*" %>
<%@ page import="taller2.E_EstadoEspectaculo" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    UsuarioDTO usuario = (UsuarioDTO) request.getAttribute("datos");
    Boolean esPerfilPropio = request.getAttribute("esPerfilPropio") != null ? (Boolean) request.getAttribute("esPerfilPropio") : false;
	Map<String, EspectaculoDTO> espectaculos = request.getAttribute("espectaculos") != null ? (Map<String, EspectaculoDTO>) request.getAttribute("espectaculos") : new HashMap<>();
	Map<String, EspectadorRegistradoAFuncionDTO> funciones = request.getAttribute("funciones") != null ? (Map<String, EspectadorRegistradoAFuncionDTO>) request.getAttribute("funciones") : new HashMap<>();
	Map<String, PaqueteDTO> paquetes = request.getAttribute("paquetes") != null ? (Map<String, PaqueteDTO>) request.getAttribute("paquetes") : new HashMap<>();
	String json = new Gson().toJson(usuario);
    
    Map<String, EspectaculoDTO> espectaculosAceptados = espectaculos.values()
                                                                 .stream()
                                                                 .filter(e -> e.getEstado().equals(E_EstadoEspectaculo.ACEPTADO))
                                                                 .collect(Collectors.toMap(EspectaculoDTO::getNombre, e -> e));

    System.out.println("es perfil propio?:"+esPerfilPropio);
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
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>

        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3><%= esPerfilPropio ? "Mi Perfil" : "Perfil de " + usuario.getNickname()%></h3>
            </div>
            <section>
                <%@ include file="/pages/sidebar.jsp" %>
                <div class="grid-container">
                    <%-- AGREGAR COMPONENTES ABAJO--%>
                    <img src="<%=usuario.getImagen()%>" alt="Foto de perfil" class="img_perfil">
                    <div class="first-data">
                        <h2><%=usuario.getNombre() + " " + usuario.getApellido() + " - " + (usuario.isEsArtista() ? "Artista" : "Espectador") %></h2>
                        <h4><span class="notbold"><%=usuario.getNickname() + " / " + usuario.getCorreo()%></span></h4>
                        <% if(esPerfilPropio) {%>
                            <a class="btn" href="modificar-usuario?nickname=<%=usuario.getNickname()%>">Modificar usuario</a>
                        <% } %>
                    </div>
                    <div class="tabs">
                        <div class="menu">

                            <p data-target="#datos_generales" class="active">Datos Generales</p>
                            <% if (usuario.isEsArtista()) { %>
                                <p data-target="#datos_artista">Datos Artista</p>
                                <p data-target="#espectaculos">Espectaculos</p>
                                 <% if (esPerfilPropio) { %>
                                    <p data-target="#finalizados">Espectaculos finalizados</p>
                                  <% } %>
                            <% }
                            else { %>
                                <p data-target="#funciones">Funciones</p>
                                <% if (esPerfilPropio) { %>
                                    <p data-target="#paquetes">Paquetes adquiridos</p>
                                <% }
                            } %>
                        </div>
                        
                        <div class="content">
                            <div data-content id="datos_generales" class="active">
                                <h4>Nombre: <span class="notbold"><%=usuario.getNombre()%></span></h4>
                                <h4>Apellido: <span class="notbold"><%=usuario.getApellido()%></span></h4>
                                <h4>Nickname: <span class="notbold"><%=usuario.getNickname()%></span></h4>
                                <h4>Correo: <span class="notbold"><%=usuario.getCorreo()%></span></h4>
                                <h4>Fecha de Nacimiento: <span class="notbold"><%=usuario.getFechaNacimiento()%></span></h4>
                            </div>
                            
                            <% if (usuario.isEsArtista()) { %>
                                <div data-content id="datos_artista">
                                    <h4>Descripción: <span class="notbold"><%=usuario.getDescripcion()%></span></h4>
                                    <h4>Biografia: <span class="notbold"><%=usuario.getBiografia()%></span></h4>
                                    <h4>Sitio Web:<span class="notbold"><%=usuario.getSitioWeb()%></span></h4>
                                </div>

                            <div data-content id="finalizados">
                                <table>
                                    <tbody>
                                    <% if (esPerfilPropio && espectaculos.size() == 0 ) { %>
                                    <tr>
                                        <th>
                                            <h4>
                                                <%= esPerfilPropio ? "No tienes espectaculos" : "No tiene espectaculos finalizados"%>
                                            </h4>
                                        </th>
                                    </tr>
                                    <% } else {
                                        for (EspectaculoDTO elem2 : espectaculos.values()) {
                                            if (elem2.getEstado() == E_EstadoEspectaculo.FINALIZADO && esPerfilPropio) { %>
                                    <tr onclick="location.href='detalle-espectaculo?nombre=<%=elem2.getNombre()%>&plataforma=<%=elem2.getPlataforma().getNombre()%>'">
                                        <th><%=elem2.getNombre()%> </th>
                                        <th> <%=elem2.getPlataforma().getNombre()%> </th>
                                    </tr>
                                     <%     }
                                        }
                                    }%>
                                    </tbody>
                                </table>
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
                                                for (EspectaculoDTO elem : espectaculos.values()) {
                                                    if (elem.getEstado() == E_EstadoEspectaculo.ACEPTADO) { %>
                                                        <tr onclick="location.href='detalle-espectaculo?nombre=<%=elem.getNombre()%>&plataforma=<%=elem.getPlataforma().getNombre()%>'">
                                                            <th><%=elem.getNombre()%> </th>
                                                            <th> <%=elem.getPlataforma().getNombre()%> </th>
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
                                                for (EspectadorRegistradoAFuncionDTO elem : funciones.values()) { %>
                                                    <tr onclick="location.href='detalle-funcion?nombre=<%=elem.getFuncion().getNombre()%>&espectaculo=<%=elem.getFuncion().getEspectaculo().getNombre()%>&plataforma=<%=elem.getFuncion().getEspectaculo().getPlataforma().getNombre()%>'">
                                                        <th><%=elem.getFuncion().getNombre()%> </th>
                                                        <th> <%=elem.getFuncion().getEspectaculo().getNombre()%> </th>
                                                        <th> <%=elem.getFuncion().getEspectaculo().getPlataforma().getNombre()%> </th>
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
                                                for (PaqueteDTO paquete : paquetes.values()) { %>
                                                    <tr>
                                                        <th>
                                                            <a href="detalle-paquete?nombre=<%=paquete.getNombre()%>"><%=paquete.getNombre()%>
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
                    <%-- AGREGAR COMPONENTES ARRIBA--%>
                </div>
            </section>
        </main>
    </div>
    
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
