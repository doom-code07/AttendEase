<!DOCTYPE html>
<html>
<head>
    <title>Add Teacher</title>
    <link rel="stylesheet" href="css/details.css">
</head>
<body>
    <div class="container">
        <h2>Add Teacher</h2>
        <form action="AddTeacherServlet" method="post">

            <label>Name:</label>
            <input type="text" name="name"  pattern="[A-Za-z ]+"
                                                  title="Only alphabets and spaces are allowed" required>

            <label>Username:</label>
            <input type="text" name="username" required>

            <label>Password:</label>
            <input type="password" name="password"
            placeholder ="Enter atleast one capital letter,digit and special character"
                   pattern="^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\-={}\[\]:;'<>.,?/~`]).{8,}$"
                          title="Password must be at least 8 characters long, contain one uppercase letter, one digit, and one special character."

                          required>

            <label>Email:</label>
            <input type="email" name="email" required>

            <label>CNIC:</label>
            <input type="text" name="cnic"
            placeholder = " Enter 13 digits without dashes"
                   required
                   pattern="\d{13}"
                   title="Enter 13 digit CNIC without dashes">

            <label>Qualification:</label>
            <input type="text" name="qualification"  pattern="[A-Za-z ]+"
                                                           title="Only alphabets and spaces are allowed" required>

            <button type="submit" >Add Teacher</button>
        </form>
    </div>
</body>
</html>
