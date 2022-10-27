<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.*" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Detalle de espectaculo</title>
        <style><%@ include file="/pages/global.css" %></style>
        <style><%@ include file="/pages/detalles.css" %></style>
    </head>
    <body>
    <%@ include file="/pages/header.jsp" %>
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <%
            Espectaculo espectaculo= (Espectaculo) request.getAttribute("datos");
        %>
        <div class="grid-container">


                <%-- AGREGAR COMPONENTES ACA--%>

                <h1 class="title">Detalle de espectaculo</h1>

                    <img src="https://cloudfront-us-east-1.images.arcpublishing.com/infobae/252IOWPHVVHDTAP73WPMQ4F62Q.jpg" alt="Foto de perfil" class="img_perfil">
            <div class="first-data">
                <h2><%=espectaculo.getNombre()%></h2>
                <h4>Duración:<%=espectaculo.getDuracion()%>hs</h4>
                <%

                    Map<String, Categoria> categorias = (Map<String, Categoria>) request.getAttribute("categorias");
                    for (Categoria categoria : categorias.values()) {
                %>

                    <h5 class="sticker"><%=categoria.getNombre()%></h5>

                <%
                    }
                %>
            </div>
            <button class="btn2" onClick="location.href='alta-funcion?espectaculo=<%=espectaculo.getNombre()%>&plataforma=<%=espectaculo.getPlataforma().getNombre()%>'">Añadir funcion</button>
            <div class="tabs">
                <div class="menu">
                        <p data-target="#datos_generales" class="active">Datos Generales</p>
                        <p data-target="#descripcion">Descripcion</p>
                        <p data-target="#funciones">Funciones</p>
                        <p data-target="#paquetes">Paquetes</p>


                </div>

                <div class="content">

                    <div data-content id="datos_generales" class="active">
                        <h4>Nombre:<%=espectaculo.getNombre()%></h4>
                        <h4>Duración:<%=espectaculo.getDuracion()%>hs</h4>
                        <h4>Mínimo de espectadores:<%=espectaculo.getMinEspectadores()%></h4>
                        <h4>Máximo de espectadores:<%=espectaculo.getMaxEspectadores()%></h4>
                        <h4>Url:<%=espectaculo.getUrl()%></h4>
                        <h4>Costo:<%=espectaculo.getCosto()%></h4>
                        <h4>Fecha de registro:<%=espectaculo.getFechaRegistro()%></h4>
                        <h4>Nombre de plataforma:<%=espectaculo.getPlataforma().getNombre()%></h4>
                        <h4 >Artista organizador: <span class="seleccion" onClick="location.href='detalle-usuario?nickname=<%=espectaculo.getArtista().getNickname()%>'"><%=espectaculo.getArtista().getNickname()%> </span></h4>

                    </div>
                    <div data-content id="descripcion">
                        <h4>Descripcion:<%=espectaculo.getDescripcion()%></h4>
                    </div>

                    <div data-content id="funciones">
                        <table >
                            <tbody>
                            <%

                                Map<String, Funcion> funciones = (Map<String, Funcion>) request.getAttribute("funciones");
                                for (Funcion funcion : funciones.values()) {
                                    if(funcion.getFechaHoraInicio().plusHours((long)espectaculo.getDuracion()).isAfter(LocalDateTime.now())){

                            %>
                                        <tr>
                                            <th onClick="location.href='detalle-funcion?nombre_funcion=<%=funcion.getNombre()%>&nombre_espectaculo=<%=espectaculo.getNombre()%>&nombre_plataforma=<%=espectaculo.getPlataforma().getNombre()%>'"> <%=funcion.getNombre()%> %>  </th>
                                        </tr>
                            <%
                                    }
                                    System.out.println(funcion.getFechaHoraInicio().plusHours((long)espectaculo.getDuracion()).isBefore(LocalDateTime.now()));
                                    System.out.println(funcion.getFechaHoraInicio().plusHours((long)espectaculo.getDuracion()));
                                    System.out.println(LocalDateTime.now());
                                }
                            %>

                            </tbody>
                        </table>
                    </div>
                    <div data-content id="paquetes">
                        <table >
                            <tbody>
                            <%

                                 Map<String, Paquete> paquetes = (Map<String, Paquete>) request.getAttribute("paquetes");
                                 for (Paquete paquete : paquetes.values()) {
                            %>
                            <tr>
                                <th onClick="location.href='detalle-paquete?nombre_paquete=<%=paquete.getNombre()%>&plataforma=<%=espectaculo.getPlataforma().getNombre()%>'"> <%=paquete.getNombre()%></th>
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
