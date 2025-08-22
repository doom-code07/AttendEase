<%@ page import="java.util.List" %>
<%@ page import="model.TeacherModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Manage Teachers</title>
</head>
<body>
<h2>Registered Teachers</h2>
<form method="post" action="DeleteAllTeachersServlet" onsubmit="return confirm('Delete all teachers?');">
    <input type="submit" value="Delete All Teachers"/>
</form>

<table border="1">
    <tr>
        <th>Name</th>
        <th>CNIC</th>
        <th>Qualification</th>
        <th>Actions</th>
    </tr>
    <%
        List<TeacherModel> teachers = (List<TeacherModel>) request.getAttribute("teachers");
        for (TeacherModel t : teachers) {
    %>
<tr>

    <td><%= t.getName() %></td>
    <td><%= t.getCnic() %></td>
    <td><%= t.getQualification() %></td>
    <td>
        <a href="ViewTeacherServlet?id=<%= t.getId() %>">View</a> |
        <a href="UpdateTeacherServlet?id=<%= t.getId() %>">Update</a> |
        <a href="DeleteTeacherServlet?id=<%= t.getId() %>" onclick="return confirm('Are you sure?')">Delete</a>
    </td>
</tr>
    <% } %>
</table>
</body>
</html>
