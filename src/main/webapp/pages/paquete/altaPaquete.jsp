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
    <style>

        #idform {
            top: 50%;
            padding: 30px;
        }
        .subtitulos{
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
            line-height: 60px;
        }
        .input-container{
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
        <h1>Registro de paquete de espectaculos</h1>
        <form id="idform" name="formEspectaculo" method="POST" action="alta-paquete">
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
                    <input type="number" min="0" name="descuento" id="descuento" placeholder="*Porcentaje de descuento" value="<%= descuento%>">
                </div>
                <div class="input-container">
                    <label class="subtitulos">Imagen</label>
                    <input type="file" name="imagen" id="imagen">
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

