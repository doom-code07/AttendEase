<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.TeacherModel" %>
<%
    TeacherModel teacher = (TeacherModel) request.getAttribute("teacher");
%>
<html>
<head>
    <title>Teacher Details</title>
    <link rel="stylesheet" href="css/details.css">
</head>
<body>
    <div class="container">
        <h2>Teacher Details</h2>
        <p><strong>Name:</strong> <%= teacher.getName() %></p>
        <p><strong>Username:</strong> <%= teacher.getUsername() %></p>
        <p><strong>Email:</strong> <%= teacher.getEmail() %></p>
        <p><strong>CNIC:</strong> <%= teacher.getCnic() %></p>
        <p><strong>Qualification:</strong> <%= teacher.getQualification() %></p>
        <a href="ManageTeacherServlet" class="btn">Back</a>
    </div>
</body>
</html>
