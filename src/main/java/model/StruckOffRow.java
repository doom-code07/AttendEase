package model;

public class StruckOffRow {
    private int historyId;
    private int studentId;
    private String rollNo;
    private String studentName;
    private String className;
    private java.sql.Date struckOffDate;
    private java.sql.Date meetingDoneDate;
    private boolean parentMeetingDone;
    private boolean isStruckOff;

    public StruckOffRow(int historyId, int studentId, String rollNo, String studentName, String className,
                        java.sql.Date struckOffDate, java.sql.Date meetingDoneDate, boolean parentMeetingDone, boolean isStruckOff) {
        this.historyId = historyId;
        this.studentId = studentId;
        this.rollNo = rollNo;
        this.studentName = studentName;
        this.className = className;
        this.struckOffDate = struckOffDate;
        this.meetingDoneDate = meetingDoneDate;
        this.parentMeetingDone = parentMeetingDone;
        this.isStruckOff = isStruckOff;
    }

    public int getHistoryId() { return historyId; }
    public void setHistoryId(int historyId) { this.historyId = historyId; }
    public int getStudentId() { return studentId; }
    public String getRollNo() { return rollNo; }
    public String getStudentName() { return studentName; }
    public String getClassName() { return className; }
    public java.sql.Date getStruckOffDate() { return struckOffDate; }
    public java.sql.Date getMeetingDoneDate() { return meetingDoneDate; }
    public boolean isParentMeetingDone() { return parentMeetingDone; }
    public boolean isStruckOff() { return isStruckOff; }

    public void setStruckOff(boolean isStruckOff) {
    }
}
