package model;

import java.sql.Date;

public class StudentModel {
    private int id;
    private int userId;
    private String rollNo;
    private String batch;
    private String name;
    private String username;
    private String email;
    private String cnic;
    private String className;
    private int classId;
    private int totalFine;

    private boolean isStruckOff;
    private Date struckOffDate;
    private boolean parentMeetingDone;
    private Date meetingDoneDate;
    private boolean currentlyStruckOff;


    public boolean isStruckOff() {
        return isStruckOff;
    }

    public void setStruckOff(boolean struckOff) {
        isStruckOff = struckOff;
    }

    public Date getStruckOffDate() {
        return struckOffDate;
    }

    public void setStruckOffDate(Date struckOffDate) {
        this.struckOffDate = struckOffDate;
    }

    public boolean isParentMeetingDone() {
        return parentMeetingDone;
    }

    public void setParentMeetingDone(boolean parentMeetingDone) {
        this.parentMeetingDone = parentMeetingDone;
    }

    public boolean isCurrentlyStruckOff() { return currentlyStruckOff; }
    public void setCurrentlyStruckOff(boolean currentlyStruckOff) { this.currentlyStruckOff = currentlyStruckOff; }

    public StudentModel(int id, int userId, String rollNo, String batch, String name, String username, String email, String cnic, String className, int classId) {
        this.id = id;
        this.userId = userId;
        this.rollNo = rollNo;
        this.batch = batch;
        this.name = name;
        this.username = username;
        this.email = email;
        this.cnic = cnic;
        this.className = className;
        this.classId = classId;
    }

    public StudentModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(int totalFine) {
        this.totalFine = totalFine;
    }

    public Date getMeetingDoneDate() {
        return meetingDoneDate;
    }

    public void setMeetingDoneDate(Date meetingDoneDate) {
        this.meetingDoneDate = meetingDoneDate;
    }
}
