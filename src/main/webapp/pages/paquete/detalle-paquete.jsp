<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Detalle de paquete</title>
        <style><%@ include file="/pages/global.css" %></style>
        <style><%@ include file="/pages/detalles.css" %></style>
    </head>
    <body>
    <%@ include file="/pages/header.jsp" %>
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <%
            Paquete paquete= (Paquete) request.getAttribute("datos");
            String respuesta = (String) request.getAttribute("respuesta");
            System.out.println("La respuesta es: "+ respuesta);
        %>
        <div class="grid-container">


                <%-- AGREGAR COMPONENTES ACA--%>

                <h1 class="title">Detalle de paquete</h1>

                    <img src="https://cdn-icons-png.flaticon.com/512/44/44248.png" alt="Foto de perfil" class="img_perfil">
            <div class="first-data">
                <h2><%=paquete.getNombre()%></h2>
                <h4>Fecha de expiracion:<%=paquete.getFechaExpiracion()%></h4>

            </div>
            <%
                if(respuesta != "Paquete Adquirido"){
            %>
                    <form class="form" action="detalle-paquete" method="POST">
                        <button class="btn2" >Comprar Paquete</button>
                    </form>
            <%
                }else{
            %>
                    <h4 class="respuesta"><%=respuesta%></h4>
            <%
                }
            %>
            <div class="tabs">
                <div class="menu">
                        <p data-target="#datos_generales" class="active">Datos Generales</p>
                        <p data-target="#descripcion">Descripcion</p>
                        <p data-target="#espectaculos">Espectaculos</p>


                </div>

                <div class="content">

                    <div data-content id="datos_generales" class="active">
                        <h4>Nombre:<%=paquete.getNombre()%></h4>
                        <h4>Descuento:<%=paquete.getDescuento()%></h4>
                        <h4>Fecha de registro:<%=paquete.getFechaRegistro()%></h4>
                        <h4>Fecha de expiracion:<%=paquete.getFechaExpiracion()%></h4>

                    </div>

                    <div data-content id="descripcion">
                        <h4><%=paquete.getDescripcion()%></h4>


                    </div>


                    <div data-content id="espectaculos">
                        <table >
                            <tbody>
                            <%

                                Map<String, Espectaculo> espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculos");
                                for (Espectaculo espectaculo : espectaculos.values()) {
                            %>
                            <tr>
                                <th onClick="location.href='detalle-espectaculo?nombre=<%=espectaculo.getNombre()%>&plataforma=<%=espectaculo.getPlataforma().getNombre()%>'"> <%=espectaculo.getNombre()%> </th>
                            </tr>
                            <%
                                }
                            %>

                            </tbody>
                        </table>
                    </div>



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
