package dao;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

import model.PolicyModel;
import model.Student;
import model.StudentModel;
import utils.DBConnection;

public class StudentDAO {
    public void insertStudent(StudentModel student) throws Exception {
        String sql = "INSERT INTO student (Users_id, roll_no, batch) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, student.getUserId());
            ps.setString(2, student.getRollNo());
            ps.setString(3, student.getBatch());
            ps.executeUpdate();
        }
    }

    public int insertStudentReturnId(StudentModel student) throws Exception {
        String sql = "INSERT INTO student (Users_id, roll_no, batch, is_struck_off, parent_meeting_done) VALUES (?, ?, ?, false, false)";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, student.getUserId());
            ps.setString(2, student.getRollNo());
            ps.setString(3, student.getBatch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // student.id
            }
        }
        return -1;
    }






    public List<StudentModel> getAllStudents() throws Exception {
        List<StudentModel> students = new ArrayList<>();

        String sql = "SELECT s.id AS student_id, u.name, u.username, u.email, u.cnic, s.roll_no, s.batch, c.name AS class_name, c.id AS class_id " +
                "FROM student s " +
                "JOIN users u ON s.Users_id = u.id " +
                "JOIN class_students cs ON cs.student_id = s.id " +
                "JOIN class c ON cs.class_id = c.id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StudentModel sm = new StudentModel();
                sm.setId(rs.getInt("student_id"));
                sm.setName(rs.getString("name"));
                sm.setUsername(rs.getString("username"));
                sm.setEmail(rs.getString("email"));
                sm.setCnic(rs.getString("cnic"));
                sm.setRollNo(rs.getString("roll_no"));
                sm.setBatch(rs.getString("batch"));
                sm.setClassName(rs.getString("class_name"));
                sm.setClassId(rs.getInt("class_id"));
                students.add(sm);
            }
        }

        return students;
    }

    public StudentModel getStudentById(int id) throws Exception {
        String sql = "SELECT s.id AS student_id, u.name, u.username, u.email, u.cnic, s.roll_no, s.batch, s.total_fine, c.name AS class_name, c.id AS class_id " +
                "FROM student s " +
                "JOIN users u ON s.Users_id = u.id " +
                "JOIN class_students cs ON cs.student_id = s.id " +
                "JOIN class c ON cs.class_id = c.id " +
                "WHERE s.id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                StudentModel sm = new StudentModel();
                sm.setId(rs.getInt("student_id"));
                sm.setName(rs.getString("name"));
                sm.setUsername(rs.getString("username"));
                sm.setEmail(rs.getString("email"));
                sm.setCnic(rs.getString("cnic"));
                sm.setRollNo(rs.getString("roll_no"));
                sm.setBatch(rs.getString("batch"));
                sm.setClassName(rs.getString("class_name"));
                sm.setTotalFine(rs.getInt("total_fine"));
                sm.setClassId(rs.getInt("class_id"));
                return sm;
            }
        }
        return null;
    }

    public void updateStudent(StudentModel sm) throws Exception {
        try (Connection con = DBConnection.getConnection()) {
            String userSql = "UPDATE users SET name=?, username=?, email=?, cnic=? WHERE id=(SELECT Users_id FROM student WHERE id=?)";
            PreparedStatement userPs = con.prepareStatement(userSql);
            userPs.setString(1, sm.getName());
            userPs.setString(2, sm.getUsername());
            userPs.setString(3, sm.getEmail());
            userPs.setString(4, sm.getCnic());
            userPs.setInt(5, sm.getId());
            userPs.executeUpdate();

            String stuSql = "UPDATE student SET roll_no=?, batch=? WHERE id=?";
            PreparedStatement stuPs = con.prepareStatement(stuSql);
            stuPs.setString(1, sm.getRollNo());
            stuPs.setString(2, sm.getBatch());
            stuPs.setInt(3, sm.getId());
            stuPs.executeUpdate();

            String classSql = "UPDATE class_students SET class_id=? WHERE student_id=?";
            PreparedStatement classPs = con.prepareStatement(classSql);
            classPs.setInt(1, sm.getClassId());
            classPs.setInt(2, sm.getId());
            classPs.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws Exception {
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement cs = con.prepareStatement("DELETE FROM class_students WHERE student_id=?");
            cs.setInt(1, id);
            cs.executeUpdate();

            PreparedStatement getUserIdStmt = con.prepareStatement("SELECT Users_id FROM student WHERE id=?");
            getUserIdStmt.setInt(1, id);
            ResultSet rs = getUserIdStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("Users_id");

                PreparedStatement deleteUserStmt = con.prepareStatement("DELETE FROM users WHERE id=?");
                deleteUserStmt.setInt(1, userId);
                deleteUserStmt.executeUpdate();
            }
        }
    }

    public void deleteAllStudents() throws Exception {
        try (Connection con = DBConnection.getConnection()) {

            con.prepareStatement("DELETE FROM class_students").executeUpdate();

            PreparedStatement getUsersStmt = con.prepareStatement("SELECT Users_id FROM student");
            ResultSet rs = getUsersStmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("Users_id");
                PreparedStatement deleteUserStmt = con.prepareStatement("DELETE FROM users WHERE id=?");
                deleteUserStmt.setInt(1, userId);
                deleteUserStmt.executeUpdate();
            }
        }
    }


    public List<StudentModel> getStudentsByClassId(int classId) throws Exception {
        List<StudentModel> students = new ArrayList<>();

        String sql = "SELECT s.id AS student_id, u.name, u.username, u.email, u.cnic, s.roll_no, s.batch " +
                "FROM student s " +
                "JOIN users u ON s.Users_id = u.id " +
                "JOIN class_students cs ON cs.student_id = s.id " +
                "WHERE cs.class_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentModel sm = new StudentModel();
                sm.setId(rs.getInt("student_id"));
                sm.setName(rs.getString("name"));
                sm.setUsername(rs.getString("username"));
                sm.setEmail(rs.getString("email"));
                sm.setCnic(rs.getString("cnic"));
                sm.setRollNo(rs.getString("roll_no"));
                sm.setBatch(rs.getString("batch"));
                students.add(sm);
            }
        }

        return students;
    }


    public int getStudentIdByUsersId(int usersId) {
        int studentId = -1;

        String sql = "SELECT id FROM student WHERE users_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usersId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                studentId = rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentId;
    }

    public StudentModel getStudentByRollNo(String rollNo) {
        StudentModel student = null;
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM student WHERE roll_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, rollNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                student = new StudentModel();
                student.setId(rs.getInt("id"));
                student.setRollNo(rs.getString("roll_no"));
                student.setBatch(rs.getString("batch"));
                // Add other attributes if needed
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }


    public void updateTotalFine(int studentId) throws Exception {
        String attendanceQuery = "SELECT COUNT(*) AS total_absents " +
                "FROM Attendance_Register " +
                "WHERE student_id = ? AND status = 'Absent'";

        String policyQuery = "SELECT fine_per_absent_subject, struck_off_after_absents " +
                "FROM Policies ORDER BY id DESC LIMIT 1";

        String updateFineQuery = "UPDATE student SET total_fine = ? WHERE id = ?";

        String strikeOffQuery = "UPDATE student SET is_struck_off = true, struck_off_date = CURDATE() WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement attendanceStmt = con.prepareStatement(attendanceQuery);
             PreparedStatement policyStmt = con.prepareStatement(policyQuery)) {

            // Get absents count
            attendanceStmt.setInt(1, studentId);
            ResultSet rsAbsents = attendanceStmt.executeQuery();

            int totalAbsents = 0;
            if (rsAbsents.next()) {
                totalAbsents = rsAbsents.getInt("total_absents");
            }

            // Get policy
            ResultSet rsPolicy = policyStmt.executeQuery();
            int finePerAbsent = 0;
            int struckOffThreshold = 0;
            if (rsPolicy.next()) {
                finePerAbsent = rsPolicy.getInt("fine_per_absent_subject");
                struckOffThreshold = rsPolicy.getInt("struck_off_after_absents");
            }

            // Calculate fine
            int totalFine = totalAbsents * finePerAbsent;

            // Update fine
            try (PreparedStatement updateFineStmt = con.prepareStatement(updateFineQuery)) {
                updateFineStmt.setInt(1, totalFine);
                updateFineStmt.setInt(2, studentId);
                updateFineStmt.executeUpdate();
            }

            // Strike off if absents exceed threshold
            if (totalAbsents >= struckOffThreshold) {
                try (PreparedStatement strikeStmt = con.prepareStatement(strikeOffQuery)) {
                    strikeStmt.setInt(1, studentId);
                    strikeStmt.executeUpdate();
                }
            }
        }
    }

    /**
     * Returns students who are either manually marked struck off (is_struck_off = true)
     * OR their total absents >= threshold provided.
     *
     * Each StudentModel will include fields: id, rollNo, name, className, isStruckOff, struckOffDate, totalFine, parentMeetingDone, meetingDoneDate
     */
    public List<StudentModel> getStruckOffCandidates(int absentThreshold) {
        List<StudentModel> list = new ArrayList<>();

        // This query fetches student info, class (if any), total absents
        String sql = "SELECT s.id as student_id, s.roll_no, u.name as student_name, c.name as class_name, " +
                "s.is_struck_off, s.struck_off_date, s.parent_meeting_done, s.total_fine, s.meeting_done_date, IFNULL(a.absent_count,0) as total_absents " +
                "FROM student s " +
                "LEFT JOIN users u ON s.Users_id = u.id " +
                "LEFT JOIN class_students cs ON cs.student_id = s.id " +
                "LEFT JOIN class c ON cs.class_id = c.id " +
                "LEFT JOIN ( SELECT student_id, COUNT(*) as absent_count FROM Attendance_Register WHERE status = 'Absent' GROUP BY student_id) a " +
                "ON a.student_id = s.id " +
                "WHERE s.is_struck_off = TRUE OR IFNULL(a.absent_count,0) >= ? " +
                "ORDER BY c.name, u.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, absentThreshold);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StudentModel sm = new StudentModel();
                    sm.setId(rs.getInt("student_id"));
                    sm.setRollNo(rs.getString("roll_no"));
                    sm.setName(rs.getString("student_name"));
                    sm.setClassName(rs.getString("class_name"));
                    sm.setStruckOff(rs.getBoolean("is_struck_off"));
                    sm.setStruckOffDate(rs.getDate("struck_off_date"));
                    sm.setParentMeetingDone(rs.getBoolean("parent_meeting_done"));
                    sm.setTotalFine(rs.getInt("total_fine"));
                    sm.setMeetingDoneDate(rs.getDate("meeting_done_date"));

                    list.add(sm);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Called when VP/Admin clicks Parent Meeting Done.
     * Resets struck off flag, marks parent_meeting_done true, sets meeting_done_date to today,
     * clears struck_off_date and resets total_fine to 0.
     */
    public boolean markParentMeetingDone(int studentId) {
        String sql = "UPDATE student SET is_struck_off = FALSE, parent_meeting_done = TRUE, meeting_done_date = CURDATE(), struck_off_date = NULL, total_fine = 0 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /*public List<StudentModel> getStruckOffStudents() throws SQLException {
        List<StudentModel> list = new ArrayList<>();
        String sql = "SELECT s.id, s.name, s.roll_number, c.name AS class_name " +
                "FROM student s " +
                "JOIN class c ON s.class_id = c.id " +
                "WHERE s.is_struck_off = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StudentModel student = new StudentModel();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setRollNo(rs.getString("roll_number"));
                student.setClassName(rs.getString("class_name")); // make sure StudentModel has this field
                list.add(student);
            }
        }
        return list;
    }
