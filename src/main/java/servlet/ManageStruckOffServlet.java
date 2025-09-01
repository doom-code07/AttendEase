package servlet;

import dao.PolicyDAO;
import dao.StudentDAO;
import model.PolicyModel;
import model.StudentModel;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;


public class ManageStruckOffServlet extends HttpServlet {

    private StudentDAO studentDAO = new StudentDAO();
    private PolicyDAO policyDAO = new PolicyDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PolicyModel policy = policyDAO.getLatestPolicy();
        int threshold = 999999; // safe default so no one shows if no policy
        if (policy != null) {
            threshold = policy.getStruckOffAfterAbsents();
        }

        List<StudentModel> struckList = studentDAO.getStruckOffCandidates(threshold);

        req.setAttribute("struckList", struckList);
        req.setAttribute("absentThreshold", threshold);
        req.getRequestDispatcher("/manage_struckOff.jsp").forward(req, resp);
    }
}
