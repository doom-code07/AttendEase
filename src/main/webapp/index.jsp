<%@ page contentType="text/html" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Attendance Management System</title>
    <link rel="stylesheet" href="css/login_style.css">
</head>
<body>
    <div class="form-box">
        <h1><i>Welcome To AttendEase</i></h1>

        <form class="login-form" action="LoginServlet" method="post">
            <!-- Username Field  -->
            <div class="input-container">
                <h4>Username</h4>
                <input name="username" type="text" placeholder="Enter your username" required>
            </div>

            <!-- Password Field -->
            <div class="input-container">
                <h4>Password</h4>
                <input name="password" type="password" placeholder="Enter your password" required>
            </div>

            <button type="submit">LOGIN</button>
        </form>

        <% if(request.getParameter("error") != null) { %>
            <p style="color:red;">Invalid username or password.</p>
        <% } %>

        <a href="student_register.jsp">Register here</a>
        <p>OR</p>
        <a href="forgot_password.jsp" class="forgot-password">FORGOT PASSWORD? CLICK HERE</a>
    </div>
</body>
</html>
