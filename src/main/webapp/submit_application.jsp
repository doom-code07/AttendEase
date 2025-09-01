<%@ page import="java.time.LocalDate, java.sql.Date, dao.TeacherDAO, model.Teacher, dao.StudentDAO, model.Student" %>
<%
    // Ensure user logged in
    Integer userId = (Integer) session.getAttribute("userId");
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Student student = StudentDAO.findByUsersId(userId);
    if (student == null) {
        out.println("<p>No student record found.</p>");
        return;
    }
    java.util.List<Teacher> teachers = TeacherDAO.listAllTeachers();
    String today = LocalDate.now().toString();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Leave Application</title>
    <link rel="stylesheet" href="css/submit_application_form.css">
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
            <li><a href="submit_application.jsp" class="active">
                <i class="fas fa-file-alt"></i> Submit Application
            </a></li>
            <li><a href="check_status.jsp">
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
            <h1>Submit Leave Application</h1>
        </header>

        <main class="application-container">
            <section class="application-student-info">
                <p><b>Name:</b> <%= student.getName() %></p>
                <p><b>Roll No:</b> <%= student.getRollNo() %></p>
                <p><b>Class/Batch:</b> <%= student.getBatch() %></p>
            </section>

            <!-- Success/Error Messages -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="application-alert error"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getAttribute("success") != null) { %>
                <div class="application-alert success"><%= request.getAttribute("success") %></div>
            <% } %>

            <form action="SubmitApplicationServlet" method="post" class="application-form">

                <div class="date-row">
                    <div class="date-field">
                        <label>Start Date:</label>
                        <input type="date" name="start_date" value="<%= today %>" min="<%= today %>" required>
                    </div>

                    <div class="date-field">
                        <label>End Date:</label>
                        <input type="date" name="end_date" value="<%= today %>" min="<%= today %>" required>
                    </div>
                </div>

                <label>Description (max 100 chars):</label>
                <textarea name="description" maxlength="100" rows="4" pattern="[A-Za-z ]+"
                                                                             title="Only alphabets and spaces are allowed" required></textarea>

                <label>Select Teacher (required for <=3 days):</label>
                <select name="teacher_id" required>
                    <option value="">-- Select Teacher --</option>
                    <% for (Teacher t : teachers) { %>
                        <option value="<%= t.getId() %>"><%= t.getName() %></option>
                    <% } %>
                </select>

                <button type="submit" class="application-btn">Submit Application</button>
            </form>

        </main>
</body>
</html>
