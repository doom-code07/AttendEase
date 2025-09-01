package model;

public class PolicyModel {
    private int id;
    private int minAttendancePercentage;
    private int finePerAbsentSubject;
    private int struckOffAfterAbsents;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMinAttendancePercentage() {
        return minAttendancePercentage;
    }
    public void setMinAttendancePercentage(int minAttendancePercentage) {
        this.minAttendancePercentage = minAttendancePercentage;
    }
    public int getFinePerAbsentSubject() {
        return finePerAbsentSubject;
    }
    public void setFinePerAbsentSubject(int finePerAbsentSubject) {
        this.finePerAbsentSubject = finePerAbsentSubject;
    }
    public int getStruckOffAfterAbsents() {
        return struckOffAfterAbsents;
    }
    public void setStruckOffAfterAbsents(int struckOffAfterAbsents) {
        this.struckOffAfterAbsents = struckOffAfterAbsents;
    }
}
