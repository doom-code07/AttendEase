package servlet;

import dao.LeaveApplicationDAO;
import dao.StudentDAO;
import model.LeaveApplication;
import model.Student;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;


public class CheckStatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
            int userId = (int) session.getAttribute("userId");
            Student s = StudentDAO.findByUsersId(userId);
            if (s == null) {
                request.setAttribute("error", "Student not found.");
                request.getRequestDispatcher("check_status.jsp").forward(request, response);
                return;
            }
            LeaveApplicationDAO dao = new LeaveApplicationDAO();
            List<LeaveApplication> list = dao.findByStudent(s.getId());

            request.setAttribute("applications", list);
            request.setAttribute("student", s);
            request.getRequestDispatcher("check_status.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Server error: " + e.getMessage());
            request.getRequestDispatcher("check_status.jsp").forward(request, response);
        }
    }
}
