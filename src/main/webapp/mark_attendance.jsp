<%@ page import="java.util.List" %>
<%@ page import="model.ClassModel, model.SubjectModel, model.StudentModel" %>
<%@ page import="dao.ClassDAO, dao.TeacherDAO, dao.SubjectDAO, dao.StudentDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="model.LeaveOnDay" %>

<%

    int teacherId = (int) session.getAttribute("teacherId");

    String selectedClassIdStr = request.getParameter("classId");
    int selectedClassId = selectedClassIdStr != null && !selectedClassIdStr.isEmpty() ? Integer.parseInt(selectedClassIdStr) : -1;

    ClassDAO classDAO = new ClassDAO();
    SubjectDAO subjectDAO = new SubjectDAO();
    StudentDAO studentDAO = new StudentDAO();

    List<ClassModel> classes = classDAO.getAllClasses();
    List<SubjectModel> subjects = new SubjectDAO().getSubjectsByTeacherId(teacherId);



    List<StudentModel> students = selectedClassId != -1 ? studentDAO.getStudentsByClassId(selectedClassId) : new java.util.ArrayList<>();
%>
 <%= "Teacher ID: " + teacherId %><br>
 <%= "Subjects fetched: " + subjects.size() %><br>

 <%
     // The servlet puts a List<LeaveOnDay> in request under "activeLeaves"
     List<LeaveOnDay> activeLeaves = (List<LeaveOnDay>) request.getAttribute("activeLeaves");
     if (activeLeaves == null) {
         activeLeaves = new ArrayList<>();
     }
 %>

<html>
<head>
    <title>Mark Attendance</title>
    <script>
        function validateDate() {
            var selectedDate = document.getElementById("date").value;
            var today = new Date().toISOString().split('T')[0];
            if (selectedDate < today) {
                alert("Cannot select a past date.");
                document.getElementById("date").value = today;
            }
        }
    </script>

     <style>
            /* keep it minimal; you said you already handle CSS */
            table { border-collapse: collapse; width: 100%; }
            th, td { border: 1px solid #ccc; padding: 8px; }
            th { text-align: left; }
            .panel { border: 1px solid #ddd; padding: 10px; margin-bottom: 16px; }
            .muted { color: #666; font-size: 0.9em; }
        </style>
</head>
<body>
    <h2>Mark Attendance</h2>

    <!-- Class selection form -->
    <form method="get" action="mark_attendance.jsp">
        <label for="classId">Class:</label>
        <select name="classId" id="classId" onchange="this.form.submit()" required>
            <option value="">Select Class</option>
            <% for (ClassModel cls : classes) { %>
                <option value="<%= cls.getId() %>" <%= (cls.getId() == selectedClassId) ? "selected" : "" %>><%= cls.getName() %></option>
            <% } %>
        </select>
    </form>
    <br>

    <% if (selectedClassId != -1) { %>
    <form method="post" action="MarkAttendanceServlet">
        <!-- Hidden input to send selected classId -->
        <input type="hidden" name="classId" value="<%= selectedClassId %>">

        <label for="subjectId">Subject:</label>
        <select name="subjectId" id="subjectId" required>
            <option value="">Select Subject</option>
            <% for (SubjectModel subj : subjects) { %>
                <option value="<%= subj.getId() %>"><%= subj.getTitle() %></option>
            <% } %>
        </select><br><br>

        <label for="date">Date:</label>
        <input type="date" id="date" name="date" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" onchange="validateDate()" required><br><br>

        <table border="1">
            <tr>
                <th>Roll No</th>
                <th>Name</th>
                <th>Status</th>
            </tr>
            <% for (StudentModel student : students) { %>
            <tr>
                <td><%= student.getRollNo() %></td>
                <td><%= student.getName() %></td>
                <td>
                    <select name="status_<%= student.getId() %>">
                        <option value="Present" selected>Present</option>
                        <option value="Absent">Absent</option>
                        <option value="Leave">Leave</option>
                        <option value="StruckOff">Struck Off</option>
                    </select>
                </td>
            </tr>
            <% } %>
        </table><br>

        <input type="submit" value="Submit Attendance">
    </form>

    <% } %>




<div class="panel">
    <h3>Students on Approved Leave (Today falls within their leave window)</h3>
    <p class="muted">
        Showing roll numbers and total leave days (inclusive of start & end dates).
        Students disappear automatically once their leave ends.
    </p>

    <%
        if (activeLeaves.isEmpty()) {
    %>
        <p>No approved leaves active today.</p>
    <%
        } else {
    %>
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Roll No</th>
                    <th>Student</th>
                    <th>Class</th>
                    <th>Leave Days</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                </tr>
            </thead>
            <tbody>
            <%
                int i = 1;
                for (LeaveOnDay item : activeLeaves) {
            %>
                <tr>
                    <td><%= i++ %></td>
                    <td><%= item.getRollNo() %></td>
                    <td><%= item.getStudentName() == null ? "" : item.getStudentName() %></td>
                    <td><%= item.getClassName() == null ? "" : item.getClassName() %></td>
                    <td><%= item.getLeaveDays() %></td>
                    <td><%= item.getStartDate() %></td>
                    <td><%= item.getEndDate() %></td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    <%
        }
    %>
</div>


</body>
</html>
