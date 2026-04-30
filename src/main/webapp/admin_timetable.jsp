<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="model.SubjectModel" %>
<%@ page import="model.TeacherModel" %>
<%@ page import="model.ClassModel" %>

<%
List<SubjectModel> subjects = (List<SubjectModel>) request.getAttribute("subjects");
List<TeacherModel> teachers = (List<TeacherModel>) request.getAttribute("teachers");
List<ClassModel> classes = (List<ClassModel>) request.getAttribute("classes");

String selectedClassId = request.getParameter("classId");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Timetable</title>

    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link rel="stylesheet" href="css/timetable.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">

</head>

<body>

<div class="container">

    <!-- SIDEBAR -->
    <aside class="sidebar">
        <h2>Admin Portal</h2>
        <ul>
            <li><a href="ManageStudentServlet"><i class="fas fa-user-graduate"></i> Manage Students</a></li>
            <li><a href="ManageTeacherServlet"><i class="fas fa-chalkboard-teacher"></i> Manage Teachers</a></li>
            <li><a href="manage_classes.jsp"><i class="fas fa-school"></i> Manage Classes</a></li>
            <li><a href="assign_subject.jsp"><i class="fas fa-book"></i> Assign Subjects</a></li>
            <li><a href="AddTimetableServlet" class="active"><i class="fas fa-clock"></i> Timetable</a></li>
            <li><a href="GenerateChallanPageServlet"><i class="fas fa-file-invoice-dollar"></i> Generate Challan</a></li>
        </ul>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="main-content">

        <a href="admin_dashboard.jsp" class="logout_icon">
            <i class="fas fa-arrow-left"></i> Back
        </a>

        <header>
            <h1>Manage Timetable</h1>
        </header>

        <!-- 🔥 CLASS SELECTION FORM (GET) -->
        <form method="get" action="AddTimetableServlet">
            <div class="form-group">
                <label>Select Class</label>
                <select name="classId" onchange="this.form.submit()" required>
                    <option value="">Select Class</option>
                    <% for(ClassModel c : classes){ %>
                        <option value="<%=c.getId()%>"
                            <%= (selectedClassId != null && selectedClassId.equals(String.valueOf(c.getId()))) ? "selected" : "" %>>
                            <%=c.getName()%>
                        </option>
                    <% } %>
                </select>
            </div>
        </form>

        <hr>

        <!-- 🔥 MAIN TIMETABLE FORM (POST) -->
        <form action="AddTimetableServlet" method="post" class="timetable-form" onsubmit="return validateForm()">

            <!-- Keep selected class -->
            <input type="hidden" name="classId" value="<%=selectedClassId != null ? selectedClassId : "" %>">

            <%
            String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
            %>

            <% for(String day : days){ %>

            <div class="day-box">

                <h2 class="day-title"><%=day%></h2>

                <% for(int i=1;i<=6;i++){ %>

                <div class="period-row">

                    <div class="period-label">Period <%=i%></div>

                    <!-- ✅ SUBJECT FILTERED -->
                    <select name="<%=day%>_subject_<%=i%>">
                        <option value="">Subject</option>
                        <% if(subjects != null){
                            for(SubjectModel s : subjects){ %>
                                <option value="<%=s.getId()%>"><%=s.getTitle()%></option>
                        <% }} %>
                    </select>

                    <!-- Teachers (unchanged) -->
                    <select name="<%=day%>_teacher_<%=i%>">
                        <option value="">Teacher</option>
                        <% for(TeacherModel t : teachers){ %>
                            <option value="<%=t.getId()%>"><%=t.getName()%></option>
                        <% } %>
                    </select>

                    <input type="time" name="<%=day%>_start_<%=i%>">
                    <input type="time" name="<%=day%>_end_<%=i%>">

                </div>

                <% } %>

            </div>

            <% } %>

            <button type="submit" class="save-btn">
                <i class="fas fa-save"></i> Save Timetable
            </button>

        <%
        String success = (String) request.getAttribute("success");
        String error = (String) request.getAttribute("error");
        %>

        <% if(success != null){ %>
            <div style="color: green; font-weight: bold;">
                <%= success %>
            </div>
        <% } %>

        <% if(error != null){ %>
            <div style="color: red; font-weight: bold;">
                <%= error %>
            </div>
        <% } %>

        </form>

    </main>

</div>

<script>
function validateForm() {

    let classId = document.querySelector("input[name='classId']").value;

    if (classId === "") {
        alert("Please select a class first");
        return false;
    }

    let valid = true;
    let rows = document.querySelectorAll(".period-row");

    rows.forEach(row => {
        let subject = row.querySelector("select[name*='_subject_']").value;
        let teacher = row.querySelector("select[name*='_teacher_']").value;
        let start = row.querySelector("input[name*='_start_']").value;
        let end = row.querySelector("input[name*='_end_']").value;

        if (subject !== "" || teacher !== "" || start !== "" || end !== "") {

            if (subject === "" || teacher === "" || start === "" || end === "") {
                alert("Please fill complete data for each selected period");
                valid = false;
            }

            if (start !== "" && end !== "") {
                if (start >= end) {
                    alert("Start time must be less than end time");
                    valid = false;
                }
            }
        }
    });

    return valid;
}
</script>

</body>
</html>
