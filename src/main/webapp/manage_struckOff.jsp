<%@ page import="java.util.*, model.StudentModel" %>

<table border="1">
    <tr>
        <th>Roll No</th>
        <th>Batch</th>
        <th>Total Fine</th>
        <th>Struck Off Date</th>
        <th>Parent Meeting</th>
        <th>Action</th>
    </tr>
    <%
        List<StudentModel> struckStudents = (List<StudentModel>) request.getAttribute("struckStudents");
        for (StudentModel student : struckStudents) {
    %>
    <tr>
        <td><%= student.getRollNo() %></td>
        <td><%= student.getBatch() %></td>
        <td><%= student.getTotalFine() %></td>
        <td><%= student.getStruckOffDate() %></td>
        <td><%= student.isParentMeetingDone() ? "Done" : "Pending" %></td>
        <td>
            <form action="MarkParentMeetingDoneServlet" method="post">
                <input type="hidden" name="studentId" value="<%= student.getId() %>" />
                <button type="submit">Parent Meeting Done</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>
