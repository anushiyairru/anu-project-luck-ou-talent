package project.database;

import java.awt.print.Book;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import project.Admin;
import project.Booking;
import project.Room;
import project.User;
import project.database.DatabaseConnect;

public class DatabasaQuery {

    public User user;
    public Admin pendingAdmin;
    public Admin admin;
    public Room room;
    public Booking booking;
    
    project.database.DatabaseConnect dataConnect;

    public DatabasaQuery() throws SQLException {
        dataConnect = new DatabaseConnect();
    }

    public boolean checkStaffID(int staffID) {

        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from user WHERE staff_Id =?");
            pstml.setInt(1, staffID);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                int staffId = rs.getInt("staff_Id");
                String ICNO = rs.getString("ICNO");
                String name = rs.getString("name");
                String tel = rs.getString("mobile_TelNo");
                String email = rs.getString("email");

                user = new User(staffId, ICNO, name, tel, email);

                return true;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public User hashUser(int id) {

        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("SELECT * from user WHERE staff_Id =?");
            pstml.setInt(1, id);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                int staffId = rs.getInt("staff_Id");
                String ICNO = rs.getString("ICNO");
                String name = rs.getString("name");
                String tel = rs.getString("mobile_TelNo");
                String email = rs.getString("email");

                user = new User(staffId, ICNO, name, tel, email);
            }

        } catch(SQLException e){
            throw new RuntimeException(e);
        }

