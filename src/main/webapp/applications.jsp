<%@ page import="java.util.List, model.LeaveApplication" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<LeaveApplication> apps = (List<LeaveApplication>) request.getAttribute("applications");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Teacher - Applications</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link rel="stylesheet" href="css/application_buttons.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Teacher Portal</h2>
        <ul>
            <li>
                <a href="ActiveApprovedLeavesServlet">
                    <i class="fas fa-marker"></i> Mark Attendance
                </a>
            </li>
            <li>
                <a href="update_attendance.jsp">
                    <i class="fas fa-edit"></i> Update Attendance
                </a>
            </li>
            <li>
                <a href="ViewAttendanceByClassServlet" target="content-frame">
                      <i class="fas fa-book"></i> View Attendance
                </a>
            </li>
            <li>
                <a href="TeacherApplicationsServlet" class="active">
                    <i class="fas fa-envelope-open-text"></i> Applications
                </a>
            </li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Back -->
        <a href="teacher_dashboard.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>Applications Assigned To You</h1>
        </header>

        <section class="content-section">
            <% if (apps == null || apps.isEmpty()) { %>
                <p>No applications.</p>
            <% } else { %>
            <label>Search:</label>
            <input type="text" id="rollSearch" placeholder="Search by Roll No..." onkeyup="filterByRollNo()"/>

                    <table id="rollSearch">
                    <thead>
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
                    </thead>
                    <tbody>
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
                                        <div class="action-buttons">
                                            <form action="ApproveDeclineServlet" method="post">
                                                <input type="hidden" name="applicationId" value="<%= a.getId() %>">
                                                <input type="hidden" name="from" value="teacher">
                                                <button class="approve-btn" name="action" value="Approved" type="submit">Approve</button>
                                            </form>
                                            <form action="ApproveDeclineServlet" method="post">
                                                <input type="hidden" name="applicationId" value="<%= a.getId() %>">
                                                <input type="hidden" name="from" value="teacher">
                                                <button class="decline-btn" name="action" value="Declined" type="submit">Decline</button>
                                            </form>
                                        </div>
                                    <% } else { %>
                                        <%= a.getStatus() %>
                                    <% } %>
                                </td>

                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
             <form action="DeleteAllApplicationsServlet" method="post">
             <button type="submit" class="decline-btn" onclick="return confirm('Are you sure you want to delete all applications?')">
             Delete All Applications</button>
             </form>

              <%
              String msg = (String) request.getAttribute("message");
              if (msg != null) {
              %>
              <p style="color:red;"><%= msg %></p>
              <% } %>
        </section>
    </main>
</div>
<script>
function filterByRollNo() {
    const input = document.getElementById("rollSearch").value.toLowerCase();
    const rows = document.querySelectorAll("table tbody tr");

    rows.forEach(row => {
        const rollNoCell = row.cells[1]; // Roll No is the 2nd column (index 1)
        if (rollNoCell) {
            const rollText = rollNoCell.textContent.toLowerCase();
            if (rollText.includes(input)) {
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
