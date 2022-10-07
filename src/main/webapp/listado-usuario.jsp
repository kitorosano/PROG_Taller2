<%--
  Created by IntelliJ IDEA.
  User: esteban.rosano
  Date: 6/10/2022
  Time: 1:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import ="main.java.taller1.Logica.Fabrica"  %>
<%@ page import="jdk.nashorn.internal.runtime.regexp.JoniRegExp" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@page import="java.util.HashMap"%>
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
    <%
        Map<String,Usuario> usuarios = Fabrica.getInstance().getIUsuario().obtenerUsuarios();
        for (Usuario elem : usuarios.values()) {
    %>
    <tr>
        <th onClick="location.href='detalle-usuario?nickname=<%=elem.getNickname()%>'"> <%=elem.getNickname()%> </th>

    </tr>

    <%
        }
    %>
    </tbody>
</table>
<a href="index.jsp">Volver</a>
</body>
</html>