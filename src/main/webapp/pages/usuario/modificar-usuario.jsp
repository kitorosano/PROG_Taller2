<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  //Traer datos precargados del request anterior
    String nombre = request.getAttribute("nombre") instanceof String ? (String) request.getAttribute("nombre") : "";
    String apellido = request.getAttribute("apellido") instanceof String ? (String) request.getAttribute("apellido") : "";
    String fechaNac = request.getAttribute("fechaNac") instanceof String ? (String) request.getAttribute("fechaNac") : "";
    String contrasenia = request.getAttribute("contrasenia") instanceof String ? (String) request.getAttribute("contrasenia") : "";
    String imagen = request.getAttribute("imagen") instanceof String ? (String) request.getAttribute("imagen") : "";

    String descripcion = request.getAttribute("descripcion") instanceof String ? (String) request.getAttribute("descripcion") : "";
    String biografia = request.getAttribute("biografia") instanceof String ? (String) request.getAttribute("biografia") : "";
    String url = request.getAttribute("url") instanceof String ? (String) request.getAttribute("url") : "";
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    
%>
<!DOCTYPE html>
<html>
<head>
    <style>
        <%@ include file="/pages/global.css" %>
    </style>
    <style><%@ include file="/pages/usuario/registro.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CoronaTicketsUY</title>
</head>
<body>
<%@ include file="/pages/header.jsp" %>

<section>
    <%@ include file="/pages/sidebar.jsp" %>
    <div class="main-container">
        <%-- AGREGAR COMPONENTES ABAJO--%>
        <h3>Modifica tus datos de usuario</h3>
        <form id="idform" name="myform" method="POST" action="registro" enctype="multipart/form-data">
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
            <button type="button" onclick="enviarForm()">Registrarse!</button>
        </form>
        <button class="volver" onclick="history.back()">Volver</button>
        <%-- AGREGAR COMPONENTES ARRIBA--%>
    </div>
</section>

<%--    Javascript    --%>
<script src="https://code.jquery.com/jquery-3.6.1.min.js"
        integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
<script>
    function enviarForm() {
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
        
        // Validar contraseñas
        if (contrasenia !== contrasenia2) {
            alert("Las contraseñas no coinciden");
            formularioValido = false;
            return;
        }
        //validar fecha nacimiento menor a hoy
        if (new Date(fechaNac) > new Date()) {
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
            return;
        }
        
    }
</script>
</body>
</html>