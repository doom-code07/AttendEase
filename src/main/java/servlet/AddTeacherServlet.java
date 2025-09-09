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
            String passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;'<>.,?/~`]).{8,}$";

            if (!password.matches(passwordPattern)) {
                response.sendRedirect("duplicate_teacher.jsp");
                return;
            }
            String email = request.getParameter("email");
            String cnic = request.getParameter("cnic");
            if (!cnic.matches("\\d{13}")) {
                response.sendRedirect("duplicate_teacher.jsp");
                return;
            }
            String qualification = request.getParameter("qualification");


            String hashedPassword = PasswordUtil.hashPassword(password);

            UserModel user = new UserModel();
            user.setName(name);
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setEmail(email);
            user.setCnic(cnic);
            user.setRole("teacher");

            TeacherModel teacher = new TeacherModel();
            teacher.setQualification(qualification);

            new TeacherDAO().addTeacher(user, teacher);

            response.sendRedirect("teacher_success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("duplicate_teacher.jsp");
        }
    }
}
