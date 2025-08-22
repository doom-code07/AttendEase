<%@page import="java.util.*,dao.*,utils.*,java.sql.*"%>
<%
    int userId = (int) session.getAttribute("userId");
    String role = (String) session.getAttribute("role");

    LeaveApplicationDAO dao = new LeaveApplicationDAO();
    List<model.LeaveApplication> apps = dao.getApplicationsForUser(userId, role);
%>

<h2>Leave Applications For Teacher</h2>
<table border="1">
    <tr>
        <th>ID</th><th>Start</th><th>End</th><th>Description</th><th>Status</th><th>Action</th>
    </tr>
    <% for (model.LeaveApplication app : apps) { %>
    <tr>
        <td><%= app.getId() %></td>
        <td><%= app.getStartDate() %></td>
        <td><%= app.getEndDate() %></td>
        <td><%= app.getDescription() %></td>
        <td><%= app.getStatus() %></td>
        <td>
            <% if ("Pending".equals(app.getStatus())) { %>
                <form method="post" action="TeacherLeaveApprovalServlet">
                    <input type="hidden" name="app_id" value="<%= app.getId() %>" />
                    <button name="decision" value="Approved">Approve</button>
                    <button name="decision" value="Declined">Decline</button>
                </form>
            <% } else { %> Already <%= app.getStatus() %> <% } %>
        </td>
    </tr>
    <% } %>
</table>
