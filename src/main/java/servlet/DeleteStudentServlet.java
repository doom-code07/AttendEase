package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.StudentDAO;

public class DeleteStudentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            new StudentDAO().deleteStudent(id);
            resp.sendRedirect("ManageStudentServlet");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error deleting student.");
        }
    }
}
