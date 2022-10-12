<%--
  Created by IntelliJ IDEA.
  User: joaco
  Date: 06/10/2022
  Time: 22:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="main.java.taller1.Logica.Fabrica" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Funcion" %>
<%@ page import="main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h4>Nombre: ${nombre}</h4>
    <h4>Apellido: ${apellido}</h4>
    <h4>Nickname: ${nickname}</h4>
    <h4>Correo: ${correo}</h4>
    <h4>Fecha de Nacimiento: ${fechaNac}</h4>
    <h4>Funciones registradas</h4>

    <table class="table">
        <thead>
        <tr>
            <th> Nombre</th>
        </tr>
        </thead>
        <tbody>

        <%
            System.out.println(request.getAttribute("nickname"));
            Map<String, EspectadorRegistradoAFuncion> funciones = Fabrica.getInstance().getIUsuario().obtenerFuncionesRegistradasDelEspectador(request.getAttribute("nickname").toString());


            for (EspectadorRegistradoAFuncion elem : funciones.values()) {
        %>
        <tr>
            <th> <%=elem.getFuncion().getNombre()%> </th>

        </tr>

        <%
            }
        %>
        </tbody>
    </table>
</body>
</html>
