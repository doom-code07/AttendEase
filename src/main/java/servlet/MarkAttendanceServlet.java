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

        for (String paramName : parameterMap.keySet()) {
            if (paramName.startsWith("status_")) {
                int studentId = Integer.parseInt(paramName.substring(7));
                String status = request.getParameter(paramName);

                try {

                    if (attendanceDAO.attendanceExists(studentId, classId, subjectId, sqlDate)) {
                        duplicateFound = true;
                        System.out.println(" Attendance already marked for student " + studentId + " on " + sqlDate);
                        continue;
                    }

                    AttendanceModel attendance = new AttendanceModel();
                    attendance.setDate(sqlDate);
                    attendance.setClassId(classId);
                    attendance.setStudentId(studentId);
                    attendance.setSubjectId(subjectId);
                    attendance.setStatus(status);
                    attendanceDAO.markAttendance(attendance);

                    if ("Absent".equalsIgnoreCase(status)) {
                        int year = sqlDate.toLocalDate().getYear();
                        int month = sqlDate.toLocalDate().getMonthValue();

                        java.sql.Date thresholdDate = attendanceDAO.findConsecutiveThresholdDate(studentId, year, month, threshold);
                        if (thresholdDate != null) {

                            studentDAO.markStruckOffEvent(studentId, thresholdDate);
                        }
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (duplicateFound) {
            request.setAttribute("error", "Some attendance records were already marked for this date.");
            RequestDispatcher rd = request.getRequestDispatcher("mark_attendance.jsp");
            rd.forward(request, response);
        } else {
            response.sendRedirect("ActiveApprovedLeavesServlet");
        }
    }
}
