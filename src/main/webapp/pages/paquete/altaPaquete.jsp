<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: paulo
  Date: 17/10/2022
  Time: 18:06
  To change this template use File | Settings | File Templates.
--%>
<%  //Traer datos precargados del request anterior
    String nombre = request.getParameter("nombre") instanceof String ? request.getParameter("nombre") : "";
    String descripcion = request.getParameter("descripcion") instanceof String ? request.getParameter("descripcion") : "";
    String vigencia = request.getParameter("vigencia") instanceof String ? request.getParameter("vigencia") : "";
    String descuento = request.getParameter("descuento") instanceof String ? request.getParameter("descuento") : "";
%>

<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="/pages/global.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="/pages/header.jsp" %>

<section>
    <%@ include file="/pages/sidebar.jsp" %>
    <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
        <h1>Alta de espectaculo</h1>
        <form id="idform" name="formEspectaculo" method="POST" action="alta-paquete">
            <div style="display: flex; flex-direction: column; align-items: flex-start">
                <div id="camposComunes" style="display: flex; flex-direction: column; align-items: flex-start">
                    <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                    <textarea name="descripcion" placeholder="*Descripcion..." maxlength="100"><%= descripcion%></textarea>
                    <input type="date" min="<%=LocalDate.now()%>" name="vigencia" placeholder="*Fecha de expiracion..." value="<%= vigencia%>">
                    <input type="number" min="0" name="descuento" placeholder="*Porcentaje de descuento" value="<%= descuento%>">%
                    <input type="file" name="imagen">
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
            function enviarForm() {
                //Obtener inputs con jquery
                let nombre = $("input[name='nombre']").val();
                let descripcion = $("input[name='descripcion']").val();
                let vigencia = $("input[name='vigencia']").val();
                let descuento = $("input[name='descuento']").val();
                let imagen = $("input[name='imagen']").val();

                let formularioValido = true;

                // Validar campos vacios comunes
                if (nombre === "" || descripcion === "" || vigencia === "" || descuento === "" ) {
                    alert("Complete todos los campos obligatorios");
                    formularioValido = false;
                }

                //Enviar formulario con jquery
                if (formularioValido) {
                    document.getElementById("idform").submit();
                } else {
                    alert("EL FORMULARIO NO SE ENVIO POR INVALIDO");
                }
            }
        </script>

        <%--                AGREGAR COMPONENTES ACA--%>
    </div>
</section>
</body>
</html>

