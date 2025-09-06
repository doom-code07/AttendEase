package servlet;

import dao.StudentDAO;
import dao.TeacherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.UserModel;
import dao.UserDAO;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        try {
            UserDAO dao = new UserDAO();
            UserModel user = dao.validateUser(username, password);

            if (user != null) {
                UserDAO userDAO = new UserDAO();  // ✅ create DAO instance to check verification
                int userId = user.getId();
                String userEmail = user.getEmail();

                // ✅ check email verification before proceeding
                if (!userDAO.isEmailVerifiedByUserId(userId)) {
                    request.setAttribute("emailPrefill", userEmail);
                    request.setAttribute("message", "Email not verified. Check your inbox or resend verification.");
                    request.getRequestDispatcher("verification_pending.jsp").forward(request, response);
                    return; // stop login
                }

                HttpSession session = request.getSession(); // only create session if verified
                session.setAttribute("user", user);
                session.setAttribute("userId", userId);
                switch (user.getRole()) {
                    case "admin":
                        response.sendRedirect("admin_dashboard.jsp");
                        break;

                    case "teacher":
                        // Get teacherId using userId
                        TeacherDAO teacherDAO = new TeacherDAO();
                        int teacherId = teacherDAO.getTeacherIdByUserId(user.getId());

                        session.setAttribute("teacherId", teacherId); // ✅ fixed session
                        response.sendRedirect("teacher_dashboard.jsp");
                        break;

                    case "student":

                        int usersId = user.getId(); // from users table
                        StudentDAO StudentDAO = new StudentDAO();
                        int studentId = StudentDAO.getStudentIdByUsersId(usersId); // get student.id from student table
                        session.setAttribute("studentId", studentId);
                        response.sendRedirect("StudentDashboardServlet");
                        break;

                    case "vice_principal":
                        response.sendRedirect("viceprincipal_dashboard.jsp");
                        break;

                    default:
                        response.sendRedirect("login.jsp?error=invalid");
                        break;
                }
            } else {
                response.sendRedirect("failure.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login failed due to server error.");
        }
    }
}
