<%@ page import="java.util.Map" %>

<%@ page import="java.time.LocalDateTime" %>
<%@ page import="taller2.DTOs.*" %>
<%@ page import="taller2.E_EstadoEspectaculo" %>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    boolean esArtista = (boolean) session.getAttribute("esArtista");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    Map<String, CategoriaDTO> categorias = (Map<String, CategoriaDTO>) request.getAttribute("categorias");
    Map<String, FuncionDTO> funciones = (Map<String, FuncionDTO>) request.getAttribute("funciones");
    Map<String, PaqueteDTO> paquetes = (Map<String, PaqueteDTO>) request.getAttribute("paquetes");
    
    EspectaculoDTO espectaculo= (EspectaculoDTO) request.getAttribute("datos");
    
    Random random = new Random();
%>
<html>
<head>
    <title>Detalle de espectaculo</title>
    <style><%@ include file="/pages/global.css" %></style>
    <style><%@ include file="/pages/detalles.css" %></style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3>Detalle de espectaculo</h3>
            </div>
        <section>
            <%@ include file="/pages/sidebar.jsp" %>
            <div class="grid-container">
                <%-- AGREGAR COMPONENTES ABAJO--%>
                <img src="<%=espectaculo.getImagen()%>" alt="Foto del espectaculo" class="img_perfil">
                <div class="first-data">
                    <h2><%=espectaculo.getNombre()%> - <%=espectaculo.getPlataforma().getNombre()%>
                        <span id="favorito" onclick="cambiarFavorito()"><i id="fav" class='fa fa-heart-o red-color'></i></span>
                    </h2>
                    <h4>Duración:<%=espectaculo.getDuracion()%>hs</h4>
                    <%  for (CategoriaDTO categoria : categorias.values()) {
                            int nextInt = random.nextInt(0xffffff + 1);
                            String randomColor = String.format("#%06x", nextInt); %>
                            <h5 class="sticker" style="background-color: <%= randomColor %>"><%=categoria.getNombre()%></h5>
                    <%  }   %>
                </div>
                <%  if(esArtista && espectaculo.getArtista().getNickname().equals(usuarioLogueado.getNickname())){
                        if(espectaculo.getEstado()== E_EstadoEspectaculo.ACEPTADO){  %>
                            <button class="btn2" onClick="location.href='registro-funcion'">Añadir funcion</button>
                <%      }
                    }   %>
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
                                <br>
                                <h4>Nombre de plataforma:<%=espectaculo.getPlataforma().getNombre()%></h4>
                                <h4 >Artista organizador: <span class="seleccion" onClick="location.href='perfil?nickname=<%=espectaculo.getArtista().getNickname()%>'"><%=espectaculo.getArtista().getNickname()%> </span></h4>
                            </div>
                            <div data-content id="descripcion">
                                <h4><%=espectaculo.getDescripcion()%></h4>
                            </div>
                            <div data-content id="funciones">
                                <table >
                                    <tbody>
                                    <%  for (FuncionDTO funcion : funciones.values()) {
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
                                    <%  for (PaqueteDTO paquete : paquetes.values()) {    %>
                                            <tr>
                                                <th onClick="location.href='detalle-paquete?nombre=<%=paquete.getNombre()%>'"> <%=paquete.getNombre()%></th>
                                            </tr>
                                    <%  }   %>
                                    </tbody>
                                </table>
                            </div>
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
    <script>
        let targets = document.querySelectorAll('[data-target]')
        const content = document.querySelectorAll('[data-content]')
        let estadoFavorito = false;
    
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
    
        function cambiarFavorito(){
            if (estadoFavorito == false){
                document.getElementById("fav").classList.remove("fa-heart-o");
                document.getElementById("fav").classList.add("fa-heart");
                estadoFavorito = true;
            } else {
                document.getElementById("fav").classList.remove("fa-heart");
                document.getElementById("fav").classList.add("fa-heart-o");
                estadoFavorito = false;
            }
        
            nickname = <%=usuarioLogueado.getNickname()%>;
            nombrePlataforma = <%=espectaculo.getPlataforma().getNombre()%>;
            nombreEspectaculo = <%=espectaculo.getNombre()%>;
        
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "DetalleEspectaculo?accion="+estadoFavorito+"&nickname="+nickname+"&nombrePlataforma="+nombrePlataforma+"&nombreEspectaculo="+nombreEspectaculo,false);
            xhttp.send();
        }

    </script>
</body>
</html>
