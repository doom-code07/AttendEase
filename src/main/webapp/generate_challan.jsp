<%@ page import="java.util.*" %>
<%@ page import="dao.ClassDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Generate Challan</title>
    <style>
        body { font-family: Arial, sans-serif; background:#0b0b0b; color:#eee; }
        .container { max-width: 900px; margin: 30px auto; background:#1a1a1a; padding: 20px; border-radius: 12px; }
        h1 { margin-top: 0; }
        fieldset { border:1px solid #333; border-radius: 10px; margin-bottom:20px; }
        legend { padding: 0 10px; color:#a0a0a0; }
        label { display:block; margin-bottom:8px; }
        input[type="text"], select {
            width: 100%; padding: 10px; border-radius: 8px; border:1px solid #333; background:#111; color:#eee;
        }
        .row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .btn { padding: 10px 14px; background:#3b82f6; color:#fff; border:none; border-radius:8px; cursor:pointer; }
        .btn:hover { background:#2563eb; }
        .note { font-size: 12px; color:#aaa; }
    </style>
</head>
<body>
<div class="container">
    <h1>Generate Challan</h1>
    <p class="note">Downloadable PDF will be generated and saved by your browser.</p>

    <!-- 1) Single Student by Roll Number -->
    <fieldset>
        <legend>Generate for One Student</legend>
        <form action="GenerateChallanServlet" method="post">
            <input type="hidden" name="mode" value="single"/>
            <label>Roll Number</label>
            <input type="text" name="roll_no" placeholder="e.g., 9A-123" required />
            <br/><br/>
            <button class="btn" type="submit">Generate Challan (Single)</button>
        </form>
    </fieldset>

    <!-- 2) Whole Class -->
    <fieldset>
        <legend>Generate for Whole Class</legend>
        <form action="GenerateChallanServlet" method="post">
            <input type="hidden" name="mode" value="class"/>
            <label>Select Class</label>
            <select name="class_id" required>
              <%
                  List<model.ClassModel> classes = (List<model.ClassModel>) request.getAttribute("classes");
                  if (classes != null) {
                      for (model.ClassModel c : classes) {
              %>
                          <option value="<%= c.getId() %>"><%= c.getName() %></option>
              <%
                      }
                  }
              %>
            </select>
            <br/><br/>
            <button class="btn" type="submit">Generate Challans (Whole Class)</button>
        </form>
    </fieldset>

</div>
</body>
</html>
