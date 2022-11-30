<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.LocalDateTime" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="taller2.DTOs.*" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    UsuarioDTO usuarioLogueado = session.getAttribute("usuarioLogueado") != null ? (UsuarioDTO) session.getAttribute("usuarioLogueado") : null;
    boolean esArtista = session.getAttribute("esArtista") != null ? (boolean) session.getAttribute("esArtista") : false;
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    //Traer datos precargados del request anterior
    String nombre = "";
    String apellido = "";
    String contrasenia = "";
    String fechaNac = "";
    String imagen = "";
    String descripcion = "";
    String biografia = "";
    String url = "";
    
    nombre = request.getAttribute("nombre") instanceof String ? (String) request.getAttribute("nombre") : usuarioLogueado.getNombre();
    apellido = request.getAttribute("apellido") instanceof String ? (String) request.getAttribute("apellido") : usuarioLogueado.getApellido();
    fechaNac = request.getAttribute("fechaNac") instanceof String ? (String) request.getAttribute("fechaNac") : usuarioLogueado.getFechaNacimiento().toString();
    contrasenia = request.getAttribute("contrasenia") instanceof String ? (String) request.getAttribute("contrasenia") : usuarioLogueado.getContrasenia();
    imagen = request.getAttribute("imagen") instanceof String ? (String) request.getAttribute("imagen") : usuarioLogueado.getImagen();
    
    if(esArtista) {
        descripcion = request.getAttribute("descripcion") instanceof String ? (String) request.getAttribute("descripcion") : ((UsuarioDTO) usuarioLogueado).getDescripcion();
        biografia = request.getAttribute("biografia") instanceof String ? (String) request.getAttribute("biografia") : ((UsuarioDTO) usuarioLogueado).getBiografia();
        url = request.getAttribute("url") instanceof String ? (String) request.getAttribute("url") : ((UsuarioDTO) usuarioLogueado).getSitioWeb();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <style>
        <%@ include file="/pages/global.css" %>
    </style>
    <style><%@ include file="/pages/registros.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CoronaTicketsUY</title>
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3>Modificar Perfil</h3>
            </div>
            <section>
                <%@ include file="/pages/sidebar.jsp" %>
                <div class="main-container">
                    <%-- AGREGAR COMPONENTES ABAJO--%>
                    <form id="idform" name="myform" method="POST" action="modificar-usuario" enctype="multipart/form-data">
                        <div id="camposComunes">
                            <div class="input-group-container">
                                <div class="input-container">
                                    <label class="subtitulos" for="nombre">Nombre</label>
                                    <input type="text" id="nombre" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                                </div>
                                <div class="input-container">
                                    <label class="subtitulos" for="apellido">Apellido</label>
                                    <input type="text" id="apellido" name="apellido" placeholder="*Apellido..." maxlength="30" value="<%= apellido%>">
                                </div>
                            </div>
                            <div class="input-container">
                                <label class="subtitulos" for="contrasenia">Contraseña</label>
                                <input type="password" id="contrasenia" name="contrasenia" placeholder="*Contraseña..." maxlength="30"
                                       value="<%= contrasenia%>">
                            </div>
                            <div class="input-group-container">
                                <div class="input-container">
                                    <label class="subtitulos" for="fechaNac">Fecha de nacimiento</label>
                                    <input type="date" id="fechaNac" name="fechaNac" placeholder="*Fecha de nacimiento..."
                                           max="<%= LocalDate.now().toString() %>" value="<%= fechaNac%>">
                                </div>
                                <div class="input-container">
                                    <label class="subtitulos" for="imagen">Imagen de perfil</label>
                                    <input type="file" accept="image/*" id="imagen" name="imagen" value="<%= imagen%>">
                                </div>
                            </div>
                        </div>
                        <% if(esArtista) {%>
                            <div id="camposArtista">
                                <div class="input-container">
                                    <label class="subtitulos" for="descripcion">Descripcion</label>
                                    <textarea id="descripcion" name="descripcion" placeholder="*Descripcion..." maxlength="100"
                                              value="<%= descripcion%>"><%= descripcion%></textarea>
                                </div>
                                <div class="input-container">
                                    <label class="subtitulos" for="biografia">Biografia</label>
                                    <input type="text" id="biografia" name="biografia" placeholder="Biografia..." maxlength="200"
                                           value="<%= biografia%>">
                                </div>
                                <div class="input-container">
                                    <label class="subtitulos" for="url">URL del sitio web</label>
                                    <input type="url" id="url" name="url" placeholder="Sitio Web Url..." maxlength="50" value="<%= url%>">
                                </div>
                            </div>
                        <% } %>
                        <button id="submitBtn" type="button" onclick="enviarForm()">Registrarse!</button>
                    </form>
                    <%-- AGREGAR COMPONENTES ARRIBA--%>
                </div>
            </section>
        </main>
    </div>
    
    <%--    Javascript    --%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js"
            integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        $(document).ready(function () {
            const MESSAGE = $("#message");
        
            if (MESSAGE.text().trim() != "") {
                MESSAGE.removeClass("hidden");
                setTimeout(() => {
                    MESSAGE.text("");
                    MESSAGE.addClass("hidden");
                }, 5000);
            } else {
                MESSAGE.addClass("hidden");
            }
        });
    
        function mensaje(msg) {
            const SUBMITBUTTON = $("#submitBtn");
            const MESSAGE = $("#message");
            MESSAGE.text(msg);
            MESSAGE.addClass("error");
            MESSAGE.removeClass("hidden");
        
            setTimeout(() => {
                MESSAGE.text("");
                MESSAGE.addClass("hidden");
                MESSAGE.removeClass("error");
            }, 5000);
    
            SUBMITBUTTON.prop("disabled", false);
        }
        //TODO: HOMOLOGAR MENSAJES DE VALICACION
        
        function enviarForm() {
            let SUBMITBUTTON = $("#submitBtn");
            SUBMITBUTTON.prop("disabled", true);
            
            //Obtener inputs con jquery
            let tipo = $("input[name='tipo']").val();
            let nombre = $("input[name='nombre']").val();
            let apellido = $("input[name='apellido']").val();
            let fechaNac = $("input[name='fechaNac']").val();
            let contrasenia = $("input[name='contrasenia']").val();
            let contrasenia2 = $("input[name='contrasenia2']").val();
            let imagen = $("input[name='imagen']").val();
            let descripcion = $("input[name='descripcion']").val();
            let biografia = $("input[name='biografia']").val();
            let url = $("input[name='url']").val();
            
            let formularioValido = true;
            
            // Validar campos vacios comunes
            if ( nombre === "" || apellido === "" || fechaNac === "" || contrasenia === "" || contrasenia2 === "") {
                alert("Complete todos los campos obligatorios");
                formularioValido = false;
                return;
            }
    
            /*
            // Validar contraseñas
            if (contrasenia !== contrasenia2) {
                alert("Las contraseñas no coinciden");
                formularioValido = false;
                return;
            }
            */
    
            //validar fecha nacimiento menor a hoy y que no haya nacido en el siglo 19 para abajo
            if ((new Date(fechaNac) > new Date()) || (new Date(fechaNac).getFullYear() < 1900)){
                alert("Fecha no valida");
                formularioValido = false;
                return;
            }
            
            if (tipo === "Artista") {
                if (descripcion === "") {
                    alert("Complete todos los campos obligatorios");
                    formularioValido = false;
                }
                
                let regexUrl = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
                if (url != "" && !url.match(regexUrl)) {
                    alert("Formato de url no valido");
                    formularioValido = false;
                    return;
                }
            }
            
            //Enviar formulario con jquery
            if (formularioValido) {
                document.getElementById("idform").submit();
            } else {
                alert("EL FORMULARIO NO SE ENVIO POR INVALIDO")
                const SUBMITBUTTON = $("#submitBtn");
                SUBMITBUTTON.prop("disabled", false);
            }
            
        }
    </script>
</body>
</html>