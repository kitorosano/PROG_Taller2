<%--
  Created by IntelliJ IDEA.
  User: sebas
  Date: 6/10/2022
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  //Traer datos precargados del request anterior
    String nickname = request.getParameter("nickname") instanceof String ? request.getParameter("nickname") : "";
    String contrasenia = request.getParameter("contrasenia") instanceof String ? request.getParameter("contrasenia") : "";
%>
<html>
    <head>
        <title>Login</title>
    </head>
    <body>
    <a href="registro">Registrarse</a>

    <form id="idform" name="myform" method="POST" action="login">
        <div style="display: flex; flex-direction: column; align-items: flex-start">
            <input type="text"  name="nickname" placeholder="*Nickname o correo" maxlength="30" value="<%= nickname%>">
            <input type="password" name="contrasenia" placeholder="*Contraseña..." maxlength="30" value="<%= contrasenia%>">
            <input type="hidden" name="esCorreo">


            <button type="button" onclick="enviarForm()">Entrar!</button>
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
        function enviarForm() {
            //Obtener inputs con jquery
            let nickname = $("input[name='nickname']").val();
            let contrasenia = $("input[name='contrasenia']").val();

            let formularioValido = true;

            // Validar campos vacios comunes
            if (nickname === "" || contrasenia === "") {
                alert("Complete todos los campos obligatorios");
                formularioValido = false;
            }

            let regexCorreo = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";

            // Si el nickname tiene formato de correo entonces se envia el campo esCorreo como true
            if (nickname.match(regexCorreo)){
                $("input[name='esCorreo']").val("true");
            } else {
                $("input[name='esCorreo']").val("false");
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
