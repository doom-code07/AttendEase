<%@ page import="java.util.*, dao.TeacherDAO, dao.SubjectDAO, model.TeacherModel, model.SubjectModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Assign Subjects to Teacher</title>
</head>
<body>
<h2>Assign Subjects to Teacher</h2>

<!-- Form to Assign Subjects -->
<form action="AssignSubjectServlet" method="post">
    <label>Select Teacher:</label>
    <select name="teacherId" required>
        <%
            List<TeacherModel> teachers = new TeacherDAO().getAllTeachers();
            for (TeacherModel teacher : teachers) {
        %>
            <option value="<%= teacher.getId() %>"><%= teacher.getName() %></option>
        <%
            }
        %>
    </select>

    <h3>Select Subjects:</h3>
    <%
        List<SubjectModel> subjects = new SubjectDAO().getAllSubjects();
        for (SubjectModel subject : subjects) {
    %>
        <input type="checkbox" name="subjectIds" value="<%= subject.getId() %>"/>
        <%= subject.getTitle() %> (<%= subject.getCode() %>)<br/>
    <%
        }
    %>

    <input type="submit" value="Assign Subjects"/>
</form>

<hr/>
<h2>Assigned Subjects</h2>

<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>Teacher Name</th>
        <th>CNIC</th>
        <th>Subject</th>
        <th>Action</th>
    </tr>
<%
    List<Map<String, String>> assigned = new SubjectDAO().getAssignedSubjects();
    for (Map<String, String> row : assigned) {
%>
    <tr>
        <td><%= row.get("name") %></td>
        <td><%= row.get("cnic") %></td>
        <td><%= row.get("title") %></td>
        <td>
            <form action="UnassignSubjectServlet" method="post" style="display:inline;">
                <input type="hidden" name="subjectId" value="<%= row.get("subject_id") %>"/>
                <input type="submit" value="Delete"/>
            </form>
        </td>
    </tr>
<% } %>
</table>

<!-- Delete All Subjects Form -->
<form action="UnassignAllSubjectsServlet" method="post" style="margin-top: 20px;">
    <input type="submit" value="Delete All Assigned Subjects"/>
</form>

</body>
</html>
