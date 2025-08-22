package dao;

import utils.DBConnection;
import model.PolicyModel;

import java.sql.*;

public class AttendancePolicyDAO {

    public void applyPolicies() {
        PolicyDAO policyDAO = new PolicyDAO();
        PolicyModel policy = policyDAO.getCurrentPolicy();

        int minPercentage = policy.getMinAttendancePercentage();
        int finePerAbsent = policy.getFinePerAbsentSubject();
        int maxAbsents = policy.getStruckOffAfterAbsents();

        try (Connection con = DBConnection.getConnection()) {
            String getStudents = "SELECT id FROM student";
            PreparedStatement ps = con.prepareStatement(getStudents);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int studentId = rs.getInt("id");

                int total = getCount(con, studentId, "ALL");
                int presents = getCount(con, studentId, "Present");
                int absents = getCount(con, studentId, "Absent");

                int attendancePercentage = (total > 0) ? (presents * 100) / total : 0;
                int fine = absents * finePerAbsent;

                boolean isStruck = (attendancePercentage < minPercentage || absents >= maxAbsents);

                // Update struck off in Attendance_Register
                if (isStruck) {
                    String updateStatus = "UPDATE Attendance_Register SET status='Struck Off' WHERE student_id=?";
                    PreparedStatement updatePs = con.prepareStatement(updateStatus);
                    updatePs.setInt(1, studentId);
                    updatePs.executeUpdate();

                    // Optionally update student table flag
                    String updateStruck = "UPDATE student SET is_struck_off = TRUE, struck_off_date = CURRENT_DATE WHERE id = ?";
                    PreparedStatement struckPs = con.prepareStatement(updateStruck);
                    struckPs.setInt(1, studentId);
                    struckPs.executeUpdate();
                }

                // Update fine in student table
                String updateFine = "UPDATE student SET total_fine = ? WHERE id = ?";
                PreparedStatement finePs = con.prepareStatement(updateFine);
                finePs.setInt(1, fine);
                finePs.setInt(2, studentId);
                finePs.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCount(Connection con, int studentId, String status) throws SQLException {
        String base = "SELECT COUNT(*) FROM Attendance_Register WHERE student_id=?";
        String query = status.equals("ALL") ? base : base + " AND status=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, studentId);
        if (!status.equals("ALL")) ps.setString(2, status);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }
}
