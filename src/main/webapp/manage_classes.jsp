<%@ page import="model.ClassModel, model.SubjectModel" %>
<%@ page import="dao.ClassDAO, dao.SubjectDAO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Classes</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        .class-block {
            border: 1px solid #aaa;
            padding: 15px;
            margin-bottom: 20px;
            background-color: #f9f9f9;
        }
        .subject-list {
            list-style-type: none;
            padding-left: 0;
        }
        .subject-list li {
            margin: 5px 0;
        }
    </style>
</head>
<body>

<h2>Add a Class</h2>
<form action="AddClassServlet" method="post">
    <input type="text" name="className" placeholder="Enter Class Name" required>
    <button type="submit">Add Class</button>
</form>

<br><hr><br>

<h2>All Classes</h2>

<%
    ClassDAO classDAO = new ClassDAO();
    SubjectDAO subjectDAO = new SubjectDAO();
    java.util.List<ClassModel> classes = classDAO.getAllClasses();

    for (ClassModel cls : classes) {
%>
    <div class="class-block">
        <form action="DeleteClassServlet" method="post" style="display:inline;">
            <input type="hidden" name="classId" value="<%= cls.getId() %>">
            <button type="submit">Delete Class</button>
        </form>
        <strong style="margin-left: 10px;">Class: <%= cls.getName() %></strong>

        <br><br>
        <!-- Subject form -->
        <form action="AddSubjectServlet" method="post">
            <input type="hidden" name="classId" value="<%= cls.getId() %>">
            <input type="text" name="subjectTitle" placeholder="Subject Title" required>
            <input type="text" name="subjectCode" placeholder="Subject Code" required>
            <button type="submit">Add Subject</button>
        </form>

        <br>
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
                        <button type="submit">Delete</button>
                    </form>
                </li>
            <%
                }
            %>
        </ul>
    </div>
<%
    }
%>

</body>
</html>
