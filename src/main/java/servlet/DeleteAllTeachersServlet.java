package servlet;

import dao.TeacherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class DeleteAllTeachersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            new TeacherDAO().deleteAllTeachers();
            resp.sendRedirect("ManageTeacherServlet");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error deleting all teachers.");
        }
    }
}
