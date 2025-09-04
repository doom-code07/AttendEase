<%@ page import="java.util.List" %>
<%@ page import="model.StruckOffRow" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Struck-Off Students</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
</head>
<body>
<div class="container">
    <aside class="sidebar">
        <h2>Vice Principal</h2>
        <ul>
            <li><a href="viceprincipal_dashboard.jsp">Dashboard</a></li>
            <li><a href="ManageStruckOffServlet" class="active">Manage Struck-Off</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <header><h1>Manage Struck-Off Students</h1></header>

        <%
            List<StruckOffRow> rows = (List<StruckOffRow>) request.getAttribute("rows");
            if (rows == null || rows.isEmpty()) {
        %>
            <p>No students are currently struck off.</p>
        <%
            } else {
        %>
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th>Roll No</th>
                <th>Name</th>
                <th>Class</th>
                <th>Struck-Off Date</th>
                <th>Meeting Done Date</th>
                <th>Action</th>
            </tr>
            </thead>
<tbody>
<%
    int i = 1;
    for (StruckOffRow r : rows) {
%>
<tr>
    <td><%= i++ %></td>
    <td><%= r.getRollNo() %></td>
    <td><%= r.getStudentName() %></td>
    <td><%= r.getClassName() == null ? "" : r.getClassName() %></td>
    <td><%= r.getStruckOffDate() == null ? "" : r.getStruckOffDate() %></td>
    <td><%= r.getMeetingDoneDate() == null ? "" : r.getMeetingDoneDate() %></td>
<td>
    <% if (r.getStruckOffDate() != null && r.getMeetingDoneDate() == null) { %>
        <form action="ParentMeetingDoneServlet" method="post">
            <input type="hidden" name="historyId" value="<%= r.getHistoryId() %>" />
            <button type="submit">Done</button>
        </form>
    <% } else if (r.getStruckOffDate() != null && r.getMeetingDoneDate() != null) { %>
        ✅ Done
    <% } else { %>
        -
    <% } %>
</td>


</tr>
<% } %>
</tbody>

        </table>
        <% } %>
    </main>
</div>
</body>
</html>
