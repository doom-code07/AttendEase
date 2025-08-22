package model;

public class ClassModel {
    private int id;
    private String name;
    private int classID;

    public ClassModel() {}

    public ClassModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.classID=classID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }
}
