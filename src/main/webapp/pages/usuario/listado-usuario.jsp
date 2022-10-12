<%--
  Created by IntelliJ IDEA.
  User: esteban.rosano
  Date: 6/10/2022
  Time: 1:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="java.util.Map" %>

<html>
<head>
    <title>Listado usuario</title>
</head>

<body>
<h1>Listado usuario</h1>
<table class="table">
    <thead>
    <tr>
        <th> Nickname</th>
    </tr>
    </thead>
    <tbody>
    <%  Map<String,Usuario> usuarios = (Map<String, Usuario>) request.getAttribute("usuarios");
        for (Usuario elem : usuarios.values()) {
    %>
    <tr>
        <th onClick="location.href='detalle-usuario?nickname=<%=elem.getNickname()%>'"> <%=elem.getNickname()%> </th>
    </tr>
    <% } %>
    </tbody>
</table>
<a href="../../home.jsp">Volver</a>

<script>
    let nom = document.getElementById("nickname");
    nom.setAttribute('onClick','window.location.href = \'detalle-usuario.jsp\';');

    var selectRow = null;

    function editar(a){

        console.log(a);
    }

    function llamarDetalleUsuario(){
        window.location.href ='detalle-usuario.jsp';
    }
</script>
</body>
</html>