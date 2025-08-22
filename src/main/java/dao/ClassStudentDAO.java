package dao;

import utils.DBConnection;
import java.sql.*;

public class ClassStudentDAO {
    public void assignClassToStudent(int studentId, int classId) throws Exception {
        String sql = "INSERT INTO class_students (student_id, class_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, classId);
            ps.executeUpdate();
        }
    }
}
