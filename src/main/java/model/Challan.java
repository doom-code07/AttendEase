package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Challan {
    private String studentName;
    private String rollNo;
    private String className;
    private BigDecimal fineAmount;
    private LocalDate issueDate;

    public Challan(String studentName, String rollNo, String className, BigDecimal fineAmount, LocalDate issueDate) {
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.className = className;
        this.fineAmount = fineAmount;
        this.issueDate = issueDate;
    }

    public String getStudentName() { return studentName; }
    public String getRollNo() { return rollNo; }
    public String getClassName() { return className; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public LocalDate getIssueDate() { return issueDate; }
}