*/
    //for application purpose

    public static Student findByUsersId(int userId) {
        Student student = null;

        String sql = "SELECT s.id AS student_id, " +
                "       s.roll_no, " +
                "       s.batch, " +
                "       u.name AS student_name " +
                "FROM student s " +
                "JOIN users u ON s.Users_id = u.id " +
                "WHERE u.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = new Student();
                    student.setId(rs.getInt("student_id"));
                    student.setRollNo(rs.getString("roll_no"));
                    student.setBatch(rs.getString("batch"));
                    student.setName(rs.getString("student_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public static Student findById(int id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getInt("id"));
                    s.setUsersId(rs.getInt("Users_id"));
                    s.setRollNo(rs.getString("roll_no"));
                    s.setBatch(rs.getString("batch"));
                    return s;
                }
            }
        }
        return null;
    }


    public Student getStudentDetailsByUserId(int userId) {
        Student student = null;
        String sql = "SELECT s.id AS student_id, s.roll_no, s.batch, u.name AS student_name " +
                "FROM student s " +
                "JOIN users u ON s.Users_id = u.id " +
                "WHERE u.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = new Student();
                    student.setId(rs.getInt("student_id"));
                    student.setRollNo(rs.getString("roll_no"));
                    student.setBatch(rs.getString("batch"));
                    student.setName(rs.getString("student_name")); // from users table
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }


    public static class StudentInfo {
        public int studentId;
        public int classId;
        public String className;
        public String rollNo;
        public String studentName;
        public Integer totalFineNullable; // can be null

        public StudentInfo(int studentId, int classId, String className, String rollNo, String studentName, Integer totalFineNullable) {
            this.studentId = studentId;
            this.classId = classId;
            this.className = className;
            this.rollNo = rollNo;
            this.studentName = studentName;
            this.totalFineNullable = totalFineNullable;
        }
    }

    // Find student by roll_no with class and user name
    public static StudentInfo findByRoll(String rollNo) {
        String sql =
                "SELECT s.id AS student_id, s.roll_no, cs.class_id, c.name AS class_name, u.name AS student_name, s.total_fine " +
                        "FROM student s " +
                        "JOIN users u ON u.id = s.Users_id " +
                        "LEFT JOIN class_students cs ON cs.student_id = s.id " +
                        "LEFT JOIN class c ON c.id = cs.class_id " +
                        "WHERE s.roll_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StudentInfo(
                            rs.getInt("student_id"),
                            rs.getInt("class_id"),
                            rs.getString("class_name"),
                            rs.getString("roll_no"),
                            rs.getString("student_name"),
                            (Integer) rs.getObject("total_fine")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Get all students in a class with names & roll nos
    public static java.util.List<StudentInfo> getStudentsByClass(int classId) {
        java.util.List<StudentInfo> list = new java.util.ArrayList<>();
        String sql =
                "SELECT s.id AS student_id, s.roll_no, c.id AS class_id, c.name AS class_name, u.name AS student_name, s.total_fine " +
                        "FROM class_students cs " +
                        "JOIN student s ON s.id = cs.student_id " +
                        "JOIN users u ON u.id = s.Users_id " +
                        "JOIN class c ON c.id = cs.class_id " +
                        "WHERE c.id = ? " +
                        "ORDER BY s.roll_no";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new StudentInfo(
                            rs.getInt("student_id"),
                            rs.getInt("class_id"),
                            rs.getString("class_name"),
                            rs.getString("roll_no"),
                            rs.getString("student_name"),
                            (Integer) rs.getObject("total_fine")
                    ));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

}



