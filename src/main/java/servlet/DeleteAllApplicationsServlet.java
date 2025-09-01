package servlet;

import dao.LeaveApplicationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteAllApplicationsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean deleted = LeaveApplicationDAO.deleteAllApplications();

        if (deleted) {
            request.setAttribute("message", "All applications have been deleted successfully!");
        } else {
            request.setAttribute("message", "No applications found to delete.");
        }

        request.getRequestDispatcher("applications.jsp").forward(request, response);
    }
}
