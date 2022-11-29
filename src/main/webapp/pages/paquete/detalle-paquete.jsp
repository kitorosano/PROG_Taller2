<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.*" %>
<%@ page import="main.java.taller1.Logica.DTOs.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%  // Cargamos el usuarioLogueado en cada pantalla
    UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    PaqueteDTO paquete = (PaqueteDTO) request.getAttribute("datos");
    String respuesta = (String) request.getAttribute("respuesta");
    Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
    
    Map<String, EspectaculoDTO> espectaculos = (Map<String, EspectaculoDTO>) request.getAttribute("espectaculos");

%>
<html>
<head>
    <title>Detalle de paquete</title>
    <style><%@ include file="/pages/global.css" %></style>
    <style><%@ include file="/pages/detalles.css" %></style>
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3>Detalle de paquete</h3>
            </div>
            <section>
                <%@ include file="/pages/sidebar.jsp" %>
                <div class="grid-container">
                    <%-- AGREGAR COMPONENTES ABAJO--%>
                    <img src="https://cdn-icons-png.flaticon.com/512/44/44248.png" alt="Foto del paquete" class="img_perfil">
                    <div class="first-data">
                        <h2><%=paquete.getNombre()%></h2>
                        <h4>Fecha de expiracion:<%=paquete.getFechaExpiracion()%></h4>
                    </div>
                    <%  if(respuesta != "Paquete Adquirido" && esEspectador.equals(true) ){ %>
                            <form class="form" action="detalle-paquete" method="POST">
                                <input type="hidden" name="nombre" value="<%=paquete.getNombre()%>">
                                <button class="btn2">Comprar Paquete</button>
                            </form>
                    <%  }else if((respuesta == "Paquete Adquirido") || (respuesta=="Paquete no encontrado")) { %>
                            <h4 class="respuesta"><%=respuesta%></h4>
                    <%  } %>
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
                                <table>
                                    <tbody>
                                    <% if (espectaculos.size() == 0) { %>
                                        <tr>
                                            <th>
                                                <h4>No tiene espectaculos</h4>
                                            </th>
                                        </tr>
                                    <% } else {
                                            for (EspectaculoDTO espectaculo : espectaculos.values()) { %>
                                                <tr  onclick="location.href='detalle-espectaculo?nombre=<%=espectaculo.getNombre()%>&plataforma=<%=espectaculo.getPlataforma().getNombre()%>'">
                                                    <th> <%=espectaculo.getNombre()%> </th>
                                                    <th> <%=espectaculo.getPlataforma().getNombre()%> </th>
                                                </tr>
                                     <%     }
                                        }   %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <%-- AGREGAR COMPONENTES ARRIBA--%>
            </section>
            </main>
        </div>

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
