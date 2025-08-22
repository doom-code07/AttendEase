package dao;

import model.ClassModel;
import utils.DBConnection;

import java.sql.*;
import java.util.*;

public class ClassDAO {
    public boolean addClass(String className) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO class(name) VALUES(?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, className);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // likely duplicate
        }
    }

    public List<ClassModel> getAllClasses() {
        List<ClassModel> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM class";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(new ClassModel(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteClass(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM class WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class ClassItem {
        public int id;
        public String name;
        public ClassItem(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString(){ return name; }
    }

    public static String getClassNameById(int classId) {
        String sql = "SELECT name FROM class WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("name");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
