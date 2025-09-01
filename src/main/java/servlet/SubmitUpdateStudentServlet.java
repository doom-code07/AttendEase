package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.StudentDAO;
import model.StudentModel;

public class SubmitUpdateStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int classId = Integer.parseInt(req.getParameter("classId"));
            StudentModel s = new StudentModel();
            s.setId(Integer.parseInt(req.getParameter("id")));
            s.setName(req.getParameter("name"));
            s.setUsername(req.getParameter("username"));
            s.setEmail(req.getParameter("email"));
            s.setCnic(req.getParameter("cnic"));
            s.setRollNo(req.getParameter("rollno"));
            s.setBatch(req.getParameter("batch"));
            s.setClassId(classId);

            new StudentDAO().updateStudent(s);
            resp.sendRedirect("ManageStudentServlet");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error updating student.");
        }
    }
}
