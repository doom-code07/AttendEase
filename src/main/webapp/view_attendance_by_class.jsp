<%@ page import="java.util.*, model.AttendanceViewModel, model.ClassModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Attendance by Class</title>
    <style>
        table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }
        table { width: 100%; margin-top: 20px; }
    </style>
</head>
<body>

<h2>View Attendance by Class</h2>

<form action="ViewAttendanceByClassServlet" method="post">
    <label>Select Class:</label>
    <select name="class_id" required>
        <option value="">-- Select Class --</option>
        <%
            List<ClassModel> classList = (List<ClassModel>) request.getAttribute("classList");
            String selectedClassIdStr = request.getParameter("class_id");
            int selectedClassId = -1;
            if (selectedClassIdStr != null && !selectedClassIdStr.isEmpty()) {
                selectedClassId = Integer.parseInt(selectedClassIdStr);
            }

            if (classList != null) {
                for (ClassModel cls : classList) {
                    boolean isSelected = (cls.getId() == selectedClassId);
        %>
            <option value="<%= cls.getId() %>" <%= isSelected ? "selected" : "" %>><%= cls.getName() %></option>
        <%
                }
            }
        %>
    </select>
    <input type="submit" value="View Attendance">
</form>

<%
    List<AttendanceViewModel> attendanceList = (List<AttendanceViewModel>) request.getAttribute("attendanceList");
    if (attendanceList != null && !attendanceList.isEmpty()) {
%>
    <h3>Attendance Records</h3>
    <table>
        <tr>
            <th>Date</th>
            <th>Roll No</th>
            <th>Name</th>
            <th>Subject</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <% for (AttendanceViewModel att : attendanceList) { %>
        <tr>
            <td><%= att.getDate() %></td>
            <td><%= att.getStudent().getRollNo() %></td>
            <td><%= att.getStudent().getName() %></td>
            <td><%= att.getSubject().getTitle() %> (<%= att.getSubject().getCode() %>)</td>
            <td><%= att.getStatus() %></td>
            <td>
                        <form action="DeleteAttendanceServlet" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= att.getId() %>">
                            <input type="hidden" name="class_id" value="<%= request.getParameter("class_id") %>">
                            <input type="hidden" name="action" value="deleteSingle">
                            <input type="submit" value="Delete">
                        </form>
            </td>
        </tr>
        <% } %>
    </table>


    <form action="DeleteAttendanceServlet" method="post" style="margin-top: 20px;">
        <input type="hidden" name="class_id" value="<%= request.getParameter("class_id") %>">
        <input type="hidden" name="action" value="deleteAll">
        <input type="submit" value="Delete All Attendance Records" onclick="return confirm('Are you sure you want to delete all records for this class?');">
    </form>

<% } else if (request.getAttribute("classId") != null) { %>
    <p>No attendance records found for this class.</p>
<% } %>

</body>
</html>
