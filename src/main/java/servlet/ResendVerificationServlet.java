package servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

import dao.UserDAO;
import utils.TokenUtil;
import utils.MailUtil;


public class ResendVerificationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // show form
        req.getRequestDispatcher("resend_verification.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String message;
        if (email == null || email.trim().isEmpty()) {
            message = "Please enter your email.";
            req.setAttribute("message", message);
            req.getRequestDispatcher("resend_result.jsp").forward(req, resp);
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            Integer userId = userDAO.getUserIdByEmail(email);
            if (userId == null) {
                message = "No account found with that email.";
                req.setAttribute("message", message);
                req.getRequestDispatcher("resend_result.jsp").forward(req, resp);
                return;
            }

            if (userDAO.isEmailVerifiedByUserId(userId)) {
                message = "Email already verified. You can log in.";
                req.setAttribute("message", message);
                req.getRequestDispatcher("resend_result.jsp").forward(req, resp);
                return;
            }

            // generate new token & expiry, save and send mail
            String token = TokenUtil.generateToken();
            LocalDateTime expiry = TokenUtil.expiryAfterHours(24);
            userDAO.saveVerificationToken(userId, token, expiry);

            String siteURL = req.getRequestURL().toString().replace(req.getServletPath(), "");
            String verifyLink = siteURL + "/VerifyEmailServlet?token=" + token;

            String subject = "New verification link - please verify your email";
            String content = "<p>Click to verify your email:</p>"
                    + "<p><a href=\"" + verifyLink + "\">Verify Email</a></p>"
                    + "<p>This link expires in 24 hours.</p>";

            // send (MailUtil should already be working)
            try {
                MailUtil.sendEmail(email, subject, content);
                message = "Verification email sent. Check your inbox / spam.";
            } catch (Exception mailEx) {
                mailEx.printStackTrace();
                message = "Failed to send email. Please try again later.";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            message = "Server error. Try again later.";
        }

        req.setAttribute("message", message);
        req.getRequestDispatcher("resend_result.jsp").forward(req, resp);
    }
}
