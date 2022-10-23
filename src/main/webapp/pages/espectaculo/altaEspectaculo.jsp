<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Categoria" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  //Traer datos precargados del request anterior
    String nombre = request.getParameter("nombre") instanceof String ? request.getParameter("nombre") : "";
    String descripcion = request.getParameter("descripcion") instanceof String ? request.getParameter("descripcion") : "";
    String duracion = request.getParameter("duracion") instanceof String ? request.getParameter("duracion") : "";
    String especMinimos = request.getParameter("espMinimos") instanceof String ? request.getParameter("espMinimos") : "";
    String especMaximos = request.getParameter("espMaximos") instanceof String ? request.getParameter("espMaximos") : "";
    String url = request.getParameter("url") instanceof String ? request.getParameter("url") : "";
    String costo = request.getParameter("costo") instanceof String ? request.getParameter("costo") : "";

    Map<String, Plataforma> plataformas= (Map<String, Plataforma>) request.getAttribute("plataformas");
    Map<String, Categoria>categorias= (Map<String, Categoria>) request.getAttribute("categorias");
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
            resize: none;
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
        <h1>Alta de espectaculo</h1>
            <form id="idform" name="formEspectaculo" method="POST" action="alta-espectaculo" enctype="multipart/form-data">
                <div style="display: flex; flex-direction: column; align-items: flex-start">
                    <div id="camposBasicos" style="display: flex; flex-direction: column; align-items: flex-start; width: 100%">
                        <div class="input-container">
                            <label class="subtitulos">Plataforma</label>
                            <select name="plataforma" >
                            <%
                                for (Plataforma elem : plataformas.values()) {
                            %>
                                <option value="<%= elem.getNombre()%>"><%=elem.getNombre()%></option>
                            <%
                                }
                            %>
                            </select>
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">Nombre</label>
                            <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%= nombre%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">Descripcion</label>
                        <textarea name="descripcion" placeholder="*Descripcion..." maxlength="100" ><%= descripcion%></textarea>
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">Duracion</label>
                        <input type="number" min="0" name="duracion" placeholder="*Duracion..." value="<%= duracion%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">Espectadores Minimos</label>
                        <input type="number" min="0" name="espMinimos" placeholder="*Espectadores Minimos..." value="<%= especMinimos%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">Espectadores Maximos</label>
                        <input type="number" min="0" name="espMaximos" placeholder="*Espectadores Maximos..." value="<%= especMaximos%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">URL sitio web</label>
                        <input type="url" name="url" placeholder="Sitio Web Url..." maxlength="50" value="<%= url%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">Costo de entrada</label>
                        <input type="number" min="0" name="costo" placeholder="*Costo de entrada..." value="<%= costo%>">
                        </div>
                        <div class="input-container">
                            <label class="subtitulos">Imagen</label>
                            <input type="file" name="imagen">
                        </div>

                        <div id="select-categorias">
                            Categorias<select name="categorias"  id="categorias">
                                <%
                            for (Categoria elem : categorias.values()) {
                                %>
                            <option value="<%= elem.getNombre()%>"><%=elem.getNombre()%></option>
                                <%
                            }
                            %>
                            </select>
                            <button type="button" onclick="AgregarCategoria()">Agregar</button>
                            <ul id="Categorias-list"></ul>
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
                    let error;

                    let formularioValido = true;

                    // Validar campos vacios comunes
                    if (plataforma === "" || nombre === "" || descripcion === "" || duracion === "" || espMaximos === "" || espMinimos === "" || costo==="") {
                        error="Complete todos los campos obligatorios";
                        formularioValido = false;
                    }

                    let regexURL = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
                    if(!url.match(regexURL)){
                        error="Formato de url no valido";
                        formularioValido = false;
                    }
                    //validar espectadores minimos mayores a 0
                    if(espMinimos<0){
                        error="Espectadores minimos no puede ser menor a 0";
                        formularioValido=false;
                    }
                    if(espMinimos>espMaximos){
                        error="Espectadores minimos no pueden ser mas que los maximos";
                        formularioValido=false;
                    }

                    //Enviar formulario con jquery
                    if (formularioValido) {
                        document.getElementById("idform").submit();
                    } else {
                        alert(error);
                    }
                }

                function AgregarCategoria(){
                    //obtengo el valor del selector
                    let categoria = $("select[name='categorias']").val();
                    //obtengo el objeto lista
                    let listaElegidas= document.getElementById("Categorias-list");
                    let liCategoria= document.createElement("li");
                    //creo un input invisible para pasar el valor de la categoria
                    let inputEnviar= document.createElement("input");
                    liCategoria.innerHTML=categoria;
                    inputEnviar.type="hidden";
                    inputEnviar.value=categoria;
                    inputEnviar.name="catElegidas";
                    liCategoria.setAttribute("class",'cat-elegidas');
                    liCategoria.setAttribute("onclick","cargarlista(this)");
                    liCategoria.appendChild(inputEnviar);
                    listaElegidas.appendChild(liCategoria);
                    //Borro la categoria del select
                    $('#categorias option[value="'+categoria+'"]').remove();
                }

                function cargarlista(element){
                    let selectCat=document.getElementById("categorias");
                    let option= document.createElement("option");
                    option.text=element.textContent;
                    option.value=element.textContent;
                    selectCat.appendChild(option);
                    element.remove();
                }
            </script>

        <%--                AGREGAR COMPONENTES ACA--%>
    </div>
</section>
</body>
</html>
