package servlet;

import dao.TimetableDAO;
import model.TimetableModel;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class TeacherTimetableServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            int teacherId = (int) session.getAttribute("teacherId");

            TimetableDAO dao = new TimetableDAO();
            List<TimetableModel> list = dao.getTeacherTimetable(teacherId);

            request.setAttribute("timetable", list);
            request.getRequestDispatcher("teacher_timetable.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
