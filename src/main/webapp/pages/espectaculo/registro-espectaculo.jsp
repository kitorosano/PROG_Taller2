<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Categoria" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    
    
    String nombre = "";
    String descripcion = "";
    String duracion = "";
    String especMinimos = "";
    String especMaximos = "";
    String url = "";
    String costo = "";
    List<String> categoriasElegidas = new ArrayList<String>();
    
    //Traer datos precargados del request anterior
    if(messageType.equals("error")) {
        nombre = request.getParameter("nombre") != null ? request.getParameter("nombre") : "";
        descripcion = request.getParameter("descripcion") != null ? request.getParameter("descripcion") : "";
        duracion = request.getParameter("duracion") != null ? request.getParameter("duracion") : "";
        especMinimos = request.getParameter("espMinimos") != null ? request.getParameter("espMinimos") : "";
        especMaximos = request.getParameter("espMaximos") != null ? request.getParameter("espMaximos") : "";
        url = request.getParameter("url") != null ? request.getParameter("url") : "";
        costo = request.getParameter("costo") != null ? request.getParameter("costo") : "";
        String[] str_categoriasElegidas = request.getParameterValues("catElegidas") != null ? request.getParameterValues("catElegidas") : new String[0];
        categoriasElegidas = new ArrayList<>(
                Arrays.asList(str_categoriasElegidas)
        );
        System.out.println("Categorias elegidas: " + categoriasElegidas);
    }
    Map<String, Plataforma> plataformas= (Map<String, Plataforma>) request.getAttribute("plataformas");
    Map<String, Categoria>categorias= (Map<String, Categoria>) request.getAttribute("categorias");
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
    <%@ include file="/pages/header.jsp" %>
    <div id="message" class="hidden <%=messageType%>" role="alert">
        <%=message%>
    </div>
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <div class="main-container">
            <%-- AGREGAR COMPONENTES ABAJO--%>
            <h1>Alta de espectaculo</h1>
