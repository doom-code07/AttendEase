package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.util.*;

import model.*;
import utils.DBConnection;

public class AttendanceDAO {

    public boolean attendanceExists(int studentId, int classId, int subjectId, java.util.Date date) throws SQLException {
        String query = "SELECT COUNT(*) FROM Attendance_Register WHERE student_id=? AND class_id=? AND subject_id=? AND date=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentId);
            ps.setInt(2, classId);
            ps.setInt(3, subjectId);
            ps.setDate(4, new java.sql.Date(date.getTime()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    public void markAttendance(AttendanceModel a) throws SQLException {
        String sql = "INSERT INTO Attendance_Register (date, class_id, student_id, subject_id, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setDate(1, a.getDate());
            st.setInt(2, a.getClassId());
            st.setInt(3, a.getStudentId());
            st.setInt(4, a.getSubjectId());
            st.setString(5, a.getStatus());
            st.executeUpdate();
        }
    }

    public List<String> getLastStatuses(int studentId, java.sql.Date upToDate, int limit) throws Exception {
        String sql = "SELECT status FROM Attendance_Register WHERE student_id = ? AND date <= ? ORDER BY date DESC LIMIT ?";
        List<String> statuses = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setDate(2, upToDate);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    statuses.add(rs.getString("status"));
                }
            }
        }
        return statuses;
    }








    public List<AttendanceViewModel> getAttendanceByStudent(int studentId, String filterDate) {
        List<AttendanceViewModel> attendanceList = new ArrayList<>();

        String sql = "SELECT ar.date, c.name AS class_name, s.title AS subject_name, ar.status " +
                "FROM Attendance_Register ar " +
                "JOIN class c ON ar.class_id = c.id " +
                "JOIN subject s ON ar.subject_id = s.id " +
                "WHERE ar.student_id = ? " +
                (filterDate != null && !filterDate.isEmpty() ? "AND ar.date = ? " : "") +
                "ORDER BY ar.date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            if (filterDate != null && !filterDate.isEmpty()) {
                ps.setDate(2, java.sql.Date.valueOf(filterDate));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AttendanceViewModel a = new AttendanceViewModel();
                a.setDate(rs.getDate("date"));

                a.setClassName(rs.getString("class_name"));
                a.setSubjectName(rs.getString("subject_name"));
                a.setStatus(rs.getString("status"));
                attendanceList.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attendanceList;
    }

    public Map<String, Double> calculateAttendancePercentage(int studentId) {
        Map<String, Integer> counts = new HashMap<>();
        int total = 0;

        String sql = "SELECT status, COUNT(*) AS count FROM Attendance_Register WHERE student_id = ? GROUP BY status";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                counts.put(status, count);
                total += count;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert counts to percentages
        Map<String, Double> percentages = new HashMap<>();
        String[] statuses = {"Present", "Absent", "Leave", "Struck Off"};
        for (String status : statuses) {
            int count = counts.getOrDefault(status, 0);
            double percentage = total == 0 ? 0 : (count * 100.0) / total;
            percentages.put(status, percentage);
        }

        return percentages;
    }

    public AttendanceModel getById(int id) throws Exception {
        String sql = "SELECT * FROM Attendance_Register WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AttendanceModel a = new AttendanceModel();
                a.setId(id);
                a.setDate(rs.getDate("date"));
                a.setClassId(rs.getInt("class_id"));
                a.setStudentId(rs.getInt("student_id"));
                a.setSubjectId(rs.getInt("subject_id"));
                a.setStatus(rs.getString("status"));
                return a;
            }
        }
        return null;
    }



    public boolean updateAttendance(AttendanceModel att) throws SQLException {
        Connection con = DBConnection.getConnection();
        String query = "UPDATE Attendance_Register SET subject_id=?, status=?, date=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, att.getSubjectId());
        ps.setString(2, att.getStatus());
        ps.setDate(3, att.getDate());
        ps.setInt(4, att.getId());
        int rows = ps.executeUpdate();
        con.close();
        return rows > 0;
    }


    public AttendanceModel getAttendanceByRollNoAndDate(String rollNo, java.sql.Date date) throws SQLException {
        AttendanceModel att = null;
        Connection con = DBConnection.getConnection();

        String query = "SELECT ar.*, cs.class_id FROM Attendance_Register ar " +
                "JOIN student s ON ar.student_id = s.id " +
                "JOIN class_students cs ON cs.student_id = s.id " +
                "WHERE s.roll_no = ? AND ar.date = ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, rollNo);
        ps.setDate(2, date);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            att = new AttendanceModel();
            att.setId(rs.getInt("id"));
            att.setStudentId(rs.getInt("student_id"));
            att.setClassId(rs.getInt("class_id"));
            att.setSubjectId(rs.getInt("subject_id"));
            att.setDate(rs.getDate("date"));
            att.setStatus(rs.getString("status"));
        }
        con.close();
        return att;
    }

    public List<SubjectModel> getSubjectsByTeacherId(int teacherId) throws SQLException {
        List<SubjectModel> subjects = new ArrayList<>();
        Connection con = DBConnection.getConnection();

        String query = "SELECT * FROM subject WHERE teacher_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, teacherId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            SubjectModel sub = new SubjectModel();
            sub.setId(rs.getInt("id"));
            sub.setTitle(rs.getString("title"));
            sub.setCode(rs.getString("code"));
            subjects.add(sub);
        }

        con.close();
        return subjects;
    }

    public List<AttendanceViewModel> getAttendanceByRollNo(String rollNo) throws SQLException {
        List<AttendanceViewModel> list = new ArrayList<>();
        Connection con = DBConnection.getConnection();

        String query = "SELECT ar.id, ar.date, ar.status, " +
                "s.roll_no, u.name as student_name, c.name as class_name, sub.title as subject_title, sub.code as subject_code " +
                "FROM Attendance_Register ar " +
                "JOIN student s ON ar.student_id = s.id " +
                "JOIN users u ON s.Users_id = u.id " +
                "JOIN class_students cs ON cs.student_id = s.id " +
                "JOIN class c ON cs.class_id = c.id " +
                "JOIN subject sub ON ar.subject_id = sub.id " +
                "WHERE s.roll_no = ? ORDER BY ar.date DESC";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, rollNo);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            AttendanceViewModel att = new AttendanceViewModel();
            att.setId(rs.getInt("id"));
            att.setDate(rs.getDate("date"));
            att.setStatus(rs.getString("status"));
            att.setRollNo(rs.getString("roll_no"));
            att.setStudentName(rs.getString("student_name"));
            att.setClassName(rs.getString("class_name"));
            att.setSubjectTitle(rs.getString("subject_title"));
            att.setSubjectCode(rs.getString("subject_code"));
            list.add(att);
        }

        con.close();
        return list;
    }

    public List<AttendanceViewModel> getAttendanceByClassId(int classId) {
        List<AttendanceViewModel> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT ar.*, " +
                    "s.roll_no, " +
                    "u.name AS student_name, " +
                    "c.name AS class_name, " +
                    "sub.title AS subject_title, " +
                    "sub.code AS subject_code " +
                    "FROM Attendance_Register ar " +
                    "JOIN student s ON s.id = ar.student_id " +
                    "JOIN users u ON u.id = s.Users_id " +
                    "JOIN class c ON c.id = ar.class_id " +
                    "JOIN subject sub ON sub.id = ar.subject_id " +
                    "WHERE ar.class_id = ? " +
                    "ORDER BY ar.date DESC";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AttendanceViewModel att = new AttendanceViewModel();

                StudentModel student = new StudentModel();
                student.setRollNo(rs.getString("roll_no"));
                student.setName(rs.getString("student_name"));

                ClassModel classModel = new ClassModel();
                classModel.setName(rs.getString("class_name"));

                SubjectModel subject = new SubjectModel();
                subject.setTitle(rs.getString("subject_title"));
                subject.setCode(rs.getString("subject_code"));

                att.setId(rs.getInt("id"));
                att.setDate(rs.getDate("date"));
                att.setStatus(rs.getString("status"));
                att.setStudent(student);
                att.setClassModel(classModel);
                att.setSubject(subject);

                list.add(att);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean deleteAttendanceById(int id) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM Attendance_Register WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAllAttendanceByClassId(int classId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM Attendance_Register WHERE class_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, classId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateAttendanceForLeave(int studentId, int subjectId, Date date, String status) {
        String sql = "UPDATE attendance_register SET status = ? " +
                "WHERE student_id = ? AND subject_id = ? AND date = ? AND status = 'Present'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, studentId);
            stmt.setInt(3, subjectId);
            stmt.setDate(4, new java.sql.Date(date.getTime()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Integer getClassIdForStudent(int studentId) throws SQLException {
        String sql = "SELECT class_id FROM class_students WHERE student_id=? ORDER BY id DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("class_id");
            }
        }
        return null;
    }

    // Get all subjects for a class
    public static List<Integer> getSubjectIdsForClass(int classId) throws SQLException {
        String sql = "SELECT subject_id FROM class_subjects WHERE class_id=?";
        List<Integer> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(rs.getInt("subject_id"));
            }
        }
        return list;
    }

    public static void applyDefaultStatusForRange(int studentId,
                                                  java.sql.Date startDate,
                                                  java.sql.Date endDate,
                                                  String status) throws SQLException {

        Integer classId = getClassIdForStudent(studentId);
        if (classId == null) return;

        List<Integer> subjects = getSubjectIdsForClass(classId);
        if (subjects.isEmpty()) return;

        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(d);

                    for (Integer subjectId : subjects) {
                        // Try to update existing 'Present'
                        String upd = "UPDATE Attendance_Register " +
                                "SET status=? " +
                                "WHERE date=? AND class_id=? AND student_id=? AND subject_id=? " +
                                "AND status='Present'";
                        int updated;
                        try (PreparedStatement ps = con.prepareStatement(upd)) {
                            ps.setString(1, status);
                            ps.setDate(2, sqlDate);
                            ps.setInt(3, classId);
                            ps.setInt(4, studentId);
                            ps.setInt(5, subjectId);
                            updated = ps.executeUpdate();
                        }

                        // If nothing was updated, check existence; if none, insert a new default row
                        if (updated == 0) {
                            String check = "SELECT id, status FROM Attendance_Register " +
                                    "WHERE date=? AND class_id=? AND student_id=? AND subject_id=?";
                            boolean exists;
                            try (PreparedStatement ps = con.prepareStatement(check)) {
                                ps.setDate(1, sqlDate);
                                ps.setInt(2, classId);
                                ps.setInt(3, studentId);
                                ps.setInt(4, subjectId);
                                try (ResultSet rs = ps.executeQuery()) {
                                    exists = rs.next();
                                }
                            }

                            if (!exists) {
                                String ins = "INSERT INTO Attendance_Register " +
                                        "(date, class_id, student_id, subject_id, status) " +
                                        "VALUES (?, ?, ?, ?, ?)";
                                try (PreparedStatement ps = con.prepareStatement(ins)) {
                                    ps.setDate(1, sqlDate);
                                    ps.setInt(2, classId);
                                    ps.setInt(3, studentId);
                                    ps.setInt(4, subjectId);
                                    ps.setString(5, status);
                                    ps.executeUpdate();
                                }
                            }
                        }
                    }
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }







/*
    public int countAbsentsThisMonth(int studentId, int year, int month) {
        String sql = "SELECT COUNT(*) FROM Attendance_Register " +
                "WHERE student_id=? AND status='Absent' AND YEAR(date)=? AND MONTH(date)=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, year);
            ps.setInt(3, month);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
*/
    // in dao/AttendanceDAO.java
    public List<java.sql.Date> getAbsentDatesThisMonthAsc(int studentId, int year, int month) {
        List<java.sql.Date> out = new ArrayList<>();
        String sql = "SELECT date FROM Attendance_Register " +
                "WHERE student_id=? AND status='Absent' AND YEAR(date)=? AND MONTH(date)=?" +
                " ORDER BY date ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, year);
            ps.setInt(3, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // rs.getDate returns java.sql.Date
                    out.add(rs.getDate("date"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    // returns the date of the Nth consecutive absent that completes a streak, or null if not found
    public java.sql.Date findConsecutiveThresholdDate(int studentId, int year, int month, int threshold) {
        List<java.sql.Date> absentDates = new ArrayList<>();
        String sql = "SELECT date FROM Attendance_Register " +
                "WHERE student_id=? AND status='Absent' AND YEAR(date)=? AND MONTH(date)=? " +
                "ORDER BY date ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, year);
            ps.setInt(3, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) absentDates.add(rs.getDate("date"));
            }
        } catch (Exception e) { e.printStackTrace(); return null; }

        if (threshold <= 1 && !absentDates.isEmpty()) return absentDates.get(0);

        int streak = 1;
        for (int i = 1; i < absentDates.size(); i++) {
            java.time.LocalDate prev = absentDates.get(i - 1).toLocalDate();
            java.time.LocalDate curr = absentDates.get(i).toLocalDate();
            if (curr.equals(prev.plusDays(1))) {
                streak++;
                if (streak >= threshold) {
                    // return the date when streak reached threshold (the current date)
                    return absentDates.get(i);
                }
            } else {
                streak = 1;
            }
        }
        return null;
    }


}
