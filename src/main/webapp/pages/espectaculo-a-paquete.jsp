<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%  //Traer datos precargados del request anterior
  Map<String, Espectaculo> espectaculos= (Map<String, Espectaculo>) request.getAttribute("espectaculos");
  Map<String, Plataforma> plataformas= (Map<String, Plataforma>) request.getAttribute("plataformas");
%>

<!DOCTYPE html>
<html>
<head>
  <style><%@ include file="./global.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>JSP - Hello World</title>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <section>
        <%@ include file="sidebar.jsp" %>
        <div class="main-container">
        <%-- AGREGAR COMPONENTES ABAJO--%>
          <h1>Registro de paquete de espectaculos</h1>
          <button class="volver" onclick="history.back()">Volver</button>
  
          <select name="plataforma" id="plataforma">
            <option value="" selected disabled hidden>Plataforma</option>
            <%
              for(Plataforma p: plataformas.values()){
            %>
            <option value="<%=p.getNombre()%>"><%=p.getNombre()%></option>
            <%
              }
            %>
          </select>
  
          <div id="artistas-list">
              <p>Artistas a invitar</p>
              <select multiple name="listEspectaculos" id="listEspectaculos">
                <%
                  for(Espectaculo e: espectaculos.values()){
                %>
                <option value="<%=e.getNombre()+"-"+e.getPlataforma().getNombre()%>"><%=e.getNombre()%></option>
                <%
                  }
                %>
              </select>
              <button type="button" id="btnAgregar">Agregar espectaculo</button>
            </div>
    
          <form id="idform" name="formEspectaculosPaquete" method="POST" action="espectaculoAPaquete">
            <input type="hidden" value="<%=request.getParameter("paquete")%>" name="paquete">
            <select multiple name="espectaculosAagregar" id="espectaculosAagregar">
            </select>
            <button type="button" id="btnEliminar">Quitar espectaculo</button>
            <button type="button" onclick="enviarForm()">Registrar!</button>
          </form>
    
          <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    
          <script>
    
            $(document).ready(function(){
              let espectaculosTotales=$("#listEspectaculos option");
    
    
              $("#plataforma").on("change", function() {
                cargarlista();
              });
    
              $("#btnEliminar").click(eliminarEspectaculo);
              $("#btnAgregar").click(agregarEspectaculo);
    
              function cargarlista(){
                $("#listEspectaculos option").remove();
                let select = document.getElementById("listEspectaculos");
                let espectaculosAagregar= document.getElementById("espectaculosAagregar");
                let plataforma = $("select[name='plataforma']").val();
                espectaculosTotales.each(function (){
                  if(this.value.endsWith("-"+plataforma)){
                    if(!espectaculosAagregar.contains(this)){
                      let copia= this;
                      select.appendChild(copia);
                    }
                  }
                });
              }
    
    
              function agregarEspectaculo(){
                //obtengo el valor del selector
                let select = document.getElementById("listEspectaculos");
                let espectaculosAagregar= document.getElementById("espectaculosAagregar");
                let options =select.selectedOptions;
    
                for (let i = 0; i < options.length; i++) {
                  let nuevo=options[i];
                  espectaculosAagregar.appendChild(nuevo);
                }
              }
    
              function eliminarEspectaculo(){
                let select = document.getElementById("espectaculosAagregar");
                let options =select.selectedOptions;
    
                for (let i = 0; i < options.length; i++) {
                  console.log(i);
                  $('#espectaculosAagregar option[value="'+options[i].value+'"]').remove();
                }
                cargarlista();
              }
            });
    
    
            function enviarForm() {
              //Agrego un input invisible para cada elemento del select de invitados
              $("#espectaculosAagregar option").each(function() {
                console.log("Entro")
                let inputEnviar=document.createElement("input");
                inputEnviar.type="hidden";
                inputEnviar.value=this.value;
                inputEnviar.name="espAgregar";
                this.appendChild(inputEnviar);
              });
              document.getElementById("idform").submit();
            }
          </script>
        <%--                AGREGAR COMPONENTES ACA--%>
      </div>
    </section>
</body>
</html>