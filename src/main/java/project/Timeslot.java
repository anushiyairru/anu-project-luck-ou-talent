package project;

public class Timeslot {

    int slotID;
    String startTime;
    String endTime;
    String session;

    public Timeslot() {

    }

    public Timeslot(int slotID, String startTime, String endTime, String session) {
        this.slotID = slotID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.session = session;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

}