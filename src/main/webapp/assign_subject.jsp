<%@ page import="java.util.*, dao.TeacherDAO, dao.SubjectDAO, model.TeacherModel, model.SubjectModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Assign Subjects to Teacher</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
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
            <li><a href="ManageTeacherServlet">
                <i class="fas fa-chalkboard-teacher"></i> Manage Teachers
            </a></li>
            <li><a href="assign_subject.jsp" class="active">
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
            <h1>Assign Subjects to Teacher</h1>
        </header>

<!-- Form to Assign Subjects -->
<section class="form-section">
    <form action="AssignSubjectServlet" method="post">

        <% if (request.getParameter("success") != null) { %>
            <p style="color:green;"><%= request.getParameter("success") %></p>
        <% } %>

        <% if (request.getParameter("info") != null) { %>
            <p style="color:orange;"><%= request.getParameter("info") %></p>
        <% } %>

        <% if (request.getParameter("error") != null) { %>
            <p style="color:red;"><%= request.getParameter("error") %></p>
        <% } %>

        <label for="teacher">Select Teacher:</label>
        <select name="teacherId" id="teacher" required>
            <%
                List<TeacherModel> teachers = new TeacherDAO().getAllTeachers();
                for (TeacherModel teacher : teachers) {
            %>
                <option value="<%= teacher.getId() %>"><%= teacher.getName() %></option>
            <%
                }
            %>
        </select>


                <h3 style="margin-top:15px;">Select Subjects:</h3>
                <div style="margin-left:10px;">
                    <%
                        List<SubjectModel> subjects = new SubjectDAO().getAllSubjects();
                        for (SubjectModel subject : subjects) {
                    %>
                        <label>
                            <input type="checkbox" name="subjectIds" value="<%= subject.getId() %>"/>
                            <%= subject.getTitle() %> (<%= subject.getCode() %>)
                        </label><br/>
                    <%
                        }
                    %>
                </div>

                <button type="submit" style="margin-top:10px;">Assign Subjects</button>
            </form>
        </section>

        <hr style="margin:20px 0;">

        <!-- Assigned Subjects Table -->
        <section>
            <h2>Assigned Subjects</h2><br>
                <label>Search Subject:</label>
                <input type="text" id="subjectSearch" placeholder="Search subjects by name or code..." onkeyup="filterSubjects()"/>

             <table id="subjectList">
                <tr>
                    <th>Teacher Name</th>
                    <th>CNIC</th>
                    <th>Subject</th>
                    <th>Action</th>
                </tr>
                <%
                    List<Map<String, String>> assigned = new SubjectDAO().getAssignedSubjects();
                    for (Map<String, String> row : assigned) {
                %>
                <tr>
                    <td><%= row.get("name") %></td>
                    <td><%= row.get("cnic") %></td>
                    <td><%= row.get("title") %>(<%= row.get("code") %>)</td>
                    <td>
                        <form action="UnassignSubjectServlet" method="post" style="display:inline;">
                            <input type="hidden" name="subjectId" value="<%= row.get("subject_id") %>"/>
                            <button type="submit" class="delete-btn" onclick="return confirm('Unassign this subject?')">Delete</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>

            <!-- Delete All Assigned Subjects -->
            <form action="UnassignAllSubjectsServlet" method="post" style="margin-top:20px;">
                <button type="submit" onclick="return confirm('Delete all assigned subjects?')">Delete All Assigned Subjects</button>
            </form>
        </section>
    </main>
</div>
<script>
function filterSubjects() {
    const input = document.getElementById("subjectSearch").value.toLowerCase();
    const rows = document.querySelectorAll("#subjectList tr");

    rows.forEach((row, index) => {
        if (index === 0) return; // skip header row

        const text = row.textContent.toLowerCase();
        if (text.includes(input)) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}
</script>
</body>
</html>
