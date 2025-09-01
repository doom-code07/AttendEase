<%@ page import="dao.PolicyDAO, dao.AttendancePolicyDAO , model.PolicyModel , model.StudentModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Student Portal</h2>
        <ul>
            <li><a href="ViewAttendanceServlet">
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
        <a href="index.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>

        <header>
            <h1>Attendance Policies</h1>
        </header>

        <div>
            <%
                PolicyDAO dao = new PolicyDAO();
                PolicyModel policy = dao.getPolicy();
                if (policy != null) {
            %>
                <p>
                    <b>Minimum Attendance:</b> <%= policy.getMinAttendancePercentage() %>%<br>
                    <b>Fine Per Absent Subject:</b> Rs. <%= policy.getFinePerAbsentSubject() %><br>
                    <b>Struck Off After:</b> <%= policy.getStruckOffAfterAbsents() %> Absents
                </p>
            <%
                } else {
            %>
                <p>No attendance policies set.</p>
            <%
                }
            %>

            <%
                StudentModel student = (StudentModel) request.getAttribute("student");
                if (student != null) {
            %>
                <p><b>Total Fine:</b> Rs. <%= student.getTotalFine() %></p>
            <%
                } else {
            %>
                <p>Student information not available.</p>
            <%
                }
            %>
        </div>

    </main>
</div>
</body>
</html>
