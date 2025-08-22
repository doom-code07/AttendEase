<%@ page contentType="text/html" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Attendance Management System</title>

    <link rel="stylesheet" href="login_style.css">
</head>
<body>
<div class="container">
        <div class="left-section">
            <img src="college_logo.jpeg" alt="College Logo">
            <h2>F.G DEGREE COLLEGE FOR MEN KHARIAN CANTT</h2>

        </div>
        <div class="right-section">
              <div class="logo-container">
                   <img src="login_logo.png" alt="College Logo">
                   <h1><i>Welcome To AttendEase</i></h1>
              </div>
                    <form class="login-form" action="LoginServlet" method="post">
                          <!-- Username Field  -->
                               <div class="input-container">
                                      <h4>Username</h4>
                                      <input name= "username" type="text" placeholder="Enter your username" required>
                               </div>
                          <!-- Password Field -->
                               <div class="input-container">
                                       <h4>Password</h4>
                                       <input name= "password" type="password" placeholder="Enter your password" required>
                               </div>
                                     <button type="submit">LOGIN</button><br>
                     </form>

<% if(request.getParameter("error") != null) { %> <p style="color:red;">Invalid username or password.</p> <% } %>
<a href = "student_register.jsp">Register here</a>
            <p>OR</p>
            <a href="forgot_password.jsp" class="forgot-password">FORGOT PASSWORD? CLICK HERE</a>

        </div>
    </div>

</body>
</html>
