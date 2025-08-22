<%@ page import="model.StudentModel" %>

<%
    StudentModel s = (StudentModel) request.getAttribute("student");
%>

<html>
<head><title>Student Details</title></head>
<body>
    <h2>Student Details</h2>
    <p><strong>Name:</strong> <%= s.getName() %></p>
    <p><strong>Username:</strong> <%= s.getUsername() %></p>
    <p><strong>Email:</strong> <%= s.getEmail() %></p>
    <p><strong>CNIC:</strong> <%= s.getCnic() %></p>
    <p><strong>Roll No:</strong> <%= s.getRollNo() %></p>
    <p><strong>Batch:</strong> <%= s.getBatch() %></p>
    <p><strong>Class:</strong> <%= s.getClassName() %></p>
</body>
</html>
