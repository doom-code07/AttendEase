package servlet;

import dao.AttendanceDAO;
import dao.SubjectDAO;
import model.AttendanceModel;
import model.SubjectModel;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class FetchAttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rollNo = request.getParameter("rollNo");
        Date date = Date.valueOf(request.getParameter("date"));

        try {
            AttendanceDAO dao = new AttendanceDAO();
            AttendanceModel att = dao.getAttendanceByRollNoAndDate(rollNo, date);

            if (att != null) {
                SubjectDAO sdao = new SubjectDAO();
                int teacherId = 1; // Replace with dynamic value if available in session
                List<SubjectModel> subjects = sdao.getSubjectsByTeacherId(teacherId);

                request.setAttribute("attendance", att);
                request.setAttribute("subjects", subjects);
                request.getRequestDispatcher("update_attendance.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "No record found.");
                request.getRequestDispatcher("update_attendance.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
