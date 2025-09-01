<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.StudentModel" %>

<%
    List<StudentModel> struckList = (List<StudentModel>) request.getAttribute("struckList");
    Integer absentThreshold = (Integer) request.getAttribute("absentThreshold");
    String message = (String) session.getAttribute("message");
    String error = (String) session.getAttribute("error");
    if (message != null) {
        out.println("<div style='color:green;'>" + message + "</div>");
        session.removeAttribute("message");
    }
    if (error != null) {
        out.println("<div style='color:red;'>" + error + "</div>");
        session.removeAttribute("error");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Struck Off Students</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f2f2f2; }
        .btn { padding: 6px 10px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-meeting { background: #28a745; color: white; }
    </style>
</head>
<body>
<h2>Manage Struck Off Students</h2>
<p>Absent threshold (struck off after): <strong><%= absentThreshold %></strong></p>

<% if (struckList == null || struckList.isEmpty()) { %>
    <p>No students found matching struck-off criteria.</p>
<% } else { %>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Roll No</th>
            <th>Name</th>
            <th>Class</th>
            <th>Struck Off?</th>
            <th>Struck Off Date</th>
            <th>Total Fine</th>
            <th>Parent Meeting Done</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            int i = 1;
            for (StudentModel s : struckList) {
        %>
            <tr>
                <td><%= i++ %></td>
                <td><%= s.getRollNo() != null ? s.getRollNo() : "-" %></td>
                <td><%= s.getName() != null ? s.getName() : "-" %></td>
                <td><%= s.getClassName() != null ? s.getClassName() : "-" %></td>
                <td><%= s.isStruckOff() ? "Yes" : "No (Reached Threshold)" %></td>
                <td><%= s.getStruckOffDate() != null ? s.getStruckOffDate() : "-" %></td>
                <td><%= s.getTotalFine() %></td>
                <td><%= s.isParentMeetingDone() ? "Yes (" + (s.getMeetingDoneDate() != null ? s.getMeetingDoneDate() : "") + ")" : "No" %></td>
                <td>
                    <% if (!s.isParentMeetingDone()) { %>
                        <form action="ParentMeetingDoneServlet" method="post" style="display:inline;">
                            <input type="hidden" name="studentId" value="<%= s.getId() %>" />
                            <button type="submit" class="btn btn-meeting" onclick="return confirm('Mark parent meeting done for this student?');">Parent Meeting Done</button>
                        </form>
                    <% } else { %>
                        <span>Already Done</span>
                    <% } %>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
<% } %>

</body>
</html>
