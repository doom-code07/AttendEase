<%@ page import="model.PolicyModel, model.StudentModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>

    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link rel="stylesheet" href="css/graph_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>

<body>
<div class="container">

    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Student Portal</h2>
        <ul>
            <li><a href="ViewAttendanceServlet"><i class="fas fa-check-circle"></i> View Attendance</a></li>
            <li><a href="submit_application.jsp"> <i class="fas fa-file-alt"></i> Submit Application</a></li>
            <li><a href="check_status.jsp"><i class="fas fa-calendar-check"></i> Application Status</a></li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">

        <a href="index.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>

        <header>
            <h1>Student Dashboard</h1>
        </header>

        <%
            // ===== Data from Servlet =====
            Double attendancePercent =
                    (Double) request.getAttribute("attendancePercent");
            if (attendancePercent == null) attendancePercent = 0.0;

            PolicyModel policy =
                    (PolicyModel) request.getAttribute("policy");

            StudentModel student =
                    (StudentModel) request.getAttribute("student");

            int minAttendance =
                    (policy != null) ? policy.getMinAttendancePercentage() : 0;
        %>

        <!-- ================= ATTENDANCE CIRCLE ================= -->
        <div class="attendance-card">
            <h2>Your Attendance</h2>

            <div class="circle"
                 style="background: conic-gradient(
                 <%= attendancePercent < minAttendance ? "#e74c3c" : "#2ecc71" %>
                 <%= attendancePercent %>%,
                 #e6e6e6 0);">
                <span><%= String.format("%.1f", attendancePercent) %>%</span>
            </div>

            <%
                if (attendancePercent < minAttendance) {
            %>
                <p class="warning-text">
                    ⚠ Attendance below minimum requirement!
                </p>
            <%
                }
            %>
        </div>

        <!-- ================= POLICY INFO ================= -->
        <%
            if (policy != null) {
        %>
        <div class="policy-card">
            <h2>Attendance Policy</h2>
            <p><b>Minimum Attendance:</b> <%= policy.getMinAttendancePercentage() %>%</p>
            <p><b>Fine Per Absent:</b> Rs. <%= policy.getFinePerAbsentSubject() %></p>
            <p><b>Struck Off After:</b> <%= policy.getStruckOffAfterAbsents() %> Absents</p>
        </div>
        <%
            }
        %>

        <!-- ================= TOTAL FINE ================= -->
        <%
            if (student != null) {
        %>
        <div class="fine-card">
            <h2>Total Fine</h2>
            <p>Rs. <%= student.getTotalFine() %></p>
        </div>
        <%
            }
        %>

    </main>
</div>
</body>
</html>
