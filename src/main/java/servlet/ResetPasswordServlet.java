package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import dao.UserDAO;

public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            resp.sendRedirect("forgot_password.jsp");
            return;
        }

        String newPassword = req.getParameter("newPassword");

        // SERVER-SIDE PASSWORD VALIDATION
        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (newPassword == null || !newPassword.matches(passwordRegex)) {
            req.setAttribute("error",
                    "Password must contain uppercase, lowercase, number and special character.");
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
