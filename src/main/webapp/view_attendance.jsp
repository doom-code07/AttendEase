<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="model.AttendanceViewModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>View Attendance</title>
</head>
<body>
<h2>View Attendance</h2>

<form method="get" action="ViewAttendanceServlet">
    <label for="filterDate">Filter by Date:</label>
    <input type="date" name="filterDate" id="filterDate" value="<%= request.getAttribute("filterDate") != null ? request.getAttribute("filterDate") : "" %>">
    <input type="submit" value="Search">
</form>

<br>

<%
    Map<String, Double> percentages = (Map<String, Double>) request.getAttribute("attendancePercentages");
    if (percentages != null) {
%>
    <h3>Attendance Summary:</h3>
    <ul>
        <li><span style="color:green;">✔️ Present:</span> <%= String.format("%.2f", percentages.get("Present")) %>%</li>
        <li><span style="color:red;">❌ Absent:</span> <%= String.format("%.2f", percentages.get("Absent")) %>%</li>
        <li><span style="color:blue;">🟡 Leave:</span> <%= String.format("%.2f", percentages.get("Leave")) %>%</li>
        <li><span style="color:orange;">⚠️ Struck Off:</span> <%= String.format("%.2f", percentages.get("StruckOff")) %>%</li>
    </ul>
<%
    }
%>

<table border="1">
    <tr>
        <th>Date</th>
        <th>Class</th>
        <th>Subject</th>
        <th>Status</th>
    </tr>

    <%
        List<AttendanceViewModel> records = (List<AttendanceViewModel>) request.getAttribute("records");
        if (records != null && !records.isEmpty()) {
            for (AttendanceViewModel a : records) {
    %>
    <tr>
        <td><%= a.getDate() %></td>
        <td><%= a.getClassName() %></td>
        <td><%= a.getSubjectName() %></td>
        <td><%= a.getStatus() %></td>
    </tr>
    <%
            }
        } else {
    %>
    <tr><td colspan="4">No attendance records found.</td></tr>
    <% } %>
</table>

</body>
</html>
