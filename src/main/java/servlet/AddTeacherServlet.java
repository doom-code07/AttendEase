package servlet;

import dao.TeacherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import model.TeacherModel;

import model.UserModel;
import utils.PasswordUtil;

import java.io.IOException;

public class AddTeacherServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String name = request.getParameter("name");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (password.length() < 8 || !password.matches(".*\\d.*")) {
                response.getWriter().println("Error: Password must be at least 8 characters long and contain at least one digit.");
                return;
            }
            String email = request.getParameter("email");
            String cnic = request.getParameter("cnic");
            if (!cnic.matches("\\d{13}")) {
                response.getWriter().println("Error: CNIC must be exactly 13 digits without dashes.");
                return;
            }
            String qualification = request.getParameter("qualification");

            if (password.length() < 8 || !password.matches(".*\\d.*")) {
                response.getWriter().println("Error: Password must be at least 8 characters long and contain at least one digit.");
                return;
            }

            String hashedPassword = PasswordUtil.hashPassword(password);

            UserModel user = new UserModel();
            user.setName(name);
            user.setUsername(username);
            user.setPassword(hashedPassword);  // use hashed password
            user.setEmail(email);
            user.setCnic(cnic);
            user.setRole("teacher");

            TeacherModel teacher = new TeacherModel();
            teacher.setQualification(qualification);

            new TeacherDAO().addTeacher(user, teacher);

            response.sendRedirect("manage_teachers.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error adding teacher: " + e.getMessage());
        }
    }
}
