<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Verification Result</title></head>
<body>
    <h2>Email Verification</h2>
    <p><%= request.getAttribute("message") %></p>
    <a href="index.jsp">Go to login</a>
</body>
</html>
