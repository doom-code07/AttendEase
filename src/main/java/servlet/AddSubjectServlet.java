package servlet;

import dao.SubjectDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AddSubjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int classId = Integer.parseInt(request.getParameter("classId"));
        String title = request.getParameter("subjectTitle");
        String code = request.getParameter("subjectCode");

        try {
            SubjectDAO dao = new SubjectDAO();
            dao.addSubjectToClass(classId, title, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("manage_classes.jsp");
    }
}
