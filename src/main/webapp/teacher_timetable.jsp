<%@ page import="java.util.*, model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
List<TimetableModel> list =
    (List<TimetableModel>) request.getAttribute("timetable");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Timetable</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>

<body>
<div class="container">
    <!-- Sidebar -->
        <aside class="sidebar">
         <h2>Teacher Portal</h2>
            <ul>
                <li><a href="TeacherTimetableServlet" class="active"><i class="fas fa-calendar-alt"></i> Timetable </a></li>
                <li><a href="ActiveApprovedLeavesServlet"><i class="fas fa-marker"></i> Mark Attendance</a></li>
                <li><a href="update_attendance.jsp"><i class="fas fa-edit"></i> Update Attendance</a></li>
                <li><a href="ViewAttendanceBySubjectServlet"><i class="fas fa-book"></i> View Attendance</a></li>
                <li><a href="TeacherApplicationsServlet"><i class="fas fa-envelope-open-text"></i> Applications</a></li>
            </ul>
        </aside>

    <!-- Main Content -->
    <main class="main-content">

        <!-- Back -->
        <a href="teacher_dashboard.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>My Weekly Timetable</h1>
        </header>

        <div>

        <% if(list != null && !list.isEmpty()) { %>

            <table>
                <thead>
                <tr>
                    <th>Day</th>
                    <th>Period</th>
                    <th>Class</th>
                    <th>Subject</th>
                    <th>Time</th>
                </tr>
                </thead>

                <tbody>

                <% for(TimetableModel timetable : list){ %>
                    <tr>
                        <td><%= timetable.getDay() %></td>
                        <td>Period <%= timetable.getPeriodNumber() %></td>
                        <td><%= timetable.getClassName() %></td>
                        <td><%= timetable.getSubjectName() %></td>
                        <td>
                            <%= timetable.getStartTime() %> -
                            <%= timetable.getEndTime() %>
                        </td>
                    </tr>

                <% } %>

                </tbody>
            </table>

        <% } else { %>

            <p style="margin-top:20px;">No timetable assigned yet.</p>

        <% } %>

        </div>

    </main>
</div>
</body>
</html>
