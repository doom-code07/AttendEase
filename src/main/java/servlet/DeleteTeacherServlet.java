package servlet;

import dao.TeacherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class DeleteTeacherServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            new TeacherDAO().deleteTeacher(id);
            resp.sendRedirect("ManageTeacherServlet");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error deleting teacher.");
        }
    }
}
