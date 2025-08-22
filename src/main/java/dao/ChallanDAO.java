package dao;

import utils.DBConnection;
import java.sql.*;

public class ChallanDAO {

    // Absent count for a student across all time (you can add date range later if needed)
    public static int getAbsentCount(int studentId) {
        String sql = "SELECT COUNT(*) FROM Attendance_Register WHERE student_id = ? AND status = 'Absent'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // Compute fine using either student.total_fine or fallback to absent * policy
    public static int computeFineAmount(StudentDAO.StudentInfo st) {
        if (st.totalFineNullable != null) return st.totalFineNullable; // use maintained total
        int finePerAbsent = PolicyDAO.getFinePerAbsentSubject();
        int absents = getAbsentCount(st.studentId);
        return finePerAbsent * absents;
    }
}
