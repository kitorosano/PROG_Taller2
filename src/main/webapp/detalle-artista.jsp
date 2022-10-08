<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="main.java.taller1.Logica.Fabrica" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: joaco
  Date: 06/10/2022
  Time: 22:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="java.util.Map" %>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <h4>Nombre: ${nombre}</h4>
        <h4>Apellido: ${apellido}</h4>
        <h4>Nickname: ${nickname}</h4>
        <h4>Correo: ${correo}</h4>
        <h4>Biografia: ${biografia}</h4>
        <h4>Fecha de Nacimiento ${fechaNac}</h4>

        <h4>Espectaculos</h4>
        <table class="table">
            <thead>
            <tr>
                <th> Nombre</th>
            </tr>
            </thead>
            <tbody>

            <%
                System.out.println(request.getAttribute("nickname"));
                Map<String,Espectaculo> espectaculos = Fabrica.getInstance().getIUsuario().obtenerEspectaculosArtista(request.getAttribute("nickname").toString());
                for (Espectaculo elem : espectaculos.values()) {
            %>
            <tr>
                <th> <%=elem.getNombre()%> </th>

            </tr>

            <%
                }
            %>
            <script>
                function editar(a){

                    console.log("2"+a.innerHTML);

                }


            </script>
            </tbody>
        </table>
    </body>
</html>
