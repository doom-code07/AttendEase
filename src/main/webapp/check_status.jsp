<%@ page import="java.util.*, dao.LeaveApplicationDAO, model.LeaveApplication" %>
<%
    Integer studentId = (Integer) session.getAttribute("studentId");
    if (studentId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    LeaveApplicationDAO dao = new LeaveApplicationDAO();
    List<LeaveApplication> apps = dao.findByStudent(studentId);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Application Status</title>
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
            <li><a href="check_status.jsp" class="active">
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
            <h1>My Leave Applications</h1>
        </header>

        <% if (apps.isEmpty()) { %>
            <p>No applications submitted yet.</p>
        <% } else { %>
        <label for="monthFilter">Filter by Month:</label>
            <select id="monthFilter" onchange="filterByMonth()">
                <option value="">All</option>
                <option value="01">January</option>
                <option value="02">February</option>
                <option value="03">March</option>
                <option value="04">April</option>
                <option value="05">May</option>
                <option value="06">June</option>
                <option value="07">July</option>
                <option value="08">August</option>
                <option value="09">September</option>
                <option value="10">October</option>
                <option value="11">November</option>
                <option value="12">December</option>
            </select>


            <table class="styled-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Description</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th>Decision Date</th>
                    </tr>
                </thead>
                <tbody>
                <% for (LeaveApplication app : apps) { %>
                    <tr>
                        <td><%= app.getId() %></td>
                        <td><%= app.getDescription() %></td>
                        <td><%= app.getStartDate() %></td>
                        <td><%= app.getEndDate() %></td>
                        <td>
                            <% if ("Approved".equalsIgnoreCase(app.getStatus())) { %>
                                <span class="status approved">Approved</span>
                            <% } else if ("Declined".equalsIgnoreCase(app.getStatus())) { %>
                                <span class="status declined">Declined</span>
                            <% } else { %>
                                <span class="status pending">Pending</span>
                            <% } %>
                        </td>
                        <td><%= app.getDecisionDate() != null ? app.getDecisionDate() : "-" %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        <% } %>
    </main>
</div>
<script>
function filterByMonth() {
    const month = document.getElementById("monthFilter").value;
    const rows = document.querySelectorAll(".styled-table tbody tr");

    rows.forEach(row => {
        const startDate = row.cells[2].innerText; // Start Date column
        const endDate = row.cells[3].innerText;   // End Date column

        if (!month) {
            row.style.display = ""; // show all if no filter
        } else {
            // Extract MM from YYYY-MM-DD (assuming format is yyyy-mm-dd)
            const startMonth = startDate.split("-")[1];
            const endMonth = endDate.split("-")[1];

            if (startMonth === month || endMonth === month) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        }
    });
}
</script>
</body>
</html>
