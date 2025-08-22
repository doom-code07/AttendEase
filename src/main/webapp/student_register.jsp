<%@ page import="dao.ClassDAO" %>
<%@ page import="model.ClassModel" %>
<%@ page import="java.util.List" %>
<html>
<head><title>Student Registration</title>
<link rel="stylesheet" href="register_style.css"></head>
<body>
    <div class="container">
        <!-- Left side (blue panel with image + welcome text) -->
        <div class="left-panel">
            <img src="registration_logo.png" alt="Register" class="side-img">
            <h3>Welcome</h3>
            <p>Register Here</p>
        </div>

        <!-- Right side (your existing form) -->
        <div class="right-panel">
            <h2>Register as Student</h2>
            <form method="post" action="StudentRegisterServlet">
                Name: <input type="text" name="name" required><br><br>
                Email: <input type="email" name="email" required><br><br>
                CNIC: <input type="text" name="cnic" pattern="\d{13}" title="Enter 13-digit CNIC" required><br><br>
                Username: <input type="text" name="username" required><br><br>
                Password: <input type="password" name="password" required
                       pattern="^(?=.*\d).{8,}$"
                       title="Password must be at least 8 characters long and include at least one digit.">
                <br><br>
                Roll No: <input type="text" name="rollno" required><br><br>
                Batch: <input type="text" name="batch" required><br><br>
                Class:
                <select name="class_id" required>
                    <option value="">-- Select Class --</option>
                    <%
                        List<ClassModel> classes = new ClassDAO().getAllClasses();
                        for (ClassModel cls : classes) {
                    %>
                    <option value="<%=cls.getId()%>"><%=cls.getName()%></option>
                    <% } %>
                </select><br><br>
                <input type="submit" value="Register">
            </form>
        </div>
    </div>
</body>

</html>
