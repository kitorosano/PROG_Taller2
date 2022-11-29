<%--
  Created by IntelliJ IDEA.
  User: esteban.rosano
  Date: 2/11/2022
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="main.java.taller1.Logica.Clases.UsuarioDTO" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
	UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
	
	String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
	String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
%>

<html>
<head>
	<style><%@ include file="./global.css" %></style>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>CoronaTicketsUY</title>
</head>
<style>
	body {
		background-color: #f5f5f5;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		margin-top: -12em;
	}
	.container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	h1 {
		font-size: 3rem;
	}
	h2 {
		font-style: italic;
		font-size: 1.5rem;
	}
	p {
		font-size: 1.5rem;
	}
	
	a,span {
		text-decoration: underline;
		font-size: 3rem;
	}
	a:hover, span:hover {
		color: var(--dark-grey);
	}
</style>
<body>
	<a href="<%= request.getContextPath()%>" class="header_title">&larr; <span>CoronaTickets</span>.uy</a>
	<br><br>
	<div class="container">
		<h1>Error 404</h1>
	    <h2>(Page not found)</h2>
	    <br><br>
	    <p>Esta pagina no existe, o no esta disponible para ti</p>
	</div>

</body>
</html>
