package dao;

import model.TimetableModel;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    // =========================
    // INSERT TIMETABLE
    // =========================
    public void addTimetable(TimetableModel t) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "INSERT INTO timetable " +
                "(class_id, subject_id, teacher_id, day, period_number, start_time, end_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, t.getClassId());
        ps.setInt(2, t.getSubjectId());
        ps.setInt(3, t.getTeacherId());
        ps.setString(4, t.getDay());
        ps.setInt(5, t.getPeriodNumber());
        ps.setTime(6, t.getStartTime());
        ps.setTime(7, t.getEndTime());

        ps.executeUpdate();

        con.close();
    }

    // =========================
    // DELETE BY CLASS + DAY
    // =========================
    public void deleteByClassAndDay(int classId, String day) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "DELETE FROM timetable WHERE class_id=? AND day=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, classId);
        ps.setString(2, day);

        ps.executeUpdate();

        con.close();
    }

    // =========================
    // CHECK CLASS SLOT EXISTS
    // =========================
    public boolean isSlotAlreadyAssigned(int classId, String day, int period) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT COUNT(*) FROM timetable WHERE class_id=? AND day=? AND period_number=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, classId);
        ps.setString(2, day);
        ps.setInt(3, period);

        ResultSet rs = ps.executeQuery();

        boolean exists = false;

        if (rs.next()) {
            exists = rs.getInt(1) > 0;
        }

        con.close();
        return exists;
    }

    // =========================
    // CHECK TEACHER CONFLICT
    // =========================
    public boolean isTeacherBusy(int teacherId, String day, int period) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT COUNT(*) FROM timetable WHERE teacher_id=? AND day=? AND period_number=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, teacherId);
        ps.setString(2, day);
        ps.setInt(3, period);

        ResultSet rs = ps.executeQuery();

        boolean busy = false;

        if (rs.next()) {
            busy = rs.getInt(1) > 0;
        }

        con.close();
        return busy;
    }

    // =========================
    // OPTIONAL: GET TEACHER TIMETABLE
    // =========================
    public List<TimetableModel> getTeacherTimetable(int teacherId) throws Exception {

        List<TimetableModel> list = new ArrayList<>();

        Connection con = DBConnection.getConnection();

        String sql =
                "SELECT t.*, c.name AS class_name, s.title AS subject_name " +
                        "FROM timetable t " +
                        "JOIN class c ON t.class_id = c.id " +
                        "JOIN subject s ON t.subject_id = s.id " +
                        "WHERE t.teacher_id=? " +
                        "ORDER BY t.day, t.period_number";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, teacherId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            TimetableModel t = new TimetableModel();

            t.setId(rs.getInt("id"));
            t.setClassId(rs.getInt("class_id"));
            t.setSubjectId(rs.getInt("subject_id"));
            t.setTeacherId(rs.getInt("teacher_id"));
            t.setDay(rs.getString("day"));
            t.setPeriodNumber(rs.getInt("period_number"));
            t.setStartTime(rs.getTime("start_time"));
            t.setEndTime(rs.getTime("end_time"));

            // ⭐ ADD THESE (IMPORTANT)
            t.setClassName(rs.getString("class_name"));
            t.setSubjectName(rs.getString("subject_name"));

            list.add(t);
        }

        con.close();
        return list;
    }

}
