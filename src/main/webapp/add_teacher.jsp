<!DOCTYPE html>
<html>
<head>
    <title>Add Teacher</title>
</head>
<body>
    <h2>Add Teacher</h2>
    <form action="AddTeacherServlet" method="post">
        Name: <input type="text" name="name" required><br><br>
        Username: <input type="text" name="username" required><br><br>
       Password: <input type="password" name="password" required pattern="(?=.*\d).{8,}" title="Must be at least 8 characters and contain at least one digit"><br><br>
        Email: <input type="email" name="email" required><br><br>
        CNIC: <input type="text" name="cnic" required pattern="\d{13}" title="Enter 13 digit CNIC without dashes"><br><br>

        Qualification: <input type="text" name="qualification" required><br><br>
        <input type="submit" value="Add Teacher">
    </form>
</body>
</html>
