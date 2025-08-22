package servlet;

import dao.LeaveApplicationDAO;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;


public class ActiveApprovedLeavesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Optional filter by class (pass ?classId=ID when needed)
        Integer classId = null;
        String classParam = req.getParameter("classId");
        if (classParam != null && !classParam.trim().isEmpty()) {
            try {
                classId = Integer.parseInt(classParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        try {
            req.setAttribute(
                    "activeLeaves",
                    LeaveApplicationDAO.getActiveApprovedLeaves(LocalDate.now(), classId)
            );
        } catch (SQLException e) {
            throw new ServletException("Error fetching active approved leaves", e);
        }

        // Forward to your attendance page (mark_attendance.jsp)
        req.getRequestDispatcher("mark_attendance.jsp").forward(req, resp);
    }
}
