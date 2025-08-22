package servlet;

import dao.SubjectDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class UnassignSubjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int subjectId = Integer.parseInt(req.getParameter("subjectId"));
            new SubjectDAO().unassignSubject(subjectId);
            resp.sendRedirect("assign_subject.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error unassigning subject.");
        }
    }
}
