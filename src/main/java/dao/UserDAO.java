package dao;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import model.UserModel;
import utils.DBConnection;

public class UserDAO {

    public UserModel getUserByUsername(String username) throws Exception {
        UserModel user = null;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setCnic(rs.getString("cnic"));
                user.setRole(rs.getString("role"));
            }
        }

        return user;
    }

    public UserModel validateUser(String username, String plainPassword) throws Exception {
        UserModel user = getUserByUsername(username);
        if (user != null) {
            String hashedInput = hashPassword(plainPassword);
            if (hashedInput.equals(user.getPassword())) {
                return user;
            }
            System.out.println("Input Hash: " + hashedInput);
            System.out.println("DB Hash: " + user.getPassword());

        }
        return null;
    }

    public int insertUser(UserModel user) throws Exception {
        String sql = "INSERT INTO users (name, username, password, email, cnic, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getCnic());
            ps.setString(6, user.getRole());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public boolean isDuplicate(String username, String email, String rollNo, String cnic) throws Exception {
        String sql = "SELECT * FROM users u JOIN student s ON u.id = s.Users_id WHERE u.username = ? OR u.email = ? OR u.cnic = ? OR s.roll_no = ?";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, cnic);
            ps.setString(4, rollNo);
            return ps.executeQuery().next();
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean emailExists(String email) throws Exception {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void updatePasswordByEmail(String email, String newHashedPassword) throws Exception {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newHashedPassword);
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }



}
