package servlet;

import dao.AttendanceDAO;
import dao.SubjectDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.AttendanceViewModel;
import model.SubjectModel;

import java.io.IOException;
import java.util.List;

public class ViewAttendanceBySubjectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer teacherId = (Integer) session.getAttribute("teacherId"); // make sure you set this at login

        if (teacherId == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            SubjectDAO subjectDAO = new SubjectDAO();
            List<SubjectModel> subjectList = subjectDAO.getSubjectsByTeacherId(teacherId);
            request.setAttribute("subjectList", subjectList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if a subject was selected, fetch attendance
        String subjectIdStr = request.getParameter("subject_id");
        if (subjectIdStr != null && !subjectIdStr.trim().isEmpty()) {
            try {
                int subjectId = Integer.parseInt(subjectIdStr);
                AttendanceDAO attendanceDAO = new AttendanceDAO();
                List<AttendanceViewModel> attendanceList = attendanceDAO.getAttendanceByTeacherAndSubject(teacherId, subjectId);
                request.setAttribute("attendanceList", attendanceList);
                request.setAttribute("subjectId", subjectId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("view_attendance_by_subject.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
