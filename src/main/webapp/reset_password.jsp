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
    <h2>Reset Password</h2>
        <label for="password">Enter your new password:</label><br>
        <input type="password" id="password" name="newPassword" pattern="\d{3,6}" title="Enter 3 to 6 digit roll number" required><br><br>
        <input type="submit" value="Reset Password">
    </form>
</body>
</html>
