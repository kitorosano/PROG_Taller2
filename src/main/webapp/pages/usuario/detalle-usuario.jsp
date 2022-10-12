<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="main.java.taller1.Logica.Clases.Artista" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Detalle usuario</title>
    </head>
    <body>
        <%
            Usuario usu= (Usuario) request.getAttribute("datos");
        %>
        <h1>Detalle usuario</h1>
        <h4>Nombre:<%=usu.getNombre()%></h4>
        <h4>Apellido:<%=usu.getApellido()%></h4>
        <h4>Nickname:<%=usu.getNickname()%></h4>
        <h4>Correo:<%=usu.getCorreo()%></h4>
        <h4>Fecha de Nacimiento:<%=usu.getFechaNacimiento()%></h4>

        <%
            if(usu instanceof Artista){
        %>
            <h4>Biografia:<%=((Artista) usu).getBiografia()%></h4>
            <h4>Espectaculos del artista</h4>
            <table class="table">
                <tbody>
                    <%
                    Map<String, Espectaculo> espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculos");
                    for (Espectaculo elem : espectaculos.values()) {
                %>
                <tr>
                    <th> <%=elem.getNombre()%> </th>
                </tr>
                    <%
                    }
                %>
                </tbody>
            </table>
        <%
            }else{
        %>
        <h4>Funciones registradas</h4>
        <table class="table">
            <tbody>
            <%
                Map<String, EspectadorRegistradoAFuncion> funciones =(Map<String, EspectadorRegistradoAFuncion>) request.getAttribute("funciones");
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
        <%
            }
        %>
        <a href="../home.jsp">Volver</a>
    </body>
</html>
