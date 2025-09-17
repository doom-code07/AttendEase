package servlet;

import dao.LeaveApplicationDAO;
import dao.TeacherDAO;
import model.LeaveApplication;
import model.Teacher;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;


public class TeacherApplicationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
            int userId = (int) session.getAttribute("userId");
            List<Teacher> teachers = TeacherDAO.listAllTeachers();
            Integer teacherId = null;
            for (Teacher t : teachers) {
                if (t.getUsersId() == userId) {
                    teacherId = t.getId();
                    break;
                }
            }
            if (teacherId == null) {
                request.setAttribute("error", "Teacher record not found.");
                request.getRequestDispatcher("applications.jsp").forward(request, response);
                return;
            }
            LeaveApplicationDAO dao = new LeaveApplicationDAO();
            List<LeaveApplication> apps = dao.findForTeacher(teacherId);

            request.setAttribute("applications", apps);
            request.getRequestDispatcher("applications.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Server error: " + ex.getMessage());
            request.getRequestDispatcher("applications.jsp").forward(request, response);
        }
    }
}
