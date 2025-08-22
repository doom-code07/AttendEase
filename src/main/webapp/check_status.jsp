<%@ page import="java.util.*,dao.LeaveApplicationDAO,model.LeaveApplication" %>
<%
    int studentId = (int) session.getAttribute("studentId"); // comes from login session
    LeaveApplicationDAO dao = new LeaveApplicationDAO();
    List<LeaveApplication> apps = dao.findByStudent(studentId);
%>

<h2>My Leave Applications</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Description</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Status</th>
        <th>Decision Date</th>
    </tr>
    <%
        for (LeaveApplication app : apps) {
    %>
    <tr>
        <td><%= app.getId() %></td>
        <td><%= app.getDescription() %></td>
        <td><%= app.getStartDate() %></td>
        <td><%= app.getEndDate() %></td>
        <td><%= app.getStatus() %></td>   <!-- ✅ Shows Approved/Declined/Pending -->
        <td><%= app.getDecisionDate() %></td>
    </tr>
    <%
        }
    %>
</table>
