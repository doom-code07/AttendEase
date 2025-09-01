<%@ page import="java.util.List" %>
<%@ page import="model.TeacherModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Teachers</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link rel="stylesheet" href="css/floating_button.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Admin Portal</h2>
        <ul>
            <li><a href="ManageStudentServlet">
                <i class="fas fa-user-graduate"></i> Manage Students
            </a></li>
            <li><a href="ManageTeacherServlet" class="active">
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
            <h1>Registered Teachers</h1>
        </header>

        <!-- Delete all teachers button -->
        <form method="post" action="DeleteAllTeachersServlet"
              onsubmit="return confirm('Delete all teachers?');"
              style="float:right; margin-bottom:10px;">
            <button type="submit" class="delete-btn">Delete All</button>
        </form>

       <!-- Search box for teacher name -->
       <label>Search Teacher:</label>
       <input type="text" id="nameSearch" onkeyup="filterByName()" placeholder="Search by Teacher Name...">

       <!-- Teachers Table -->
       <table id="teachersTable">
            <tr>
                <th>Name</th>
                <th>CNIC</th>
                <th>Qualification</th>
                <th>Actions</th>
            </tr>
            <%
                List<TeacherModel> teachers = (List<TeacherModel>) request.getAttribute("teachers");
                if (teachers != null && !teachers.isEmpty()) {
                    for (TeacherModel t : teachers) {
            %>
            <tr>
                <td><%= t.getName() %></td>
                <td><%= t.getCnic() %></td>
                <td><%= t.getQualification() %></td>
                <td>
                    <a href="ViewTeacherServlet?id=<%= t.getId() %>" title="View">
                        <i class="fas fa-eye"></i>
                    </a>
                    |
                    <a href="UpdateTeacherServlet?id=<%= t.getId() %>" title="Update"
                        <i class="fas fa-pencil-alt"></i>
                    </a>
                    |
                    <a href="DeleteTeacherServlet?id=<%= t.getId() %>" title="Delete" onclick="return confirm('Are you sure?')" style="color: red;">
                        <i class="fas fa-trash"></i>
                    </a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="4">No teachers found.</td>
            </tr>
            <% } %>
        </table>
        <a href="add_teacher.jsp" class="floating-add-btn">+</a>
    </main>
</div>
<script>
function filterByName() {
    let input = document.getElementById("nameSearch");
    let filter = input.value.toUpperCase();
    let table = document.getElementById("teachersTable");
    let tr = table.getElementsByTagName("tr");

    for (let i = 1; i < tr.length; i++) { // start from row 1 (skip header)
        let td = tr[i].getElementsByTagName("td")[0]; // Name column
        if (td) {
            let txtValue = td.textContent || td.innerText;
            tr[i].style.display = txtValue.toUpperCase().indexOf(filter) > -1 ? "" : "none";
        }
    }
}
</script>
</body>
</html>
