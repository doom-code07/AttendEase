<%@ page import="model.StudentModel" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ClassModel" %>


<%
    StudentModel s = (StudentModel) request.getAttribute("student");
    List<ClassModel> classes = (List<ClassModel>) request.getAttribute("classList");
%>




<html>
<head><title>Update Student</title></head>
<body>
    <h2>Update Student</h2>
    <form action="SubmitUpdateStudentServlet" method="post">
        <input type="hidden" name="id" value="<%= s.getId() %>"/>
        Name: <input type="text" name="name" value="<%= s.getName() %>"/><br>
        Username: <input type="text" name="username" value="<%= s.getUsername() %>"/><br>
        Email: <input type="email" name="email" value="<%= s.getEmail() %>"/><br>
        CNIC: <input type="text" name="cnic" value="<%= s.getCnic() %>"/><br>
        Roll No: <input type="text" name="rollno" value="<%= s.getRollNo() %>"/><br>
        Batch: <input type="text" name="batch" value="<%= s.getBatch() %>"/><br>
        Class:
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
        </select><br>

        <button type="submit">Update</button>
    </form>

</body>
</html>
