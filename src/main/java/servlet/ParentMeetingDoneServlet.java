package servlet;

import dao.StudentDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/parentMeetingDone")
public class ParentMeetingDoneServlet extends HttpServlet {

    private StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sid = req.getParameter("studentId");
        if (sid != null) {
            try {
                int studentId = Integer.parseInt(sid);
                boolean ok = studentDAO.markParentMeetingDone(studentId);
                if (!ok) {
                    req.getSession().setAttribute("error", "Unable to update student with id " + studentId);
                } else {
                    req.getSession().setAttribute("message", "Student updated successfully.");
                }
            } catch (NumberFormatException e) {
                req.getSession().setAttribute("error", "Invalid student id.");
            }
        } else {
            req.getSession().setAttribute("error", "Student id missing.");
        }

        resp.sendRedirect(req.getContextPath() + "/manageStruckOff");
    }
}
