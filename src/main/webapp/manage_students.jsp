<%@ page import="java.util.List" %>
<%@ page import="model.StudentModel" %>
<%
    List<StudentModel> students = (List<StudentModel>) request.getAttribute("students");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Students</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar (same as admin dashboard) -->
    <aside class="sidebar">
        <h2>Admin Portal</h2>
        <ul>
            <li><a href="ManageStudentServlet" class="active">
                <i class="fas fa-user-graduate"></i> Manage Students
            </a></li>
            <li><a href="ManageTeacherServlet">
                <i class="fas fa-chalkboard-teacher"></i> Manage Teachers
            </a></li>
            <li><a href="assign_subject.jsp">
                <i class="fas fa-book"></i> Assign Subjects
            </a></li>
            <li><a href="GenerateChallanPageServlet">
                <i class="fas fa-file-invoice-dollar"></i> Generate Challan
            </a></li>
            <li><a href="manage_classes.jsp">
                <i class="fas fa-school"></i> Manage Classes
            </a></li>
        </ul>
    </aside>

    <!-- Main content -->
    <main class="main-content">
        <a href="admin_dashboard.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>Registered Students</h1>
        </header>

        <!-- Show DB error if any -->
        <% if (error != null) { %>
            <p style="color: red;"><%= error %></p>
        <% } %>

        <!-- Delete all students button -->
        <form action="DeleteAllStudentsServlet" method="post" style="float:right; margin-bottom:10px;">
            <button type="submit" class="delete-btn">Delete All</button>
        </form>

        <!-- Search box for roll number -->
            <label>Search Student:</label>
            <input type="text" id="rollSearch" onkeyup="filterByRollNo()" placeholder="Search by Roll No...">

        <!-- Students Table -->
        <table id="studentsTable">
            <tr>
                <th>Roll No</th>
                <th>Name</th>
                <th>Class</th>
                <th>Actions</th>
            </tr>

            <%
                if (students != null && !students.isEmpty()) {
                    for (StudentModel s : students) {
            %>
            <tr>
                <td><%= s.getRollNo() %></td>
                <td><%= s.getName() %></td>
                <td><%= s.getClassName() %></td>
                <td>
                    <a href="ViewStudentServlet?id=<%= s.getId() %>" title="View">
                        <i class="fas fa-eye"></i>
                    </a>
                    |
                    <a href="UpdateStudentServlet?id=<%= s.getId() %>" title="Update">
                        <i class="fas fa-pencil-alt"></i>
                    </a>
                    |
                    <a href="DeleteStudentServlet?id=<%= s.getId() %>" title="Delete" onclick="return confirm('Are you sure?')" style="color: red;">
                        <i class="fas fa-trash"></i>
                    </a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="4">No students found.</td>
            </tr>
            <% } %>
        </table>
    </main>
</div>
<script>
function filterByRollNo() {
    let input = document.getElementById("rollSearch");
    let filter = input.value.toUpperCase();
    let table = document.getElementById("studentsTable");
    let tr = table.getElementsByTagName("tr");

    // Loop through all table rows (except the header)
    for (let i = 1; i < tr.length; i++) {
        let td = tr[i].getElementsByTagName("td")[0]; // Roll No column
        if (td) {
            let txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = ""; // show row
            } else {
                tr[i].style.display = "none"; // hide row
            }
        }
    }
}
</script>
</body>
</html>
