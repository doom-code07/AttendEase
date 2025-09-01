package servlet;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dao.ChallanDAO;
import dao.ClassDAO;
import dao.StudentDAO;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class GenerateChallanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mode = req.getParameter("mode"); // "single" or "class"

        resp.setContentType("application/pdf");
        String fileName = "challan_" + System.currentTimeMillis() + ".pdf";
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream os = resp.getOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, os);
            doc.open();

            if ("single".equalsIgnoreCase(mode)) {
                String rollNo = req.getParameter("roll_no");
                StudentDAO.StudentInfo st = StudentDAO.findByRoll(rollNo);
                if (st == null) {
                    addSimpleMessage(doc, "No student found with roll number: " + rollNo);
                } else {
                    int fine = ChallanDAO.computeFineAmount(st);
                    addChallan(doc, st.studentName, st.rollNo, st.className, fine, LocalDate.now());
                }
            } else if ("class".equalsIgnoreCase(mode)) {
                int classId = Integer.parseInt(req.getParameter("class_id"));
                String className = ClassDAO.getClassNameById(classId);
                List<StudentDAO.StudentInfo> students = StudentDAO.getStudentsByClass(classId);
                if (students.isEmpty()) {
                    addSimpleMessage(doc, "No students found in class: " + className + " (ID: " + classId + ")");
                } else {
                    // One page per student
                    for (int i = 0; i < students.size(); i++) {
                        StudentDAO.StudentInfo st = students.get(i);
                        int fine = ChallanDAO.computeFineAmount(st);
                        addChallan(doc, st.studentName, st.rollNo, st.className, fine, LocalDate.now());
                        if (i < students.size() - 1) doc.newPage();
                    }
                }
            } else {
                addSimpleMessage(doc, "Invalid mode.");
            }

            doc.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addSimpleMessage(Document doc, String msg) throws DocumentException {
        Paragraph p = new Paragraph(msg, FontFactory.getFont(FontFactory.HELVETICA, 12));
        p.setSpacingBefore(10f);
        doc.add(p);
    }


    private void addChallan(Document doc, String studentName, String rollNo, String className, int fineAmount, LocalDate issueDate)
            throws DocumentException {


        Paragraph title = new Paragraph("FGDCM\nFine Challan", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10f);
        doc.add(title);

        Paragraph meta = new Paragraph("Issue Date: " + issueDate.toString(), FontFactory.getFont(FontFactory.HELVETICA, 10));
        meta.setAlignment(Element.ALIGN_RIGHT);
        meta.setSpacingAfter(10f);
        doc.add(meta);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        addCell(table, "Student Name");
        addCell(table, studentName);

        addCell(table, "Roll Number");
        addCell(table, rollNo);

        addCell(table, "Class");
        addCell(table, className != null ? className : "-");

        addCell(table, "Fine Amount (PKR)");
        addCell(table, new BigDecimal(fineAmount).toPlainString());

        doc.add(table);


        Paragraph footer = new Paragraph(
                "\nInstructions:\n" +
                        "1) This challan is generated based on recorded absences.\n" +
                        "2) Please pay the fine at the accounts office within 7 days.\n" +
                        "3) For queries, contact the Vice Principal office.",
                FontFactory.getFont(FontFactory.HELVETICA, 10)
        );
        footer.setSpacingBefore(12f);
        doc.add(footer);
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell c1 = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 11)));
        c1.setPadding(8f);
        table.addCell(c1);
    }
}
