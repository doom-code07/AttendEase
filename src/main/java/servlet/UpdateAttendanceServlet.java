package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

import utils.DBConnection;

public class UpdateAttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int attendanceId = Integer.parseInt(request.getParameter("attendance_id"));
            String rollNo = request.getParameter("roll_no");
            String status = request.getParameter("status");
            int subjectId = Integer.parseInt(request.getParameter("subject_id"));
            Date newDate = Date.valueOf(request.getParameter("date"));

            System.out.println("---- Form Submission ----");
            System.out.println("attendance_id: " + attendanceId);
            System.out.println("roll_no: " + rollNo);
            System.out.println("subject_id: " + subjectId);
            System.out.println("status: " + status);
            System.out.println("date: " + newDate);

            Connection con = DBConnection.getConnection();
            String query = "UPDATE Attendance_Register SET status = ?, subject_id = ?, date = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, subjectId);
            ps.setDate(3, newDate); // update with new date
            ps.setInt(4, attendanceId);

            int rows = ps.executeUpdate();
            System.out.println("Rows updated: " + rows);

            con.close();

            // redirect to updated attendance
            response.sendRedirect("update_attendance.jsp?roll_no=" + rollNo + "&date=" + newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
