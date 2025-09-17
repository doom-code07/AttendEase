<%@ page import="model.ClassModel, model.SubjectModel" %>
<%@ page import="dao.ClassDAO, dao.SubjectDAO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Classes</title>
    <link rel="stylesheet" href="css/dashboard_styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Admin Portal</h2>
        <ul>
            <li><a href="ManageStudentServlet">
                <i class="fas fa-user-graduate"></i> Manage Students
            </a></li>
            <li><a href="ManageTeacherServlet">
                <i class="fas fa-chalkboard-teacher"></i> Manage Teachers
            </a></li>
            <li><a href="assign_subject.jsp">
                <i class="fas fa-book"></i> Assign Subjects
            </a></li>
            <li><a href="GenerateChallanPageServlet">
                <i class="fas fa-file-invoice-dollar"></i> Generate Challan
            </a></li>
            <li><a href="manage_classes.jsp" class="active">
                <i class="fas fa-school"></i> Manage Classes
            </a></li>
        </ul>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <a href="admin_dashboard.jsp" class="logout_icon">
           <i class="fas fa-sign-out-alt"></i> Back
        </a>

        <header>
            <h1>Manage Classes</h1>
        </header>

        <!-- Add Class Form -->
        <section class="form-section">
            <h2>Add a Class</h2>

            <%
                String success = (String) session.getAttribute("success");
                String duplicate = (String) session.getAttribute("duplicate");

                if (success != null) {
            %>
                <p style="color:green;"><%= success %></p>
            <%
                    session.removeAttribute("success"); // clear message after showing
                }

                if (duplicate != null) {
            %>
                <p style="color:red;"><%= duplicate %></p>
            <%
                    session.removeAttribute("duplicate"); // clear message after showing
                }
            %>

               <form action="AddClassServlet" method="post">
                   <input type="text" name="className" placeholder="Enter Class Name" pattern="[A-Za-z ]+"
                                                                                             title="Only alphabets and spaces are allowed" required>
                    <button type="submit">Add Class</button>
               </form>
                    <label>Search Class:</label>
                    <input type="text" id="classSearch" placeholder="Search classes by name..."
                           onkeyup="filterClasses()" pattern="[A-Za-z ]+"
                                                            title="Only alphabets and spaces are allowed" />
        </section>

                <hr style="margin:20px 0;">
        <section>
             <h2>All Classes</h2>

             <%
                 String successMsg = (String) session.getAttribute("success");
                 String duplicateMsg = (String) session.getAttribute("duplicate");
                 String errorMsg = (String) session.getAttribute("error");
             %>

             <% if (successMsg != null) { %>
                 <p style="color:green;"><%= successMsg %></p>
                 <%
                     session.removeAttribute("success");
                 %>
             <% } %>

             <% if (duplicateMsg != null) { %>
                 <p style="color:red;"><%= duplicateMsg %></p>
                 <%
                     session.removeAttribute("duplicate");
                 %>
             <% } %>

             <% if (errorMsg != null) { %>
                 <p style="color:orange;"><%= errorMsg %></p>
                 <%
                     session.removeAttribute("error");
                 %>
             <% } %>




                <div class="class-container">
                 <%
                   ClassDAO classDAO = new ClassDAO();
                   SubjectDAO subjectDAO = new SubjectDAO();
                   java.util.List<ClassModel> classes = classDAO.getAllClasses();

                   for (ClassModel cls : classes) {
                   %>
                   <div class="class-block">
            <!-- Delete Class -->
             <form action="DeleteClassServlet" method="post" style="display:inline;">
                  <input type="hidden" name="classId" value="<%= cls.getId() %>">
                  <button type="submit" class="delete-btn" onclick="return confirm('Delete this class?')">Delete Class</button>
             </form>
                   <strong style="margin-left:10px;">Class: <%= cls.getName() %></strong>

             <!-- Add Subject Form -->
                 <form action="AddSubjectServlet" method="post" style="margin-top:15px;">
                      <input type="hidden" name="classId"  value="<%= cls.getId() %>">
                      <label>Subject Title</label>
                      <input type="text" name="subjectTitle" placeholder="Subject Title" pattern="[A-Za-z ]+"
                                                                                                title="Only alphabets and spaces are allowed" required><br>
                      <label>Subject Code</label>
                      <input type="text" name="subjectCode" maxlength="5" placeholder="Subject Code" pattern="[0-9]+"
                                                                                              title="Only digits are allowed" required>
                      <button type="submit">Add Subject</button>
                 </form>

             <!-- Subjects List -->
                 <div style="margin-top:10px;">
                      <strong>Subjects:</strong>
                         <ul class="subject-list">
                 <%
                    java.util.List<SubjectModel> subjects = subjectDAO.getSubjectsByClassId(cls.getId());
                    for (SubjectModel sub : subjects) {
                 %>
                 <li>
                     <%= sub.getTitle() %> (<%= sub.getCode() %>)
                     <form action="DeleteSubjectServlet" method="post" style="display:inline;">
                      <input type="hidden" name="subjectId" value="<%= sub.getId() %>">
                      <button type="submit" class="delete-btn" onclick="return confirm('Delete this subject?')">Delete</button>
                     </form>
                 </li>
                 <%
                      }
                  %>
                 </ul>
                      </div>
                      </div>
                 <%
                       }
                 %>
                    </div>
        </section>

    </main>
</div>
<script>
function filterClasses() {
    const input = document.getElementById("classSearch").value.toLowerCase();
    const classBlocks = document.querySelectorAll(".class-block");

    classBlocks.forEach(block => {
        // get the class name text inside <strong>
        const classNameElement = block.querySelector("strong");
        const className = classNameElement ? classNameElement.textContent.toLowerCase() : "";

        if (className.includes(input)) {
            block.style.display = "block"; // show if matches
        } else {
            block.style.display = "none";  // hide if not matches
        }
    });
}
</script>
</body>
</html>
