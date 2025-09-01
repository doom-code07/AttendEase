package servlet;

import dao.AttendanceDAO;
import dao.StudentDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.AttendanceModel;
import model.StudentModel;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.*;
import java.util.Map;
import java.util.List;

public class MarkAttendanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int classId = Integer.parseInt(request.getParameter("classId"));
        int subjectId = Integer.parseInt(request.getParameter("subjectId"));
        String dateStr = request.getParameter("date");
        Date sqlDate = Date.valueOf(dateStr);

        AttendanceDAO attendanceDAO = new AttendanceDAO();
        Map<String, String[]> parameterMap = request.getParameterMap();
        boolean duplicateFound = false;

        for (String paramName : parameterMap.keySet()) {
            if (paramName.startsWith("status_")) {
                int studentId = Integer.parseInt(paramName.substring(7));
                String status = request.getParameter(paramName);

                AttendanceModel attendance = new AttendanceModel();
                attendance.setDate(sqlDate);
                attendance.setClassId(classId);
                attendance.setStudentId(studentId);
                attendance.setSubjectId(subjectId);
                attendance.setStatus(status);

                try {
                    if (attendanceDAO.attendanceExists(studentId, classId, subjectId, sqlDate)) {
                        duplicateFound = true;
                        System.out.println("⚠️ Attendance already marked for student " + studentId + " on " + sqlDate);
                    } else {
                        attendanceDAO.markAttendance(attendance);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        int threshold = 0;
        try (Connection con = utils.DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT struck_off_after_absents FROM Policies ORDER BY id DESC LIMIT 1");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                threshold = rs.getInt("struck_off_after_absents");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Fetch struck-off students based on threshold
        List<StudentModel> struckOffStudents = new StudentDAO().getStruckOffCandidates(threshold);
        request.setAttribute("struckOffStudents", struckOffStudents);

        if (duplicateFound) {
            request.setAttribute("error", "Some attendance records were already marked for this date.");
            RequestDispatcher rd = request.getRequestDispatcher("mark_attendance.jsp");
            rd.forward(request, response);
        } else {
            response.sendRedirect("ActiveApprovedLeavesServlet");
        }
    }
}
