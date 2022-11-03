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
            <%-- AGREGAR COMPONENTES ABAJO--%>
            <h1 class="title">Detalle de espectaculo</h1>
            <img src="<%=espectaculo.getImagen()%>" alt="Foto del espectaculo" class="img_perfil">
            <div class="first-data">
                <h2><%=espectaculo.getNombre()%></h2>
                <h4>Duración:<%=espectaculo.getDuracion()%>hs</h4>
                <%  Map<String, Categoria> categorias = (Map<String, Categoria>) request.getAttribute("categorias");
                    for (Categoria categoria : categorias.values()) {   %>
                        <h5 class="sticker"><%=categoria.getNombre()%></h5>
                <%  }   %>
            </div>
            <%

                if(session.getAttribute("esArtista").equals(true) && espectaculo.getArtista().getNickname().equals(((Artista)session.getAttribute("usuarioLogueado")).getNickname())){
                    if(espectaculo.getEstado()==E_EstadoEspectaculo.ACEPTADO){
            %>
                        <button class="btn2" onClick="location.href='registro-funcion'">Añadir funcion</button>
            <%
                    }
                }
            %>
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
                        <h4 >Artista organizador: <span class="seleccion" onClick="location.href='perfil?nickname=<%=espectaculo.getArtista().getNickname()%>'"><%=espectaculo.getArtista().getNickname()%> </span></h4>
                    </div>
                    <div data-content id="descripcion">
                        <h4><%=espectaculo.getDescripcion()%></h4>
                    </div>
                    <div data-content id="funciones">
                        <table >
                            <tbody>
                            <%  Map<String, Funcion> funciones = (Map<String, Funcion>) request.getAttribute("funciones");
                                for (Funcion funcion : funciones.values()) {
                                    if(funcion.getFechaHoraInicio().plusHours((long)espectaculo.getDuracion()).isAfter(LocalDateTime.now())){   %>
                                        <tr>
                                            <th onClick="location.href='detalle-funcion?nombre=<%=funcion.getNombre()%>&espectaculo=<%=espectaculo.getNombre()%>&plataforma=<%=espectaculo.getPlataforma().getNombre()%>'"> <%=funcion.getNombre()%>   </th>
                                        </tr>
                            <%      }
                                }   %>
                            </tbody>
                        </table>
                    </div>
                    <div data-content id="paquetes">
                        <table >
                            <tbody>
                            <%  Map<String, Paquete> paquetes = (Map<String, Paquete>) request.getAttribute("paquetes");
                                 for (Paquete paquete : paquetes.values()) {    %>
                                    <tr>
                                        <th onClick="location.href='detalle-paquete?nombre=<%=paquete.getNombre()%>'"> <%=paquete.getNombre()%></th>
                                    </tr>
                            <%  }   %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        
            <button class="volver" onclick="history.back()">Volver</button>
        </div>
        <%-- AGREGAR COMPONENTES ARRIBA--%>
    </section>

    <%--    Javascript--%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        let targets = document.querySelectorAll('[data-target]')
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