        return user;
    }

    public void addUser(User user) {

        this.user = user;

        PreparedStatement pstml = null;

        try {

            pstml = dataConnect.connection.prepareStatement("INSERT INTO user(staff_Id,ICNO,name,mobile_TelNo,email) VALUES (?,?,?,?,?)");

            pstml.setInt(1, user.getStaffID());

            pstml.setString(2, user.getICNO());

            pstml.setString(3, user.getName());

            pstml.setString(4, user.getTelephoneNumber());

            pstml.setString(5, user.getEmailAddress());

            pstml.executeUpdate();

            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }


    public void cancelBooking(int staffID,int bookingId) {
        
        PreparedStatement pstml = null;

        try {
            pstml = dataConnect.connection.prepareStatement("DELETE from booking WHERE booking_ID ==? AND staff_Id == ?");

            pstml.setInt(1, bookingId);
            pstml.setInt(2,staffID);

            pstml.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void applySchoolAdmin(Admin pendingAdmin) {
        this.pendingAdmin = pendingAdmin;

        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("INSERT INTO pendingAdmin(staff_Id,office_Tel,school) VALUES (?,?,?)");

            pstml.setInt(1, pendingAdmin.getStaffID());
            pstml.setString(2, pendingAdmin.getOffice_Tel());
            pstml.setString(3, pendingAdmin.getSchoolID());

            pstml.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean checkAdmin(int staffID) {
        try {

            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from admin WHERE staff_Id =?");

            pstml.setInt(1, staffID);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                String office_tel = rs.getString("office_Tel");
                int staffId = rs.getInt("staff_Id");
                String schoolID = rs.getString("schoolID");
                
                admin = new Admin(office_tel, schoolID, staffId);
                
                return true;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void addRoom(Room room) {
        this.room = room;
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("INSERT INTO room(room_Id,room_Description,max_Capacity,room_type,schoolID) VALUES (?,?,?,?,?)");
            pstml.setString(1, room.getRoomId());
            pstml.setString(2, room.getRoomDesc());
            pstml.setInt(3, room.getMaxCapacity());
            pstml.setString(4, room.getRoomType());
            pstml.setString(5, room.getSchoolID());

            pstml.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSchoolID(int staffID) {
        String schoolID = "";

        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from admin WHERE staff_Id =?");
            pstml.setInt(1, staffID);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                schoolID = rs.getString("schoolID");

                return schoolID;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return schoolID;
    }


    public Room[] displayEditRoom(int staffID) {
        Room[] roomList ;
        int size =0;
        String schoolid= "";

        try {
            PreparedStatement pstml0 = dataConnect.connection.prepareStatement("SELECT * from admin WHERE staff_Id =?");
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from room WHERE room.schoolID =?");
            PreparedStatement pstml1 = dataConnect.connection.prepareStatement("SELECT COUNT(*) as size from room where schoolID = ?");
            pstml0.setInt(1,staffID);

            ResultSet rs0 = pstml0.executeQuery();

            while(rs0.next()){

                schoolid = rs0.getString("schoolID");
            }

            pstml.setString(1, schoolid);
            pstml1.setString(1, schoolid);

            ResultSet rs = pstml.executeQuery();

            ResultSet rs1 = pstml1.executeQuery();


            while (rs1.next()){
                size = rs1.getInt("size");

            }


            roomList = new Room[size];

            while (rs.next()) {

                String roomID = rs.getString("room_Id");

                String roomDesc =rs.getString("room_Description");

                int maxCapacity = rs.getInt("max_Capacity");

                String roomType = rs.getString("room_type");

                String schoolID = rs.getString("schoolID");


                roomList[rs.getRow()-1] = new Room(roomID,roomDesc,maxCapacity,roomType,schoolID);


            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomList;
    }


    //Code by Wong
    public String displayRoomInfo(String roomId) {
        String detailMessage="";
        try {

            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from room WHERE room_Id =?");

            pstml.setString(1, roomId);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                String room_Id = rs.getString("room_Id");
                String room_Desc = rs.getString("room_Description");
                int maxCapacity = rs.getInt("max_Capacity");
                String roomType = rs.getString("room_type");


                detailMessage = "Editing "+room_Id+" info \n\nRoom_ID : " + room_Id+ "\nRoom Desciption : " + room_Desc + "\nRoom Max Capacity : "
                        + maxCapacity + "\nRoom Type : " + roomType + "\n\n";


            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return detailMessage;

    }

    public Room updateName(Room room, Message message){
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE room SET room_Id = ? WHERE room_Id = ?");
            pstml.setString(1, message.getText());
            pstml.setString(2,room.getRoomId());
            pstml.executeUpdate();

            room.setRoomId(message.getText());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return room;
    }

    public void editBooking(Booking booking){
        PreparedStatement pstml;

        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE booking SET booking_date = ? ,time =? , room_Id =? WHERE booking_ID =? ");
            pstml.setString(1, booking.getDate());

            pstml.setString(2, booking.getTime());

            pstml.setString(3, booking.getRoomID());

            pstml.setInt(4,booking.getBookingID());
            pstml.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Room updateDesc(Room room, Message message){
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE room SET room_Description = ? WHERE room_Id = ?");
            pstml.setString(1, message.getText());
            pstml.setString(2,room.getRoomId());
            pstml.executeUpdate();

            room.setRoomDesc(message.getText());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return room;
    }

    public Room updateMax(Room room, Message message){
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE room SET max_Capacity = ? WHERE room_Id = ?");
            pstml.setInt(1, Integer.parseInt(message.getText()));
            pstml.setString(2,room.getRoomId());
            pstml.executeUpdate();

            room.setMaxCapacity(Integer.parseInt(message.getText()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return room;
    }

    public Room updateType(Room room, String type){
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE room SET room_type = ? WHERE room_Id = ?");
            pstml.setString(1,type);
            pstml.setString(2,room.getRoomId());
            pstml.executeUpdate();

            room.setRoomType(type);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return room;
    }

    public String showUserInfo(int staffID) {


        String showInfo = "";
        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from user WHERE staff_Id =?");
            pstml.setInt(1, staffID);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                int staffId = rs.getInt("staff_Id");
                String ICNO = rs.getString("ICNO");
                String name = rs.getString("name");
                String tel = rs.getString("mobile_TelNo");
                String email = rs.getString("email");
                showInfo ="Staff ID: " + staffId + "\n" + "IC No: " + ICNO + "\n" + "Name: " + name + "\n" + "Telephone No: " + tel + "\n" + "Email: " + email;


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return showInfo;

    }

    public String showAdminInfo(int staffID) {


        String showInfo = "";
        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from user WHERE staff_Id =?");
            PreparedStatement pstml1 = dataConnect.connection.prepareStatement("SELECT * from admin WHERE staff_Id =?");

            pstml1.setInt(1,staffID);
            pstml.setInt(1, staffID);
            ResultSet rs = pstml.executeQuery();
            ResultSet rs1 = pstml1.executeQuery();

            while (rs.next()) {

                int staffId = rs.getInt("staff_Id");
                String ICNO = rs.getString("ICNO");
                String name = rs.getString("name");
                String tel = rs.getString("mobile_TelNo");
                String email = rs.getString("email");
                String office = rs1.getString("office_Tel");
                showInfo ="Staff ID: " + staffId + "\n" + "IC No: " + ICNO + "\n" + "Name: " + name + "\n" + "Telephone No: " + tel + "\n" + "Email: " + email+"\n" +"Office No : "+office;


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return showInfo;

    }

    public void updateUserIC(int staffID, String staffIC) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE user SET ICNO =?WHERE staff_Id=?");
            pstml.setInt(2, staffID);
            pstml.setString(1, staffIC);
            pstml.executeUpdate();
            user.setICNO(staffIC);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void udpateUserName(int staffID, String name) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE user SET name =?WHERE staff_Id=?");
            pstml.setInt(2, staffID);
            pstml.setString(1, name);
            pstml.executeUpdate();
            user.setName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void udpateUserStaffID(int staffID, int staffIDNew) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE user SET staff_Id =?WHERE staff_Id=?");
            pstml.setInt(2, staffID);
            pstml.setInt(1, staffIDNew);
            pstml.executeUpdate();
            user.setStaffID(staffIDNew);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void udpateAdminUserStaffID(int staffID, int staffIDNew) {
        PreparedStatement pstml = null;
        PreparedStatement pstml1 = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE user SET staff_Id =?WHERE staff_Id=?");
            pstml1 = dataConnect.connection.prepareStatement("UPDATE admin SET staff_Id =?WHERE staff_Id=?");
            pstml.setInt(2, staffID);
            pstml.setInt(1, staffIDNew);
            pstml.executeUpdate();
            pstml1.setInt(2, staffID);
            pstml1.setInt(1, staffIDNew);
            pstml1.executeUpdate();
            user.setStaffID(staffIDNew);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void udpateUserTel(int staffID, String tele) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE user SET mobile_TelNo =?WHERE staff_Id=?");
            pstml.setInt(2, staffID);
            pstml.setString(1, tele);
            pstml.executeUpdate();
            user.setTelephoneNumber(tele);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void udpateUserEmail(int staffID, String email) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE user SET email =?WHERE staff_Id=?");
            pstml.setInt(2, staffID);
            pstml.setString(1, email);
            pstml.executeUpdate();
            user.setEmailAddress(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void udpateOffice(int staffID, String office) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE admin SET office_Tel =?WHERE staff_Id=?");
            pstml.setInt(2, staffID);
            pstml.setString(1, office);
            pstml.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public String checkBooking(int staffID) {
        PreparedStatement pstml;
        int count = 1;
        String displayMessage = "";
        try {
            pstml = dataConnect.connection.prepareStatement("SELECT booking_ID,room.room_Id,room_Description,max_Capacity,booking_date,time,booking_purpose from booking,room WHERE staff_Id == ? AND booking.room_Id == room.room_Id");
            pstml.setInt(1, staffID);

            ResultSet resultSet = pstml.executeQuery();


            while (resultSet.next()) {
                int book_id = resultSet.getInt(1);
                String room_id = resultSet.getString(2);
                String roomDes = resultSet.getString(3);
                int capacity = resultSet.getInt(4);
                String date = resultSet.getString(5);
                String time = resultSet.getString(6);
                String purpose = resultSet.getString(7);

                displayMessage = displayMessage +
                        "No : " + count +
                        "\nBooking ID : " + book_id +
                        "\nRoom ID : " + room_id +
                        "\nRoom Description : " + roomDes +
                        "\nMax Capacity : " + capacity +
                        "\nBooking Date : " + date +
                        "\nBooking Time : " + time +
                        "\nBooking Purpose : " + purpose +
                        "\n\n";
                count++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return displayMessage;
    }

    // This is to print all booking related to school id
    public String checkAllBooking(String schoolId) {
        PreparedStatement pstml;
        int count = 1;
        String displayMessage = "";
        try {
            pstml = dataConnect.connection.prepareStatement("SELECT booking_ID,room.room_Id,room_Description,max_Capacity,booking_date,time,booking_purpose from booking,room WHERE booking.room_Id == room.room_Id AND room.schoolID = ?");
            pstml.setString(1, schoolId);

            ResultSet resultSet = pstml.executeQuery();


            while (resultSet.next()) {
                int book_id = resultSet.getInt(1);
                String room_id = resultSet.getString(2);
                String roomDes = resultSet.getString(3);
                int capacity = resultSet.getInt(4);
                String date = resultSet.getString(5);
                String time = resultSet.getString(6);
                String purpose = resultSet.getString(7);

                displayMessage = displayMessage +
                        "No : " + count +
                        "\nBooking ID : " + book_id +
                        "\nRoom ID : " + room_id +
                        "\nRoom Description : " + roomDes +
                        "\nMax Capacity : " + capacity +
                        "\nBooking Date : " + date +
                        "\nBooking Time : " + time +
                        "\nBooking Purpose : " + purpose +
                        "\n\n";
                count++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return displayMessage;
    }


    ////new code by wong
    public String displaySelectedBooked(int bookingID) {
        PreparedStatement pstml;
        String displayMessage = "";
        try {
            pstml = dataConnect.connection.prepareStatement("SELECT booking_ID,room.room_Id,room_Description,max_Capacity,booking_date,time,booking_purpose from booking,room WHERE booking_ID == ?AND booking.room_Id == room.room_Id");
            pstml.setInt(1, bookingID);

            ResultSet resultSet = pstml.executeQuery();


            while (resultSet.next()) {
                int book_id = resultSet.getInt(1);
                String room_id = resultSet.getString(2);
                String roomDes = resultSet.getString(3);
                int capacity = resultSet.getInt(4);
                String date = resultSet.getString(5);
                String time = resultSet.getString(6);
                String purpose = resultSet.getString(7);

                displayMessage = displayMessage +
                        "\nBooking ID : " + book_id +
                        "\nRoom ID : " + room_id +
                        "\nRoom Description : " + roomDes +
                        "\nMax Capacity : " + capacity +
                        "\nBooking Date : " + date +
                        "\nBooking Time : " + time +
                        "\nBooking Purpose : " + purpose +
                        "\n\n";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return displayMessage;
    }

    ////new code by wong
    public Room[] displayEditRoom() {
        Room[] roomList ;
        int size =0;
        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from room");
            PreparedStatement pstml1 = dataConnect.connection.prepareStatement("SELECT COUNT(*) AS size from room");

            ResultSet rs = pstml.executeQuery();
            ResultSet rs1 = pstml1.executeQuery();

            while (rs1.next()){
                size = rs1.getInt("size");

            }

            roomList = new Room[size];


            while (rs.next()) {

                String roomID = rs.getString("room_Id");

                String roomDesc = rs.getString("room_Description");

                int maxCapacity = rs.getInt("max_Capacity");

                String roomType = rs.getString("room_type");

                roomList[rs.getRow() - 1] = new Room(roomID, roomDesc, maxCapacity, roomType);


            }
            return roomList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    ////new code by wong

//    public void udpateRoom(int staffID, String roomID) {
//        PreparedStatement pstml = null;
//        try {
//            pstml = dataConnect.connection.prepareStatement("UPDATE booking SET room_Id =?WHERE booking_ID=?");
//            pstml.setInt(2, staffID);
//            pstml.setString(1, roomID);
//            pstml.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
    ////new code by wong

    public void udpatePurpose(int bookID, String purpose) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("UPDATE booking SET booking_purpose =?WHERE booking_ID=?");
            pstml.setInt(2, bookID);
            pstml.setString(1, purpose);
            pstml.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRoomID(String roomDesc) {
        PreparedStatement pstml;
        String roomID;

        try {
            pstml = dataConnect.connection.prepareStatement("SELECT room_Id FROM room WHERE room_Description = ?;");
            pstml.setString(1, roomDesc);

            ResultSet resultSet = pstml.executeQuery();
            roomID = resultSet.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return roomID;
    }


    // code by Yeap
    public Room[] displayRoom() {
        Room[] roomList = null;
        int size =0;

        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * FROM room");
            PreparedStatement pstml1 = dataConnect.connection.prepareStatement("SELECT COUNT(*) AS size FROM room");

            ResultSet rs = pstml.executeQuery();
            ResultSet rs1 = pstml1.executeQuery();


            while (rs1.next()) {
                size = rs1.getInt("size");
            }

            roomList = new Room[size];

            while (rs.next()) {

                String roomID = rs.getString("room_Id");

                String roomDesc = rs.getString("room_Description");

                int maxCapacity = rs.getInt("max_Capacity");

                String roomType = rs.getString("room_type");

                String schoolID = rs.getString("schoolID");

                roomList[rs.getRow()-1] = new Room(roomID,roomDesc,maxCapacity,roomType,schoolID);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomList;
    }

    public Room[] displayAdminRoom(String schoolId) {
        Room[] roomList = null;
        int size =0;

        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * FROM room WHERE schoolID=?");
            PreparedStatement pstml1 = dataConnect.connection.prepareStatement("SELECT COUNT(*) AS size FROM room");
            pstml.setString(1,schoolId);
            ResultSet rs = pstml.executeQuery();
            ResultSet rs1 = pstml1.executeQuery();

            while (rs1.next()) {
                size = rs1.getInt("size");
            }

            roomList = new Room[size];

            while (rs.next()) {

                String roomID = rs.getString("room_Id");

                String roomDesc = rs.getString("room_Description");

                int maxCapacity = rs.getInt("max_Capacity");

                String roomType = rs.getString("room_type");

                String schoolID = rs.getString("schoolID");

                roomList[rs.getRow()-1] = new Room(roomID,roomDesc,maxCapacity,roomType,schoolID);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomList;
    }

    public String[] getTimeSlot(String session) {

        int noOfSlot;

        String[] time;

        try {

            PreparedStatement pstml1 = dataConnect.connection.prepareStatement("SELECT COUNT(time) AS available FROM timeslot WHERE session = ?");

            pstml1.setString(1, session);
            ResultSet rs1 = pstml1.executeQuery();

            noOfSlot = rs1.getInt("available");

            time = new String[noOfSlot];

            PreparedStatement pstml2 = dataConnect.connection.prepareStatement("SELECT time FROM timeslot WHERE session = ?");

            pstml2.setString(1, session);

            ResultSet rs2 = pstml2.executeQuery();

            while (rs2.next()) {



                time[rs2.getRow()-1] = rs2.getString(1);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return time;
    }

    //Add by Wong
    public String[] getBookedDate(String roomID, String date) {
        int bookedCnt;
        String[] bookedRoom;

        try {
            PreparedStatement pstml1 = dataConnect.connection.prepareStatement("SELECT COUNT(time) AS bookCnt FROM booking WHERE room_Id = ? AND booking_date=?");
            pstml1.setString(1, roomID);
            pstml1.setString(2, date);
            ResultSet rs0 = pstml1.executeQuery();
            bookedCnt = rs0.getInt("bookCnt");
            bookedRoom = new String[bookedCnt];

            PreparedStatement pstml2 = dataConnect.connection.prepareStatement("SELECT time FROM booking WHERE room_Id = ? AND booking_date=?");
//
            pstml2.setString(1, roomID);
            pstml2.setString(2, date);
            ResultSet rs1 = pstml2.executeQuery();

            while (rs1.next()) {
                bookedRoom[rs1.getRow() - 1] = rs1.getString(1);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bookedRoom;
    }

    public void saveBooking(Booking booking) {
        PreparedStatement pstml;

        try {
            pstml = dataConnect.connection.prepareStatement("INSERT INTO booking(booking_date,time, room_id, staff_id, booking_purpose) VALUES (?, ?, ?, ?, ?);");
            pstml.setString(1, booking.getDate());
            pstml.setString(2, booking.getTime());
            pstml.setString(3, booking.getRoomID());
            pstml.setInt(4, booking.getStaffID());
            pstml.setString(5, booking.getPurpose());
            pstml.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //add by woon to get booking info
    public Booking getBooking(int bookingID) {
        PreparedStatement pstml = null;
        try {
            pstml = dataConnect.connection.prepareStatement("SELECT * from booking WHERE booking_ID =?");
            pstml.setInt(1, bookingID);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                int bookingId = rs.getInt("booking_ID");
                String date = rs.getString("booking_date");
                String purpose = rs.getString("booking_purpose");
                String time = rs.getString("time");
                String roomID = rs.getString("room_Id");
                int staffID = rs.getInt("staff_Id");

                booking = new Booking(bookingId,purpose,date,null,time, roomID,staffID);
            }

        } catch(SQLException e){
            throw new RuntimeException(e);
        }

        return booking;
    }

    // Check user apply for school admin

    public boolean checkUserApply(int staffID) {
        try {

            PreparedStatement pstml = dataConnect.connection.prepareStatement("SELECT * from pendingAdmin WHERE staff_Id =?");

            pstml.setInt(1, staffID);
            ResultSet rs = pstml.executeQuery();

            while (rs.next()) {

                return true;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void timerUpdate(String day){
        try {
            PreparedStatement pstml = dataConnect.connection.prepareStatement("DELETE FROM booking WHERE booking_date < ? ");

            pstml.setString(1, day);
            pstml.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}

