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

                String message = new SubjectDAO().assignSubjectsToTeacher(teacherId, subjectIds);

                if (message.isEmpty()) {
                    resp.sendRedirect("assign_subject.jsp?success=Subjects assigned successfully");
                } else {
                    resp.sendRedirect("assign_subject.jsp?info=" + java.net.URLEncoder.encode(message, "UTF-8"));
                }

                return;
            }

            resp.sendRedirect("assign_subject.jsp?error=No subjects selected");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error assigning subjects.");
        }
    }
}
