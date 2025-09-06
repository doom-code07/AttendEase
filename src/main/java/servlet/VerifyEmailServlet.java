package servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import dao.UserDAO;
import java.io.IOException;


public class VerifyEmailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String message = "";
        if (token == null || token.isEmpty()) {
            message = "Invalid verification link.";
            request.setAttribute("message", message);
            request.getRequestDispatcher("verification_result.jsp").forward(request, response);
            return;
        }
        try {
            UserDAO userDAO = new UserDAO();
            int result = userDAO.verifyToken(token);
            if (result > 0) {
                message = "Email verified successfully. You can now login.";
            } else if (result == -2) {
                message = "Verification link has expired. Please request a new verification email.";
            } else {
                message = "Invalid verification token.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Server error during verification.";
        }
        request.setAttribute("message", message);
        request.getRequestDispatcher("verification_result.jsp").forward(request, response);
    }
}
