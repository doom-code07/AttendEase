<%@ page import="java.util.List, model.LeaveApplication" %>
<%
    List<LeaveApplication> apps = (List<LeaveApplication>) request.getAttribute("applications");
%>
<html>
<head><title>Teacher - Applications</title></head>
<body>
<h2>Applications Assigned To You</h2>
<% if (apps == null || apps.isEmpty()) { %>
    <p>No applications.</p>
<% } else { %>
    <table border="1" cellpadding="5" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Roll No</th>
            <th>Name</th>
            <th>Class</th>
            <th>Start</th>
            <th>End</th>
            <th>Description</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <% for (LeaveApplication a : apps) { %>
            <tr>
                <td><%= a.getId() %></td>
                <td><%= a.getRollNo() %></td>
                <td><%= a.getStudentName() %></td>
                <td><%= a.getClassName() %></td>
                <td><%= a.getStartDate() %></td>
                <td><%= a.getEndDate() %></td>
                <td><%= a.getDescription() %></td>
                <td><%= a.getStatus() %></td>
                <td>
                    <% if ("Pending".equals(a.getStatus())) { %>
                        <form action="ApproveDeclineServlet" method="post" style="display:inline">
                            <input type="hidden" name="applicationId" value="<%= a.getId() %>">
                            <input type="hidden" name="from" value="teacher">
                            <button name="action" value="Approved" type="submit">Approve</button>
                        </form>
                        <form action="ApproveDeclineServlet" method="post" style="display:inline">
                            <input type="hidden" name="applicationId" value="<%= a.getId() %>">
                            <input type="hidden" name="from" value="teacher">
                            <button name="action" value="Declined" type="submit">Decline</button>
                        </form>
                    <% } else { %>
                        <%= a.getStatus() %>
                    <% } %>
                </td>
            </tr>
        <% } %>
    </table>
<% } %>
</body>
</html>
