package servlet;

import dao.TeacherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.TeacherModel;

import java.io.IOException;

public class ViewTeacherServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int teacherId = Integer.parseInt(req.getParameter("id"));
            TeacherModel teacher = new TeacherDAO().getTeacherById(teacherId);
            req.setAttribute("teacher", teacher);
            req.getRequestDispatcher("view_teacher.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error fetching teacher details.");
        }
    }
}
