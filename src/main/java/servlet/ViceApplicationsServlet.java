package servlet;

import dao.LeaveApplicationDAO;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import model.LeaveApplication;

public class ViceApplicationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        try {

            LeaveApplicationDAO dao = new LeaveApplicationDAO();
            List<LeaveApplication> apps = dao.findForVicePrincipal();

            request.setAttribute("applications", apps);
            request.getRequestDispatcher("check_applications.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Server error: " + e.getMessage());
            request.getRequestDispatcher("check_applications.jsp").forward(request, response);
        }
    }
}
