package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.UserDAO;

public class ResetPasswordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            resp.sendRedirect("forgot_password.jsp");
            return;
        }

        String newPassword = req.getParameter("newPassword");

        if (newPassword.length() < 8 || !newPassword.matches(".*\\d.*")) {
            req.setAttribute("error", "Password must be at least 8 characters and contain at least one digit.");
            req.getRequestDispatcher("reset_password.jsp").forward(req, resp);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            String hashedPassword = dao.hashPassword(newPassword);
            dao.updatePasswordByEmail(email, hashedPassword);
            session.removeAttribute("resetEmail");
            resp.sendRedirect("success.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("failure.jsp");
        }
    }
}
