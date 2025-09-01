package servlet;

import dao.PolicyDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class DeletePolicyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            new PolicyDAO().deletePolicy(id);
            resp.sendRedirect("set_policies.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error deleting policy: " + e.getMessage());
        }
    }
}
