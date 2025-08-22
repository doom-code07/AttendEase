package servlet;

import dao.TeacherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.TeacherModel;

import java.io.IOException;

public class UpdateTeacherServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            TeacherModel teacher = new TeacherDAO().getTeacherById(id);
            req.setAttribute("teacher", teacher);
            req.getRequestDispatcher("update_teacher.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TeacherModel teacher = new TeacherModel();
            teacher.setId(Integer.parseInt(req.getParameter("id")));
            teacher.setUsersId(Integer.parseInt(req.getParameter("usersId")));
            teacher.setName(req.getParameter("name"));
            teacher.setUsername(req.getParameter("username"));
            teacher.setEmail(req.getParameter("email"));
            teacher.setCnic(req.getParameter("cnic"));
            teacher.setQualification(req.getParameter("qualification"));

            new TeacherDAO().updateTeacher(teacher);
            resp.sendRedirect("ManageTeacherServlet");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error updating teacher.");
        }
    }
}
