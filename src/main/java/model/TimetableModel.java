package model;

import java.sql.Time;

public class TimetableModel {

    private int id;
    private int classId;
    private int subjectId;
    private int teacherId;
    private String day;
    private int periodNumber;
    private Time startTime;
    private Time endTime;

    // ⭐ NEW FIELDS (for display only)
    private String className;
    private String subjectName;
    private String teacherName;

    // =====================
    // ORIGINAL GETTERS/SETTERS
    // =====================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public int getPeriodNumber() { return periodNumber; }
    public void setPeriodNumber(int periodNumber) { this.periodNumber = periodNumber; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    // =====================
    // NEW GETTERS/SETTERS
    // =====================

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
