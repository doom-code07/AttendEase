package dao;

import model.LeaveApplication;
import model.LeaveOnDay;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate; // ✅ FIX: Import LocalDate
import java.util.ArrayList;
import java.util.List;

import static dao.AttendanceDAO.updateAttendanceForLeave;
import static dao.StudentDAO.findById;

public class LeaveApplicationDAO {

    public static void save(LeaveApplication app) throws SQLException {
        String sql = "INSERT INTO LeaveApplication (applicant_id, teacher_id, status, description, start_date, end_date, is_teacher_application) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, app.getApplicantId());
            if (app.getTeacherId() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, app.getTeacherId());
            ps.setString(3, app.getStatus() == null ? "Pending" : app.getStatus());
            ps.setString(4, app.getDescription());
            ps.setDate(5, app.getStartDate());
            ps.setDate(6, app.getEndDate());
            ps.setBoolean(7, app.isTeacherApplication());
            ps.executeUpdate();
        }
    }

    public List<LeaveApplication> findForTeacher(int teacherId) throws Exception {
        List<LeaveApplication> applications = new ArrayList<>();

        String sql = "SELECT la.id, la.start_date, la.end_date, la.description, la.status, " +
                "s.roll_no, u.name AS student_name, c.name AS class_name " +
                "FROM LeaveApplication la " +
                "JOIN student s ON la.applicant_id = s.id " +
                "JOIN users u ON s.Users_id = u.id " +
                "JOIN class_students cs ON s.id = cs.student_id " +
                "JOIN class c ON cs.class_id = c.id " +
                "WHERE la.teacher_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveApplication app = new LeaveApplication();
                app.setId(rs.getInt("id"));
                app.setStartDate(rs.getDate("start_date"));
                app.setEndDate(rs.getDate("end_date"));
                app.setDescription(rs.getString("description"));
                app.setStatus(rs.getString("status"));
                app.setRollNo(rs.getString("roll_no"));
                app.setStudentName(rs.getString("student_name"));
                app.setClassName(rs.getString("class_name"));
                applications.add(app);
            }
        }
        return applications;
    }

    public List<LeaveApplication> findForVicePrincipal() throws Exception {
        List<LeaveApplication> list = new ArrayList<>();
        String sql = "SELECT la.*, s.roll_no, u.name, c.name AS class_name " +
                "FROM LeaveApplication la " +
                "JOIN student s ON la.applicant_id = s.id " +
                "JOIN users u ON s.Users_id = u.id " +
                "JOIN class_students cs ON s.id = cs.student_id " +
                "JOIN class c ON cs.class_id = c.id " +
                "WHERE la.is_teacher_application = FALSE " +
                "ORDER BY la.start_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LeaveApplication app = new LeaveApplication();
                app.setId(rs.getInt("id"));
                app.setApplicantId(rs.getInt("applicant_id"));
                app.setStartDate(rs.getDate("start_date"));
                app.setEndDate(rs.getDate("end_date"));
                app.setDescription(rs.getString("description"));
                app.setStatus(rs.getString("status"));
                app.setRollNo(rs.getString("roll_no"));
                app.setStudentName(rs.getString("name"));
                app.setClassName(rs.getString("class_name"));

                list.add(app);
            }
        }
        return list;
    }

    public List<LeaveApplication> findByStudent(int studentId) throws Exception {
        List<LeaveApplication> applications = new ArrayList<>();
        String sql = "SELECT id, status, description, start_date, end_date, decision_date " +
                "FROM LeaveApplication WHERE applicant_id = ? ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LeaveApplication app = new LeaveApplication();
                app.setId(rs.getInt("id"));
                app.setStatus(rs.getString("status"));
                app.setDescription(rs.getString("description"));
                app.setStartDate(rs.getDate("start_date"));
                app.setEndDate(rs.getDate("end_date"));
                app.setDecisionDate(rs.getDate("decision_date"));
                applications.add(app);
            }
        }
        return applications;
    }


    public static void updateStatus(int appId, String status, Integer decisionBy) throws SQLException {
        String sql = "UPDATE LeaveApplication SET status = ?, decision_by = ?, decision_date = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            if (decisionBy == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, decisionBy);
            ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.setInt(4, appId);
            ps.executeUpdate();
        }
    }

    private static LeaveApplication mapRow(ResultSet rs) throws SQLException {
        LeaveApplication a = new LeaveApplication();
        a.setId(rs.getInt("id"));
        a.setApplicantId(rs.getInt("applicant_id"));
        int tId = rs.getInt("teacher_id");
        if (rs.wasNull()) a.setTeacherId(null); else a.setTeacherId(tId);
        a.setStatus(rs.getString("status"));
        a.setDescription(rs.getString("description"));
        a.setStartDate(rs.getDate("start_date"));
        a.setEndDate(rs.getDate("end_date"));
        a.setTeacherApplication(rs.getBoolean("is_teacher_application"));
        a.setDecisionBy(rs.getInt("decision_by"));
        if (rs.wasNull()) a.setDecisionBy(null);
        a.setDecisionDate(rs.getDate("decision_date"));
        return a;
    }

    public static LeaveApplication getApplicationById(int applicationId) {
        String sql = "SELECT id, applicant_id, start_date, end_date, status " +
                "FROM LeaveApplication WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LeaveApplication app = new LeaveApplication();
                    app.setId(rs.getInt("id"));
                    app.setApplicantId(rs.getInt("applicant_id"));
                    app.setStartDate(rs.getDate("start_date"));
                    app.setEndDate(rs.getDate("end_date"));
                    app.setStatus(rs.getString("status"));
                    return app;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<LeaveOnDay> getActiveApprovedLeaves(LocalDate date, Integer classId) throws SQLException {
        List<LeaveOnDay> list = new ArrayList<>();

        String sql =
                "SELECT s.id AS student_id, s.roll_no, u.name AS student_name, " +
                        "       cs.class_id, c.name AS class_name, " +
                        "       la.start_date, la.end_date, " +
                        "       (DATEDIFF(la.end_date, la.start_date) + 1) AS leave_days " +
                        "FROM LeaveApplication la " +
                        "JOIN student s ON la.applicant_id = s.id " +
                        "JOIN users u ON s.Users_id = u.id " +
                        "LEFT JOIN class_students cs ON cs.student_id = s.id " +
                        "LEFT JOIN class c ON c.id = cs.class_id " +
                        "WHERE la.status = 'Approved' " +
                        "  AND ? BETWEEN la.start_date AND la.end_date " +
                        "  AND (? IS NULL OR cs.class_id = ?) " +
                        "ORDER BY s.roll_no ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            if (classId == null) {
                ps.setNull(2, Types.INTEGER);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(2, classId);
                ps.setInt(3, classId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LeaveOnDay item = new LeaveOnDay(
                            rs.getInt("student_id"),
                            rs.getString("roll_no"),
                            rs.getString("student_name"),
                            (Integer) rs.getObject("class_id"),
                            rs.getString("class_name"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getInt("leave_days")
                    );
                    list.add(item);
                }
            }
        }

        return list;
    }

    public static boolean hasSubmittedToday(int studentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LeaveApplication " +
                "WHERE applicant_id = ? AND DATE(start_date) = CURDATE()";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    public static boolean deleteAllApplications() {
        String sql = "DELETE FROM LeaveApplication";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
