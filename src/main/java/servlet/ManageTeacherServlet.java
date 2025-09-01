package servlet;

import dao.TeacherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.TeacherModel;

import java.io.IOException;
import java.util.List;

public class ManageTeacherServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<TeacherModel> teachers = new TeacherDAO().getAllTeachers();
            request.setAttribute("teachers", teachers);
            request.getRequestDispatcher("manage_teachers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error loading teachers.");
        }
    }
}
