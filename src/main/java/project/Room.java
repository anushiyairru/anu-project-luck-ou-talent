package project;

public class Room {


    private String roomId;

    private String roomDesc;
    private int maxCapacity;
    private String roomType;

    private String schoolID;
    private Room currentRoom;
    private Room[] roomlist;

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }


    public Room[] getRoomlist() {
        return roomlist;
    }

    public void setRoomlist(Room[] roomlist) {
        this.roomlist = roomlist;
    }

    public Room(String roomId, String roomDesc, int maxCapacity, String roomType, String schoolID) {
        this.roomId = roomId;
        this.roomDesc = roomDesc;
        this.maxCapacity = maxCapacity;
        this.roomType = roomType;
        this.schoolID = schoolID;
    }

    public Room() {

    }
    //Change by Wong , Need to change because no school involve
    public Room(String roomID, String roomDesc, int maxCapacity, String roomType) {
        this.roomId = roomID;
        this.roomDesc = roomDesc;
        this.maxCapacity = maxCapacity;
        this.roomType = roomType;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getRoomType() {
        return roomType;
    }


}