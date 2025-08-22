<%@ page import="java.util.List" %>
<%@ page import="dao.PolicyDAO" %>
<%@ page import="model.PolicyModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    PolicyDAO dao = new PolicyDAO();
    PolicyModel policy = dao.getPolicy(); // This should return the single policy, if exists.
%>
<html>
<head>
    <title>Set Policies</title>
</head>
<body>
    <h2>Set/Update Attendance Policies</h2>
    <form method="post" action="ApplyPolicyServlet">
        Minimum Attendance Percentage: <input type="number" name="min_attendance_percentage" required value="<%= (policy != null ? policy.getMinAttendancePercentage() : "") %>"><br><br>
        Fine Per Absent Subject: <input type="number" name="fine_per_absent_subject" required value="<%= (policy != null ? policy.getFinePerAbsentSubject() : "") %>"><br><br>
        Struck Off After Absents: <input type="number" name="struck_off_after_absents" required value="<%= (policy != null ? policy.getStruckOffAfterAbsents() : "") %>"><br><br>
        <input type="submit" value="Save Policy">
    </form>

    <hr>
    <h2>Current Policy</h2>
    <%
        if (policy != null) {
    %>
    <table border="1">
        <tr>
            <th>Min Attendance %</th>
            <th>Fine Per Absent Subject</th>
            <th>Struck Off After Absents</th>
            <th>Action</th>
        </tr>
        <tr>
            <td><%= policy.getMinAttendancePercentage() %>%</td>
            <td>Rs. <%= policy.getFinePerAbsentSubject() %></td>
            <td><%= policy.getStruckOffAfterAbsents() %> Absents</td>
            <td>
                <form method="post" action="DeletePolicyServlet" style="display:inline;">
                    <input type="hidden" name="id" value="<%= policy.getId() %>">
                    <input type="submit" value="Delete">
                </form>
            </td>
        </tr>
    </table>
    <% } else { %>
        <p>No policy set.</p>
    <% } %>
</body>
</html>
