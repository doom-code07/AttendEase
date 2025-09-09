<html>
<head><title>Verification Pending</title>


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
            width: 500px;
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
            line-height: 1.6;
            margin-bottom: 15px;
        }

        a {
            color: #4a90e2;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        a:hover {
            color: #357abd;
            text-decoration: underline;
        }
    </style>

</head>
<body>
  <h2>Registration successful — verify your email</h2>
  <p>A verification link has been sent to your email. Please open your email and click the verification link to activate your account.</p>
  <p>Didn't receive email? Check spam, or <a href="resend_verification.jsp">resend</a>.</p>
  <p>Email not verified. <a href="resend_verification.jsp?email=<%= request.getAttribute("emailPrefill") %>">Resend verification email</a></p>

</body>
</html>
