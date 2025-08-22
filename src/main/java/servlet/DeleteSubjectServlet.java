package servlet;

import dao.SubjectDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;


import java.io.IOException;


public class DeleteSubjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectIdStr = request.getParameter("subjectId");

        if (subjectIdStr != null) {
            try {
                int subjectId = Integer.parseInt(subjectIdStr);
                SubjectDAO dao = new SubjectDAO();
                dao.deleteSubject(subjectId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("manage_classes.jsp");
    }
}
