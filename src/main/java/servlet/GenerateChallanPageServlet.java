package servlet;

import dao.AttendanceDAO;
import dao.ClassDAO;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import java.io.IOException;


public class GenerateChallanPageServlet extends HttpServlet {
    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ClassDAO dao = new ClassDAO();
            req.setAttribute("classes", dao.getAllClasses());
            req.getRequestDispatcher("generate_challan.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
