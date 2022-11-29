<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.Clases.UsuarioDTO" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    
    String tipo = "";
    String nickname = "";
    String nombre = "";
    String apellido = "";
    String correo = "";
    String fechaNac = "";
    String contrasenia = "";
    String contrasenia2 = "";
    String imagen = "";
    String descripcion = "";
    String biografia = "";
    String url = "";
    
    
    //Traer datos precargados del request anterior
    if(messageType.equals("error")) {
        tipo = request.getParameter("tipo") != null ? request.getParameter("tipo") : "";
        nickname = request.getParameter("nickname") != null ? request.getParameter("nickname") : "";
        nombre = request.getParameter("nombre") != null ? request.getParameter("nombre") : "";
        apellido = request.getParameter("apellido") != null ? request.getParameter("apellido") : "";
        correo = request.getParameter("correo") != null ? request.getParameter("correo") : "";
        fechaNac = request.getParameter("fechaNac") != null ? request.getParameter("fechaNac") : "";
        contrasenia = request.getParameter("contrasenia") != null ? request.getParameter("contrasenia") : "";
        contrasenia2 = request.getParameter("contrasenia2") != null ? request.getParameter("contrasenia2") : "";
        imagen = request.getParameter("imagen") != null ? request.getParameter("imagen") : "";
    
        descripcion = request.getParameter("descripcion") != null ? request.getParameter("descripcion") : "";
        biografia = request.getParameter("biografia") != null ? request.getParameter("biografia") : "";
        url = request.getParameter("url") != null ? request.getParameter("url") : "";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="/pages/global.css" %></style>
    <style><%@ include file="/pages/usuario/registro.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CoronaTicketsUY</title>
</head>
<body>
    <div class="registro-container">
        <img src="https://i.imgur.com/d6cWesT.jpeg" alt="background_img"  />
        <div class="background_container">
        
            <div id="message" class="hidden <%=messageType%>" role="alert">
                <%=message%>
            </div>
    
            <div class="container">
                <h3>Registrate creando tu usuario</h3>
                <form id="idform" name="myform" method="POST" action="registro" enctype="multipart/form-data">
                    <div class="tipoUsuario">
                        <label for="tipoEspectador">Espectador</label>
                        <input type="radio" name="tipo" value="Espectador" id="tipoEspectador" <%= tipo.equals("Espectador") || tipo.isEmpty() ? "checked" : ""%> />
                        <label for="tipoArtista">Artista</label>
                        <input type="radio" name="tipo" value="Artista" id="tipoArtista" <%= tipo.equals("Artista") ? "checked" : ""%> />
                    </div>
                    <div id="camposComunes">
                        <div class="input-container">
                            <label class="subtitulos" for="nickname">Nickname</label>
                            <input type="text" id="nickname" name="nickname" placeholder="*Nickname..." maxlength="30" value="<%= nickname%>">
                        </div>
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
                            <label class="subtitulos" for="correo">Correo</label>
                            <input type="email" id="correo" name="correo" placeholder="*Correo..." maxlength="50" value="<%= correo%>">
                        </div>
                        <div class="input-group-container">
                            <div class="input-container">
                                <label class="subtitulos" for="contrasenia">Contraseña</label>
                                <input type="password" id="contrasenia" name="contrasenia" placeholder="*Contraseña..." maxlength="30"
                                       value="<%= contrasenia%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos" for="contrasenia2">Repetir Contraseña</label>
                                <input type="password" id="contrasenia2" name="contrasenia2" placeholder="*Repetir contraseña..." maxlength="30"
                                       value="<%= contrasenia2%>">
                            </div>
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
                    <button id="submitBtn" type="button" onclick="enviarForm()">Registrarse!</button>
                </form>
                <a href="login">Ya tienes una cuenta? Inicia Sesion!</a>
            </div>
        </div>
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

            let tipoArtista = document.getElementById("tipoArtista");
            let tipoEspectador = document.getElementById("tipoEspectador");
            
            if(tipoEspectador.checked){
                $("#camposArtista").hide()
            }

            tipoArtista.onclick = () => {
                $("#camposArtista").slideDown();
            }
            tipoEspectador.onclick = () => {
                $("#camposArtista").slideUp();
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
        
        function validarCamposVacios() {
            const NICKNAME = $("#nickname").val().trim()
            const NOMBRE = $("#nombre").val().trim()
            const APELLIDO = $("#apellido").val().trim()
            const CORREO = $("#correo").val().trim()
            const CONTRASENIA = $("#contrasenia").val().trim()
            const CONTRASENIA2 = $("#contrasenia2").val().trim()
            const FECHA_NAC = $("#fechaNac").val().trim()
    
            if (NICKNAME == "") {
                mensaje("El campo 'Nickname' no puede estar vacio");
                NICKNAME.addClass("invalid-input");
                return false;
            }
            if (NOMBRE == "") {
                mensaje("El campo 'Nombre' no puede estar vacio");
                NOMBRE.addClass("invalid-input");
                return false;
            }
            if (APELLIDO == "") {
                mensaje("El campo 'Apellido' no puede estar vacio");
                APELLIDO.addClass("invalid-input");
                return false;
            }
            if (CORREO == "") {
                mensaje("El campo 'Correo' no puede estar vacio");
                CORREO.addClass("invalid-input");
                return false;
            }
            if (CONTRASENIA == "") {
                mensaje("El campo 'Contraseña' no puede estar vacio");
                CONTRASENIA.addClass("invalid-input");
                return false;
            }
            if (CONTRASENIA2 == "") {
                mensaje("El campo 'Repetir Contraseña' no puede estar vacio");
                CONTRASENIA2.addClass("invalid-input");
                return false;
            }
            if (FECHA_NAC == "") {
                mensaje("El campo 'Fecha de nacimiento' no puede estar vacio");
                FECHA_NAC.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function validarContrasenias() {
            const CONTRASENIA = $("#contrasenia");
            const CONTRASENIA2 = $("#contrasenia2");
            
            if (CONTRASENIA.val().trim() != CONTRASENIA2.val().trim()) {
                mensaje("Las contraseñas no coinciden");
                CONTRASENIA.addClass("invalid-input");
                CONTRASENIA2.addClass("invalid-input");
                return false;
            }
            return true;
        }

        function validarFormatoNickname() {
            const NICKNAME = $("#nickname");
            const PATTERN =new RegExp(/^[A-Za-z0-9]+$/g);
            if (!NICKNAME.val().trim().match(PATTERN)) {
                mensaje("El campo 'Nickname' solo puede contener letras y numeros");
                NICKNAME.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function validarFormatoCorreo() {
            const CORREO = $("#correo").val().trim()
            const PATTERN = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$"
            
            if (!CORREO.match(PATTERN)) {
                mensaje("El campo 'Correo' no tiene un formato valido");
                CORREO.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function validarFechaNac() {
            const FECHA_NAC = $("#fechaNac");
    
            if (new Date(fechaNac) > new Date()) {
                mensaje("La fecha de nacimiento no puede ser mayor a la fecha actual");
                FECHA_NAC.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function validarArtista() {
            const DESCRIPCION = $("#descripcion");
            const URL = $("#url").val().trim();
            if (DESCRIPCION.val() == "") {
                mensaje("El campo 'Descripcion' no puede estar vacio");
                DESCRIPCION.addClass("invalid-input");
                return false;
            }
            
            let regexUrl = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
    
            if (URL != "" && !URL.match(regexUrl)) {
                mensaje("El campo 'URL' no tiene un formato valido");
                URL.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function enviarForm() {
            let tipo = $("#tipo").val();
            let SUBMITBUTTON = $("#submitBtn");
            SUBMITBUTTON.prop("disabled", true);
            
            let formularioValido = validarCamposVacios() && validarContrasenias() && validarFormatoNickname() && validarFormatoCorreo() && validarFechaNac();
            if(tipo === "Artista") {
                formularioValido = formularioValido && validarArtista();
            }
            
            //Enviar formulario con jquery
            if (formularioValido) {
                document.getElementById("idform").submit();
            }

        }
    </script>

</body>
</html>










