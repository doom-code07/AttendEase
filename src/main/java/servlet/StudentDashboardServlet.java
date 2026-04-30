package servlet;

import dao.StudentDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.StudentModel;
import model.PolicyModel;   // ✅ ADD THIS

import java.io.IOException;

public class StudentDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Integer studentId = (Integer) session.getAttribute("studentId");

            if (studentId != null) {
                StudentDAO studentDAO = new StudentDAO();

                // 🔹 EXISTING (DON'T TOUCH)
                studentDAO.updateTotalFine(studentId);

                StudentModel student = studentDAO.getStudentById(studentId);
                request.setAttribute("student", student);

                // ================== NEW CODE (SAFE) ==================

                // 1️⃣ Attendance Percentage (Circle Graph)
                double attendancePercent =
                        studentDAO.getAttendancePercentage(studentId);
                request.setAttribute("attendancePercent", attendancePercent);

                // 2️⃣ Attendance Policy (Info Cards)
                PolicyModel policy =
                        studentDAO.getCurrentAttendancePolicy();
                request.setAttribute("policy", policy);

                // =====================================================

                RequestDispatcher rd =
                        request.getRequestDispatcher("student_dashboard.jsp");
                rd.forward(request, response);

            } else {
                response.sendRedirect("login.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
