package dao;

import model.PolicyModel;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PolicyDAO {

    // Insert a new policy
    public void addPolicy(PolicyModel policy) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO Policies (min_attendance_percentage, fine_per_absent_subject, struck_off_after_absents) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, policy.getMinAttendancePercentage());
        ps.setInt(2, policy.getFinePerAbsentSubject());
        ps.setInt(3, policy.getStruckOffAfterAbsents());
        ps.executeUpdate();
        con.close();
    }

    // Update the existing policy (assuming only one row exists)
    public void updatePolicy(PolicyModel policy) throws Exception {
        Connection con = DBConnection.getConnection();
        // Using a subquery to target the single row, since you expect only one
        String sql = "UPDATE Policies SET min_attendance_percentage = ?, fine_per_absent_subject = ?, struck_off_after_absents = ? WHERE id = (SELECT id FROM (SELECT id FROM Policies LIMIT 1) AS t)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, policy.getMinAttendancePercentage());
        ps.setInt(2, policy.getFinePerAbsentSubject());
        ps.setInt(3, policy.getStruckOffAfterAbsents());
        ps.executeUpdate();
        con.close();
    }

    // Check if a policy record exists
    public boolean policyExists() throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM Policies";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        boolean exists = false;
        if (rs.next()) {
            exists = rs.getInt(1) > 0;
        }
        con.close();
        return exists;
    }

    public PolicyModel getCurrentPolicy() {
        String sql = "SELECT id, min_attendance_percentage, fine_per_absent_subject, struck_off_after_absents " +
                "FROM Policies ORDER BY id DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PolicyModel p = new PolicyModel();
                    p.setId(rs.getInt("id"));
                    p.setMinAttendancePercentage(rs.getInt("min_attendance_percentage"));
                    p.setFinePerAbsentSubject(rs.getInt("fine_per_absent_subject"));
                    p.setStruckOffAfterAbsents(rs.getInt("struck_off_after_absents"));
                    return p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // If no policy yet, caller should guard against null.
    }

    // Retrieve current policy (if needed for display)
    public PolicyModel getPolicy() throws Exception {
        PolicyModel policy = null;
        Connection con = DBConnection.getConnection();
        String sql = "SELECT * FROM Policies LIMIT 1";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            policy = new PolicyModel();
            policy.setId(rs.getInt("id"));
            policy.setMinAttendancePercentage(rs.getInt("min_attendance_percentage"));
            policy.setFinePerAbsentSubject(rs.getInt("fine_per_absent_subject"));
            policy.setStruckOffAfterAbsents(rs.getInt("struck_off_after_absents"));
        }
        con.close();
        return policy;
    }

    // Optionally, a method to delete a policy if needed
    public void deletePolicy(int id) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "DELETE FROM Policies WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        con.close();
    }


    public static int getFinePerAbsentSubject() {
        String sql = "SELECT fine_per_absent_subject FROM Policies ORDER BY id DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }













    public PolicyModel getLatestPolicy() {
        String sql = "SELECT id, min_attendance_percentage, fine_per_absent_subject, struck_off_after_absents FROM Policies ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                PolicyModel p = new PolicyModel();
                p.setId(rs.getInt("id"));
                p.setMinAttendancePercentage(rs.getInt("min_attendance_percentage"));
                p.setFinePerAbsentSubject(rs.getInt("fine_per_absent_subject"));
                p.setStruckOffAfterAbsents(rs.getInt("struck_off_after_absents"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}



