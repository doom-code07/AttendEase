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

            UserDAO userDAO = new UserDAO();

            if (userDAO.isDuplicate(username, email, rollNo, cnic)) {
                resp.sendRedirect("duplicate_entry.jsp");
                return;
            }

            String hashedPassword = UserDAO.hashPassword(password);

            UserModel user = new UserModel();
            user.setName(name);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setCnic(cnic);
            user.setRole("student");

            int userId = userDAO.insertUser(user);

            StudentModel student = new StudentModel();
            student.setUserId(userId);
            student.setRollNo(rollNo);
            student.setBatch(batch);

            int studentId = new StudentDAO().insertStudentReturnId(student);
            new ClassStudentDAO().assignClassToStudent(studentId, classId);

            // -------- EMAIL VERIFICATION INTEGRATION START --------
           /* try {
                String token = TokenUtil.generateToken();
                LocalDateTime expiry = TokenUtil.expiryAfterHours(24); // valid for 24h

                // save token in DB
                userDAO.saveVerificationToken(userId, token, expiry);

                String baseURL;
                if (req.getServerName().equals("localhost") || req.getServerName().startsWith("192.168.")) {
                    baseURL = "https://88846f4daec4.ngrok-free.app/practice";
                } else {
                    baseURL = req.getRequestURL().toString().replace(req.getServletPath(), "");
                }
                String verifyLink = baseURL + "/VerifyEmailServlet?token=" + token;
                System.out.println("Generated Verification Link: " + verifyLink);

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
*/
            // redirect user to pending page instead of success.jsp
            resp.sendRedirect("success.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("failure.jsp");
        }
    }
}
