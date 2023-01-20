package project;

public class Booking {

    int bookingID;
    String purpose;
    String date;
    String session;
    String time;
    String roomID;
    int staffID;
    String [] timeSlot;

    public String[] getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String[] timeSlot) {
        this.timeSlot = timeSlot;
    }


    public Booking() {

    }

    public Booking(int bookingID, String purpose, String date, String session, String time, String roomID, int staffID) {
        this.bookingID = bookingID;
        this.purpose = purpose;
        this.date = date;
        this.session = session;
        this.time = time;
        this.roomID = roomID;
        this.staffID = staffID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public String getPurpose() {
        return purpose;
    }


    public String getDate() {
        return date;
    }

    public String getSession() {
        return session;
    }

    public String getTime() {
        return time;
    }

    public String getRoomID() {
        return roomID;
    }

    public int getStaffID() {
        return staffID;
    }
}