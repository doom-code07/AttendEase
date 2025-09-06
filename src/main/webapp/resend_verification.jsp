<%@ page contentType="text/html;charset=UTF-8" %>
<html><body>
  <h3>Resend Verification Email</h3>
  <form method="post" action="ResendVerificationServlet">
    <label>Email:</label>
    <input type="email" name="email" required value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" />
    <input type="submit" value="Resend">
  </form>
</body></html>
