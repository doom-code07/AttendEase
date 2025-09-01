package servlet;

import dao.LeaveApplicationDAO;
import dao.StudentDAO;
import model.LeaveApplication;
import model.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SubmitApplicationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = (int) session.getAttribute("userId");
            Student student = StudentDAO.findByUsersId(userId);

            if (student == null) {
                request.setAttribute("error", "Student record not found.");
                request.getRequestDispatcher("submit_application.jsp").forward(request, response);
                return;
            }

            String startStr = request.getParameter("start_date");
            String endStr = request.getParameter("end_date");
            String desc = request.getParameter("description");
            String teacherIdStr = request.getParameter("teacher_id");

            LocalDate today = LocalDate.now();
            LocalDate start = LocalDate.parse(startStr);
            LocalDate end = LocalDate.parse(endStr);


            if (LeaveApplicationDAO.hasSubmittedToday(student.getId())) {
                request.setAttribute("error", "You can only submit one application per day.");
                request.setAttribute("student", student);
                request.getRequestDispatcher("submit_application.jsp").forward(request, response);
                return;
            }


            if (start.isBefore(today) || end.isBefore(today)) {
                request.setAttribute("error", "Dates cannot be in the past.");
                request.setAttribute("student", student);
                request.getRequestDispatcher("submit_application.jsp").forward(request, response);
                return;
            }
            if (end.isBefore(start)) {
                request.setAttribute("error", "End date cannot be before start date.");
                request.setAttribute("student", student);
                request.getRequestDispatcher("submit_application.jsp").forward(request, response);
                return;
            }
            if (desc == null) desc = "";
            if (desc.length() > 100) {
                request.setAttribute("error", "Description must be at most 100 characters.");
                request.setAttribute("student", student);
                request.getRequestDispatcher("submit_application.jsp").forward(request, response);
                return;
            }

            long days = ChronoUnit.DAYS.between(start, end) + 1;
            boolean isTeacherApp = days <= 3;

            LeaveApplication app = new LeaveApplication();
            app.setApplicantId(student.getId());
            app.setDescription(desc);
            app.setStartDate(Date.valueOf(start));
            app.setEndDate(Date.valueOf(end));
            app.setTeacherApplication(isTeacherApp);
            app.setStatus("Pending");

            if (isTeacherApp) {
                if (teacherIdStr == null || teacherIdStr.trim().isEmpty()) {
                    request.setAttribute("error", "Please select a teacher for applications up to 3 days.");
                    request.setAttribute("student", student);
                    request.getRequestDispatcher("submit_application.jsp").forward(request, response);
                    return;
                }
                app.setTeacherId(Integer.parseInt(teacherIdStr));
            } else {
                app.setTeacherId(null);
            }

            LeaveApplicationDAO.save(app);
            request.setAttribute("success", "Application submitted successfully.");
            request.setAttribute("student", student);
            request.getRequestDispatcher("submit_application.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Server error: " + ex.getMessage());
            request.getRequestDispatcher("submit_application.jsp").forward(request, response);
        }
    }
}
