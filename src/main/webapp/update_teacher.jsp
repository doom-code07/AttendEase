<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.TeacherModel" %>
<%
    TeacherModel teacher = (TeacherModel) request.getAttribute("teacher");
%>
<html>
<head>
    <title>Update Teacher</title>
    <link rel="stylesheet" href="css/details.css">
</head>
<body>
    <div class="container">
    <a href="ManageTeacherServlet" class="btn">Back</a>
        <h2>Update Teacher</h2>
        <form method="post" action="UpdateTeacherServlet">
            <input type="hidden" name="id" value="<%= teacher.getId() %>"/>
            <input type="hidden" name="usersId" value="<%= teacher.getUsersId() %>"/>

            <label>Name:</label>
            <input type="text" name="name" value="<%= teacher.getName() %>"  pattern="[A-Za-z ]+"
                                                                                   title="Only alphabets and spaces are allowed" required/>

            <label>Username:</label>
            <input type="text" name="username" value="<%= teacher.getUsername() %>" required/>

            <label>Email:</label>
            <input type="email" name="email" value="<%= teacher.getEmail() %>" required/>

            <label>CNIC:</label>
            <input type="text" name="cnic"pattern="\d{13}" title="Enter 13-digit CNIC" value="<%= teacher.getCnic() %>" required/>

            <label>Qualification:</label>
            <input type="text" name="qualification" value="<%= teacher.getQualification() %>"  pattern="[A-Za-z ]+"
                                                                                                     title="Only alphabets and spaces are allowed" required/>

            <button type="submit" >Update</button>
        </form>
    </div>
</body>
</html>
