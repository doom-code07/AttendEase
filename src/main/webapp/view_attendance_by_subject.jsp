<%@ page import="java.util.*, model.AttendanceViewModel, model.SubjectModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Attendance by Subject</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <aside class="sidebar">
        <h2>Teacher Portal</h2>
        <ul>
            <li><a href="ActiveApprovedLeavesServlet"><i class="fas fa-marker"></i> Mark Attendance</a></li>
            <li><a href="update_attendance.jsp"><i class="fas fa-edit"></i> Update Attendance</a></li>
            <li><a href="ViewAttendanceBySubjectServlet" class="active"><i class="fas fa-book"></i> View Attendance</a></li>
            <li><a href="TeacherApplicationsServlet"><i class="fas fa-envelope-open-text"></i> Applications</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <a href="index.jsp" class="logout_icon"><i class="fas fa-sign-out-alt"></i> Logout</a>

        <header><h1>View Attendance by Subject</h1></header>

        <%
            List<SubjectModel> subjectList = (List<SubjectModel>) request.getAttribute("subjectList");
            Integer selectedSubjectId = (Integer) request.getAttribute("subjectId");
        %>

        <form method="post" action="ViewAttendanceBySubjectServlet">
            <label for="subject_id">Select Subject:</label>
            <select name="subject_id" id="subject_id" required>
                <option value="">-- choose subject --</option>
                <%
                    if (subjectList != null && !subjectList.isEmpty()) {
                        for (SubjectModel sub : subjectList) {
                            String sel = (selectedSubjectId != null && selectedSubjectId == sub.getId()) ? "selected" : "";
                %>
                            <option value="<%= sub.getId() %>" <%= sel %>><%= sub.getTitle() %> (<%= sub.getCode() %>)</option>
                <%
                        }
                    } else {
                %>
                        <option value="">No assigned subjects</option>
                <%
                    }
                %>
            </select>
            <button type="submit">View Attendance</button>
        </form>

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


            <table id="attendanceTable" border="1" cellpadding="6" cellspacing="0">
                <tr>
                    <th>Date</th>
                    <th>Roll No</th>
                    <th>Student Name</th>
                    <th>Class</th>
                    <th>Subject (Code)</th>
                    <th>Status</th>
                </tr>
                <% for (AttendanceViewModel att : attendanceList) { %>
                <tr>
                    <td><%= att.getDate() %></td>
                    <td><%= att.getRollNo() %></td>
                    <td><%= att.getStudentName() %></td>
                    <td><%= att.getClassName() %></td>
                    <td><%= att.getSubjectTitle() %> (<%= att.getSubjectCode() %>)</td>
                    <td><%= att.getStatus() %></td>
                </tr>
                <% } %>
            </table>
        <%
            } else if (selectedSubjectId != null) {
        %>
            <p>No attendance records found for the selected subject.</p>
        <%
            }
        %>
    </main>
</div>

<script>
function filterTable() {
    let month = document.getElementById("monthFilter").value;

    let table = document.getElementById("attendanceTable");
    let rows = table.getElementsByTagName("tr");

    for (let i = 1; i < rows.length; i++) { // skip header
        let dateCell = rows[i].getElementsByTagName("td")[0];

        if (dateCell) {
            let dateValue = dateCell.textContent.trim();

            // Extract month from date (assuming yyyy-mm-dd format)
            let rowMonth = "";
            if (dateValue.match(/^\d{4}-\d{2}-\d{2}$/)) {
                rowMonth = dateValue.split("-")[1];
            }

            // Apply month filter
            let matchMonth = (month === "" || rowMonth === month);

            rows[i].style.display = matchMonth ? "" : "none";
        }
    }
}
</script>




</body>
</html>
