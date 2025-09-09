<%@ page import="java.util.List" %>
<%@ page import="model.StruckOffRow" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Struck Off</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Vice Principal</h2>
        <ul>
            <li><a href="ManageStruckOffServlet">
                <i class="fas fa-user-times"></i> Manage Struck Off
            </a></li>
            <li><a href="set_policies.jsp">
                <i class="fas fa-cogs"></i> Set Policies
            </a></li>
            <li><a href="ViceApplicationsServlet">
                <i class="fas fa-envelope-open-text"></i> Manage Applications
            </a></li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <a href="index.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>

        <header>
            <h1>Manage Struck Off</h1>
        </header>


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
        Done
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
