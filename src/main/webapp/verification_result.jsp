<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Verification Result</title>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Verification Result</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background: #ffffff;
            padding: 30px 35px;
            border-radius: 12px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
            width: 400px;
            max-width: 90%;
            text-align: center;
        }

        h2 {
            color: #333333;
            font-size: 22px;
            margin-bottom: 20px;
        }

        p {
            color: #555555;
            font-size: 15px;
            margin-bottom: 20px;
            line-height: 1.6;
        }

        a {
            display: inline-block;
            padding: 12px 20px;
            background: #4a90e2;
            color: #ffffff;
            font-weight: bold;
            text-decoration: none;
            border-radius: 6px;
            transition: background 0.3s ease;
        }

        a:hover {
            background: #357abd;
        }
    </style>
</head>
<body>
    <h2>Email Verification</h2>
    <p><%= request.getAttribute("message") %></p>
    <a href="index.jsp">Go to login</a>
</body>
</html>
