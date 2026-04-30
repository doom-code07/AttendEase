<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    HttpSession sessionReset = request.getSession(false);
    if (sessionReset == null || sessionReset.getAttribute("resetEmail") == null) {
        response.sendRedirect("forgot_password.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Reset Password</title>
    <link rel="stylesheet" href="css/forgot_style.css">

</head>
<body>
    <form action="ResetPasswordServlet" method="post">
    <a href="forgot_password.jsp" class="btn">Back</a>
        <h2>Reset Password</h2>

        <label for="password">Enter new password:</label>
        <input type="password" id="password" name="newPassword" required
               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$"
                    title="Password must be at least 8 characters long and include uppercase, lowercase, number, and special character (@$!%*?&)"
               >
        <input type="submit" value="Reset Password">
    </form>

</body>
</html>

