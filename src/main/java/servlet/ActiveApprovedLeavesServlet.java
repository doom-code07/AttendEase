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

        req.getRequestDispatcher("mark_attendance.jsp").forward(req, resp);
    }
}
