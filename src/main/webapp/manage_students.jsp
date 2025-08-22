<%@ page import="java.util.List" %>
<%@ page import="model.StudentModel" %>
<%
    List<StudentModel> students = (List<StudentModel>) request.getAttribute("students");
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Manage Students</title>
</head>
<body>
    <h1>Registered Students</h1>

    <!-- Show DB error if any -->
    <% if (error != null) { %>
        <p style="color: red;"><%= error %></p>
    <% } %>

    <!-- Delete all students button -->
    <form action="DeleteAllStudentsServlet" method="post" style="float:right;">
        <input type="submit" value="Delete All">
    </form>

    <table border="1">
        <tr>
            <th>Roll No</th>
            <th>Name</th>
            <th>Class</th>
            <th>Actions</th>
        </tr>

        <%
            if (students != null && !students.isEmpty()) {
                for (StudentModel s : students) {
        %>
        <tr>
            <td><%= s.getRollNo() %></td>
            <td><%= s.getName() %></td>
            <td><%= s.getClassName() %></td>
            <td>
                <a href="ViewStudentServlet?id=<%= s.getId() %>">View</a> |
                <a href="UpdateStudentServlet?id=<%= s.getId() %>">Update</a> |
                <a href="DeleteStudentServlet?id=<%= s.getId() %>">Delete</a>
            </td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="4">No students found.</td>
        </tr>
        <% } %>
    </table>
</body>
</html>
