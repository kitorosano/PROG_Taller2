<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    FuncionDTO funcion= (FuncionDTO) request.getAttribute("datos");
    Map<String, EspectadorRegistradoAFuncion> espectador_registrado_funcion = (Map<String, EspectadorRegistradoAFuncion>) request.getAttribute("espectadores");
    int contador = espectador_registrado_funcion.size();
    EspectadorRegistradoAFuncion registro = espectador_registrado_funcion.get(usuarioLogueado.getNickname());

%>
<html>
<head>
    <title>Detalle de funcion</title>
    <style><%@ include file="/pages/global.css" %></style>
    <style><%@ include file="/pages/detalles.css" %></style>
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3>Detalle de funcion</h3>
            </div>
            <section>
                <%@ include file="/pages/sidebar.jsp" %>
                <div class="grid-container">
                    <%-- AGREGAR COMPONENTES ACA--%>
                    <img src="<%=funcion.getImagen()%>" alt="Foto de la funcion" class="img_perfil">
                    <div class="first-data">
                        <h2><%=funcion.getNombre()%></h2>
                        <h4>Fecha y hora de inicio:<%=funcion.getFechaHoraInicio()%></h4>
                    <% if((Boolean) session.getAttribute("esEspectador") && registro==null){
                            if(contador +1 <= funcion.getEspectaculo().getMaxEspectadores()){ %>
                                <button class="btn" onClick="location.href='registro-espectadores-a-funcion?nombre=<%=funcion.getNombre()%>&espectaculo=<%=funcion.getEspectaculo().getNombre()%>&plataforma=<%=funcion.getEspectaculo().getPlataforma().getNombre()%>'">Registrarme a función</button>
                    <%      }else { %>
                                <h4>Funcion llena</h4>
                    <%      }
                        }   %>
                    </div>
                    <div class="tabs">
                        <div class="menu">
                            <p data-target="#datos_generales" class="active">Datos Generales</p>
                            <p data-target="#espectadores">Espectadores Registrados</p>
                        </div>
                        <div class="content">
                            <div data-content id="datos_generales" class="active">
                                <h4>Nombre:<%=funcion.getNombre()%></h4>
                                <h4>Espectaculo:<%=funcion.getEspectaculo().getNombre()%></h4>
                                <h4>Fecha y hora de inicio:<%=funcion.getFechaHoraInicio()%></h4>
                                <h4>Fecha de registro:<%=funcion.getFechaRegistro()%></h4>
                            </div>
                            <div data-content id="espectadores">
                                <table >
                                    <tbody>
                                    <%  for (EspectadorRegistradoAFuncion espect_reg_funcion : espectador_registrado_funcion.values()) {    %>
                                    <tr>
                                        <th onClick="location.href='perfil?nickname=<%=espect_reg_funcion.getEspectador().getNickname()%>'"> <%=espect_reg_funcion.getEspectador().getNickname()%>  </th>
                                    </tr>
                                    <%  }   %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                
                </div>
                <%-- AGREGAR COMPONENTES ARRIBA--%>
                </section>
        </main>
    </div>
                
    <%--    Javascript--%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
    let targets = document.querySelectorAll('[data-target]')
    const content = document.querySelectorAll('[data-content]')
    
    targets.forEach(target  => {
        console.log(target);
    
        target.addEventListener('click', () => {
            content.forEach(c => {
                c.classList.remove('active')
            })
            targets.forEach(ts =>{
              ts.classList.remove('active')
            })
            console.log(target.classList);
            const t = document.querySelector(target.dataset.target)
            t.classList.add('active')
            target.classList.add('active')
            console.log(target.classList);
        })
    })
    
    
    </script>
</body>
</html>
