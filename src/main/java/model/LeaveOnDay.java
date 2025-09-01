package model;

import java.time.LocalDate;

public class LeaveOnDay {
    private int studentId;
    private String rollNo;
    private String studentName;
    private Integer classId;
    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private int leaveDays;

    public LeaveOnDay(int studentId, String rollNo, String studentName,
                      Integer classId, String className,
                      LocalDate startDate, LocalDate endDate, int leaveDays) {
        this.studentId = studentId;
        this.rollNo = rollNo;
        this.studentName = studentName;
        this.classId = classId;
        this.className = className;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveDays = leaveDays;
    }

    public int getStudentId() { return studentId; }
    public String getRollNo() { return rollNo; }
    public String getStudentName() { return studentName; }
    public Integer getClassId() { return classId; }
    public String getClassName() { return className; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getLeaveDays() { return leaveDays; }
}
