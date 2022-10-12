package taller2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;

@WebServlet(name = "Home", value = "/")
public class Home extends HttpServlet {
    
    Fabrica fabrica;
    
    public void init() {
        fabrica = Fabrica.getInstance();
    }
    
    protected void dispatchPage(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher view = request.getRequestDispatcher(page);
        view.forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el usuario de la sesion
        HttpSession session = request.getSession();
        String nickname = (String) session.getAttribute("nickname");
        
        // Si no hay usuario en la sesion, redirigir a login
        if(nickname == null) {
            response.sendRedirect("login");
            return;
        }
        
        dispatchPage("/pages/home.jsp", request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // CERRAR SESION
        HttpSession session = request.getSession();
        session.invalidate();
        
        response.sendRedirect("login");
    }
    
}
