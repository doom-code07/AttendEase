<%@ page import="model.StudentModel" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ClassModel" %>


<%
    StudentModel s = (StudentModel) request.getAttribute("student");
    List<ClassModel> classes = (List<ClassModel>) request.getAttribute("classList");
%>
<html>
<head>
    <title>Update Student</title>
    <link rel="stylesheet" href="css/details.css">
</head>
<body>
    <div class="container">
    <a href="ManageStudentServlet" class="btn">Back</a>
        <h2>Update Student</h2>
        <form action="SubmitUpdateStudentServlet" method="post">
            <input type="hidden" name="id" value="<%= s.getId() %>"/>

            <label>Name:</label>
            <input type="text" name="name" required pattern="[A-Za-z ]+"
                                                  title="Only alphabets and spaces are allowed" value="<%= s.getName() %>"/>

            <label>Username:</label>
            <input type="text" name="username" value="<%= s.getUsername() %>" required/>

            <label>Email:</label>
            <input type="email" name="email" value="<%= s.getEmail() %>" required/>

            <label>CNIC:</label>
            <input type="text" name="cnic" pattern="\d{13}" title="Enter 13-digit CNIC" value="<%= s.getCnic() %>" required/>

            <label>Roll No:</label>
            <input type="text" name="rollno"
                    pattern="\d{3,6}" title="Enter 3 to 6 digit roll number" value="<%= s.getRollNo() %>" required/>

            <label>Batch:</label>
            <input type="text" name="batch" pattern="[0-9]+"
                                                   title="Only digits are allowed"  value="<%= s.getBatch() %>" required/>

            <label>Class:</label>
            <select name="classId">
                <%
                    if (classes != null) {
                        for (ClassModel c : classes) {
                            String selected = (c.getId() == s.getClassId()) ? "selected" : "";
                %>
                    <option value="<%= c.getId() %>" <%= selected %>><%= c.getName() %></option>
                <%
                        }
                    }
                %>
            </select>

            <button type="submit">Update</button>
        </form>
    </div>
</body>
</html>
