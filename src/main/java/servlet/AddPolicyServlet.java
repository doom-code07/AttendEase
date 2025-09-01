package servlet;

import dao.PolicyDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.PolicyModel;

import java.io.IOException;

public class AddPolicyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int minAttendance = Integer.parseInt(req.getParameter("min_attendance_percentage"));
            int fine = Integer.parseInt(req.getParameter("fine_per_absent_subject"));
            int struckOff = Integer.parseInt(req.getParameter("struck_off_after_absents"));

            PolicyModel policy = new PolicyModel();
            policy.setMinAttendancePercentage(minAttendance);
            policy.setFinePerAbsentSubject(fine);
            policy.setStruckOffAfterAbsents(struckOff);

            PolicyDAO dao = new PolicyDAO();
            if (dao.policyExists()) {
                dao.updatePolicy(policy);
            } else {
                dao.addPolicy(policy);
            }

            resp.sendRedirect("set_policies.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error adding/updating policy: " + e.getMessage());
        }
    }
}
