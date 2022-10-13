<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="main.java.taller1.Logica.Clases.Artista" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Detalle usuario</title>
        <style><%@ include file="/pages/global.css" %></style>
        <style><%@ include file="/pages/detalle-usuario.css" %></style>
    </head>
    <body>
    <%@ include file="/pages/header.jsp" %>
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <%
            Usuario usu= (Usuario) request.getAttribute("datos");
        %>
        <div class="grid-container">


                <%-- AGREGAR COMPONENTES ACA--%>

            <h1 class="title">Detalle usuario</h1>
            <div class="tabs">
                <div class="menu">
                    <p data-target="#datos_generales">Datos Generales</p>
                    <p data-target="#espectaculos">Espectaculos</p>
                    <p data-target="#descripcion">Descripcion</p>
                    <p data-target="#biografia">Biorgrafia</p>
                    <p data-target="#Funciones">Biorgrafia</p>
                </div>

                <div class="content">

                    <div data-content id="datos_generales" class="active">
                        <h4>Nombre:<%=usu.getNombre()%></h4>
                        <h4>Apellido:<%=usu.getApellido()%></h4>
                        <h4>Nickname:<%=usu.getNickname()%></h4>
                        <h4>Correo:<%=usu.getCorreo()%></h4>
                        <h4>Fecha de Nacimiento:<%=usu.getFechaNacimiento()%></h4>


                        <%
                            if(usu instanceof Artista){
                        %>

                                <h4>SioWeb:<%=((Artista) usu).getSitioWeb()%></h4>

                    </div>

                    <div data-content id="espectaculos">
                        <table class="table">
                            <tbody>
                            <%
                                Map<String, Espectaculo> espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculos");
                                for (Espectaculo elem : espectaculos.values()) {
                            %>
                            <tr>
                                <th> <%=elem.getNombre()%> </th>
                            </tr>
                            <%
                                }
                            %>

                            </tbody>
                        </table>
                    </div>

                    <div data-content id="descripcion">
                        <h4>Descripcion:<%=((Artista) usu).getDescripcion()%></h4>
                    </div>

                    <div data-content id="biografia">
                        <h4>Biografia:<%=((Artista) usu).getBiografia()%></h4>
                    </div>
                    <%
                        }else{
                    %>

                    <div data-content id="funciones">
                        <table class="table">
                            <tbody>
                            <%
                                Map<String, EspectadorRegistradoAFuncion> funciones =(Map<String, EspectadorRegistradoAFuncion>) request.getAttribute("funciones");
                                for (EspectadorRegistradoAFuncion elem : funciones.values()) {
                            %>
                            <tr>
                                <th> <%=elem.getFuncion().getNombre()%> </th>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </div>

                    <%
                        }
                    %>
                </div>
            </div>
            <div class="table">


            </div>
                    <a href="/pages/home.jsp" class="back">Volver</a>
        </div>

            <%--                AGREGAR COMPONENTES ACA--%>

    </section>

    <script>
        const targets = document.querySelectorAll('[data-target]')
        const content = document.querySelectorAll('[data-content]')

        targets.forEach(target => {
            target.addEventListener('click', () => {
                content.forEach(c => {
                    c.classList.remove('active')
                })
                const t = document.querySelector(target.dataset.target)
                t.classList.add('active')
            })
        })

    </script>
    </body>
</html>
