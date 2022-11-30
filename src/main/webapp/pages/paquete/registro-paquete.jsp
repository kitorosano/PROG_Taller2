<%@ page import="java.time.LocalDate" %>
<%@ page import="taller2.DTOs.*" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    
    //Traer datos precargados del request anterior
    String nombre = request.getParameter("nombre") instanceof String ? request.getParameter("nombre") : "";
    String descripcion = request.getParameter("descripcion") instanceof String ? request.getParameter("descripcion") : "";
    String vigencia = request.getParameter("vigencia") instanceof String ? request.getParameter("vigencia") : "";
    String descuento = request.getParameter("descuento") instanceof String ? request.getParameter("descuento") : "";
%>

<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="/pages/global.css" %></style>
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
                <h3>Alta de Paquete</h3>
            </div>
            <section>
                <%@ include file="/pages/sidebar.jsp" %>
                <div class="main-container">
                    <%-- AGREGAR COMPONENTES ABAJO--%>
                    <form id="idform" name="formEspectaculo" method="POST" action="registro-paquete" enctype="multipart/form-data">
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
                            <button id="submitBtn" type="button" onclick="enviarForm()">Confirmar</button>
                        </div>
                    </form>
                    <%-- AGREGAR COMPONENTES ARRIBA--%>
                </div>
            </section>
        </main>
    </div>

    <%--    Javascript    --%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
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
            let nombre = $("input[name='nombre']").val();
            let descripcion = $("input[name='descripcion']").val();
            let vigencia = $("input[name='vigencia']").val();
            let descuento = $("input[name='descuento']").val();
            let imagen = $("input[name='imagen']").val();
            let error;
        
            let formularioValido = true;
        
            // Validar campos vacios comunes
            if (nombre === "" || descripcion === "" || vigencia === "" || descuento === "" ) {
                error="Complete todos los campos obligatorios";
                formularioValido = false;
            }
        
            //Enviar formulario con jquery
            if (formularioValido) {
                document.getElementById("idform").submit();
            } else {
                alert(error);
                const SUBMITBUTTON = $("#submitBtn");
                SUBMITBUTTON.prop("disabled", false);
            }
        }
    </script>
</body>
</html>

