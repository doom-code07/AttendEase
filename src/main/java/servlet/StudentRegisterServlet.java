package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

import model.UserModel;
import model.StudentModel;
import dao.UserDAO;
import dao.StudentDAO;
import dao.ClassStudentDAO;
import utils.TokenUtil;
import utils.MailUtil;

public class StudentRegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String cnic = req.getParameter("cnic");
            String rollNo = req.getParameter("rollno");
            String batch = req.getParameter("batch");
            int classId = Integer.parseInt(req.getParameter("class_id"));

            // password validation
            if (password.length() < 8 || !password.matches(".*\\d.*")) {
                resp.getWriter().println("Password must be at least 8 characters long and contain at least one digit.");
                return;
            }

            UserDAO userDAO = new UserDAO();

            // duplicate check
            if (userDAO.isDuplicate(username, email, rollNo, cnic)) {
                resp.getWriter().println("Username, Email, CNIC or Roll No already exists.");
                return;
            }

            // hash password
            String hashedPassword = UserDAO.hashPassword(password);

            // build User
            UserModel user = new UserModel();
            user.setName(name);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setCnic(cnic);
            user.setRole("student");

            // insert user
            int userId = userDAO.insertUser(user);

            // build Student
            StudentModel student = new StudentModel();
            student.setUserId(userId);
            student.setRollNo(rollNo);
            student.setBatch(batch);

            // insert student + assign class
            int studentId = new StudentDAO().insertStudentReturnId(student);
            new ClassStudentDAO().assignClassToStudent(studentId, classId);

            // -------- EMAIL VERIFICATION INTEGRATION START --------
            try {
                String token = TokenUtil.generateToken();
                LocalDateTime expiry = TokenUtil.expiryAfterHours(24); // valid for 24h

                // save token in DB
                userDAO.saveVerificationToken(userId, token, expiry);

                // build verification link
                String siteURL = req.getRequestURL().toString().replace(req.getServletPath(), "");
                String verifyLink = "http://192.168.188.49:8080/practice/VerifyEmailServlet?token=" + token;


                String subject = "Please verify your email";
                String content = "<p>Dear " + name + ",</p>"
                        + "<p>Thank you for registering. Please click the link below to verify your email:</p>"
                        + "<p><a href='" + verifyLink + "'>Verify Email</a></p>"
                        + "<p>This link will expire in 24 hours.</p>";

                MailUtil.sendEmail(email, subject, content);

            } catch (Exception mailEx) {
                mailEx.printStackTrace();
                // don’t stop registration — user is saved but unverified
            }
            // -------- EMAIL VERIFICATION INTEGRATION END --------

            // redirect user to pending page instead of success.jsp
            resp.sendRedirect("verification_pending.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error during registration.");
        }
    }
}
