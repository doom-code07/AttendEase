package servlet;

import dao.ClassDAO;
import dao.SubjectDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class DeleteClassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int classId = Integer.parseInt(request.getParameter("classId"));

            SubjectDAO subjectDAO = new SubjectDAO();
            subjectDAO.deleteSubjectsByClassId(classId);

            ClassDAO classDAO = new ClassDAO();
            classDAO.deleteClass(classId);

            response.sendRedirect("manage_classes.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
