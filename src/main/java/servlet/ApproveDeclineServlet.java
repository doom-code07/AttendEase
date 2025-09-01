package servlet;

import dao.LeaveApplicationDAO;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;


public class ApproveDeclineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
            int decisionBy = (int) session.getAttribute("userId"); // users.id of approver
            int appId = Integer.parseInt(request.getParameter("applicationId"));
            String action = request.getParameter("action"); // "Approved" or "Declined"

            if (!"Approved".equals(action) && !"Declined".equals(action)) {
                response.sendRedirect("TeacherApplicationsServlet"); // or show error
                return;
            }

            LeaveApplicationDAO.updateStatus(appId, action, decisionBy);

            String from = request.getParameter("from");
            if ("vice".equals(from)) response.sendRedirect("ViceApplicationsServlet");
            else response.sendRedirect("TeacherApplicationsServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeacherApplicationsServlet");
        }
    }
}
