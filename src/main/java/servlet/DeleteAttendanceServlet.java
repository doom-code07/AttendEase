package servlet;

import dao.AttendanceDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class DeleteAttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        AttendanceDAO dao = new AttendanceDAO();

        try {
            if ("deleteSingle".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteAttendanceById(id);
            } else if ("deleteAll".equals(action)) {
                int classId = Integer.parseInt(request.getParameter("class_id"));
                dao.deleteAllAttendanceByClassId(classId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("ViewAttendanceByClassServlet?class_id=" + request.getParameter("class_id"));
    }
}
