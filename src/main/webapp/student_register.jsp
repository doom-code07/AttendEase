<%@ page import="dao.ClassDAO" %>
<%@ page import="model.ClassModel" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Student Registration</title>
    <link rel="stylesheet" href="css/register_style.css">
</head>
<body>
    <div class="form-box">
        <h2>Register as Student</h2>
        <form method="post" action="StudentRegisterServlet">

            <label for="name">Name</label>
            <input type="text" id="name" name="name"  pattern="[A-Za-z ]+"
                                                            title="Only alphabets and spaces are allowed" required>

            <label for="email">Email</label>
            <input type="email" id="email" name="email" required>

            <label for="cnic">CNIC</label>
            <input type="text" id="cnic" name="cnic" pattern="\d{13}" title="Enter 13-digit CNIC" required>

            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required
                   pattern="^(?=.*\d).{8,}$"
                   title="Password must be at least 8 characters long and include at least one digit.">

            <label for="rollno">Roll No</label>
            <input type="text" name="rollno" required
                   pattern="\d{3,6}" title="Enter 3 to 6 digit roll number" />


            <label for="batch">Batch</label>
            <input type="text" id="batch" name="batch" pattern="[0-9]+"
                                                            title="Only digits are allowed" required>

            <label for="class_id">Class</label>
            <select id="class_id" name="class_id" required>
                <option value="">-- Select Class --</option>
                <%
                    List<ClassModel> classes = new ClassDAO().getAllClasses();
                    for (ClassModel cls : classes) {
                %>
                <option value="<%=cls.getId()%>"><%=cls.getName()%></option>
                <% } %>
            </select>

            <input type="submit" value="Register">
        </form>
    </div>
</body>
</html>
