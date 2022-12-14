<%--
  Created by IntelliJ IDEA.
  User: sebas
  Date: 6/10/2022
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="taller2.DTOs.*" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    //Traer datos precargados del request anterior
    String nickname = request.getParameter("nickname") != null ? request.getParameter("nickname") : "";
    String contrasenia = request.getParameter("contrasenia") != null ? request.getParameter("contrasenia") : "";
%>
<html>
<head>
    <style><%@ include file="../global.css" %></style>
    <style><%@ include file="/pages/usuario/login.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login</title>
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>

        <div class="container">
            <div class="container__left">
                <img
                  class="w-full object-cover h-full"
                  src="https://i.imgur.com/fDe4KjV.png"
                  alt="left_section_image"
                />
            </div>
            <div class="vertical"></div>
            <div class="container__right">
                <h3>Bienvenid@ a <span>CoronaTicketsUY</span></h3>

                <form id="idform" name="myform" method="POST" action="login">
                    
                    <label for='nickname'>Nickname o Correo *</label>
                    <input id="nickname" type="text" name="nickname" maxlength="30" value="<%= nickname%>">
                    <label for='contrasenia'>Contrase??a *</label>
                    <input id="contrasenia" type="password" name="contrasenia" maxlength="30" value="<%= contrasenia%>">

                    <a href="#" onclick="alert('una pena brother')" class="forgot">??Olvidaste tu contrase??a?</a>

                    <button id="submitBtn" type="button" onclick="enviarForm()">INGRESAR </button>
                </form>
                <a href="registro">No tienes una cuenta? Registrarte!</a>
                <br>
                <a href="home">Volver como invitado</a>
            </div>
        </div>
    </div>
    
    <%--    Javascript    --%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const MESSAGE = $("#message");
            const NICKNAME_INPUT = $("#nickname");
            const CONTRASENIA_INPUT = $("#contrasenia");
    
            if (MESSAGE.text().trim() != "") {
                MESSAGE.removeClass("hidden");
                setTimeout(() => {
                    MESSAGE.text("");
                    MESSAGE.addClass("hidden");
                }, 5000);
            } else {
                MESSAGE.addClass("hidden");
            }
    
            NICKNAME_INPUT.on("input", function() {
                MESSAGE.addClass("hidden");
                validarNickname();
            });
            
            CONTRASENIA_INPUT.on("input", function() {
                MESSAGE.addClass("hidden");
                validarContrasenia();
            });
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
                MESSAGE.removeClass("error");;
            }, 5000);
    
    
            SUBMITBUTTON.prop("disabled", false);
        }
    
        function validarNickname(){
            const NICKNAME_INPUT = $("#nickname");
            const NICKNAME = NICKNAME_INPUT.val();
            const REGEX_NICKNAME = /^[a-zA-Z0-9_]{1,30}$/;
            const REGEX_CORREO = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
    
            if (NICKNAME == "") {
                mensaje("El nickname no puede estar vac??o");
                NICKNAME_INPUT.addClass("invalidInput");
                return false;
            }
    
            if (!REGEX_NICKNAME.test(NICKNAME) && !REGEX_CORREO.test(NICKNAME)) {
                NICKNAME_INPUT.addClass("invalidInput");
                mensaje("El nickname debe ser alfanum??rico y/o un correo v??lido");
                return false;
            }
    
            NICKNAME_INPUT.removeClass("invalidInput");
            return true;
        }
    
        function validarContrasenia(){
            const CONTRASENIA_INPUT = $("#contrasenia");
            const CONTRASENIA = CONTRASENIA_INPUT.val();
    
            if (CONTRASENIA == "") {
                mensaje("La contrase??a no puede estar vac??a");
                CONTRASENIA_INPUT.addClass("invalidInput");
                return false;
            }
    
            CONTRASENIA_INPUT.removeClass("invalidInput");
            return true;
        }
    
        function enviarForm() {
            const SUBMITBUTTON = $("#submitBtn");
            SUBMITBUTTON.prop("disabled", true);
    
            let formularioValido = validarNickname() && validarContrasenia();
            if (formularioValido) {
                $("#idform").submit();
            }
        }
    </script>
</body>
</html>
