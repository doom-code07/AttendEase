package servlet;

import dao.SubjectDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class AssignSubjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int teacherId = Integer.parseInt(req.getParameter("teacherId"));
            String[] subjectIdStrings = req.getParameterValues("subjectIds");

            if (subjectIdStrings != null) {
                int[] subjectIds = new int[subjectIdStrings.length];
                for (int i = 0; i < subjectIdStrings.length; i++) {
                    subjectIds[i] = Integer.parseInt(subjectIdStrings[i]);
                }

                new SubjectDAO().assignSubjectsToTeacher(teacherId, subjectIds);
            }

            resp.sendRedirect("assign_subject.jsp?success=1");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error assigning subjects.");
        }
    }
}
