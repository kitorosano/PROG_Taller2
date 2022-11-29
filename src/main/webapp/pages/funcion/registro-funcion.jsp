<%@ page import="java.time.LocalDate" %>
<%@ page import="main.java.taller1.Logica.DTOs.EspectaculoDTO" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    
    //Traer datos precargados del request anterior
  String nombrespectaculo = request.getParameter("espectaculo");
  String nombreplataforma = request.getParameter("plataforma");
  String nombrefuncion= request.getParameter("nombre") instanceof String ? request.getParameter("nombre") : "";
  String fecha = request.getParameter("fechaInicio") instanceof String ? request.getParameter("fechaInicio") : "";
  String hora = request.getParameter("horaInicio") instanceof String ? request.getParameter("horaInicio") : "";
  String imagen = request.getParameter("imagen") instanceof String ? request.getParameter("imagen") : "";
  String[] artistasInvitados = request.getParameterValues("artInvitado");
  Map<String, EspectaculoDTO> espectaculos= (Map<String, EspectaculoDTO>) request.getAttribute("espectaculos");
  List<String> artistas= (List<String>) request.getAttribute("artistas");
%>

<!DOCTYPE html>
<html>
<head>
  <style><%@ include file="/pages/global.css" %></style>
  <style><%@ include file="/pages/registros.css" %></style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>CoronaTicketsUY</title>
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3>Alta de Funcion</h3>
            </div>
        <section>
            <%@ include file="/pages/sidebar.jsp" %>
            <div class="main-container">
                <%-- AGREGAR COMPONENTES ABAJO--%>
                <form id="idform" name="formEspectaculo" method="POST" action="registro-funcion" enctype="multipart/form-data">
                  <div style="display: flex; flex-direction: column; align-items: flex-start">
                    <div id="camposBasicos" style="display: flex; flex-direction: column; align-items: flex-start; width: 100%">
                      <div class="input-container">
                          <label class="subtitulos">Espectaculo</label>
                          <select name="espectaculo" >
                          <%  if(nombrespectaculo==null || nombreplataforma==null){
                                    if(espectaculos.values().size() == 0) { %>
                                        <option value="">No tienes espectaculos aceptados</option>
                          <%        } else {
                                        for (EspectaculoDTO elem : espectaculos.values()) {  %>
                                            <option value="<%= elem.getNombre()+"-"+elem.getPlataforma().getNombre()%>"><%=elem.getNombre()%></option>
                          <%            }
                                    }
                            }else{ %>
                                    <option value="<%=nombrespectaculo+"-"+nombreplataforma%>"><%=nombrespectaculo%></option>
                        <%    } %>
                          </select>
                      </div>
                      <div class="input-container">
                        <label class="subtitulos">Nombre</label>
                        <input type="text" name="nombre" placeholder="*Nombre..." maxlength="30" value="<%=nombrefuncion%>">
                      </div>
                      <div class="input-container">
                        <label class="subtitulos">Fecha de inicio</label>
                        <input type="date" name="fechaInicio" min="<%= LocalDate.now().toString() %>" value="<%=fecha%>">
                      </div>
                      <div class="input-container">
                        <label class="subtitulos">Hora de inicio</label>
                        <input type="time" name="horaInicio" value="<%=hora%>">
                      </div>
                      <div class="input-container">
                        <label class="subtitulos">Imagen</label>
                        <input type="file" accept="image/*" name="imagen">
                      </div>
                    </div>
                      <div style="display: flex; gap: 30px">
                            <div id="artistas-list">
                              <p>Artistas a invitar</p>
                              <select multiple name="listArtistas" id="listArtistas" style="width: 120px; height: 200px; padding: 5px">
                                  <%  for(String artista: artistas){ %>
                                  <option value="<%=artista%>"><%=artista%></option>
                                  <%  }   %>
                              </select>
                              <button type="button" onclick="agregarArtista()">Invitar artista</button>
                          </div>
                          <div>
                            <p>Artistas invitados</p>
                            <select multiple name="artistasInvitados" id="artistasInvitados" style="width: 120px; height: 200px; padding: 5px">
                              <%    if(artistasInvitados!=null){
                                        for(String nombInvitado:artistasInvitados){ %>
                                            <option value="<%=nombInvitado%>"><%=nombInvitado%></option>
                              <%        }
                                    }   %>
                            </select>
                            <button type="button" onclick="eliminarArtista()">Quitar artista</button>
                          </div>
                      </div>
                      <br>
                    <button id="submitBtn" type="button" onclick="enviarForm()">Registrar!</button>
                  </div>
                </form>
    
                <%-- AGREGAR COMPONENTES ARRIBA--%>
            </div>
        </section>
        </main>
    </div>


