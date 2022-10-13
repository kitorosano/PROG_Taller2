<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  //Traer datos precargados del request anterior
    String nombre = request.getParameter("nombre") instanceof String ? request.getParameter("nombre") : "";
    String descripcion = request.getParameter("descripcion") instanceof String ? request.getParameter("descripcion") : "";
    String duracion = request.getParameter("duracion") instanceof String ? request.getParameter("duracion") : "";
    String especMinimos = request.getParameter("espMaximos") instanceof String ? request.getParameter("espMaximos") : "";
    String especMaximos = request.getParameter("espMinimos") instanceof String ? request.getParameter("espMinimos") : "";
    String url = request.getParameter("url") instanceof String ? request.getParameter("url") : "";
    String costo = request.getParameter("costo") instanceof String ? request.getParameter("costo") : "";
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
<%--nombre,descripción, duración, espectadores mínimos, espectadores máximos, URL asociada, costo, imagen(opcional))--%>
            <form id="idform" name="formEspectaculo" method="POST" action="alta-espectaculo">
                <div style="display: flex; flex-direction: column; align-items: flex-start">
                    <div id="camposComunes" style="display: flex; flex-direction: column; align-items: flex-start">
                        Plataforma<select name="plataforma" >
                        <%
                            Map<String, Plataforma> plataformas= (Map<String, Plataforma>) request.getAttribute("plataformas");
                            for (Plataforma elem : plataformas.values()) {
                        %>
                            <option value="<%= elem.getNombre()%>"><%=elem.getNombre()%></option>
                        <%
                            }
                        %>
                        </select>
                        <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                        <input type="textarea" name="descripcion" placeholder="*Descripcion..." maxlength="100" value="<%= descripcion%>">
                        <input type="number" name="duracion" placeholder="*Duracion..." value="<%= duracion%>">
                        <input type="number" name="espMinimos" placeholder="*Espectadores Minimos..." value="<%= especMinimos%>">
                        <input type="number" name="espMaximos" placeholder="*Espectadores Maximos..." value="<%= especMaximos%>">
                        <input type="url" name="url" placeholder="Sitio Web Url..." maxlength="50" value="<%= url%>">
                        <input type="number" name="costo" placeholder="*Costo de entrada..." value="<%= costo%>">
                        <input type="file" name="imagen">
                        <%--TODO: AGREGAR SELECTOR DE CATEOGRIAS--%>
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
                    let plataforma = $("input[name='plataforma']").val();
                    let nombre = $("input[name='nombre']").val();
                    let descripcion = $("input[name='descripcion']").val();
                    let duracion = $("input[name='duracion']").val();
                    let espMaximos = $("input[name='espMaximos']").val();
                    let espMinimos = $("input[name='espMinimos']").val();
                    let url = $("input[name='url']").val();
                    let costo = $("input[name='costo']").val();
                    let imagen = $("input[name='imagen']").val();

                    let formularioValido = true;

                    // Validar campos vacios comunes
                    if (plataforma === "" || nombre === "" || descripcion === "" || duracion === "" || espMaximos === "" || espMinimos === "" || costo==="") {
                        alert("Complete todos los campos obligatorios");
                        formularioValido = false;
                    }

                    let regexURL = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
                    if(!url.match(regexURL)){
                        alert("Formato de url no valido");
                        formularioValido = false;
                    }
                    //validar espectadores minimos mayores a 0
                    if(espMinimos<0){
                        alert("Espectadores minimos no puede ser menor a 0");
                        formularioValido=false;
                    }
                    if(espMinimos>espMaximos){
                        alert("Espectadores minimos no pueden ser mas que los maximos");
                        formularioValido=false;
                    }

                    //Enviar formulario con jquery
                    if (formularioValido) {
                        document.getElementById("idform").submit();
                    } else {
                        alert("EL FORMULARIO NO SE ENVIO POR INVALIDO")
                    }
                }
            </script>

        <%--                AGREGAR COMPONENTES ACA--%>
    </div>
</section>
</body>
</html>
