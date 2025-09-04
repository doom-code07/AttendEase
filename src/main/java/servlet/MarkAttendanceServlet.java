package servlet;

import dao.AttendanceDAO;
import dao.StudentDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.AttendanceModel;
import utils.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

public class MarkAttendanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int classId = Integer.parseInt(request.getParameter("classId"));
        int subjectId = Integer.parseInt(request.getParameter("subjectId"));
        Date sqlDate = Date.valueOf(request.getParameter("date"));

        AttendanceDAO attendanceDAO = new AttendanceDAO();
        StudentDAO studentDAO = new StudentDAO();
        Map<String, String[]> parameterMap = request.getParameterMap();
        boolean duplicateFound = false;

        // Fetch threshold from Policies table
        int threshold = 0;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT struck_off_after_absents FROM Policies ORDER BY id DESC LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                threshold = rs.getInt("struck_off_after_absents");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Loop through all student status inputs
        for (String paramName : parameterMap.keySet()) {
            if (paramName.startsWith("status_")) {
                int studentId = Integer.parseInt(paramName.substring(7));
                String status = request.getParameter(paramName);

                try {
                    // Check duplicate attendance
                    if (attendanceDAO.attendanceExists(studentId, classId, subjectId, sqlDate)) {
                        duplicateFound = true;
                        System.out.println("⚠️ Attendance already marked for student " + studentId + " on " + sqlDate);
                        continue;
                    }

                    // Mark attendance for all statuses
                    AttendanceModel attendance = new AttendanceModel();
                    attendance.setDate(sqlDate);
                    attendance.setClassId(classId);
                    attendance.setStudentId(studentId);
                    attendance.setSubjectId(subjectId);
                    attendance.setStatus(status);
                    attendanceDAO.markAttendance(attendance);

                    // Apply struck-off policy ONLY if status is "Absent"
                    if ("Absent".equalsIgnoreCase(status)) {
                        int year = sqlDate.toLocalDate().getYear();
                        int month = sqlDate.toLocalDate().getMonthValue();
                        int absentCount = studentDAO.countAbsentsThisMonth(studentId, year, month);

                        if (absentCount >= threshold) {
                            // Always mark struck-off when threshold reached
                            studentDAO.markStruckOffEvent(studentId, sqlDate);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Forward error if duplicate found
        if (duplicateFound) {
            request.setAttribute("error", "Some attendance records were already marked for this date.");
            RequestDispatcher rd = request.getRequestDispatcher("mark_attendance.jsp");
            rd.forward(request, response);
        } else {
            response.sendRedirect("ActiveApprovedLeavesServlet");
        }
    }
}
