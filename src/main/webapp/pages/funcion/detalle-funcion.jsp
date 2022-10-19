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
            Funcion funcion= (Funcion) request.getAttribute("datos");
        %>
        <div class="grid-container">


                <%-- AGREGAR COMPONENTES ACA--%>

                <h1 class="title">Detalle de función</h1>

                    <img src="https://hoy.com.do/wp-content/uploads/2022/04/Kooza.jpeg?mrf-size=m" alt="Foto de perfil" class="img_perfil">
            <div class="first-data">
                <h2><%=funcion.getNombre()%></h2>
                <h4></h4>
            </div>
            <div class="tabs">
                <div class="menu">
                        <p data-target="#datos_generales" class="active">Datos Generales</p>
                        <p data-target="#espectadores">Espectadores</p>


                </div>

                <div class="content">

                    <div data-content id="datos_generales" class="active">
                        <h4>Nombre:<%=funcion.getNombre()%></h4>
                        <h4>Espectaculo:<%=funcion.getEspectaculo().getNombre()%></h4>
                        <h4>Fecha y hora de inicio:<%=funcion.getFechaHoraInicio()%></h4>
                        <h4>Fecha de registro:<%=funcion.getFechaRegistro()%></h4>


                    </div>


                    <div data-content id="espectadores">
                        <table >
                            <tbody>
                            <%

                                Map<String, EspectadorRegistradoAFuncion> espectador_registrado_funcion = (Map<String, EspectadorRegistradoAFuncion>) request.getAttribute("espectadores");
                                for (EspectadorRegistradoAFuncion espect_reg_funcion : espectador_registrado_funcion.values()) {
                            %>
                            <tr>
                                <th onClick="location.href='detalle-usuario?nickname=<%=espect_reg_funcion.getEspectador().getNickname()%>'"> <%=espect_reg_funcion.getEspectador().getNickname()%>  </th>
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
