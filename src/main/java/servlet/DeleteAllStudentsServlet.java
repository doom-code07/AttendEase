package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.StudentDAO;

public class DeleteAllStudentsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            new StudentDAO().deleteAllStudents();
            resp.sendRedirect("ManageStudentServlet");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error deleting all students.");
        }
    }
}
