<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <!-- Font Awesome for icons -->
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
                    <a href="update_attendance.jsp">
                        <i class="fas fa-edit"></i> Update Attendance
                    </a>
                </li>
                <li>
                <li>
                     <a href="ViewAttendanceByClassServlet">
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
            <!-- Logout button -->
            <a href="index.jsp" class="logout_icon">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>

            <header>
                <h1>Welcome to the Teacher Dashboard</h1>
            </header>
            <p >Select an option from the menu to proceed.</p>



        </main>
    </div>
</body>
</html>
