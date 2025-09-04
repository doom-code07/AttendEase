package servlet;

import dao.StudentDAO;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import model.StruckOffRow;
import java.io.IOException;
import java.util.List;


public class ManageStruckOffServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        StudentDAO dao = new StudentDAO();
        List<StruckOffRow> rows = dao.getStruckOffRows();
        req.setAttribute("rows", rows);
        req.getRequestDispatcher("manage_struckOff.jsp").forward(req, resp);
    }
}
