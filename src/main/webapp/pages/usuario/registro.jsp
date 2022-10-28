<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<% //Traer datos precargados del request anterior
    String nickname = request.getParameter("nickname") instanceof String ? request.getParameter("nickname") : "";
    String nombre = request.getParameter("nombre") instanceof String ? request.getParameter("nombre") : "";
    String apellido = request.getParameter("apellido") instanceof String ? request.getParameter("apellido") : "";
    String correo = request.getParameter("correo") instanceof String ? request.getParameter("correo") : "";
    String fechaNac = request.getParameter("fechaNac") instanceof String ? request.getParameter("fechaNac") : "";
    String contrasenia = request.getParameter("contrasenia") instanceof String ? request.getParameter("contrasenia") : "";
    String contrasenia2 = request.getParameter("contrasenia2") instanceof String ? request.getParameter("contrasenia2") : "";
    String imagen = request.getParameter("imagen") instanceof String ? request.getParameter("imagen") : "";

    String descripcion = request.getParameter("descripcion") instanceof String ? request.getParameter("descripcion") : "";
    String biografia = request.getParameter("biografia") instanceof String ? request.getParameter("biografia") : "";
    String url = request.getParameter("url") instanceof String ? request.getParameter("url") : "";
%>
<!DOCTYPE html>
<html>
<head>
    <style>
        <%@ include file="/pages/global.css" %>
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
    <style>

        #idform {
            top: 50%;
            padding: 30px;
        }

        .subtitulos {
            width: 30%;
        }

        #idform input {
            width: 50%;
            float: left;
            text-align: left;
            line-height: 30px;
        }

        #idform textarea {
            width: 50%;
            float: left;
            text-align: left;
            resize: none;
            line-height: 60px;
        }

        .input-container {
            border-bottom: 1px solid #f2f2f2;
            padding-bottom: 20px;
            width: 100%;
            display: flex;
            -webkit-box-pack: justify;
            /*justify-content: space-between;*/
            -webkit-box-align: center;
            align-items: center;
            margin-bottom: 15px;
        }
    </style>

</head>
    <body>
    <%@ include file="/pages/header.jsp" %>

        <section>
            <%@ include file="/pages/sidebar.jsp" %>
            <div class="main-container">
                <%--                AGREGAR COMPONENTES ACA--%>
                <h1>Alta usuario</h1>
                <a href="../home.jsp">Volver</a>
                <form id="idform" name="myform" method="POST" action="registro" enctype="multipart/form-data">
                    <div style="display: flex; flex-direction: column; align-items: flex-start">
                        <div class="input-container">
                            <label>Espectador</label>
                            <input type="radio" name="tipo" value="Espectador" id="tipoEspectador" checked>
                            <label>Artista</label>
                            <input type="radio" name="tipo" value="Artista" id="tipoArtista">
                        </div>
                        <div id="camposComunes" style="display: flex; flex-direction: column; align-items: flex-start; width: 100%">
                            <div class="input-container">
                            <label class="subtitulos">Nickname</label>
                            <input type="text" name="nickname" placeholder="*Nickname..." maxlength="30" value="<%= nickname%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Nombre</label>
                                <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Apellido</label>
                                <input type="text" name="apellido" placeholder="*Apellido..." maxlength="30" value="<%= apellido%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Correo</label>
                                <input type="email" name="correo" placeholder="*Correo..." maxlength="50" value="<%= correo%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Fecha de nacimiento</label>
                                <input type="date" name="fechaNac" placeholder="*Fecha de nacimiento..."
                                       max="<%= LocalDate.now().toString() %>" value="<%= fechaNac%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Contraseña</label>
                                <input type="password" name="contrasenia" placeholder="*Contraseña..." maxlength="30"
                                       value="<%= contrasenia%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Repetir Contraseña</label>
                                <input type="password" name="contrasenia2" placeholder="*Repetir contraseña..." maxlength="30"
                                       value="<%= contrasenia2%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Imagen</label>
                                <input type="file" accept="image/*" name="imagen">
                            </div>
                        </div>
                        <div id="camposArtista" style="display: flex; flex-direction: column; align-items: flex-start; width: 100%">
                            <div class="input-container">
                                <label class="subtitulos">Descripcion</label>
                                <textarea name="descripcion" placeholder="*Descripcion..." maxlength="100"
                                          value="<%= descripcion%>"><%= descripcion%></textarea>
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">Biografia</label>
                                <input type="text" name="biografia" placeholder="Biografia..." maxlength="200"
                                       value="<%= biografia%>">
                            </div>
                            <div class="input-container">
                                <label class="subtitulos">URL del sitio web</label>
                                <input type="url" name="url" placeholder="Sitio Web Url..." maxlength="50" value="<%= url%>">
                            </div>
                        </div>
                        <button type="button" onclick="enviarForm()">Registrarse!</button>
                    </div>
                </form>

                <%
                    String error = (String) request.getAttribute("error");
                    if (error != "") {
                %>
                <div role="alert">
                    ${error}
                </div>
                <%
                    }
                %>

                <script src="https://code.jquery.com/jquery-3.6.1.min.js"
                        integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
                <%--    Javascript    --%>
                <script>
                    $(document).ready(function () {
                        $("#camposArtista").hide();

                        let tipoArtista = document.getElementById("tipoArtista");
                        let tipoEspectador = document.getElementById("tipoEspectador");

                        tipoArtista.onclick = () => {
                            $("#camposArtista").slideDown();
                        }
                        tipoEspectador.onclick = () => {
                            $("#camposArtista").slideUp();
                        }
                    });

                    function enviarForm() {
                        //Obtener inputs con jquery
                        let tipo = $("input[name='tipo']:checked").val();
                        let nickname = $("input[name='nickname']").val();
                        let nombre = $("input[name='nombre']").val();
                        let apellido = $("input[name='apellido']").val();
                        let correo = $("input[name='correo']").val();
                        let fechaNac = $("input[name='fechaNac']").val();
                        let contrasenia = $("input[name='contrasenia']").val();
                        let contrasenia2 = $("input[name='contrasenia2']").val();
                        let imagen = $("input[name='imagen']").val();
                        let descripcion = $("input[name='descripcion']").val();
                        let biografia = $("input[name='biografia']").val();
                        let url = $("input[name='url']").val();

                        let formularioValido = true;

                        // Validar campos vacios comunes
                        if (nickname === "" || nombre === "" || apellido === "" || correo === "" || fechaNac === "" || contrasenia === "" || contrasenia2 === "") {
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

                        let regexCorreo = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";

                        // Validar formato nickname
                        if (nickname.match(regexCorreo)) {
                            alert("Formato de nickname no valido, no puede ser un correo");
                            formularioValido = false;
                            return;
                        }
                        // Validar formato correo
                        if (!correo.match(regexCorreo)) {
                            alert("Formato de correo no valido");
                            formularioValido = false;
                            return;
                        }
                        //validar fecha nacimiento menor a hoy
                        if (new Date(fechaNac) > new Date()) {
                            alert("Fecha no valida");
                            formularioValido = false;
                            return;
                        }

                        if (tipo == "Artista") {
                            if (descripcion == "") {
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

                <%--                AGREGAR COMPONENTES ACA--%>
            </div>
        </section>
    </body>
</html>










