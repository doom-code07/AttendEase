<%@ page import="java.util.List, model.LeaveApplication" %>
<%
    List<LeaveApplication> apps = (List<LeaveApplication>) request.getAttribute("applications");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vice Principal - Applications</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link rel="stylesheet" href="css/application_buttons.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Vice Principal</h2>
        <ul>
            <li><a href="manage_struckOff.jsp">
                <i class="fas fa-user-times"></i> Manage Struck Off
            </a></li>
            <li><a href="set_policies.jsp">
                <i class="fas fa-cogs"></i> Set Policies
            </a></li>
            <li><a href="ViceApplicationsServlet".jsp" class="active">
                <i class="fas fa-envelope-open-text"></i> Manage Applications
            </a></li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <a href="viceprincipal_dashboard.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>Applications for Vice Principal (More than 3 Days)</h1>
        </header>

        <div class="content">
            <% if (apps == null || apps.isEmpty()) { %>
                <p>No applications.</p>
            <% } else { %>
                <div class="table-container">
                   <label>Search:</label>
                   <input type="text" id="rollSearch" placeholder="Search by Roll No..." onkeyup="filterByRollNo()"/>

                    <table id="rollSearch" class="styled-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Roll No</th>
                                <th>Name</th>
                                <th>Class</th>
                                <th>Start</th>
                                <th>End</th>
                                <th>Days</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (LeaveApplication a : apps) {
                                 long days = (a.getEndDate().getTime() - a.getStartDate().getTime())/(1000*60*60*24) + 1;
                            %>
                            <tr>
                                <td><%= a.getId() %></td>
                                <td><%= a.getRollNo() %></td>
                                <td><%= a.getStudentName() %></td>
                                <td><%= a.getClassName() %></td>
                                <td><%= a.getStartDate() %></td>
                                <td><%= a.getEndDate() %></td>
                                <td><%= days %></td>
                                <td><%= a.getDescription() %></td>
                                <td><%= a.getStatus() %></td>
                                <td>
                                    <% if ("Pending".equals(a.getStatus())) { %>
                                    <div class="action-buttons">
                                        <form action="ApproveDeclineServlet" method="post" style="display:inline">
                                            <input type="hidden" name="applicationId" value="<%= a.getId() %>">
                                            <input type="hidden" name="from" value="vice">
                                            <button class="approve-btn"name="action" value="Approved" type="submit" class="action-btn">Approve</button>
                                        </form>
                                        <form action="ApproveDeclineServlet" method="post" style="display:inline">
                                            <input type="hidden" name="applicationId" value="<%= a.getId() %>">
                                            <input type="hidden" name="from" value="vice">
                                            <button class="decline-btn" name="action" value="Declined" type="submit" class="action-btn danger">Decline</button>
                                        </form>
                                        </div>
                                    <% } else { %>
                                        <%= a.getStatus()%>
                                    <% } %>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>
        <form action="DeleteAllViceApplicationsServlet" method="post">
        <button type="submit" class="decline-btn" onclick="return confirm('Are you sure you want to delete all applications?')">
        Delete All Applications</button>
        </form>

         <%
         String msg = (String) request.getAttribute("message");
         if (msg != null) {
          %>
          <p style="color:red;"><%= msg %></p>
          <% } %>
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
