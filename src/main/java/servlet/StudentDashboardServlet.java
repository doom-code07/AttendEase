package servlet;

import dao.StudentDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.StudentModel;

import java.io.IOException;

public class StudentDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Integer studentId = (Integer) session.getAttribute("studentId");

            if (studentId != null) {
                StudentDAO studentDAO = new StudentDAO();

                studentDAO.updateTotalFine(studentId);

                StudentModel student = studentDAO.getStudentById(studentId);
                request.setAttribute("student", student);

                RequestDispatcher rd = request.getRequestDispatcher("student_dashboard.jsp");
                rd.forward(request, response);
            } else {
                response.sendRedirect("login.jsp"); // or error page
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
