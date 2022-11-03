<%--
  Created by IntelliJ IDEA.
  User: esteban.rosano
  Date: 12/10/2022
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    //Si no hay usuario logueado, se redirige a la página de login
    if(usuarioLogueado == null){
        response.sendRedirect("home");
        return;
    }
%>
<!DOCTYPE html>
<div class="header">
    <div class="header__top">
        <div class="header__left">
            <img src="https://i.imgur.com/UYQjz9O.png" alt="coronaTicketsLogo" onclick="redirigirHome()" />
        </div>
        <div class="header__right" onclick="toggleMenu()">
            <div class="vertical">|</div>
            <p><%= usuarioLogueado.getNickname() %></p>
            <span>&#9660;</span>
            <img src="<%= usuarioLogueado.getImagen()%>" alt="user_img">
        </div>
        <div class="header__right__submenu_wrap">
            <div class="header__right__submenu">
                <div class="header__right__submenu__userinfo">
                    <img src="<%= usuarioLogueado.getImagen()%>" alt="user_img">
                    <div class="header__right__submenu__userinfo__text">
                        <h3><%= session.getAttribute("esArtista") != null && (boolean)session.getAttribute("esArtista") ? "Artista" : "Usuario" %></h3>
                        <hr>
                        <span><%= usuarioLogueado.getCorreo() %></span>
                    </div>
                </div>
                <hr>
                <%-- PERFIL USUARIO --%>
                <div class="header__right__submenu__options">
                    <a href="perfil">Mi perfil</a>
                    <span>></span>
                </div>
                
                <% if(session.getAttribute("esArtista") != null && (boolean)session.getAttribute("esArtista")){ %>
                <%-- AGREGAR ESPECTACULO A PAQUETE (ARTISTA) --%>
                
                <% } else { %>
                <%-- REGISTRARSE A UNA FUNCION (ESPECTADOR) --%>
                <div class="header__right__submenu__options">
                    <a href="registro-espectadores-a-funcion">Registrarse a Funcion</a>
                    <span>></span>
                </div>
                <% } %>
                
                <%-- CERRAR SESION --%>
                <div class="header__right__submenu__options">
                    <p onclick="cerrarSesion()">Cerrar Sesion</p>
                    <span>></span>
                </div>
                <form id="cerrarSesionForm" method="POST" action="home" hidden>
                    <button type="submit"></button>
                </form>
            </div>
        </div>
    </div>
    <div class="header__center">
        <div class="header__left">
            <ul>
                <li><a href="home">Inicio</a></li>
                <li><a href="espectaculos">Espectaculos</a></li>
                <li><a href="funciones">Funciones</a></li>
                <li><a href="paquetes">Paquetes</a></li>
                <li><a href="espectadores">Espectadores</a></li>
                <li><a href="artistas">Artistas</a></li>
            </ul>
        </div>
        <div class="header__right">
            <div class="header__right__search">
                <input type="text" placeholder="Buscar espectaculo, paquete, y más...">
                <button type="submit">
                    <img src="https://i.imgur.com/wkx2zCs.png" alt="search">
                </button>
            </div>
        </div>
    </div>
</div>

<%--Javascript--%>
<script>
    function redirigirHome(){
        window.location.href = "home";
    }
    function toggleMenu(){
        let menu = document.querySelector(".header__right__submenu_wrap");
        menu.classList.toggle("open-menu");
    }
    function cerrarSesion() {
        document.getElementById("cerrarSesionForm").submit();
    }
</script>