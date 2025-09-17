package dao;

import model.SubjectModel;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectDAO {

    public boolean addSubjectToClass(int classId, String title, String code) throws Exception {
        Connection con = DBConnection.getConnection();

        // Check if subject with same title and code already exists in that class
        String checkSql = "SELECT COUNT(*) FROM subject s " +
                "JOIN class_subjects cs ON s.id = cs.subject_id " +
                "WHERE cs.class_id = ? AND s.title = ? AND s.code = ?";
        PreparedStatement checkPs = con.prepareStatement(checkSql);
        checkPs.setInt(1, classId);
        checkPs.setString(2, title);
        checkPs.setString(3, code);

        ResultSet rs = checkPs.executeQuery();
        rs.next();
        if (rs.getInt(1) > 0) {
            con.close();
            return false; // duplicate found
        }

        // Insert subject
        String insertSubject = "INSERT INTO subject (title, code) VALUES (?, ?)";
        PreparedStatement ps1 = con.prepareStatement(insertSubject, Statement.RETURN_GENERATED_KEYS);
        ps1.setString(1, title);
        ps1.setString(2, code);
        ps1.executeUpdate();

        ResultSet keys = ps1.getGeneratedKeys();
        int subjectId = 0;
        if (keys.next()) {
            subjectId = keys.getInt(1);
        }

        // Map subject to class
        String insertClassSubject = "INSERT INTO class_subjects (class_id, subject_id) VALUES (?, ?)";
        PreparedStatement ps2 = con.prepareStatement(insertClassSubject);
        ps2.setInt(1, classId);
        ps2.setInt(2, subjectId);
        ps2.executeUpdate();

        con.close();
        return true;
    }


    public List<SubjectModel> getSubjectsByClassId(int classId) {
        List<SubjectModel> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT s.id, s.title, s.code FROM subject s JOIN class_subjects cs ON s.id = cs.subject_id WHERE cs.class_id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SubjectModel sub = new SubjectModel();
                sub.setId(rs.getInt("id"));
                sub.setTitle(rs.getString("title"));
                sub.setCode(rs.getString("code"));
                list.add(sub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteSubjectFromClass(int subjectId, int classId) {
        try (Connection conn = DBConnection.getConnection()) {

            String deleteClassSubject = "DELETE FROM class_subjects WHERE subject_id=? AND class_id=?";
            PreparedStatement stmt1 = conn.prepareStatement(deleteClassSubject);
            stmt1.setInt(1, subjectId);
            stmt1.setInt(2, classId);
            stmt1.executeUpdate();

            String check = "SELECT COUNT(*) FROM class_subjects WHERE subject_id=?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, subjectId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String deleteSub = "DELETE FROM subject WHERE id=?";
                PreparedStatement stmt2 = conn.prepareStatement(deleteSub);
                stmt2.setInt(1, subjectId);
                stmt2.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM subject WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubjectsByClassId(int classId) {
        String getSubjectsQuery = "SELECT subject_id FROM class_subjects WHERE class_id = ?";
        String deleteSubjectQuery = "DELETE FROM subject WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psGet = conn.prepareStatement(getSubjectsQuery)) {

            psGet.setInt(1, classId);
            ResultSet rs = psGet.executeQuery();

            while (rs.next()) {
                int subjectId = rs.getInt("subject_id");

                try (PreparedStatement psDelete = conn.prepareStatement(deleteSubjectQuery)) {
                    psDelete.setInt(1, subjectId);
                    psDelete.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<SubjectModel> getAllSubjects() throws Exception {
        List<SubjectModel> list = new ArrayList<>();
        Connection con = DBConnection.getConnection();
        String sql = "SELECT * FROM subject";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            SubjectModel s = new SubjectModel();
            s.setId(rs.getInt("id"));
            s.setTitle(rs.getString("title"));
            s.setCode(rs.getString("code"));
            s.setTeacherId(rs.getInt("teacher_id"));
            list.add(s);
        }
        con.close();
        return list;
    }

    public String assignSubjectsToTeacher(int teacherId, int[] subjectIds) throws Exception {
        Connection con = DBConnection.getConnection();
        String checkSql = "SELECT teacher_id, title, code FROM subject WHERE id = ?";
        String updateSql = "UPDATE subject SET teacher_id = ? WHERE id = ?";

        PreparedStatement checkPs = con.prepareStatement(checkSql);
        PreparedStatement updatePs = con.prepareStatement(updateSql);

        StringBuilder message = new StringBuilder();

        for (int subjectId : subjectIds) {
            checkPs.setInt(1, subjectId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                int currentTeacher = rs.getInt("teacher_id");
                String title = rs.getString("title");
                String code = rs.getString("code");

                if (currentTeacher != 0) {
                    if (currentTeacher == teacherId) {
                        // Already assigned to the same teacher
                        message.append("Already assigned: ").append(title).append(" (").append(code).append(")<br>");
                        continue;
                    } else {
                        // Conflict with another teacher
                        message.append("Conflict: ").append(title).append(" (").append(code).append(") already assigned to another teacher.<br>");
                        continue;
                    }
                }
            }

            // Assign subject if not assigned
            updatePs.setInt(1, teacherId);
            updatePs.setInt(2, subjectId);
            updatePs.executeUpdate();
        }

        con.close();

        return message.toString(); // empty if everything was newly assigned
    }

    public List<Map<String, String>> getAssignedSubjects() throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Connection con = DBConnection.getConnection();

        String sql = "SELECT s.id AS subject_id, s.title, s.code, u.name, u.cnic " +
                "FROM subject s " +
                "JOIN teacher t ON s.teacher_id = t.id " +
                "JOIN users u ON t.Users_id = u.id " +
                "WHERE s.teacher_id IS NOT NULL";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Map<String, String> row = new HashMap<>();
            row.put("subject_id", String.valueOf(rs.getInt("subject_id")));
            row.put("title", rs.getString("title"));
            row.put("code", rs.getString("code"));
            row.put("name", rs.getString("name"));
            row.put("cnic", rs.getString("cnic"));
            list.add(row);
        }

        con.close();
        return list;
    }


    public void unassignSubject(int subjectId) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE subject SET teacher_id = NULL WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, subjectId);
        ps.executeUpdate();
        con.close();
    }

    public void unassignAllSubjects() throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE subject SET teacher_id = NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
        con.close();
    }

    public List<SubjectModel> getSubjectsByTeacherId(int teacherId) {
        List<SubjectModel> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM subject WHERE teacher_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SubjectModel subject = new SubjectModel();
                subject.setId(rs.getInt("id"));
                subject.setTitle(rs.getString("title"));
                subject.setCode(rs.getString("code"));
                subject.setTeacherId(rs.getInt("teacher_id"));
                list.add(subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



}
