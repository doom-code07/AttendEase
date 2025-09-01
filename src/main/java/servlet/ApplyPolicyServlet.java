package servlet;

import dao.PolicyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.PolicyModel;

import java.io.IOException;

public class ApplyPolicyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int minPercentage = Integer.parseInt(request.getParameter("min_attendance_percentage"));
            int fine = Integer.parseInt(request.getParameter("fine_per_absent_subject"));
            int struckLimit = Integer.parseInt(request.getParameter("struck_off_after_absents"));

            PolicyModel policy = new PolicyModel();
            policy.setMinAttendancePercentage(minPercentage);
            policy.setFinePerAbsentSubject(fine);
            policy.setStruckOffAfterAbsents(struckLimit);

            PolicyDAO dao = new PolicyDAO();

            if (dao.policyExists()) {
                dao.updatePolicy(policy);
            } else {
                dao.addPolicy(policy);
            }

            request.setAttribute("message", "Policy saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Failed to save policy. Please check input.");
        }

        request.getRequestDispatcher("set_policies.jsp").forward(request, response);
    }
}