<%--            <button class="volver" onclick="history.back()">Volver</button>--%>
            <form id="idform" name="formEspectaculo" method="POST" action="registro-espectaculo" enctype="multipart/form-data">
                <div style="display: flex; flex-direction: column; align-items: flex-start">
                    <div id="camposBasicos" style="display: flex; flex-direction: column; align-items: flex-start; width: 100%">
                        <div class="input-container">
                            <label class="subtitulos" for="plataforma">Plataforma</label>
                            <select name="plataforma" id="plataforma">
                            <% for (Plataforma elem : plataformas.values()) { %>
                                <option value="<%= elem.getNombre()%>"><%=elem.getNombre()%></option>
                            <% } %>
                            </select>
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="nombre">Nombre</label>
                            <input type="text" id="nombre" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="descripcion">Descripcion</label>
                        <textarea name="descripcion" id="descripcion" placeholder="*Descripcion..." maxlength="100" ><%= descripcion%></textarea>
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="duracion">Duracion</label>
                        <input type="number" min="0" id="duracion" name="duracion" placeholder="*Duracion..." step="0.01" value="<%= duracion%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="espMinimos">Espectadores Minimos</label>
                        <input type="number" min="0" id="espMinimos" name="espMinimos" placeholder="*Espectadores Minimos..." value="<%= especMinimos%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="espMaximos">Espectadores Maximos</label>
                        <input type="number" min="0" id="espMaximos" name="espMaximos" placeholder="*Espectadores Maximos..." value="<%= especMaximos%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="url">URL sitio web</label>
                        <input type="url" name="url" id="url" placeholder="Sitio Web Url..." maxlength="50" value="<%= url%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="costo">Costo de entrada</label>
                        <input type="number" min="0" id="costo" name="costo" placeholder="*Costo de entrada..." step="0.01" value="<%= costo%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos" for="imagen">Imagen</label>
                            <input type="file" name="imagen" id="imagen" accept="image/*">
                        </div>
    
                        <div id="select-categorias">
                            <label for="categorias">Categorias</label>
                            <select name="categorias"  id="categorias">
                            <% for (Categoria cat : categorias.values()) {
                                    if(!categoriasElegidas.contains(cat.getNombre())){ %>
                                        <option value="<%= cat.getNombre()%>"><%=cat.getNombre()%></option>
                            <%      }
                                } %>
                            </select>
                            <button type="button" onclick="AgregarCategoria()">Agregar</button>
                            
                            <%-- Aca se agregan las categorias seleccionadas para mostrar --%>
                            <ul id="Categorias-list">
                                <% for (String cat : categoriasElegidas) { %>
                                    <li class="cat-elegidas" onclick="QuitarCategoria(this)"> <%=cat%> </li>
                                <% } %>
                            </ul>
    
                            <%-- Aca se agregan las categorias seleccionadas para el envio --%>
                            <select hidden name="catElegidas" id="catElegidas" multiple>
                                <% for (String cat : categoriasElegidas) { %>
                                <option value="<%= cat%>" selected><%=cat%></option>
                                <% } %>
                            </select>
                        </div>
                    </div>
                    <button type="button" onclick="enviarForm()">Registrar Espectaculo</button>
                </div>
            </form>
            <%-- AGREGAR COMPONENTES ARRIBA--%>
        </div>
    </section>


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
            const MESSAGE = $("#message");
            MESSAGE.text(msg);
            MESSAGE.addClass("error");
            MESSAGE.removeClass("hidden");
    
            setTimeout(() => {
                MESSAGE.text("");
                MESSAGE.addClass("hidden");
                MESSAGE.removeClass("error");
            }, 5000);
        }

        function validarCamposVacios() {
            const PLATAFORMA = $("#plataforma")
            const NOMBRE = $("#nombre")
            const DESCRIPCION = $("#descripcion")
            const DURACION = $("#duracion")
            const ESPMINIMOS = $("#espMinimos")
            const ESPMAXIMOS = $("#espMaximos")
            const COSTO = $("#costo")
            const URL = $("#url")
            if (PLATAFORMA.val().trim() == "") {
                mensaje("El campo 'Plataforma' no puede estar vacio");
                PLATAFORMA.addClass("invalid-input");
                return false;
            }
            if (NOMBRE.val().trim() == "") {
                mensaje("El campo 'Nombre' no puede estar vacio");
                NOMBRE.addClass("invalid-input");
                return false;
            }
            if (DESCRIPCION.val().trim() == "") {
                mensaje("El campo 'Descripcion' no puede estar vacio");
                DESCRIPCION.addClass("invalid-input");
                return false;
            }
            if (DURACION.val().trim() == "") {
                mensaje("El campo 'Duracion' no puede estar vacio");
                DURACION.addClass("invalid-input");
                return false;
            }
            if (ESPMINIMOS.val().trim() == "") {
                mensaje("El campo 'Espectadores minimos' no puede estar vacio");
                ESPMINIMOS.addClass("invalid-input");
                return false;
            }
            if (ESPMAXIMOS.val().trim() == "") {
                mensaje("El campo ''Espectadores maximos' no puede estar vacio");
                ESPMAXIMOS.addClass("invalid-input");
                return false;
            }
            if (COSTO.val().trim() == "") {
                mensaje("El campo 'Costo' no puede estar vacio");
                COSTO.addClass("invalid-input");
                return false;
            }
            if (URL.val().trim() == "") {
                mensaje("El campo 'URL' no puede estar vacio");
                URL.addClass("invalid-input");
                return false;
            }
            return true;
        }

        function validarFormatoURL() {
            const URL = $("#url")
            const PATTERN = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
            
            if (!URL.val().trim().match(PATTERN)) {
                mensaje("El campo 'URL' no tiene un formato valido");
                URL.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function validarEspectadoresMinimosMaximos() {
            const ESPMINIMOS = $("#espMinimos")
            const ESPMAXIMOS = $("#espMaximos")
    
            if (Number(ESPMINIMOS.val()) > Number(ESPMAXIMOS.val())) {
                mensaje("El campo 'Espectadores minimos' no puede ser mayor al campo 'Espectadores maximos'");
                ESPMINIMOS.addClass("invalid-input");
                ESPMAXIMOS.addClass("invalid-input");
                return false;
            }
            
            if(Number(ESPMINIMOS.val()) < 0 || Number(ESPMAXIMOS.val() < 0)) {
                mensaje("El campo 'Espectadores minimos' y 'Espectadores maximos' no pueden ser negativos");
                ESPMINIMOS.addClass("invalid-input");
                ESPMAXIMOS.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function validarCategorias() {
            const CATEGORIAS = $("#Categorias-list")
            if (CATEGORIAS.children().length == 0) {
                mensaje("Debe seleccionar al menos una categoria");
                CATEGORIAS.addClass("invalid-input");
                return false;
            }
            return true;
        }
        
        function enviarForm() {;
            let button = $("#button");
            button.prop("disabled", true);
    
            let formularioValido = validarCamposVacios() && validarFormatoURL() && validarEspectadoresMinimosMaximos() && validarCategorias();
            
            //Enviar formulario con jquery
            if (formularioValido) {
                document.getElementById("idform").submit();
            } else {
                button.prop("disabled", false);
            }
        }
    
        function AgregarCategoria(){
            //obtengo el valor del selector
            let categoria = $("select[name='categorias']").val();
            
            //obtengo el select de categorias invisible
            let selectElegidas = $("#catElegidas");
            let optionEnviar= document.createElement("option");
            optionEnviar.innerHTML=categoria;
            optionEnviar.value=categoria;
            optionEnviar.selected=true;
            selectElegidas.append(optionEnviar);
            
            // creo un li para la lista visible
            let listaElegidas= document.getElementById("Categorias-list");
            let liCategoria= document.createElement("li");
            liCategoria.innerHTML=categoria;
            liCategoria.setAttribute("class",'cat-elegidas');
            liCategoria.setAttribute("onclick","QuitarCategoria(this)");
            listaElegidas.appendChild(liCategoria);
            
            //Borro la categoria del select
            $('#categorias option[value="'+categoria+'"]').remove();
        }
    
        function QuitarCategoria(element){
            let selectCat=document.getElementById("categorias");
            let option= document.createElement("option");
            option.text=element.textContent;
            option.value=element.textContent;
            selectCat.appendChild(option);
            element.remove();
        }
    </script>
</body>
</html>
