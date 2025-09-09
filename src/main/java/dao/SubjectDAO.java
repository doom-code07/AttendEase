package dao;

import model.SubjectModel;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectDAO {

    public void addSubjectToClass(int classId, String title, String code) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String checkQuery = "SELECT s.id FROM subject s JOIN class_subjects cs ON s.id = cs.subject_id WHERE cs.class_id=? AND (s.title=? OR s.code=?)";
        PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
        checkStmt.setInt(1, classId);
        checkStmt.setString(2, title);
        checkStmt.setString(3, code);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            conn.close();
            return;
        }

        String insertSubject = "INSERT INTO subject(title, code) VALUES(?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSubject, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, title);
        insertStmt.setString(2, code);
        insertStmt.executeUpdate();
        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        int subjectId = 0;
        if (generatedKeys.next()) {
            subjectId = generatedKeys.getInt(1);
        }

        String link = "INSERT INTO class_subjects(class_id, subject_id) VALUES(?, ?)";
        PreparedStatement linkStmt = conn.prepareStatement(link);
        linkStmt.setInt(1, classId);
        linkStmt.setInt(2, subjectId);
        linkStmt.executeUpdate();

        conn.close();
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

    public void assignSubjectsToTeacher(int teacherId, int[] subjectIds) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE subject SET teacher_id = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        for (int subjectId : subjectIds) {
            ps.setInt(1, teacherId);
            ps.setInt(2, subjectId);
            ps.executeUpdate();
        }

        con.close();
    }

    public List<Map<String, String>> getAssignedSubjects() throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Connection con = DBConnection.getConnection();

        String sql = "SELECT s.id AS subject_id, s.title, u.name, u.cnic " +
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
