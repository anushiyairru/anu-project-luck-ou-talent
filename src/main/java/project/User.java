package project;

public class User {

    private int staffID;
    private String ICNO;
    private String name;
    private String telephoneNumber;
    private String emailAddress;
    
    public User() {

    }
    
    public User(int staffID, String ICNO, String name, String telephoneNumber, String emailAddress) {
        this.staffID = staffID;
        this.ICNO = ICNO;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.emailAddress = emailAddress;
    }
    
    public int getStaffID() {
        return staffID;
    }
    
    public String getICNO() {
        return ICNO;
    }

    public String getName() {
        return name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public void setICNO(String ICNO){
        this.ICNO = ICNO;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
}