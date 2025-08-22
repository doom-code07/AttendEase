package servlet;

import dao.StudentDAO;
import model.StudentModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageStudentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<StudentModel> students = new ArrayList<>();

        try {
            students = new StudentDAO().getAllStudents();
        } catch (Exception e) {
            e.printStackTrace(); // Still log for debugging
            req.setAttribute("error", "Unable to fetch student records from the database.");
        }

        req.setAttribute("students", students); // Always set, even if empty
        req.getRequestDispatcher("manage_students.jsp").forward(req, resp);
    }
}
