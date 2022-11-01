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
    <style><%@ include file="/pages/altas.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
    <%@ include file="/pages/header.jsp" %>
    
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <div class="main-container">
            <%-- AGREGAR COMPONENTES ABAJO--%>
            <h1>Registro de paquete de espectaculos</h1>
            <button class="volver" onclick="history.back()">Volver</button>
            <form id="idform" name="formEspectaculo" method="POST" action="alta-paquete" enctype="multipart/form-data">
                <div style="display: flex; flex-direction: column; align-items: flex-start">
                    <div class="input-container">
                        <label class="subtitulos">Nombre</label>
                        <input type="text" name="nombre" id="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                    </div>
                    <div class="input-container">
                        <label class="subtitulos">Descripcion</label>
                        <textarea name="descripcion" id="descripcion" placeholder="*Descripcion..." maxlength="100"><%= descripcion%></textarea>
                    </div>
                    <div class="input-container">
                        <label class="subtitulos">Fecha de expiracion</label>
                        <input type="date" min="<%=LocalDate.now()%>" name="vigencia" id="fecha-expiracion" placeholder="*Fecha de expiracion..." value="<%= vigencia%>">
                    </div>
                    <div class="input-container">
                        <label class="subtitulos">Porcentaje de descuento</label>
                        <input type="number" min="0" name="descuento" id="descuento" placeholder="*Porcentaje de descuento" step="0.1" value="<%= descuento%>">
                    </div>
                    <div class="input-container">
                        <label class="subtitulos">Imagen</label>
                        <input type="file" accept="image/*" name="imagen" id="imagen">
                    </div>
                    <button type="button" onclick="enviarForm()">Confirmar</button>
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
            <%-- AGREGAR COMPONENTES ARRIBA--%>
        </div>
        
        <%--    Javascript    --%>
        <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
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
    
    </section>
</body>
</html>

