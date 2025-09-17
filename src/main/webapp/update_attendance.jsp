<%@ page import="java.util.*, java.sql.*, model.*, dao.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Attendance</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
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
                <a href="update_attendance.jsp" class="active">
                    <i class="fas fa-edit"></i> Update Attendance
                </a>
            </li>
            <li>
                <a href="ViewAttendanceBySubjectServlet">
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
        <!-- Back -->
        <a href="teacher_dashboard.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>Update Attendance</h1>
        </header>

        <section class="content-section">
            <%
                String rollNo = request.getParameter("roll_no");
                String dateStr = request.getParameter("date");
                int teacherId = (Integer) session.getAttribute("teacherId");

                AttendanceModel attendance = null;
                List<SubjectModel> subjects = new ArrayList<>();

                if (rollNo != null && dateStr != null) {
                    java.sql.Date date = java.sql.Date.valueOf(dateStr);
                    AttendanceDAO adao = new AttendanceDAO();
                    attendance = adao.getAttendanceByRollNoAndDate(rollNo, date);

                    SubjectDAO sdao = new SubjectDAO();
                    subjects = sdao.getSubjectsByTeacherId(teacherId);
                }
            %>

            <% if (attendance == null) { %>

                <form method="get" action="update_attendance.jsp" class="form-box">
                    <label>Roll Number:</label>
                        <input type="text" name="roll_no" pattern="[0-9]+"
                                                                 title="Only digits are allowed"  required>

                    <label>Date: </label>
                        <input type="date" name="date" required>

                    <button type="submit">Fetch Attendance</button>
                </form>
            <% } else { %>


                <form action="UpdateAttendanceServlet" method="post" class="form-box">
                    <input type="hidden" name="attendance_id" value="<%= attendance.getId() %>" />
                    <input type="hidden" name="roll_no" value="<%= rollNo %>" />

                    <label>Status:</label>
                        <select name="status">
                            <option value="Present" <%= "Present".equals(attendance.getStatus()) ? "selected" : "" %>>Present</option>
                            <option value="Absent" <%= "Absent".equals(attendance.getStatus()) ? "selected" : "" %>>Absent</option>
                            <option value="Leave" <%= "Leave".equals(attendance.getStatus()) ? "selected" : "" %>>Leave</option>
                            <option value="Struck Off" <%= "Struck Off".equals(attendance.getStatus()) ? "selected" : "" %>>Struck Off</option>
                        </select>

                    <label>Subject:</label>
                        <select name="subject_id">
                            <% for (SubjectModel s : subjects) { %>
                                <option value="<%= s.getId() %>" <%= s.getId() == attendance.getSubjectId() ? "selected" : "" %>>
                                    <%= s.getTitle() %>
                                </option>
                            <% } %>
                        </select>

                    <label>Date:</label>
                        <input type="date" name="date" value="<%= attendance.getDate() %>" required>

                    <button type="submit">Update Attendance</button>
                </form>
            <% } %>
        </section>
    </main>
</div>
</body>
</html>
