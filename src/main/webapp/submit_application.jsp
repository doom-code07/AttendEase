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
<html>
<head><title>Submit Leave Application</title></head>
<body>
<h2>Submit Leave Application</h2>
<p>Name: <strong><%= student.getName() %></strong></p>
<p>Roll No: <strong><%= student.getRollNo() %></strong></p>
<p>Class/Batch: <strong><%= student.getBatch() %></strong></p>

<% if (request.getAttribute("error") != null) { %>
  <div style="color:red;"><%= request.getAttribute("error") %></div>
<% } %>
<% if (request.getAttribute("success") != null) { %>
  <div style="color:green;"><%= request.getAttribute("success") %></div>
<% } %>

<form action="SubmitApplicationServlet" method="post">
    <label>Start Date: <input type="date" name="start_date" value="<%= today %>" min="<%= today %>" required></label><br><br>
    <label>End Date: <input type="date" name="end_date" value="<%= today %>" min="<%= today %>" required></label><br><br>
    <label>Description (max 100 chars):<br>
      <textarea name="description" maxlength="100" rows="4" cols="50" required></textarea>
    </label><br><br>

    <label>Select Teacher (required for ≤3 days):<br>
      <select name="teacher_id">
          <option value="">-- Select Teacher --</option>
          <% for (Teacher t : teachers) { %>
            <option value="<%= t.getId() %>"><%= t.getName() %></option>
          <% } %>
      </select>
    </label><br><br>

    <button type="submit">Submit Application</button>
</form>

</body>
</html>
