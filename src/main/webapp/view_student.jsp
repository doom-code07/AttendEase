<%@ page import="model.StudentModel" %>

<%
    StudentModel s = (StudentModel) request.getAttribute("student");
%>

<html>
<head>
    <title>Student Details</title>
    <link rel="stylesheet" href="css/details.css">
</head>
<body>
    <div class="container">
        <h2>Student Details</h2>
        <p><strong>Name:</strong> <%= s.getName() %></p>
        <p><strong>Username:</strong> <%= s.getUsername() %></p>
        <p><strong>Email:</strong> <%= s.getEmail() %></p>
        <p><strong>CNIC:</strong> <%= s.getCnic() %></p>
        <p><strong>Roll No:</strong> <%= s.getRollNo() %></p>
        <p><strong>Batch:</strong> <%= s.getBatch() %></p>
        <p><strong>Class:</strong> <%= s.getClassName() %></p>
        <a href="ManageStudentServlet" class="btn">Back</a>
    </div>
</body>
</html>
