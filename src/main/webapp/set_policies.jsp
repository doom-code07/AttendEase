<%@ page import="java.util.List" %>
<%@ page import="dao.PolicyDAO" %>
<%@ page import="model.PolicyModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    PolicyDAO dao = new PolicyDAO();
    PolicyModel policy = dao.getPolicy(); // This should return the single policy, if exists.
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vice Principal - Set Policies</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
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
            <li><a href="set_policies.jsp" class="active">
                <i class="fas fa-cogs"></i> Set Policies
            </a></li>
            <li><a href="ViceApplicationsServlet"">
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
            <h1>Set/Update Attendance Policies</h1>
        </header>

        <!-- Policy Form -->
        <div class="content">
            <form method="post" action="ApplyPolicyServlet" class="form-container">
                <label>Minimum Attendance Percentage</label><br>
                <input type="number" name="min_attendance_percentage" pattern="[0-9]+"
                                                                             title="Only digits are allowed" required
                       value="<%= (policy != null ? policy.getMinAttendancePercentage() : "") %>">
                <br>
                <label>Fine Per Absent Subject</label><br>
                <input type="number" name="fine_per_absent_subject" pattern="[0-9]+"
                                                                           title="Only digits are allowed" required
                       value="<%= (policy != null ? policy.getFinePerAbsentSubject() : "") %>">
                <br>
                <label>Struck Off After Absents</label><br>
                <input type="number" name="struck_off_after_absents" pattern="[0-9]+"
                                                                            title="Only digits are allowed" required
                       value="<%= (policy != null ? policy.getStruckOffAfterAbsents() : "") %>">

                <button type="submit" class="action-btn">Save Policy</button>
            </form>
        </div>

        <hr>

        <!-- Current Policy -->
        <div class="content">
            <h2>Current Policy</h2>
            <%
                if (policy != null) {
            %>
            <div class="table-container">
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Min Attendance %</th>
                            <th>Fine Per Absent Subject</th>
                            <th>Struck Off After Absents</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><%= policy.getMinAttendancePercentage() %>%</td>
                            <td>Rs. <%= policy.getFinePerAbsentSubject() %></td>
                            <td><%= policy.getStruckOffAfterAbsents() %> Absents</td>
                            <td>
                                <form method="post" action="DeletePolicyServlet" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= policy.getId() %>">
                                    <button type="submit" class="delete-btn">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <% } else { %>
                <p>No policy set.</p>
            <% } %>
        </div>
    </main>
</div>
</body>
</html>
