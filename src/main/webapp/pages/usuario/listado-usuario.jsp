<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Artista" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    
    Map<String, Usuario> usuarios = request.getAttribute("usuarios") != null ? (Map<String, Usuario>) request.getAttribute("usuarios") : null;
    String json = new Gson().toJson(usuarios);
%>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="/pages/global.css" %></style>
    <style><%@ include file="/pages/usuario/listado-usuarios.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CoronaTicketsUY</title>
</head>
<body>
    <%@ include file="/pages/header.jsp" %>
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <div class="main-container">
            <%-- AGREGAR COMPONENTES ABAJO--%>
            <div class="busqueda">
                <label for="txtBuscar">Buscar Usuario</label>
                <input type="text" name="buscarUsuario" id="txtBuscar" value="Nombre...">
            </div>
            <div>
                <h2>Usuarios</h2>
                <button class="volver" onclick="history.back()">Volver</button>
                <table class="tablaUsuarios" id="tabla">
                    <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Tipo de usuario</th>
                    </tr>
                    </thead>
                    <tbody id="cuerpoTabla">
                    </tbody>
                </table>
            </div>
            <%-- AGREGAR COMPONENTES ARRIBA--%>
        </div>
    </section>
    
    <%--    Javascript--%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        // Declaramos elementos del DOM
        const TABLA = document.getElementById("cuerpoTabla");
        const TXT_BUSCAR = $("#txtBuscar");
    
        //CUANDO EL DOCUMENTO ESTE LISTO
        $(document).ready(function(){
            crearTabla();
        });
    
    
        function crearTabla(){
            let nuevaFila;
            let celdaNickname;
            let celdaTipo;
    
            <% for (Usuario elem : usuarios.values()) {%>
                nuevaFila = TABLA.insertRow(-1);
                celdaNickname = nuevaFila.insertCell(0);
                celdaTipo = nuevaFila.insertCell(1);
    
                celdaNickname.innerHTML = "<%=elem.getNickname()%>";
                <% if (elem instanceof Artista){%>
                    celdaTipo.innerHTML = "Artista";
                <%}else{%>
                    celdaTipo.innerHTML = "Espectador";
                <%}%>

                nuevaFila.addEventListener("click", function(){
                    window.location.href = "perfil?nickname=<%=elem.getNickname()%>";
                });
            <% } %>
        }
    
        TXT_BUSCAR.on("keyup", function() {
            let keyword = this.value;
            keyword = keyword.toUpperCase();
            let table_1 = document.getElementById("tabla");
            let all_tr = table_1.getElementsByTagName("tr");
            for(let i=0; i<all_tr.length; i++){
                let name_column = all_tr[i].getElementsByTagName("td")[0];
                if(name_column){
                    let name_value = name_column.textContent || name_column.innerText;
                    name_value = name_value.toUpperCase();
                    if(name_value.indexOf(keyword) > -1){
                        all_tr[i].style.display = ""; // show
                    }else{
                        all_tr[i].style.display = "none"; // hide
                    }
                }
            }
        });
    
        TXT_BUSCAR.click(function(){
            TXT_BUSCAR.val("");
        });
    
    </script>
</body>
</html>