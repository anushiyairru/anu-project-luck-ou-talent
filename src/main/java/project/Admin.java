package project;

public class Admin {



    private String schoolID;
    private int staffID;



    private String office_Tel;



    public Admin(String office_Tel, String schoolID, int staffID) {
        this.office_Tel = office_Tel;
        this.staffID = staffID;
        this.schoolID = schoolID;
    }

    public Admin() {

    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public void setOffice_Tel(String office_Tel) {
        this.office_Tel = office_Tel;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public int getStaffID() {
        return staffID;
    }

    public String getOffice_Tel() {
        return office_Tel;
    }

    public String getSchoolID() {
        return schoolID;
    }
}