<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.TeacherModel" %>
<%
    TeacherModel teacher = (TeacherModel) request.getAttribute("teacher");
%>
<html>
<head><title>Update Teacher</title></head>
<body>
<h2>Update Teacher</h2>
<form method="post" action="UpdateTeacherServlet">
    <input type="hidden" name="id" value="<%= teacher.getId() %>"/>
    <input type="hidden" name="usersId" value="<%= teacher.getUsersId() %>"/>
    Name: <input type="text" name="name" value="<%= teacher.getName() %>" required/><br/>
    Username: <input type="text" name="username" value="<%= teacher.getUsername() %>" required/><br/>
    Email: <input type="email" name="email" value="<%= teacher.getEmail() %>" required/><br/>
    CNIC: <input type="text" name="cnic" value="<%= teacher.getCnic() %>" required/><br/>
    Qualification: <input type="text" name="qualification" value="<%= teacher.getQualification() %>" required/><br/>
    <input type="submit" value="Update"/>
</form>
</body>
</html>
