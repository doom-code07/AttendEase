<html>
<head><title>Verify Email</title></head>
<body>
  <h2>Registration successful — verify your email</h2>
  <p>A verification link has been sent to your email. Please open your email and click the verification link to activate your account.</p>
  <p>Didn't receive email? Check spam, or <a href="resend_verification.jsp">resend</a>.</p>
  <p>Email not verified. <a href="resend_verification.jsp?email=<%= request.getAttribute("emailPrefill") %>">Resend verification email</a></p>

</body>
</html>
