<%@ page import="java.util.*" %>
<%@ page import="dao.ClassDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Generate Challan</title>
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
            <li><a href="assign_subject.jsp" >
                <i class="fas fa-book"></i> Assign Subjects
            </a></li>
                <li><a href="GenerateChallanPageServlet" class="active">
                <i class="fas fa-file-invoice-dollar"></i> Generate Challan
            </a></li>
            <li><a href="manage_classes.jsp">
                <i class="fas fa-school"></i> Manage Classes
            </a></li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <a href="admin_dashboard.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>Generate Challan</h1>
            <p class="note">Downloadable PDF will be generated and saved by your browser.</p>
        </header>

        <div class="card">
            <!-- 1) Single Student by Roll Number -->
            <fieldset>
                <label>Generate for One Student</label>
                <form action="GenerateChallanServlet" method="post">
                    <input type="hidden" name="mode" value="single"/>
                    <label>Roll Number</label>
                    <input type="text" name="roll_no" required
                           pattern="\d{3,6}" title="Enter 3 to 6 digit roll number" />

                    <br/><br/>
                    <button class="btn" type="submit">Generate Challan (Single)</button>
                </form>
            </fieldset>

            <!-- 2) Whole Class -->
            <fieldset>
                <label>Generate for Whole Class</label>
                <form action="GenerateChallanServlet" method="post">
                    <input type="hidden" name="mode" value="class"/>
                    <label>Select Class</label>
                    <select name="class_id" required>
                        <%
                            List<model.ClassModel> classes = (List<model.ClassModel>) request.getAttribute("classes");
                            if (classes != null) {
                                for (model.ClassModel c : classes) {
                        %>
                                    <option value="<%= c.getId() %>"><%= c.getName() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <br/><br/>
                    <button class="btn" type="submit">Generate Challans (Whole Class)</button>
                </form>
            </fieldset>
        </div>
    </main>
</div>
</body>
</html>
