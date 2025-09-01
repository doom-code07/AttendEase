package servlet;

import dao.AttendanceDAO;
import dao.ClassDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.AttendanceViewModel;
import model.ClassModel;

import java.io.IOException;
import java.util.List;

public class ViewAttendanceByClassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String classIdStr = request.getParameter("class_id");
        List<AttendanceViewModel> attendanceList = null;

        try {
            int classId = Integer.parseInt(classIdStr);
            AttendanceDAO dao = new AttendanceDAO();
            attendanceList = dao.getAttendanceByClassId(classId);
            request.setAttribute("attendanceList", attendanceList);
            request.setAttribute("classId", classId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ClassDAO classDAO = new ClassDAO();
            List<ClassModel> classList = classDAO.getAllClasses();
            request.setAttribute("classList", classList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("view_attendance_by_class.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
