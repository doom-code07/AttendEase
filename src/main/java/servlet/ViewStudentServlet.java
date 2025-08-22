package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.StudentDAO;
import model.StudentModel;

public class ViewStudentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            StudentModel student = new StudentDAO().getStudentById(id);
            req.setAttribute("student", student);
            req.getRequestDispatcher("view_student.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error viewing student.");
        }
    }
}
