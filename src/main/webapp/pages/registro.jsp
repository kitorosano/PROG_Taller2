<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: esteban.rosano
  Date: 6/10/2022
  Time: 1:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  //Traer datos precargados del request anterior
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
<html>
    <head>
        <title>Alta usuario</title>
    </head>
    <body>
        <h1>Alta usuario</h1>
        <a href="home.jsp">Volver</a>
        <form id="idform" name="myform" method="POST" action="registro">
            <div style="display: flex; flex-direction: column; align-items: flex-start">
                <label>Espectador</label>
                <input type="radio" name="tipo" value="Espectador" id="tipoEspectador" checked>
                <label>Artista</label>
                <input type="radio" name="tipo" value="Artista" id="tipoArtista">
                <div id="camposComunes" style="display: flex; flex-direction: column; align-items: flex-start">
                    <input type="text"  name="nickname" placeholder="*Nickname..." maxlength="30" value="<%= nickname%>">
                    <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                    <input type="text" name="apellido" placeholder="*Apellido..." maxlength="30" value="<%= apellido%>">
                    <input type="email" name="correo" placeholder="*Correo..." maxlength="50" value="<%= correo%>">
                    <input type="date" name="fechaNac" placeholder="*Fecha de nacimiento..." max="<%= LocalDate.now().toString() %>" value="<%= fechaNac%>">
                    <input type="password" name="contrasenia" placeholder="*Contraseña..." maxlength="30" value="<%= contrasenia%>">
                    <input type="password" name="contrasenia2" placeholder="*Repetir contraseña..." maxlength="30" value="<%= contrasenia2%>">
                    <input type="file" name="imagen">
                </div>
                <div id="camposArtista" style="display: flex; flex-direction: column; align-items: flex-start">
                    <input type="textarea" name="descripcion" placeholder="*Descripcion..." maxlength="100" value="<%= descripcion%>">
                    <input type="text" name="biografia" placeholder="Biografia..." maxlength="200" value="<%= biografia%>">
                    <input type="url" name="url" placeholder="Sitio Web Url..." maxlength="50" value="<%= url%>">
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

        <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
<%--    Javascript    --%>
        <script>
            $(document).ready(function(){
                $("#camposArtista").hide();

                let tipoArtista = document.getElementById("tipoArtista");
                let tipoEspectador = document.getElementById("tipoEspectador");

                tipoArtista.onclick= () => {
                    $("#camposArtista").slideDown();
                }
                tipoEspectador.onclick= () => {
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
                }

                // Validar contraseñas
                if (contrasenia !== contrasenia2) {
                    alert("Las contraseñas no coinciden");
                    formularioValido = false;
                }

                let regexCorreo = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";

                // Validar formato nickname
                if (nickname.match(regexCorreo)){
                    alert("Formato de nickname no valido, no puede ser un correo");
                    formularioValido = false;
                }
                // Validar formato correo
                if (!correo.match(regexCorreo)){
                    alert("Formato de correo no valido");
                    formularioValido = false;
                }
                //validar fecha nacimiento menor a hoy
                if(new Date(fechaNac) > new Date()){
                    alert("Fecha no valida");
                    formularioValido = false;
                }

                if(tipo == "Artista"){
                    if (descripcion=="") {
                        alert("Complete todos los campos obligatorios");
                        formularioValido = false;
                    }

                    let regexUrl = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
                    if(!url.match(regexUrl)){
                        alert("Formato de url no valido");
                        formularioValido = false;
                    }
                }

                //Enviar formulario con jquery
                if (formularioValido) {
                    //document.getElementById("registerForm").submit();
                    $( "form" ).first().submit();
                } else {
                    alert("EL FORMULARIO NO SE ENVIO POR INVALIDO")
                }

            }
        </script>
    </body>
</html>
