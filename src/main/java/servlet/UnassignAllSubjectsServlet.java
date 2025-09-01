package servlet;

import dao.SubjectDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class UnassignAllSubjectsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            new SubjectDAO().unassignAllSubjects();
            resp.sendRedirect("assign_subject.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error unassigning all subjects.");
        }
    }
}
