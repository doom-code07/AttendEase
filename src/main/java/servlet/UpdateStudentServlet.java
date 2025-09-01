package servlet;

import dao.ClassDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import dao.StudentDAO;
import model.ClassModel;
import model.StudentModel;

public class UpdateStudentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            StudentModel student = new StudentDAO().getStudentById(id);
            List<ClassModel> classList = new ClassDAO().getAllClasses();

            req.setAttribute("student", student);
            req.setAttribute("classList", classList);
            req.getRequestDispatcher("update_student.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error loading student for update.");
        }
    }
}
