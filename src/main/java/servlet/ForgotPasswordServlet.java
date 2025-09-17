package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.UserDAO;

public class ForgotPasswordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String email = req.getParameter("email");
            UserDAO dao = new UserDAO();

            if (dao.emailExists(email)) {
                req.getSession().setAttribute("resetEmail", email);
                resp.sendRedirect("reset_password.jsp");
            } else {
                resp.sendRedirect("forgot_failure.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("An error occurred.");
        }
    }
}