<%--    Javascript    --%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        $(document).ready(function () {
            const MESSAGE = $("#message");
        
            if (MESSAGE.text().trim() != "") {
                MESSAGE.removeClass("hidden");
                setTimeout(() => {
                    MESSAGE.text("");
                    MESSAGE.addClass("hidden");
                }, 5000);
            } else {
                MESSAGE.addClass("hidden");
            }
        });
    
        function mensaje(msg) {
            const MESSAGE = $("#message");
            MESSAGE.text(msg);
            MESSAGE.addClass("error");
            MESSAGE.removeClass("hidden");
        
            setTimeout(() => {
                MESSAGE.text("");
                MESSAGE.addClass("hidden");
                MESSAGE.removeClass("error");
            }, 5000);
        }
        //TODO: HOMOLOGAR MENSAJES DE VALICACION
    
        function enviarForm() {
            let SUBMITBUTTON = $("#submitBtn");
            SUBMITBUTTON.prop("disabled", true);
            
            //Obtener inputs con jquery
            let espectaculo = $("input[name='espectaculo']").val();
            let nombre = $("input[name='nombre']").val();
            let fecha = $("input[name='fechaInicio']").val();
            let hora = $("input[name='horaInicio']").val();
            let imagen = $("input[name='imagen']").val();
            let invitadosSelector = document.getElementById("artistasInvitados");
            let error;
        
            let formularioValido = true;
        
            // Validar campos vacios
            if (espectaculo === "" || nombre === "" || fecha === "" || hora === "") {
                error="Complete todos los campos obligatorios";
                formularioValido = false;
            }
        
            //Agrego un input invisible para cada elemento del select de invitados
            $("#artistasInvitados option").each(function() {
                let inputEnviar=document.createElement("input");
                inputEnviar.type="hidden";
                inputEnviar.value=this.value;
                inputEnviar.name="artInvitado";
                this.appendChild(inputEnviar);
            });
        
            //Enviar formulario con jquery
            if (formularioValido) {
                document.getElementById("idform").submit();
            } else {
                alert(error);
                const SUBMITBUTTON = $("#submitBtn");
                SUBMITBUTTON.prop("disabled", false);
            }
        }
    
        function agregarArtista(){
            //obtengo el valor del selector
            let artista = $("select[name='listArtistas']").val();
            //obtengo el objeto lista
            let artistasInvitados= document.getElementById("artistasInvitados");
            let invitado= document.createElement("option");
            invitado.text=artista;
            invitado.value=artista;
            artistasInvitados.appendChild(invitado);
            //Borro la categoria del select
            $('#listArtistas option[value="'+artista+'"]').remove();
        }
    
        function eliminarArtista(){
            let artista = $("select[name='artistasInvitados']").val();
            let artistas=document.getElementById("listArtistas");
            let invitado=document.createElement("option");
            invitado.text=artista;
            invitado.value=artista;
            artistas.appendChild(invitado);
            $('#artistasInvitados option[value="'+artista+'"]').remove();
        }
    </script>
</body>
</html>
