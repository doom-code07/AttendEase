<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Admin Portal</h2>
        <ul>
            <li>
                 <a href="ManageStudentServlet">
                      <i class="fas fa-user-graduate"></i> Manage Students
                 </a>
            </li>
            <li>
                <a href="ManageTeacherServlet">
                    <i class="fas fa-chalkboard-teacher"></i> Manage Teachers
                </a>
            </li>
            <li>
                <a href="assign_subject.jsp">
                    <i class="fas fa-book"></i> Assign Subjects
                </a>
            </li>
            <li>
                <a href="GenerateChallanPageServlet">
                    <i class="fas fa-file-invoice-dollar"></i> Generate Challan
                </a>
            </li>
            <li>
                <a href="manage_classes.jsp">
                    <i class="fas fa-school"></i> Manage Classes
                </a>
            </li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <a href="index.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>

        <header>
            <h1>Welcome Admin</h1>
        </header>


    </main>
</div>
</body>
</html>