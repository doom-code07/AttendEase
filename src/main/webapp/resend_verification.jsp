<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Resend Verfiication</title>
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

        form {
            background: #ffffff;
            padding: 25px 30px;
            border-radius: 12px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
            width: 350px;
        }

        h3 {
            text-align: center;
            margin-bottom: 20px;
            color: #333333;
            font-size: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #444444;
        }

        input[type="email"] {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #cccccc;
            border-radius: 6px;
            margin-bottom: 15px;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s;
        }

        input[type="email"]:focus {
            border-color: #4a90e2;
        }

        input[type="submit"] {
            width: 100%;
            padding: 12px;
            background: #4a90e2;
            border: none;
            border-radius: 6px;
            color: #ffffff;
            font-size: 15px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s ease;
        }

        input[type="submit"]:hover {
            background: #357abd;
        }
    </style>
</head>
<body>
  <h3>Resend Verification Email</h3>
  <form method="post" action="ResendVerificationServlet">
    <label>Email:</label>
    <input type="email" name="email" required value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" />
    <input type="submit" value="Resend">
  </form>
</body></html>
