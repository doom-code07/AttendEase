<%@ page import="java.util.List" %>
<%@ page import="model.ClassModel, model.SubjectModel, model.StudentModel" %>
<%@ page import="dao.ClassDAO, dao.TeacherDAO, dao.SubjectDAO, dao.StudentDAO" %>
<%@ page import="java.util.*" %>
<%@ page import="model.LeaveOnDay" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Mark Attendance</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
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
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Teacher Portal</h2>
        <ul>
            <li>
                <a href="ActiveApprovedLeavesServlet" class="active">
                    <i class="fas fa-marker"></i> Mark Attendance
                </a>
            </li>
            <li>
                <a href="update_attendance.jsp">
                    <i class="fas fa-edit"></i> Update Attendance
                </a>
            </li>
            <li>
                 <a href="ViewAttendanceByClassServlet">
                    <i class="fas fa-book"></i> View Attendance
                 </a>
            </li>
            <li>
                <a href="TeacherApplicationsServlet">
                    <i class="fas fa-envelope-open-text"></i> Applications
                </a>
            </li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Back -->
         <a href="teacher_dashboard.jsp" class="logout_icon">
              <i class="fas fa-sign-out-alt"></i> Back
         </a>

        <header>
            <h1>Mark Attendance</h1>
        </header>

        <section class="content-section">
            <%
                int teacherId = (int) session.getAttribute("teacherId");

                String selectedClassIdStr = request.getParameter("classId");
                int selectedClassId = selectedClassIdStr != null && !selectedClassIdStr.isEmpty()
                        ? Integer.parseInt(selectedClassIdStr) : -1;

                ClassDAO classDAO = new ClassDAO();
                SubjectDAO subjectDAO = new SubjectDAO();
                StudentDAO studentDAO = new StudentDAO();


                List<ClassModel> classes = classDAO.getAllClasses();
                List<SubjectModel> subjects = subjectDAO.getSubjectsByTeacherId(teacherId);
                List<StudentModel> students = selectedClassId != -1
                        ? studentDAO.getStudentsByClassId(selectedClassId)
                        : new ArrayList<>();
                 List<StudentModel> struckOffStudents = (List<StudentModel>) request.getAttribute("struckOffStudents");
                // Active leaves from servlet
                List<LeaveOnDay> activeLeaves = (List<LeaveOnDay>) request.getAttribute("activeLeaves");
                if (activeLeaves == null) {
                    activeLeaves = new ArrayList<>();
                }
            %>

            <!-- Class selection form -->
            <form method="get" action="ActiveApprovedLeavesServlet" class="form-box">
                <label for="classId">Class:</label>
                <select name="classId" id="classId" onchange="this.form.submit()" required>
                    <option value="">Select Class</option>
                    <% for (ClassModel cls : classes) { %>
                        <option value="<%= cls.getId() %>" <%= (cls.getId() == selectedClassId) ? "selected" : "" %>>
                            <%= cls.getName() %>
                        </option>
                    <% } %>
                </select>
            </form><br>


            <% String error = (String) request.getAttribute("error");
               if (error != null) { %>
               <p style="color:red;"><%= error %></p>
            <% } %>





<h3>Struck Off Students</h3>
<% if (struckOffStudents == null || struckOffStudents.isEmpty()) { %>
    <p>No students are currently struck off.</p>
<% } else { %>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>#</th>
            <th>Roll No</th>
            <th>Name</th>
            <th>Class</th>
        </tr>
        </thead>
        <tbody>
        <%
            int i = 1;
            for (StudentModel s : struckOffStudents) {
        %>
        <tr>
            <td><%= i++ %></td>
            <td><%= s.getRollNo() %></td>
            <td><%= s.getName() %></td>
            <td><%= s.getClassName() %></td>
        </tr>
        <% } %>
        </tbody>
    </table>
<% } %>














            <% if (selectedClassId != -1) { %>
                <!-- Attendance form -->
                <form method="post" action="MarkAttendanceServlet" class="form-box">
                    <input type="hidden" name="classId" value="<%= selectedClassId %>">

                    <label for="subjectId">Subject:</label>
                    <select name="subjectId" id="subjectId" required>
                        <option value="">Select Subject</option>
                        <% for (SubjectModel subj : subjects) { %>
                            <option value="<%= subj.getId() %>"><%= subj.getTitle() %></option>
                        <% } %>
                    </select><br><br>

                    <label for="date">Date:</label>
                    <input type="date" id="date" name="date"
                           value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"
                           onchange="validateDate()" required><br><br>

                    <table>
                        <thead>
                            <tr>
                                <th>Roll No</th>
                                <th>Name</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% for (StudentModel student : students) { %>
                            <tr>
                                <td><%= student.getRollNo() %></td>
                                <td><%= student.getName() %></td>
                                <td>
                                    <select name="status_<%= student.getId() %>">
                                        <option value="Present" selected>Present</option>
                                        <option value="Absent">Absent</option>
                                        <option value="Leave">Leave</option>
                                        <option value="Struck Off">Struck Off</option>
                                    </select>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table><br>

                    <button type="submit">Submit Attendance</button>
                </form>
            <% } %>

            <!-- Active leave panel -->
            <div class="panel">
                <h3>Students on Approved Leave</h3>
                <p class="muted">
                    Showing roll numbers and total leave days (inclusive of start & end dates).<br>
                    Students disappear automatically once their leave ends.
                </p>

                <% if (activeLeaves.isEmpty()) { %>
                    <p>No approved leaves active today.</p>
                <% } else { %>
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
                        <% } %>
                        </tbody>
                    </table>
                <% } %>
            </div>
        </section>
    </main>
</div>
</body>
</html>
