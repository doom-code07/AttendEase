<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vice Principal Dashboard</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Vice Principal</h2>
        <ul>
            <li><a href="ManageStruckOffServlet">
                <i class="fas fa-user-times"></i> Manage Struck Off
            </a></li>
            <li><a href="set_policies.jsp">
                <i class="fas fa-cogs"></i> Set Policies
            </a></li>
            <li><a href="ViceApplicationsServlet">
                <i class="fas fa-envelope-open-text"></i> Manage Applications
            </a></li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <a href="index.jsp" class="logout_icon">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>

        <header>
            <h1>Welcome Vice Principal</h1>
        </header>


    </main>
</div>
</body>
</html>
