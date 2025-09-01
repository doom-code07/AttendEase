package servlet;

import dao.AttendanceDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.AttendanceViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ViewAttendanceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer studentId = (Integer) session.getAttribute("studentId");

        if (studentId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String filterDate = request.getParameter("filterDate"); // optional

        AttendanceDAO dao = new AttendanceDAO();

        List<AttendanceViewModel> records = dao.getAttendanceByStudent(studentId, filterDate);
        Map<String, Double> percentages = dao.calculateAttendancePercentage(studentId); // ✅ Correct call

        request.setAttribute("records", records);
        request.setAttribute("filterDate", filterDate);
        request.setAttribute("attendancePercentages", percentages); // ✅ Set before forwarding

        RequestDispatcher rd = request.getRequestDispatcher("view_attendance.jsp");
        rd.forward(request, response);
    }
}
