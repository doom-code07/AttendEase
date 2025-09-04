<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="model.AttendanceViewModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Attendance</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">

    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Student Portal</h2>
        <ul>
            <li><a href="ViewAttendanceServlet" class="active">
                <i class="fas fa-check-circle"></i> View Attendance
            </a></li>
            <li><a href="submit_application.jsp">
                <i class="fas fa-file-alt"></i> Submit Application
            </a></li>
            <li><a href="check_status.jsp">
                <i class="fas fa-calendar-check"></i> Application Status
            </a></li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <a href="student_dashboard.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>View Attendance</h1>
        </header>

        <!-- Filter Form -->
        <form method="get" action="ViewAttendanceServlet" class="form-style">
            <label for="filterDate">Filter by Date:</label>
            <input type="date" name="filterDate" id="filterDate"
                   value="<%= request.getAttribute("filterDate") != null ? request.getAttribute("filterDate") : "" %>">
            <button type="submit" class="btn-submit">Search</button>
        </form>

        <!-- Attendance Summary -->
        <%
            Map<String, Double> percentages = (Map<String, Double>) request.getAttribute("attendancePercentages");
            if (percentages != null) {
        %>
            <h3>Attendance Summary:</h3>
            <ul>
                <li><span style="color:green;">✔️ Present:</span> <%= String.format("%.2f", percentages.get("Present")) %>%</li>
                <li><span style="color:red;">❌ Absent:</span> <%= String.format("%.2f", percentages.get("Absent")) %>%</li>
                <li><span style="color:blue;">🟡 Leave:</span> <%= String.format("%.2f", percentages.get("Leave")) %>%</li>
                <li><span style="color:orange;">⚠️ Struck Off:</span> <%= String.format("%.2f", percentages.get("Struck Off")) %>%</li>
            </ul>
        <%
            }
        %>

        <!-- Attendance Records -->
        <table class="styled-table">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Class</th>
                    <th>Subject</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<AttendanceViewModel> records = (List<AttendanceViewModel>) request.getAttribute("records");
                if (records != null && !records.isEmpty()) {
                    for (AttendanceViewModel a : records) {
            %>
                <tr>
                    <td><%= a.getDate() %></td>
                    <td><%= a.getClassName() %></td>
                    <td><%= a.getSubjectName() %></td>
                    <td>
                        <% if ("Present".equalsIgnoreCase(a.getStatus())) { %>
                            <span class="status approved">Present</span>
                        <% } else if ("Absent".equalsIgnoreCase(a.getStatus())) { %>
                            <span class="status declined">Absent</span>
                        <% } else if ("Leave".equalsIgnoreCase(a.getStatus())) { %>
                            <span class="status pending">Leave</span>
                        <% } else { %>
                            <span class="status struck Off">Struck Off</span>
                        <% } %>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="4">No attendance records found.</td></tr>
            <% } %>
            </tbody>
        </table>
    </main>
</div>
</body>
</html>
