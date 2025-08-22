package servlet;

import dao.ClassDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;


import java.io.IOException;


public class AddClassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String className = request.getParameter("className");

        if (className != null && !className.trim().isEmpty()) {
            ClassDAO classDAO = new ClassDAO();
            boolean added = classDAO.addClass(className.trim());

            HttpSession session = request.getSession();
            if (added) {
                session.setAttribute("success", "Class added successfully.");
            } else {
                session.setAttribute("duplicate", "Class already exists.");
            }
        }

        response.sendRedirect("manage_classes.jsp");
    }
}
