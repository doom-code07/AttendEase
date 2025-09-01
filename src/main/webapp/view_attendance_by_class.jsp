<%@ page import="java.util.*, model.AttendanceViewModel, model.ClassModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Attendance by Class</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar (same as Teacher Dashboard) -->
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
                <a href="ViewAttendanceByClassServlet" class="active">
                    <i class="fas fa-book"></i> View Attendance
                </a>
            </li>
            <li>
                <a href="TeacherApplicationsServlet">
                    <i class="fas fa-envelope-open-text"></i> Applications
                </a>
            </li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Logout -->
        <a href="index.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>

        <header>
            <h1>View Attendance by Class</h1>
        </header>

        <!-- Form -->
        <form action="ViewAttendanceByClassServlet" method="post">
            <label for="class_id">Select Class:</label>
            <select name="class_id" id="class_id" required>
                <option value="">-- Select Class --</option>
                <%
                    List<ClassModel> classList = (List<ClassModel>) request.getAttribute("classList");
                    String selectedClassIdStr = request.getParameter("class_id");
                    int selectedClassId = -1;
                    if (selectedClassIdStr != null && !selectedClassIdStr.isEmpty()) {
                        selectedClassId = Integer.parseInt(selectedClassIdStr);
                    }

                    if (classList != null) {
                        for (ClassModel cls : classList) {
                            boolean isSelected = (cls.getId() == selectedClassId);
                %>
                <option value="<%= cls.getId() %>" <%= isSelected ? "selected" : "" %>><%= cls.getName() %></option>
                <%
                        }
                    }
                %>
            </select>
            <button type="submit">View Attendance</button>
        </form>

        <!-- Attendance Records -->

        <%
            List<AttendanceViewModel> attendanceList = (List<AttendanceViewModel>) request.getAttribute("attendanceList");
            if (attendanceList != null && !attendanceList.isEmpty()) {
        %>
            <h3>Attendance Records</h3>
            <label for="monthFilter">Filter by Month:</label>
                <select id="monthFilter" onchange="filterTable()">
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

                <label for="subjectFilter">Filter by Subject:</label>
                <input type="text" id="subjectFilter" placeholder="Enter subject..." onkeyup="filterTable()">


                <table id="attendanceTable">
                <tr>
                    <th>Date</th>
                    <th>Roll No</th>
                    <th>Name</th>
                    <th>Subject</th>
                    <th>Status</th>
                    <!-- <th>Action</th> -->
                </tr>
                <% for (AttendanceViewModel att : attendanceList) { %>
                <tr>
                    <td><%= att.getDate() %></td>
                    <td><%= att.getStudent().getRollNo() %></td>
                    <td><%= att.getStudent().getName() %></td>
                    <td><%= att.getSubject().getTitle() %> (<%= att.getSubject().getCode() %>)</td>
                    <td><%= att.getStatus() %></td>
                   <!-- <td>
                        <form action="DeleteAttendanceServlet" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= att.getId() %>">
                            <input type="hidden" name="class_id" value="<%= request.getParameter("class_id") %>">
                            <input type="hidden" name="action" value="deleteSingle">
                            <button type="submit" class="delete-btn">Delete</button>
                        </form>
                    </td> -->
                </tr>
                <% } %>
            </table>

            <form action="DeleteAttendanceServlet" method="post" style="margin-top: 20px;">
                <input type="hidden" name="class_id" value="<%= request.getParameter("class_id") %>">
                <input type="hidden" name="action" value="deleteAll">
                <button type="submit" class="delete-btn" onclick="return confirm('Are you sure you want to delete all records for this class?');">
                    Delete All Attendance Records
                </button>
            </form>
        <%
            } else if (request.getParameter("class_id") != null) {
        %>
            <p>No attendance records found for this class.</p>
        <%
            }
        %>
    </main>
</div>
<script>
function filterTable() {
    let month = document.getElementById("monthFilter").value;
    let subject = document.getElementById("subjectFilter").value.toLowerCase();

    let table = document.getElementById("attendanceTable");
    let rows = table.getElementsByTagName("tr");

    for (let i = 1; i < rows.length; i++) { // skip header
        let dateCell = rows[i].getElementsByTagName("td")[0];
        let subjectCell = rows[i].getElementsByTagName("td")[3];

        if (dateCell && subjectCell) {
            let dateValue = dateCell.textContent.trim();
            let subjectValue = subjectCell.textContent.toLowerCase();

            // Extract month from date (assuming yyyy-mm-dd format)
            let rowMonth = "";
            if (dateValue.match(/^\d{4}-\d{2}-\d{2}$/)) {
                rowMonth = dateValue.split("-")[1];
            }

            // Apply filters
            let matchMonth = (month === "" || rowMonth === month);
            let matchSubject = (subject === "" || subjectValue.includes(subject));

            if (matchMonth && matchSubject) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    }
}
</script>
</body>
</html>
