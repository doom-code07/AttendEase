package servlet;

import dao.StudentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class ParentMeetingDoneServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String historyIdStr = req.getParameter("historyId");
        if (historyIdStr != null && !historyIdStr.isEmpty()) {
            int historyId = Integer.parseInt(historyIdStr);
            StudentDAO dao = new StudentDAO();

            // Update the specific struck-off history row and reset attendance
            dao.resetAfterParentMeeting(historyId, Date.valueOf(LocalDate.now()));
        }

        // Redirect back to manage struck-off page
        resp.sendRedirect("ManageStruckOffServlet");
    }
}
