package servlet;

import dao.StudentDAO;
import model.StudentModel;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class VicePrincipalStruckOffServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StudentDAO dao = new StudentDAO();
            List<StudentModel> struckStudents = dao.getStruckOffStudents();
            request.setAttribute("struckStudents", struckStudents);
            request.getRequestDispatcher("manage_struckOff.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
