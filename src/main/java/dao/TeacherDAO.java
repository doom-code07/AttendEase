package dao;

import model.LeaveApplication;
import model.Teacher;
import model.UserModel;

import model.TeacherModel;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    public void addTeacher(UserModel user, TeacherModel teacher) throws Exception {
        Connection con = DBConnection.getConnection();

        String insertUserSql = "INSERT INTO users (name, username, password, email, cnic, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement psUser = con.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
        psUser.setString(1, user.getName());
        psUser.setString(2, user.getUsername());
        psUser.setString(3, user.getPassword());
        psUser.setString(4, user.getEmail());
        psUser.setString(5, user.getCnic());
        psUser.setString(6, "teacher");
        psUser.executeUpdate();

        ResultSet rs = psUser.getGeneratedKeys();
        if (rs.next()) {
            int userId = rs.getInt(1);

            String insertTeacherSql = "INSERT INTO teacher (Users_id, qualification) VALUES (?, ?)";
            PreparedStatement psTeacher = con.prepareStatement(insertTeacherSql);
            psTeacher.setInt(1, userId);
            psTeacher.setString(2, teacher.getQualification());
            psTeacher.executeUpdate();
        }

        con.close();
    }


    public List<TeacherModel> getAllTeachers() throws Exception {
        List<TeacherModel> teachers = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT t.id, u.name, u.cnic, t.qualification FROM teacher t JOIN users u ON t.Users_id = u.id WHERE u.role = 'teacher'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TeacherModel teacher = new TeacherModel();
                teacher.setId(rs.getInt("id"));
                teacher.setName(rs.getString("name"));
                teacher.setCnic(rs.getString("cnic"));
                teacher.setQualification(rs.getString("qualification"));
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public TeacherModel getTeacherById(int id) throws Exception {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT t.id, u.id as user_id, u.name, u.username, u.email, u.cnic, t.qualification FROM teacher t JOIN users u ON t.Users_id = u.id WHERE t.id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TeacherModel teacher = new TeacherModel();
                teacher.setId(rs.getInt("id"));
                teacher.setUsersId(rs.getInt("user_id"));
                teacher.setName(rs.getString("name"));
                teacher.setUsername(rs.getString("username"));
                teacher.setEmail(rs.getString("email"));
                teacher.setCnic(rs.getString("cnic"));
                teacher.setQualification(rs.getString("qualification"));
                return teacher;
            }
        }
        return null;
    }


    public void updateTeacher(TeacherModel teacher) throws Exception {
        Connection con = DBConnection.getConnection();

        String updateUserSql = "UPDATE users SET name=?, username=?, email=?, cnic=? WHERE id=?";
        PreparedStatement psUser = con.prepareStatement(updateUserSql);
        psUser.setString(1, teacher.getName());
        psUser.setString(2, teacher.getUsername());
        psUser.setString(3, teacher.getEmail());
        psUser.setString(4, teacher.getCnic());
        psUser.setInt(5, teacher.getUsersId());
        psUser.executeUpdate();

        String updateTeacherSql = "UPDATE teacher SET qualification=? WHERE id=?";
        PreparedStatement psTeacher = con.prepareStatement(updateTeacherSql);
        psTeacher.setString(1, teacher.getQualification());
        psTeacher.setInt(2, teacher.getId());
        psTeacher.executeUpdate();

        con.close();
    }


    public void deleteTeacher(int id) throws Exception {
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement getUser = con.prepareStatement("SELECT Users_id FROM teacher WHERE id=?");
            getUser.setInt(1, id);
            ResultSet rs = getUser.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("Users_id");

                PreparedStatement t = con.prepareStatement("DELETE FROM teacher WHERE id=?");
                t.setInt(1, id);
                t.executeUpdate();

                PreparedStatement u = con.prepareStatement("DELETE FROM users WHERE id=?");
                u.setInt(1, userId);
                u.executeUpdate();
            }
        }
    }

    public void deleteAllTeachers() throws Exception {
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement("DELETE u FROM users u JOIN teacher t ON u.id = t.Users_id WHERE u.role = 'teacher'");
            ps.executeUpdate();
        }
    }

    public int getTeacherIdByUserId(int userId) {
        int teacherId = -1;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id FROM teacher WHERE Users_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                teacherId = rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Returned teacherId: " + teacherId);
        return teacherId;
    }

    public static List<Teacher> listAllTeachers() throws SQLException {
        String sql = "SELECT t.id, t.Users_id, u.name FROM teacher t JOIN users u ON t.Users_id = u.id ORDER BY u.name";
        List<Teacher> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Teacher t = new Teacher();
                t.setId(rs.getInt("id"));
                t.setUsersId(rs.getInt("Users_id"));
                t.setName(rs.getString("name"));
                list.add(t);
            }
        }
        return list;
    }

    public static Teacher findById(int id) throws SQLException {
        String sql = "SELECT t.id, t.Users_id, u.name FROM teacher t JOIN users u ON t.Users_id = u.id WHERE t.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Teacher t = new Teacher();
                    t.setId(rs.getInt("id"));
                    t.setUsersId(rs.getInt("Users_id"));
                    t.setName(rs.getString("name"));
                    return t;
                }
            }
        }
        return null;
    }


    public static List<LeaveApplication> getApplicationsForTeacher(int teacherId) {
        List<LeaveApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_applications WHERE teacher_id = ? AND is_teacher_application = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LeaveApplication app = new LeaveApplication();
                list.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



}
