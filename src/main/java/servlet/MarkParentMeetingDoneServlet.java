package servlet;

import dao.StudentDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class MarkParentMeetingDoneServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            StudentDAO dao = new StudentDAO();
            dao.markParentMeetingDone(studentId);
            response.sendRedirect("VicePrincipalStruckOffServlet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
