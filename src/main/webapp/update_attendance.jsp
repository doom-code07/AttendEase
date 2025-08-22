<%@ page import="java.util.*, java.sql.*, model.*, dao.*" %>
<%
    String rollNo = request.getParameter("roll_no");
    String dateStr = request.getParameter("date");
    int teacherId = (Integer) session.getAttribute("teacherId");

    AttendanceModel attendance = null;
    List<SubjectModel> subjects = new ArrayList<>();

    if (rollNo != null && dateStr != null) {
        java.sql.Date date = java.sql.Date.valueOf(dateStr);
        AttendanceDAO adao = new AttendanceDAO();
        attendance = adao.getAttendanceByRollNoAndDate(rollNo, date);

        SubjectDAO sdao = new SubjectDAO();
        subjects = sdao.getSubjectsByTeacherId(teacherId);
    }
%>

<h2>Update Attendance</h2>

<!-- Step 1: Ask for roll number and date if not yet submitted -->
<% if (attendance == null) { %>
    <form method="get" action="update_attendance.jsp">
        <p>Roll Number: <input type="text" name="roll_no" required></p>
        <p>Date: <input type="date" name="date" required></p>
        <input type="submit" value="Fetch Attendance">
    </form>
<% } else { %>

<!-- Step 2: If attendance found, show the editable form -->
    <form action="UpdateAttendanceServlet" method="post">
        <input type="hidden" name="attendance_id" value="<%= attendance.getId() %>" />
        <input type="hidden" name="roll_no" value="<%= rollNo %>" />

        <p>Status:
            <select name="status">
                <option value="Present" <%= "Present".equals(attendance.getStatus()) ? "selected" : "" %>>Present</option>
                <option value="Absent" <%= "Absent".equals(attendance.getStatus()) ? "selected" : "" %>>Absent</option>
                <option value="Leave" <%= "Leave".equals(attendance.getStatus()) ? "selected" : "" %>>Leave</option>
                <option value="Struck Off" <%= "Struck Off".equals(attendance.getStatus()) ? "selected" : "" %>>Struck Off</option>
            </select>
        </p>

        <p>Subject:
            <select name="subject_id">
                <% for (SubjectModel s : subjects) { %>
                    <option value="<%= s.getId() %>" <%= s.getId() == attendance.getSubjectId() ? "selected" : "" %>>
                        <%= s.getTitle() %>
                    </option>
                <% } %>
            </select>
        </p>

        <p>Date:
            <input type="date" name="date" value="<%= attendance.getDate() %>" required>
        </p>

        <input type="submit" value="Update Attendance">
    </form>
<% } %>
