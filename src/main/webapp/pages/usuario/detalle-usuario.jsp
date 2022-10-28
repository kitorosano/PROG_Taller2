<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Detalle usuario</title>
        <style><%@ include file="/pages/global.css" %></style>
        <style><%@ include file="/pages/detalles.css" %></style>
    </head>
    <body>
    <%@ include file="/pages/header.jsp" %>
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <%
            Usuario usu= (Usuario) request.getAttribute("datos");
            System.out.println(usu.getImagen());
        %>
        <div class="grid-container">


                <%-- AGREGAR COMPONENTES ACA--%>
            <%
                if(usu instanceof Artista){
            %>
                <h1 class="title">Detalle de artista</h1>
            <%
                }else{
            %>
                <h1 class="title">Detalle de espectador</h1>
            <%
                }
            %>
                    <img src="<%=usu.getImagen()%>" alt="Foto de perfil" class="img_perfil">
            <div class="first-data">
                <h2><%=usu.getNombre()+" "+usu.getApellido()%></h2>
                <h4><%=usu.getNickname()+" / "+usu.getCorreo()%></h4>
                <button class="btn" onClick="location.href='modificar-usuario?nickname=<%=usu.getNickname()%>'">Modificar usuario</button>
            </div>
            <div class="tabs">
                <div class="menu">
                    <p data-target="#datos_generales" class="active">Datos Generales</p>
                    <%
                        if(usu instanceof Artista){
                    %>
                        <p data-target="#espectaculos">Espectaculos</p>
                        <p data-target="#descripcion">Descripcion</p>
                        <p data-target="#biografia">Biografia</p>
                    <%
                        }
                        else{

                    %>
                        <p data-target="#funciones" >Funciones</p>

                    <%
                            if(session.getAttribute("nickname")==usu.getNickname()){
                    %>
                                <p data-target="#paquetes">Paquetes adquiridos</p>
                    <%
                            }
                        }
                    %>
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
                        <%
                            }
                        %>

                    </div>

                    <%
                        if(usu instanceof Artista){
                    %>
                    <div data-content id="espectaculos">
                        <table >
                            <tbody>
                            <%
                                System.out.println(session.getAttribute("nickname"));
                                //queda esperar a que este el estado de aceptado o cancelado para e filtro
                                Map<String, Espectaculo> espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculos");
                                for (Espectaculo elem : espectaculos.values()) {
                                    if(elem.getEstado()== E_EstadoEspectaculo.ACEPTADO){
                            %>
                                    <tr>
                                        <th onClick="location.href='detalle-espectaculo?nombre=<%=elem.getNombre()%>&plataforma=<%=elem.getPlataforma().getNombre()%>'"> <%=elem.getNombre()%> </th>
                                    </tr>
                            <%
                                    }
                                    if((elem.getEstado()== E_EstadoEspectaculo.RECHAZADO || elem.getEstado()== E_EstadoEspectaculo.INGRESADO)&& session.getAttribute("nickname")==usu.getNickname()){
                            %>
                                    <tr>
                                        <th onClick="location.href='detalle-espectaculo?nombre=<%=elem.getNombre()%>&plataforma=<%=elem.getPlataforma().getNombre()%>'"> <%=elem.getNombre()%> </th>
                                    </tr>
                            <%
                                    }
                                }
                            %>

                            </tbody>
                        </table>
                    </div>

                    <div data-content id="descripcion">
                        <h4><%=((Artista) usu).getDescripcion()%></h4>
                    </div>

                    <div data-content id="biografia">
                        <h4><%=((Artista) usu).getBiografia()%></h4>
                    </div>
                    <%
                        }
                        else{
                    %>

                    <div data-content id="funciones" >
                        <table class="table" >
                            <tbody>
                            <%
                                Map<String, EspectadorRegistradoAFuncion> funciones =(Map<String, EspectadorRegistradoAFuncion>) request.getAttribute("funciones");
                                for (EspectadorRegistradoAFuncion elem : funciones.values()) {

                            %>
                            <tr>

                                <th onClick="location.href='detalle-funcion?nombre_funcion=<%=elem.getFuncion().getNombre()%>&nombre_espectaculo=<%=elem.getFuncion().getEspectaculo().getNombre()%>&nombre_plataforma=<%=elem.getFuncion().getEspectaculo().getPlataforma().getNombre()%>'"> <%=elem.getFuncion().getNombre()%> </th>


                            </tr>
                            <%

                                }
                            %>
                            </tbody>
                        </table>
                    </div>

                    <div data-content id="paquetes">
                        <table class="table" >
                            <tbody>
                            <%
                                Map<String, EspectadorPaquete> paquetes =(Map<String, EspectadorPaquete>) request.getAttribute("paquetes");
                                for (EspectadorPaquete paquete : paquetes.values()) {

                            %>
                                    <tr>
                                        <th onClick="location.href='detalle-paquete?nombre_paquete=<%=paquete.getPaquete().getNombre()%>'"> <%=paquete.getPaquete().getNombre()%> </th>
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

                    <a href="/pages/home.jsp" class="back">Volver</a>
        </div>

            <%--                AGREGAR COMPONENTES ACA--%>

    </section>

    <script>
        var targets = document.querySelectorAll('[data-target]')
        const content = document.querySelectorAll('[data-content]')

        targets.forEach(target  => {
            console.log(target);

            target.addEventListener('click', () => {
                content.forEach(c => {
                    c.classList.remove('active')
                })
                targets.forEach(ts =>{
                  ts.classList.remove('active')
                })
                console.log(target.classList);
                const t = document.querySelector(target.dataset.target)
                t.classList.add('active')
                target.classList.add('active')
                console.log(target.classList);
            })
        })


    </script>
    </body>
</html>
