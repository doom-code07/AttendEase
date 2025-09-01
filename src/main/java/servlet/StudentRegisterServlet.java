package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.UserModel;
import model.StudentModel;
import dao.UserDAO;
import dao.StudentDAO;
import dao.ClassStudentDAO;


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

            if (password.length() < 8 || !password.matches(".*\\d.*")) {
                resp.getWriter().println("Password must be at least 8 characters long and contain at least one digit.");
                return;
            }

            UserDAO userDAO = new UserDAO();

            if (userDAO.isDuplicate(username, email, rollNo, cnic)) {
                resp.getWriter().println("Username, Email, CNIC or Roll No already exists.");
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

            resp.sendRedirect("success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error during registration.");
        }
    }
}
