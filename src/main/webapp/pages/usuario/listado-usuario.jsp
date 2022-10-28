<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Artista" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="/pages/global.css" %></style>
    <style><%@ include file="/pages/usuario/listado-usuarios.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="/pages/header.jsp" %>

<section>
    <%@ include file="/pages/sidebar.jsp" %>
    <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
            <div class="busqueda">
                <br/>
                <label for="">Buscar Usuario</label>
                <input type="text" name="buscarUsuario" id="txtBuscar" value="Nombre...">
            </div>
            <h2>Usuarios</h2>
            <br>
            <div>
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
        <%--                AGREGAR COMPONENTES ACA--%>
    </div>
</section>

<script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
<script>
    $(document).ready(function(){

    });

    <% Map<String, Usuario> usuarios = (Map<String, Usuario>) request.getAttribute("usuarios");
    for (Usuario elem : usuarios.values()) {%>
    nuevaFila = tabla.insertRow(-1);
    celdaNickname = nuevaFila.insertCell(0);
    celdaTipo = nuevaFila.insertCell(1);

    celdaNickname.innerHTML = "<%=elem.getNickname()%>";
    <% if (elem instanceof Artista){%>
    celdaTipo.innerHTML = "Artista";
    <%}else{%>
    celdaTipo.innerHTML = "Espectador";
    <%}%>

    celdaNickname.setAttribute('onClick',"location.href='detalle-usuario?nickname=<%=elem.getNickname()%>'");
    <% } %>

    $("#txtBuscar").on("keyup", function() {
        var keyword = this.value;
        keyword = keyword.toUpperCase();
        var table_1 = document.getElementById("tabla");
        var all_tr = table_1.getElementsByTagName("tr");
        for(var i=0; i<all_tr.length; i++){
            var name_column = all_tr[i].getElementsByTagName("td")[0];
            if(name_column){
                var name_value = name_column.textContent || name_column.innerText;
                name_value = name_value.toUpperCase();
                if(name_value.indexOf(keyword) > -1){
                    all_tr[i].style.display = ""; // show
                }else{
                    all_tr[i].style.display = "none"; // hide
                }
            }
        }
    });

    $("#txtBuscar").click(function(){
        $("#txtBuscar").val("");
    });



</script>
</body>
</html>