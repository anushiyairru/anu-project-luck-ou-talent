package project;

import org.apache.commons.lang3.ArrayUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import project.database.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class telegramBot extends TelegramLongPollingBot {

    DatabasaQuery databasaQuery = new DatabasaQuery();
    
    User registerUser = new User();
    Admin pendingAdmin = new Admin();
    Admin admin = new Admin();
    Room room = new Room(); //Use for create room in database
    Room[] roomlist = null;
    Room[] editAdminRoomList = null;
    String[] dateFormat = new String[7];
    
    String answer;
    
    //create hashmap to store user info
    private Map<Long, org.telegram.telegrambots.meta.api.objects.User> userMap = new HashMap<>();
    private Map<Long, Integer> userStateMap = new HashMap<>();
    private Map<Long, String> userDataMap = new HashMap<>();
    private Map<Long, Booking> userBookingMap = new HashMap<>();
    private Map<Long, User> userLoginMap = new HashMap<>();
    private Map<Long, Room> userRoomMap = new HashMap<>();

    private org.telegram.telegrambots.meta.api.objects.User teleUser;
    
    private static long chatId;
    
    private static final int StartState = 0;
    private static final int decisionLRState = 15;
    private static final int registrationState = 1;
    private static final int icState = 2;
    private static final int staffIDState = 3;
    private static final int nameState = 4;
    private static final int telephoneState = 5;
    private static final int emailState = 6;
    private static final int loginState = 7;
    private static final int bookingState = 8;
    private static final int purposeState = 9;
    private static final int roomIDState = 10;
    private static final int timeState = 11;
    private static final int dateState = 12;
    private static final int checkState = 13;
    private static final int cancelState = 14;

    private static final int decisionBookState = 16;
    private static final int successBookState = 17;
    private static final int decisionBackMainState = 18;
    private static final int successRegisterState = 19;
    private static final int selectSchool = 20;
    private static final int roomDescription = 21;
    private static final int roomCapacity = 22;
    private static final int roomType = 23;
    private static final int editNameSuccess = 24;
    private static final int editDescSuccess = 25;
    private static final int editMaxSuccess = 26;
    private static final int editState = 27;
    private static final int editState1 = 28;
    private static final int editState2 = 29;
    private static final int editState3 = 30;
    private static final int editState4 = 31;
    private static final int displayInfo = 32;
//    private static final int updateRoom = 28;
    private static final int updateDate = 33;
    private static final int updateTime = 34;
    private static final int updatePurpose = 35;

    //Done by Wong Zi Qing
    private static final int displayBookingSelection = 36;
//    private static final int displayRoom = 37;


    //Done by Yeap Shu Qi
    private static final int confirmBookState = 37;
    private static final int sessionState = 38;
    //------------------------
    private static final int cancelSuccessState = 39;
    private static final int editSessionState = 40;
    private static final int editAdminState = 41;
    private static final int editOfficeState =42;
    private static int state = 0;

    private boolean checkingUser = false;

    public telegramBot() throws SQLException {
    
    }
    
    public String getBotUsername() {
        return "STIW3054_luckoutalent_bot";
    }

    @Override
    public String getBotToken() {
        return "5817604254:AAFd6jX4tXjrxmPAXrPfpkqiiFXaqdh1GuQ";
    }

    @Override
    public void onUpdateReceived(Update update) {
        
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                try {
                    teleUser = update.getMessage().getFrom();
                    long userId = teleUser.getId();

                    // Check if the user is already in the HashMap
                    boolean checkExist = userMap.containsValue(teleUser);
                    if (!checkExist) {
                        // If not, create a new user and add it to the HashMap
                        userMap.put(userId, teleUser);
                        userDataMap.put(userId, "");
                        userStateMap.put(userId, 0);
                        userBookingMap.put(userId, new Booking());
                        userLoginMap.put(userId, new User());
                        userRoomMap.put(userId, new Room());

//                        chatId = userId;
                    }

                    handleIncomingMessage(message, teleUser.getId());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (update.hasCallbackQuery()) {
            teleUser = update.getCallbackQuery().getFrom();
            Message message = update.getCallbackQuery().getMessage();

            try {
                handleCallBackQuery(update, message, teleUser.getId());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            
        }
    }
    
    private void handleCallBackQuery(Update update, Message message, long chatId) throws TelegramApiException {

        SendMessage sendMessage = new SendMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        userDataMap.put(chatId, callbackQuery.getData());
        String data = userDataMap.get(chatId);



        if (userRoomMap.get(chatId).getRoomlist() != null) {

            if (userRoomMap.get(chatId).getRoomlist().length >= 1) {

                for (int i = 0; i < userRoomMap.get(chatId).getRoomlist().length; i++) {
                    // This is for admin edit room

                    if (data.equals(userRoomMap.get(chatId).getRoomlist()[i].getRoomId())) {

                        userRoomMap.get(chatId).setCurrentRoom(userRoomMap.get(chatId).getRoomlist()[i]);

                        sendMessage = displayRoomDetail(message, userRoomMap.get(chatId).getRoomlist()[i].getRoomId(), chatId);

                    }

                    // This is for user edit booking room (display all to let them choose)
                    if (data.equals(userRoomMap.get(chatId).getRoomlist()[i].getRoomId() + 1)) {
                        userRoomMap.get(chatId).setCurrentRoom(userRoomMap.get(chatId).getRoomlist()[i]);

                        sendMessage = editdateMessage(message, userRoomMap.get(chatId).getRoomlist()[i].getRoomId(), chatId);
                        //sendMessage = displayNewRoom(message, roomlist[i].getRoomId(),chatId);
                    }

                    // This is for user make new booking
                    if (data.equals(userRoomMap.get(chatId).getRoomlist()[i].getRoomId() + 2)) {

                        userRoomMap.get(chatId).setCurrentRoom(userRoomMap.get(chatId).getRoomlist()[i]);
                        
                        sendMessage = dateMessage(message, userRoomMap.get(chatId).getRoomlist()[i].getRoomId(), chatId);

                    }

                }
            }
        }

        // New Add by Woon
        if (dateFormat != null) {
            for (int i = 0; i < dateFormat.length; i++) {
                if (data.equals(dateFormat[i])) {
                    userBookingMap.get(chatId).setDate(data);
                    //newBooking.setDate(data);

                    sendMessage = sessionMessage(message, chatId);
                } else if (data.equals(dateFormat[i] + 1)) {
                    userBookingMap.get(chatId).setDate(dateFormat[i]);
                    sendMessage = editsessionMessage(message, chatId);
                }
            }
        }
        
        // New add Woon
        if (userBookingMap.get(chatId).getTimeSlot() != null) {

            for (int i = 0; i < userBookingMap.get(chatId).getTimeSlot().length; i++) {

                // This to check whether the step is for new booking or edit , because the step for both are the same for selecting room, date and time
                if (data.equals(userBookingMap.get(chatId).getTimeSlot()[i])) {
                    userBookingMap.get(chatId).setTime(data);

                    //newBooking.setTime(data);
                    sendMessage = confirmBookMessage(message, chatId);
                    
                } else if (data.equals(userBookingMap.get(chatId).getTimeSlot()[i] + 1)) {
                    userBookingMap.get(chatId).setTime(userBookingMap.get(chatId).getTimeSlot()[i]);
                    sendMessage = confirmEditMessage(message, chatId);
                }
            }
        }
        
        switch (data) {
            case "apply":
                if (!(databasaQuery.checkUserApply(userLoginMap.get(chatId).getStaffID()))) {
                    sendMessage = applySchoolAdmin(message, chatId);
                } else {
                    sendMessage = appliedAdmin(message, chatId);
                }
                break;
            case "booking":
            case "bookingAdmin":
                userBookingMap.put(chatId, new Booking());
                sendMessage = bookingPurpose(message, chatId);
                break;
            case "display":
                sendMessage = checkBookingMessage(message, chatId);
                break;
            case "cancel":
            case "cancelAdmin":
                sendMessage = cancelMessage(message, chatId);
                break;
            case "update":
                sendMessage = editUserMessage(message, chatId);
                break;
            case "editBooking":
                sendMessage = editStartMessage(message, chatId);
                break;
            case "COB":
            case "CAS":
            case "COLGIS":
                pendingAdmin.setSchoolID(data);
                sendMessage = waitingApprove(message, chatId);
                break;
            case "register_room":
                sendMessage = addRoomMessage(message, chatId);
                break;
            case "edit":
                // This for show admin edit room
                sendMessage = editAdminRoomMessage(message, chatId);
                break;
            case "meeting":
            case "training":
            case "lab":
                userRoomMap.get(chatId).setRoomType(data);
                //room.setRoomType(data);
                sendMessage = successAddRoomMessage(message, chatId);
                break;
            case "displayAll":
                sendMessage = checkAllBookingMessage(message, chatId);
                break;
            case "updateAdmin":
                sendMessage = editAdminMessage(message, chatId);
                break;
            case "editName":
                sendMessage = editNameMessage(message, chatId);
                break;
            case "editDesc":
                sendMessage = editDescMessage(message, chatId);
                break;
            case "editMax":
                sendMessage = editMaxMessage(message, chatId);
                break;
            case "editRoomType":
                sendMessage = editRoomTypeMessage(message, chatId);
                break;
            case "backMain":
                sendMessage = displayOption(message, chatId);
                break;
            case "backEdit":
                sendMessage = displayRoomDetail(message, userRoomMap.get(chatId).getCurrentRoom().getRoomId(), chatId);
                break;
            case "chooseMeeting":
            case "chooseLab":
            case "chooseTraining":
            case "chooseLect":
                sendMessage = successChangeType(message, data, chatId);
                break;
            case "icno":
                sendMessage = displayEditICMessage(message, chatId);
                break;
            case "name":
                sendMessage = displayEditNameMessage(message, chatId);
                break;
            case "staffID":
                sendMessage = displayEditStaffIDMessage(message, chatId);
                break;
            case "staffAdminID":
                sendMessage = displayEditAdminStaffIDMessage(message, chatId);
                break;
            case "phoneNo":
                sendMessage = displayEditTelMessage(message, chatId);
                break;
            case "email":
                sendMessage = displayEditEmailMessage(message, chatId);
                break;
            case "office":
                sendMessage = displayOfficeMessage(message,chatId);
                break;
            case "backEditUser":
                sendMessage = editUserMessage(message, chatId);
                break;
            case "backUserMain":
                sendMessage = displayOption(message, chatId);
                break;
            case "updatePurpose":
                sendMessage = editPurposeMessage(message, chatId);
                break;
            case "updateRoom":
                sendMessage = editRoomMessage(message, chatId);
                break;
            case "updateDate":
                sendMessage = editdateMessage(message, userBookingMap.get(chatId).getRoomID(), chatId);
                break;
            case "updateTime":
                sendMessage = editsessionMessage(message, chatId);
                break;
            case "backEditMenu":
                sendMessage = editStartMessage(message, chatId);
                break;
            // New Add by Woon
            case "morning":
            case "evening":
            case "afternoon":
                if (userStateMap.get(chatId) != editSessionState) {
                    userBookingMap.get(chatId).setSession(data);
                    sendMessage = timeMessage(message, data, chatId);
                } else if (userStateMap.get(chatId) == editSessionState) {
                    userBookingMap.get(chatId).setSession(data);
                    sendMessage = edittimeMessage(message, data, chatId);
                }
                break;
            case "yes":
            case "no":
                sendMessage = successBookMessage(message, data, chatId);
                break;
            case "yesedit":
            case "noedit":
                sendMessage = successEditMessage(message, data, chatId);
        }
        
        execute(sendMessage);
    }
    
    private void handleIncomingMessage(Message message, long chatId) throws TelegramApiException {

        if (message.getText().equals("/start")) {
            userStateMap.put(chatId, StartState);
        }
        
        SendMessage sendMessage = new SendMessage();
        
        switch (userStateMap.get(chatId)) {
            case StartState:
                sendMessage = mainMenu(message, chatId);
                break;

            case decisionLRState:
                sendMessage = decisionLR(message, chatId);
                break;

            case loginState:
                sendMessage = checkID(message, chatId);
                break;

            case registrationState:
            case icState:
            case staffIDState:
            case nameState:
            case telephoneState:
            case emailState:
                sendMessage = registrationMethod(message, chatId);
                break;

            case successRegisterState:
            case bookingState:
                sendMessage = displayOption(message, chatId);
                break;

            case decisionBookState:
                sendMessage = decisionUser(message, chatId);
                break;
                
            case purposeState:
            case roomIDState:
            case timeState:
            case dateState:
            case successBookState:
                sendMessage = bookingMessage(message, chatId);
                break;

            case decisionBackMainState:
                sendMessage = decisionMainMenu(message, chatId);
                break;

            case checkState:
                checkBookingMessage(message, chatId);
                break;

            case cancelState:
                cancelMessage(message, chatId);
                break;
                
            case cancelSuccessState:
                sendMessage = sendCancelMessage(message, chatId);
                break;
                
            case selectSchool:
                sendMessage = selectSchoolAdmin(message, chatId);
                break;
                
            case roomType:
                sendMessage = roomTypeMessage(message, chatId);
                break;
                
            case roomDescription:
                sendMessage = roomDescMessage(message, chatId);
                break;
                
            case roomCapacity:
                sendMessage = roomCapacityMessage(message, chatId);
                break;
                
            case editNameSuccess:
                sendMessage = displayNameSuccess(message, chatId);
                break;
                
            case editDescSuccess:
                sendMessage = displayDescSuccess(message, chatId);
                break;
                
            case editMaxSuccess:
                sendMessage = displayMaxSuccess(message, chatId);
                break;
                
            case editState:
                sendMessage = updateIC(message, chatId);
                break;
                
            case editState1:
                sendMessage = updateName(message, chatId);
                break;
                
            case editState2:
                sendMessage = updateStaffID(message, chatId);
                break;
            case editAdminState:
                sendMessage = updateAdminStaffID(message,chatId);
                break;
            case editState3:
                sendMessage = updateTele(message, chatId);
                break;
                
            case editState4:
                sendMessage = updateEmail(message, chatId);
                break;
            case editOfficeState:
                sendMessage = updateOffice(message,chatId);
                break;
                
//            case displayInfo:
//                sendMessage = displayInfoMessage(message);
//                break;
            
            case updatePurpose:
                sendMessage = updatePurposeMessage(message, chatId);
                break;
                
            case updateDate:
                sendMessage = editNameMessage(message, chatId);
                break;
                
            case displayBookingSelection:
                sendMessage = displayBookingSelectionMessage(message, chatId);
                break;
                
            // default
        }

        execute(sendMessage);
    }
    
    private SendMessage mainMenu(Message message, long chatId) {

        SendMessage sendMessage = new SendMessage();

        if (message.getText().equals("/start")) {
            String welcomeMessage = """
                    Welcome to UUM meeting room booking system!!!
                                        
                    I'm your MEETING ROOM BOOKING virtual assistant, live chat with me to book a meeting room!
                                        
                    You can read them by clicking the links below:
                    Terms and Conditions: https://www.nationalbusinessfurniture.com/blog/7-tips-for-meeting-room-etiquette
                                        
                    Kindly be informed that a booking is required for a meeting room.
                                       
                    Before I allow you to have a booking with me. I need to verify your identity first.

                    Important Note: You are not able to use the booking system if you are not a member of UUM
                    
                    ----------------------------------------------------------------------------------------------------
                    * If you are not registered with the system yet, you need to
                    * reply 1 to register,                                                      
                    * otherwise reply 2 to login.                                                
                    ----------------------------------------------------------------------------------------------------
                    
                    Reply 1 for registration
                    Reply 2 for login  \s
                    """;
            
            sendMessage.setChatId(chatId);
            sendMessage.setText(welcomeMessage);
            userStateMap.put(chatId, decisionLRState);


        }

        return sendMessage;
    }

    private SendMessage decisionLR(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();


        if (message.getText().equals("1")) {
            userStateMap.put(chatId, registrationState);

            sendMessage = registrationMethod(message, chatId);

        } else if (message.getText().equals("2")) {

            userStateMap.put(chatId, loginState);

            sendMessage = checkID(message, chatId);

        } else {

            String invalidMainMenuMessage = """
                    Please enter a valid value  \s""";

            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText(invalidMainMenuMessage);
        }
        return sendMessage;
    }

    private SendMessage registrationMethod(Message message, long chatId) {

        SendMessage sendMessage;
        switch (userStateMap.get(chatId)) {
            case registrationState:
            case icState:
                sendMessage = icMessage(message, chatId);
                break;

            case staffIDState:
                sendMessage = staffIDMessage(message, chatId);
                break;
            case nameState:
                sendMessage = nameMessage(message, chatId);
                break;
            case telephoneState:
                sendMessage = telephoneMessage(message, chatId);
                break;
            case emailState:
                sendMessage = emailMessage(message, chatId);
                break;
            default:
                sendMessage = sendErrorMessage(message, chatId);
                break;

        }
        return sendMessage;
    }


    private SendMessage icMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String registerMessage = """
                Please enter your IC number
                                    
                Example : 000912010212
                """;

        sendMessage.setChatId(chatId);
        sendMessage.setText(registerMessage);
        userStateMap.put(chatId, staffIDState);
        return sendMessage;
    }

    private SendMessage staffIDMessage(Message message, long chatId) {

        userLoginMap.get(chatId).setICNO(message.getText());

        SendMessage sendMessage = new SendMessage();
        String registerMessage = """
                Please enter your staff ID
                                    
                Example : 123123
                """;

        sendMessage.setChatId(chatId);
        sendMessage.setText(registerMessage);
        userStateMap.put(chatId, nameState);

        return sendMessage;
    }

    private SendMessage nameMessage(Message message, long chatId) {
        userLoginMap.get(chatId).setStaffID(Integer.parseInt(message.getText()));
        SendMessage sendMessage = new SendMessage();
        String registerMessage = """
                Please enter your name
                                    
                Example : Woon Zhun Ping
                """;
    
        // New Add Ibtihal
        if (message.getText().matches("^[0-9]{6}$")) {
            sendMessage.setText(registerMessage);
            sendMessage.setChatId(chatId);
            userStateMap.put(chatId, telephoneState);
        } else {
            String invalidStaffID = """
                 Input is not a valid 6 digit Staff ID
                 
                 Please input 6 digit Staff ID
                 """;
            sendMessage.setText(invalidStaffID);
            sendMessage.setChatId(chatId);
            userStateMap.put(chatId, nameState);
        }



        return sendMessage;
    }

    private SendMessage telephoneMessage(Message message, long chatId) {
        userLoginMap.get(chatId).setName(message.getText());
        SendMessage sendMessage = new SendMessage();
        String registerMessage = """
                Please enter your mobile telephone number
                                    
                Example : 0171212312
                """;
    
        // New Add Ibtihal
        if (message.getText().matches("[a-zA-Z\s]+")) {
            sendMessage.setText(registerMessage);
            sendMessage.setChatId(chatId);
            userStateMap.put(chatId, emailState);
        } else {
            String invalidName = """
                 Input is not a valid Name.
                 
                 Please input name again
                 """;
            sendMessage.setText(invalidName);
            sendMessage.setChatId(chatId);
            userStateMap.put(chatId, telephoneState);
        }

        return sendMessage;
    }

    private SendMessage emailMessage(Message message, long chatId) {
        userLoginMap.get(chatId).setTelephoneNumber(message.getText());
        SendMessage sendMessage = new SendMessage();
        String registerMessage = """
                Please enter your email
                                    
                Example : email@hotmail.com
                """;
    
        // New Add Ibtihal
        if (message.getText().matches("\\d{10}")) {
            sendMessage.setText(registerMessage);
            sendMessage.setChatId(chatId);
            userStateMap.put(chatId, successRegisterState);
        } else {
            String invalid = """
                 Input is not a valid 10-digit Mobile Telephone.
                 
                 Please input telephone number again
                 """;
            sendMessage.setText(invalid);
            sendMessage.setChatId(chatId);
            userStateMap.put(chatId, emailState);
        }
        
        // New Add Ibtihal
//        String pattern = "^[A-Za-z0-9+_.-]+@(.+)$";
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(message.getText());
//
//        if (m.matches()) {
//            String validEmail = """
//                Please enter valid email!!
//                """;
//            sendMessage.setText(validEmail);
//        } else {
//            String invalidEmail = """
//                Please enter valid email!!
//                """;
//            sendMessage.setText(invalidEmail);
//        }

        sendMessage.setChatId(chatId);

        return sendMessage;
    }

    private SendMessage checkID(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String loginMessage = """
                Please enter your staffID
                                    
                Example : 123456
                """;

        sendMessage.setChatId(chatId);
        sendMessage.setText(loginMessage);
        userStateMap.put(chatId, bookingState);

        return sendMessage;
    }

    private SendMessage displayOption(Message message, long chatId) {

        InlineKeyboardMarkup markupInline;
        boolean checkAdmin;
        
        if (userStateMap.get(chatId) == successRegisterState) {

            userLoginMap.get(chatId).setEmailAddress(message.getText());

            databasaQuery.addUser(userLoginMap.get(chatId));
            checkingUser = true;

        } else if (userStateMap.get(chatId) == bookingState) {

            int staffID = Integer.parseInt(message.getText());
            checkingUser = databasaQuery.checkStaffID(staffID);
            userLoginMap.put(chatId, databasaQuery.hashUser(staffID));
        }
        
        // later add more method to separate them as register to booking and login to booking (must!! else cannot method cannot load)
        // error on if result is false(staffid is not in the database)
        // it will run error, so cannot print fail to login message
        
        checkAdmin = databasaQuery.checkAdmin(userLoginMap.get(chatId).getStaffID());

        SendMessage sendMessage = new SendMessage();

        if (checkAdmin) {
            admin = databasaQuery.admin;
            String loginMessage = "Welcome "+userLoginMap.get(chatId).getName() + " !!!! You are ready to use the booking system in UUM!";
            
            sendMessage.setChatId(chatId);
            sendMessage.setText(loginMessage);

            markupInline = setInlineButtonAdminMainMenu();
            sendMessage.setReplyMarkup(markupInline);

        } else if (checkingUser || message.getText().equals("0")) {

            String userName = userLoginMap.get(chatId).getName();
            String loginMessage = "Welcome " + userName + ", you are ready to use the booking system in UUM!";
            
            sendMessage.setChatId(chatId);
            sendMessage.setText(loginMessage);

            markupInline = setInlineButtonMainMenu();
            sendMessage.setReplyMarkup(markupInline);

            userStateMap.put(chatId, decisionBookState);
            
        } else {

            sendMessage = sendErrorMessage(message, chatId);

        }

        return sendMessage;

    }
    
    private SendMessage decisionUser(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();

        if (message.getText().equals("1")) {
            userStateMap.put(chatId, purposeState);

            sendMessage = bookingMessage(message, chatId);

        } else if (message.getText().equals("2")) {
            userStateMap.put(chatId, checkState);

            sendMessage = checkBookingMessage(message, chatId);

        } else if (message.getText().equals("3")) {
            userStateMap.put(chatId, cancelState);

            sendMessage = cancelMessage(message, chatId);

        } else {
            String invalidMainMenuMessage = """
                    Please enter a valid value  \s""";

            sendMessage.setChatId(chatId);
            sendMessage.setText(invalidMainMenuMessage);
        }
        return sendMessage;
    }

    private SendMessage decisionMainMenu(Message message, long chatId) {
        SendMessage sendMessage;

        if (message.getText().equals("0")) {
            sendMessage = displayOption(message, chatId);
            return sendMessage;
        } else {
            sendMessage = sendErrorMessage(message, chatId);
            return sendMessage;
        }
    }

    private SendMessage checkBookingMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();

        String successMessage = databasaQuery.checkBooking(userLoginMap.get(chatId).getStaffID());
        successMessage = successMessage.concat("\n Enter 0 to return to main menu");

        sendMessage.setChatId(chatId);
        sendMessage.setText(successMessage);
        userStateMap.put(chatId, decisionBackMainState);


        return sendMessage;
    }

    private SendMessage checkAllBookingMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String successMessage = databasaQuery.checkAllBooking(admin.getSchoolID());
        successMessage = successMessage.concat("\n Enter 0 to return to main menu");

        sendMessage.setChatId(chatId);
        sendMessage.setText(successMessage);
        userStateMap.put(chatId, decisionBackMainState);


        return sendMessage;
    }

    private SendMessage cancelMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String cancelMessage = databasaQuery.checkBooking(userLoginMap.get(chatId).getStaffID());
        cancelMessage = cancelMessage.concat("Enter the BookingID in order to cancel the booking  \n Example :1");

        InlineKeyboardMarkup markupInlineBack = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backMain = new InlineKeyboardButton();

        backMain.setText("Back Main");
        backMain.setCallbackData("backUserMain");


        row1.add(backMain);

        inlineButton.add(row1);

        markupInlineBack.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(cancelMessage);
        sendMessage.setReplyMarkup(markupInlineBack);

        userStateMap.put(chatId, cancelSuccessState);

        return sendMessage;
    }

    private SendMessage sendCancelMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();

        int bookingId = Integer.parseInt(message.getText());


        databasaQuery.cancelBooking(userLoginMap.get(chatId).getStaffID(), bookingId);


        String cancelBooking = "Successfully deleted the booking! \nPlease type 0 to return to the Main Menu";

        sendMessage.setChatId(chatId);
        sendMessage.setText(cancelBooking);
        userStateMap.put(chatId, decisionBackMainState);


        return sendMessage;
    }

    private SendMessage sendErrorMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String invalidMessage = """
                Please enter a valid value  \s""";

        sendMessage.setChatId(chatId);
        sendMessage.setText(invalidMessage);

        return sendMessage;
    }

    private InlineKeyboardMarkup setInlineButtonMainMenu() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton applyButton = new InlineKeyboardButton();
        InlineKeyboardButton bookingButton = new InlineKeyboardButton();
        InlineKeyboardButton displayButton = new InlineKeyboardButton();
        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        InlineKeyboardButton editButton = new InlineKeyboardButton();
        InlineKeyboardButton updateButton = new InlineKeyboardButton();

        applyButton.setText("Apply as School Admin");
        applyButton.setCallbackData("apply");

        bookingButton.setText("Make a Booking");
        bookingButton.setCallbackData("booking");

        displayButton.setText("Display Booking");
        displayButton.setCallbackData("display");

        cancelButton.setText("Cancel Booking");
        cancelButton.setCallbackData("cancel");

        editButton.setText("Edit Booking");
        editButton.setCallbackData("editBooking");

        updateButton.setText("Update Personal Info");
        updateButton.setCallbackData("update");


        row1.add(applyButton);
        row1.add(bookingButton);

        row2.add(displayButton);
        row2.add(cancelButton);

        row3.add(editButton);
        row3.add(updateButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage applySchoolAdmin(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();

        String officeTelMessage = """
                Please enter your office telephone number
                                
                Example: 04-3112121
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(officeTelMessage);
        userStateMap.put(chatId, selectSchool);


        return sendMessage;
    }

    //One user only can apply once for school admin
    //This is to print applied message
    private SendMessage appliedAdmin(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();

        String appliedMessage = """
                We found that you already applied for school admin before
                               
                Your application maybe processing or rejected. 
                 """;


        sendMessage.setChatId(chatId);
        sendMessage.setText(appliedMessage);

        InlineKeyboardMarkup markupInlineBack = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backMain = new InlineKeyboardButton();

        backMain.setText("Back Main");
        backMain.setCallbackData("backUserMain");

        row1.add(backMain);

        inlineButton.add(row1);

        markupInlineBack.setKeyboard(inlineButton);

        sendMessage.setReplyMarkup(markupInlineBack);

        return sendMessage;
    }

    private SendMessage selectSchoolAdmin(Message message, long chatId) {
        pendingAdmin.setOffice_Tel(message.getText());
        SendMessage sendMessage = new SendMessage();

        String selectSchoolMessage = """
                Please select which school you're from
                                    
                """;

        InlineKeyboardMarkup markupInline;

        markupInline = selectSchool();

        sendMessage.setReplyMarkup(markupInline);

        sendMessage.setChatId(chatId);
        sendMessage.setText(selectSchoolMessage);

        return sendMessage;
    }

    private InlineKeyboardMarkup selectSchool() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton casButton = new InlineKeyboardButton();
        InlineKeyboardButton cobButton = new InlineKeyboardButton();
        InlineKeyboardButton colgisButton = new InlineKeyboardButton();


        casButton.setText("College of Art and Sciences");
        casButton.setCallbackData("CAS");

        cobButton.setText("College of Business");
        cobButton.setCallbackData("COB");

        colgisButton.setText("College of Law, Government and International Studies");
        colgisButton.setCallbackData("COLGIS");


        row1.add(casButton);
        row2.add(cobButton);
        row3.add(colgisButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage waitingApprove(Message message, long chatId) {

        pendingAdmin.setStaffID(userLoginMap.get(chatId).getStaffID());

        databasaQuery.applySchoolAdmin(pendingAdmin);
        SendMessage sendMessage = new SendMessage();


        String successMessage = """
                Your application are under processing
                Please wait for the email verification
                                         
                Type 0 to return to main menu
                """;

        sendMessage.setChatId(chatId);
        sendMessage.setText(successMessage);
        userStateMap.put(chatId, decisionBackMainState);

        return sendMessage;
    }

    private InlineKeyboardMarkup setInlineButtonAdminMainMenu() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        InlineKeyboardButton roomButton = new InlineKeyboardButton();
        InlineKeyboardButton editRoomButton = new InlineKeyboardButton();
        InlineKeyboardButton bookingButton = new InlineKeyboardButton();
        InlineKeyboardButton displayButton = new InlineKeyboardButton();
        InlineKeyboardButton displayAllButton = new InlineKeyboardButton();
        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        InlineKeyboardButton editBookingButton = new InlineKeyboardButton();
        InlineKeyboardButton updateButton = new InlineKeyboardButton();

        roomButton.setText("Register Room");
        roomButton.setCallbackData("register_room");

        editRoomButton.setText("Edit Room");
        editRoomButton.setCallbackData("edit");

        bookingButton.setText("Make a Booking");
        bookingButton.setCallbackData("bookingAdmin");

        displayButton.setText("Display Booking");
        displayButton.setCallbackData("display");

        displayAllButton.setText("Display All Booking");
        displayAllButton.setCallbackData("displayAll");

        cancelButton.setText("Cancel Booking");
        cancelButton.setCallbackData("cancelAdmin");

        editBookingButton.setText("Edit Booking");
        editBookingButton.setCallbackData("editBooking");

        updateButton.setText("Update Personal Info");
        updateButton.setCallbackData("updateAdmin");


        row1.add(roomButton);
        row1.add(editRoomButton);

        row2.add(bookingButton);
        row2.add(cancelButton);

        row3.add(displayButton);
        row3.add(displayAllButton);

        row4.add(editBookingButton);
        row4.add(updateButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);
        inlineButton.add(row4);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage addRoomMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String roomIDMessage = """
                Please type in the room ID as the identity of the room
                                
                Example: SOC_LH1
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(roomIDMessage);
        userStateMap.put(chatId, roomDescription);

        return sendMessage;
    }


    private SendMessage roomDescMessage(Message message, long chatId) {
        userRoomMap.get(chatId).setRoomId(message.getText());
        //room.setRoomId(message.getText());
        SendMessage sendMessage = new SendMessage();
        String roomDescMessage = """
                Please set the description of the room
                                
                Example: Lecturer Hall 1 equipped with complete PA system. 
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(roomDescMessage);
        userStateMap.put(chatId, roomCapacity);

        return sendMessage;
    }

    private SendMessage roomCapacityMessage(Message message, long chatId) {
        userRoomMap.get(chatId).setRoomDesc(message.getText());
        //room.setRoomDesc(message.getText());
        SendMessage sendMessage = new SendMessage();
        String roomCapMessage = """
                Please set the maximum capacity of the meeting room
                                
                Example: 10
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(roomCapMessage);
        userStateMap.put(chatId, roomType);

        return sendMessage;
    }

    private SendMessage roomTypeMessage(Message message, long chatId) {
        userRoomMap.get(chatId).setMaxCapacity(Integer.parseInt(message.getText()));
        //room.setMaxCapacity(Integer.parseInt(message.getText()));
        SendMessage sendMessage = new SendMessage();

        String roomTypeMessage = """
                Please select the type of your room
                                    
                """;

        InlineKeyboardMarkup markupInline;

        markupInline = selectRoomType();

        sendMessage.setReplyMarkup(markupInline);

        sendMessage.setChatId(chatId);
        sendMessage.setText(roomTypeMessage);

        return sendMessage;

    }

    private InlineKeyboardMarkup selectRoomType() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton meetingButton = new InlineKeyboardButton();
        InlineKeyboardButton trainingButton = new InlineKeyboardButton();
        InlineKeyboardButton labButton = new InlineKeyboardButton();


        meetingButton.setText("Meeting Room");
        meetingButton.setCallbackData("meeting");

        trainingButton.setText("Training Room");
        trainingButton.setCallbackData("training");

        labButton.setText("Laboratory");
        labButton.setCallbackData("lab");

        row1.add(meetingButton);
        row2.add(trainingButton);
        row3.add(labButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage successAddRoomMessage(Message message, long chatId) {
        userRoomMap.get(chatId).setSchoolID(databasaQuery.getSchoolID(userLoginMap.get(chatId).getStaffID()));


        databasaQuery.addRoom(userRoomMap.get(chatId));
        SendMessage sendMessage = new SendMessage();


        String successAddRoomMessage = """
                Success on added the room for others to book
                                         
                Type 0 to return to main menu
                """;

        sendMessage.setChatId(chatId);
        sendMessage.setText(successAddRoomMessage);
        userStateMap.put(chatId, decisionBackMainState);

        return sendMessage;
    }


    //Display Admin room for editing
    private InlineKeyboardMarkup displayAdminRoom(long chatId) {
        int id = userLoginMap.get(chatId).getStaffID();

        userRoomMap.get(chatId).setRoomlist(databasaQuery.displayEditRoom(id));
        //roomlist = databasaQuery.displayEditRoom();


        int length = 0;
        //check the length of listbutton in order to know create how much row of List<InlineKeyboardButton>

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();

        InlineKeyboardButton listButton[] = new InlineKeyboardButton[userRoomMap.get(chatId).getRoomlist().length];

        if (userRoomMap.get(chatId).getRoomlist().length % 2 == 0) {
            length = userRoomMap.get(chatId).getRoomlist().length / 2;
        } else if (userRoomMap.get(chatId).getRoomlist().length % 2 != 0) {
            length = (userRoomMap.get(chatId).getRoomlist().length / 2) + 1;

        }

        List<InlineKeyboardButton> row[] = (ArrayList<InlineKeyboardButton>[]) new ArrayList[length];


        for (int i = 0; i < userRoomMap.get(chatId).getRoomlist().length; i++) {
            listButton[i] = new InlineKeyboardButton();

            listButton[i].setText(userRoomMap.get(chatId).getRoomlist()[i].getRoomId());


            listButton[i].setCallbackData(userRoomMap.get(chatId).getRoomlist()[i].getRoomId());


        }

        int count = 0;

        for (int j = 0; j < row.length; j++) {
            row[j] = new ArrayList<>();

            row[j].add(listButton[count + j]);

            count += 1;
            if (!(count + j >= listButton.length)) {

                row[j].add(listButton[count + j]);


            }
            inlineButton.add(row[j]);
        }


        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage displayRoomDetail(Message message, String roomId, long chatId) {
        SendMessage sendMessage = new SendMessage();

        String roomDetailMessage = databasaQuery.displayRoomInfo(roomId);


        InlineKeyboardMarkup markupInline;

        markupInline = displayEditOption();

        sendMessage.setReplyMarkup(markupInline);

        sendMessage.setChatId(chatId);
        sendMessage.setText(roomDetailMessage);
        userStateMap.put(chatId, decisionBackMainState);


        return sendMessage;
    }

    private InlineKeyboardMarkup displayEditOption() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton nameButton = new InlineKeyboardButton();
        InlineKeyboardButton desciptionButton = new InlineKeyboardButton();
        InlineKeyboardButton capacityButton = new InlineKeyboardButton();
        InlineKeyboardButton roomTypeButton = new InlineKeyboardButton();
        InlineKeyboardButton backMainButton = new InlineKeyboardButton();


        nameButton.setText("Edit Name");
        nameButton.setCallbackData("editName");

        desciptionButton.setText("Edit Description");
        desciptionButton.setCallbackData("editDesc");

        capacityButton.setText("Edit Max Capacity");
        capacityButton.setCallbackData("editMax");

        roomTypeButton.setText("Edit room Type");
        roomTypeButton.setCallbackData("editRoomType");

        backMainButton.setText("Back to Main Menu");
        backMainButton.setCallbackData("backMain");

        row1.add(nameButton);
        row1.add(desciptionButton);
        row2.add(capacityButton);
        row2.add(roomTypeButton);
        row3.add(backMainButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    public SendMessage editNameMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String editNameMessage = """
                Enter a new name for your meeting room
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(editNameMessage);
        userStateMap.put(chatId, editNameSuccess);

        return sendMessage;
    }

    public SendMessage editDescMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String editDescMessage = """
                Enter a new description for your meeting room
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(editDescMessage);
        userStateMap.put(chatId, editDescSuccess);

        return sendMessage;
    }

    public SendMessage editMaxMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String editMaxMessage = """
                Enter a number of maximum capacity for your meeting room
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(editMaxMessage);
        userStateMap.put(chatId, editMaxSuccess);

        return sendMessage;
    }

    public SendMessage displayNameSuccess(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        userRoomMap.get(chatId).setCurrentRoom(databasaQuery.updateName(userRoomMap.get(chatId).getCurrentRoom(), message));

        String successNameMessage = """
                Successfully updated the meeting room's name!
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(successNameMessage);

        InlineKeyboardMarkup markupInline;

        markupInline = displayEditBack();

        sendMessage.setReplyMarkup(markupInline);


        return sendMessage;
    }

    private SendMessage displayMaxSuccess(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        userRoomMap.get(chatId).setCurrentRoom(databasaQuery.updateMax(userRoomMap.get(chatId).getCurrentRoom(), message));
        String successNameMessage = """
                Successfully updated the maximum capacity of the meeting room!
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(successNameMessage);

        InlineKeyboardMarkup markupInline;

        markupInline = displayEditBack();

        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }

    private SendMessage displayDescSuccess(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        userRoomMap.get(chatId).setCurrentRoom(databasaQuery.updateDesc(userRoomMap.get(chatId).getCurrentRoom(), message));
        String successDescMessage = """
                Successfully updated the meeting's room description!
                """;

        InlineKeyboardMarkup markupInline;

        markupInline = displayEditBack();

        sendMessage.setReplyMarkup(markupInline);

        sendMessage.setChatId(chatId);
        sendMessage.setText(successDescMessage);


        return sendMessage;
    }

    private InlineKeyboardMarkup displayEditBack() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backEditButton = new InlineKeyboardButton();
        InlineKeyboardButton backMainButton = new InlineKeyboardButton();


        backEditButton.setText("Back to Edit");
        backEditButton.setCallbackData("backEdit");

        backMainButton.setText("Back to Main Menu");
        backMainButton.setCallbackData("backUserMain");


        row1.add(backEditButton);
        row1.add(backMainButton);


        inlineButton.add(row1);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage editRoomTypeMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String editMaxMessage = """
                Please select one of the option below to change your room type
                """;
        sendMessage.setChatId(chatId);
        sendMessage.setText(editMaxMessage);

        InlineKeyboardMarkup markupInline;

        markupInline = displayTypeChange();

        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }


    private InlineKeyboardMarkup displayTypeChange() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton meetingButton = new InlineKeyboardButton();
        InlineKeyboardButton trainingButton = new InlineKeyboardButton();
        InlineKeyboardButton labButton = new InlineKeyboardButton();
        InlineKeyboardButton lectButton = new InlineKeyboardButton();


        meetingButton.setText("Meeting Room");
        meetingButton.setCallbackData("chooseMeeting");

        trainingButton.setText("Training Room");
        trainingButton.setCallbackData("chooseTraining");

        labButton.setText("Laboratory");
        labButton.setCallbackData("chooseLab");

        lectButton.setText("Lecturer Hall");
        lectButton.setCallbackData("chooseLect");

        row1.add(meetingButton);
        row2.add(trainingButton);
        row3.add(labButton);
        row4.add(lectButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);
        inlineButton.add(row4);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    public SendMessage successChangeType(Message message, String type, long chatId) {
        String roommType = "";
        if (type.equals("chooseMeeting")) {
            roommType = "Meeting Room";
        } else if (type.equals("chooseLab")) {
            roommType = "Laboratory";
        } else if (type.equals("chooseTraining")) {
            roommType = "Training Room";
        } else if (type.equals("chooseLect")) {
            roommType = "Lecturer Hall";

        }

        SendMessage sendMessage = new SendMessage();
        userRoomMap.get(chatId).setCurrentRoom(databasaQuery.updateType(userRoomMap.get(chatId).getCurrentRoom(), roommType));
        String successTypeMessage = """
                Successfully updated the meeting room's type!
                """;

        InlineKeyboardMarkup markupInline;

        markupInline = displayEditBack();

        sendMessage.setReplyMarkup(markupInline);

        sendMessage.setChatId(chatId);
        sendMessage.setText(successTypeMessage);


        return sendMessage;


    }


    //Wong work start here
    private InlineKeyboardMarkup setInlineButtonEditInfoMenu() {

        InlineKeyboardMarkup markupInline1 = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton icNoButton = new InlineKeyboardButton();
        InlineKeyboardButton nameButton = new InlineKeyboardButton();
        InlineKeyboardButton staffIDButton = new InlineKeyboardButton();
        InlineKeyboardButton phoneNoButton = new InlineKeyboardButton();
        InlineKeyboardButton emailButton = new InlineKeyboardButton();

        icNoButton.setText("ICNo");
        icNoButton.setCallbackData("icno");

        nameButton.setText("Name");
        nameButton.setCallbackData("name");

        staffIDButton.setText("Staff ID");
        staffIDButton.setCallbackData("staffID");

        phoneNoButton.setText("Phone No");
        phoneNoButton.setCallbackData("phoneNo");

        emailButton.setText("Email");
        emailButton.setCallbackData("email");

        row1.add(icNoButton);
        row1.add(nameButton);

        row2.add(staffIDButton);
        row2.add(phoneNoButton);

        row3.add(emailButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        markupInline1.setKeyboard(inlineButton);

        return markupInline1;
    }

    private InlineKeyboardMarkup setInlineButtonEditAdminInfoMenu() {

        InlineKeyboardMarkup markupInline1 = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton icNoButton = new InlineKeyboardButton();
        InlineKeyboardButton nameButton = new InlineKeyboardButton();
        InlineKeyboardButton staffIDButton = new InlineKeyboardButton();
        InlineKeyboardButton phoneNoButton = new InlineKeyboardButton();
        InlineKeyboardButton emailButton = new InlineKeyboardButton();
        InlineKeyboardButton officeButton = new InlineKeyboardButton();

        icNoButton.setText("ICNo");
        icNoButton.setCallbackData("icno");

        nameButton.setText("Name");
        nameButton.setCallbackData("name");

        staffIDButton.setText("Staff ID");
        staffIDButton.setCallbackData("staffAdminID");

        phoneNoButton.setText("Phone No");
        phoneNoButton.setCallbackData("phoneNo");

        emailButton.setText("Email");
        emailButton.setCallbackData("email");

        officeButton.setText("Office Telephone No");
        officeButton.setCallbackData("office");



        row1.add(icNoButton);
        row1.add(nameButton);

        row2.add(staffIDButton);
        row2.add(phoneNoButton);

        row3.add(emailButton);
        row3.add(officeButton);


        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        markupInline1.setKeyboard(inlineButton);

        return markupInline1;
    }

    private InlineKeyboardMarkup setInlineButtonEditBookingDetails() {

        InlineKeyboardMarkup markupInlineBooking = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton bookRoomBT = new InlineKeyboardButton();
        InlineKeyboardButton bookDateBT = new InlineKeyboardButton();
        InlineKeyboardButton bookTimeBT = new InlineKeyboardButton();
        InlineKeyboardButton bookPurposeBT = new InlineKeyboardButton();

        bookRoomBT.setText("Room");
        bookRoomBT.setCallbackData("updateRoom");

        bookDateBT.setText("Date");
        bookDateBT.setCallbackData("updateDate");

        bookTimeBT.setText("Time");
        bookTimeBT.setCallbackData("updateTime");

        bookPurposeBT.setText("Purpose");
        bookPurposeBT.setCallbackData("updatePurpose");

        row1.add(bookRoomBT);
        row1.add(bookDateBT);

        row2.add(bookTimeBT);
        row2.add(bookPurposeBT);

        inlineButton.add(row1);
        inlineButton.add(row2);

        markupInlineBooking.setKeyboard(inlineButton);

        return markupInlineBooking;
    }


    // Wong edit booking detail
//    private SendMessage editBookingRoomMessage(Message message, long chatId) {
//        InlineKeyboardMarkup markupInlineRoom;
//        SendMessage sendMessage = new SendMessage();
//        String editMessage = "Please select the new room";
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(editMessage);
//        markupInlineRoom = setInlineButtonEditRoom();
//        sendMessage.setReplyMarkup(markupInlineRoom);
//        return sendMessage;
//    }

    private InlineKeyboardMarkup setInlineButtonEditDate() {
        // date formatter get current local date
        // +0 until +6, 7 days to choose including today
        // loop, add button into inlineKeyboardMarkup

        // ddf : date & day of week format (user-friendly)
        // dof : date only format (SQLite)

        DateTimeFormatter ddf = DateTimeFormatter.ofPattern("dd/MM/yyyy, EEEE");
        DateTimeFormatter dof = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime day;

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> inlineButtonList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        for (int i = 0; i < 6; i++) {
            day = LocalDateTime.now().plusDays(i);
            button.setText(ddf.format(day));
            button.setCallbackData(dof.format(day));
            inlineButtonList.add(button);
        }

        inlineButton.add(inlineButtonList);
        inlineMarkup.setKeyboard(inlineButton);
        userStateMap.put(chatId, updateDate);


        return inlineMarkup;
    }

    // Edit part for booking , rmb to change to button


    // Wong edit user info
    private SendMessage displayEditICMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();

        if (userLoginMap.get(chatId).getStaffID() != 0) {
            String userName = userLoginMap.get(chatId).getName();
            String newDataMessage = "Hey " + userName + ", please enter the new IC number!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(newDataMessage);
            userStateMap.put(chatId, editState);
            state = editState;

        } else {
            sendMessage = sendErrorMessage(message, chatId);
        }
        return sendMessage;

    }

    private SendMessage displayEditStaffIDMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        if (userLoginMap.get(chatId).getStaffID() != 0) {
            String userName = userLoginMap.get(chatId).getName();
            String newDataMessage = "Hey " + userName + ", please enter the new staff ID!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(newDataMessage);
            userStateMap.put(chatId, editState2);


        } else {
            sendMessage = sendErrorMessage(message, chatId);
        }
        return sendMessage;

    }

    private SendMessage displayEditAdminStaffIDMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        if (userLoginMap.get(chatId).getStaffID() != 0) {
            String userName = userLoginMap.get(chatId).getName();
            String newDataMessage = "Hey " + userName + ", please enter the new staff ID!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(newDataMessage);
            userStateMap.put(chatId, editAdminState);


        } else {
            sendMessage = sendErrorMessage(message, chatId);
        }
        return sendMessage;

    }

    private SendMessage displayEditTelMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        if (userLoginMap.get(chatId).getStaffID() != 0) {
            String userName = userLoginMap.get(chatId).getName();
            String newDataMessage = "Hey " + userName + ", please enter the new telephone number!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(newDataMessage);
            userStateMap.put(chatId, editState3);


        } else {
            sendMessage = sendErrorMessage(message, chatId);
        }
        return sendMessage;

    }

    private SendMessage displayEditEmailMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        if (userLoginMap.get(chatId).getStaffID() != 0) {
            String userName = userLoginMap.get(chatId).getName();
            String newDataMessage = "Hey " + userName + ", please enter the new email!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(newDataMessage);
            userStateMap.put(chatId, editState4);


        } else {
            sendMessage = sendErrorMessage(message, chatId);
        }
        return sendMessage;

    }

    private SendMessage displayOfficeMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        if (userLoginMap.get(chatId).getStaffID() != 0) {
            String userName = userLoginMap.get(chatId).getName();
            String newDataMessage = "Hey " + userName + ", please enter the new office telephone number!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(newDataMessage);
            userStateMap.put(chatId, editOfficeState);


        } else {
            sendMessage = sendErrorMessage(message, chatId);
        }
        return sendMessage;

    }



    private SendMessage displayEditNameMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        if (userLoginMap.get(chatId).getStaffID() != 0) {
            String userName = userLoginMap.get(chatId).getName();
            String newDataMessage = "Hey " + userName + ", please enter the new name!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(newDataMessage);
            userStateMap.put(chatId, editState1);


        } else {
            sendMessage = sendErrorMessage(message, chatId);
        }
        return sendMessage;

    }

    private SendMessage updateIC(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String newIC = message.getText();

        // New Add Ibtihal
        if (newIC.matches("\\d{12}")) {
            databasaQuery.updateUserIC(userLoginMap.get(chatId).getStaffID(), newIC);
            String editMessage = "Data successfully edited!";

            sendMessage.setChatId(chatId);
            sendMessage.setText(editMessage);

            InlineKeyboardMarkup markupInline;
            markupInline = displayEditUserBack();

            sendMessage.setReplyMarkup(markupInline);
            userStateMap.put(chatId, displayInfo);
        } else {
            String invalid = """
                 Input is not a valid 12-digit IC number.
                 
                 Please input IC number again
                 """;
            sendMessage.setText(invalid);
            sendMessage.setChatId(chatId);

        }

        return sendMessage;
    }

    private SendMessage updateName(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String newName = message.getText();

        // New Add Ibtihal
        if (newName .matches("[a-zA-Z\s]+")) {
            databasaQuery.udpateUserName(userLoginMap.get(chatId).getStaffID(), newName);
            String editMessage = "Data successfully edited!";
            sendMessage.setChatId(chatId);
            sendMessage.setText(editMessage);

            userStateMap.put(chatId, displayInfo);
            InlineKeyboardMarkup markupInline;
            markupInline = displayEditUserBack();

            sendMessage.setReplyMarkup(markupInline);
        } else {
            String invalidName = """
                 Input is not a valid Name.
                 
                 Please input name again
                 """;
            sendMessage.setText(invalidName);
            sendMessage.setChatId(chatId);
        }

        return sendMessage;
    }

    private SendMessage updateStaffID(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        int newStaffID = Integer.parseInt(message.getText());

        // New Add Ibtihal
        if (message.getText().matches("^[0-9]{6}$")) {
            databasaQuery.udpateUserStaffID(userLoginMap.get(chatId).getStaffID(), newStaffID);
            String editMessage = "Data successfully edited!";
            sendMessage.setChatId(chatId);
            sendMessage.setText(editMessage);
            userStateMap.put(chatId, displayInfo);
            state = displayInfo;

            InlineKeyboardMarkup markupInline;
            markupInline = displayEditUserBack();

            sendMessage.setReplyMarkup(markupInline);
        } else {
            String invalidStaffID = """
                 Input is not a valid 6 digit Staff ID
                 
                 Please input 6 digit Staff ID
                 """;
            sendMessage.setText(invalidStaffID);
            sendMessage.setChatId(chatId);
        }

        return sendMessage;
    }

    private SendMessage updateAdminStaffID(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        int newStaffID = Integer.parseInt(message.getText());

        // New Add Ibtihal
        if (message.getText().matches("^[0-9]{6}$")) {
            databasaQuery.udpateAdminUserStaffID(userLoginMap.get(chatId).getStaffID(), newStaffID);
            String editMessage = "Data successfully edited!";
            sendMessage.setChatId(chatId);
            sendMessage.setText(editMessage);
            userStateMap.put(chatId, displayInfo);
            state = displayInfo;

            InlineKeyboardMarkup markupInline;
            markupInline = displayEditUserBack();

            sendMessage.setReplyMarkup(markupInline);
        } else {
            String invalidStaffID = """
                 Input is not a valid 6 digit Staff ID
                 
                 Please input 6 digit Staff ID
                 """;
            sendMessage.setText(invalidStaffID);
            sendMessage.setChatId(chatId);
        }
        return sendMessage;
    }

    private SendMessage updateTele(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String newTele = message.getText();

        // New Add Ibtihal
        if (newTele.matches("\\d{10}")) {
            databasaQuery.udpateUserTel(userLoginMap.get(chatId).getStaffID(), newTele);
            String editMessage = "Data successfully edited!";
            sendMessage.setChatId(chatId);
            sendMessage.setText(editMessage);
            userStateMap.put(chatId, displayInfo);


            InlineKeyboardMarkup markupInline;
            markupInline = displayEditUserBack();

            sendMessage.setReplyMarkup(markupInline);
        } else {
            String invalid = """
                 Input is not a valid 10-digit Mobile Telephone.
                 
                 Please input telephone number again
                 """;
            sendMessage.setText(invalid);
            sendMessage.setChatId(chatId);
        }
        return sendMessage;
    }

    private SendMessage updateEmail(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String newEmail = message.getText();

        // New Add Ibtihal
        String pattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(newEmail);

        if (m.matches()) {
            databasaQuery.udpateUserEmail(userLoginMap.get(chatId).getStaffID(), newEmail);
            String editMessage = "Data successfully edited!";
            sendMessage.setChatId(chatId);
            sendMessage.setText(editMessage);

            userStateMap.put(chatId, displayInfo);

            InlineKeyboardMarkup markupInline;
            markupInline = displayEditUserBack();

            sendMessage.setReplyMarkup(markupInline);
        } else {
            String invalidEmail = """
                 Input is not a valid email address.
                 
                 Please input telephone number again
                                
                                """;
            sendMessage.setText(invalidEmail);
            sendMessage.setChatId(chatId);
        }

        return sendMessage;
    }

    private SendMessage updateOffice(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String newOffice = message.getText();

        databasaQuery.udpateOffice(userLoginMap.get(chatId).getStaffID(), newOffice);
        String editMessage = "Data successfully edited!";
        sendMessage.setChatId(chatId);
        sendMessage.setText(editMessage);

        userStateMap.put(chatId, displayInfo);

        InlineKeyboardMarkup markupInline;
        markupInline = displayEditUserBack();

        sendMessage.setReplyMarkup(markupInline);
        return sendMessage;
    }


    private SendMessage editUserMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markupInline;


        String userinfo = databasaQuery.showUserInfo(userLoginMap.get(chatId).getStaffID());
        sendMessage.setChatId(chatId);
        userinfo.concat("\n  Please select the type of information you wish to edit");
        sendMessage.setText(userinfo);

        markupInline = setInlineButtonEditInfoMenu();
        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }

    private SendMessage editAdminMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markupInline;


        String userinfo = databasaQuery.showAdminInfo(userLoginMap.get(chatId).getStaffID());

        sendMessage.setChatId(chatId);
        userinfo.concat("\n  Please select the type of information you wish to edit");
        sendMessage.setText(userinfo);

        markupInline = setInlineButtonEditAdminInfoMenu();
        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }



    private InlineKeyboardMarkup displayEditUserBack() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backEditButton = new InlineKeyboardButton();
        InlineKeyboardButton backMainButton = new InlineKeyboardButton();


        backEditButton.setText("Back to User Edit");
        backEditButton.setCallbackData("backEditUser");

        backMainButton.setText("Back to Main Menu");
        backMainButton.setCallbackData("backUserMain");


        row1.add(backEditButton);
        row1.add(backMainButton);


        inlineButton.add(row1);

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage editBookingMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markupInline;
        String editMessage = "Please select the booking details to edit";
        sendMessage.setChatId(chatId);
        sendMessage.setText(editMessage);
        markupInline = setInlineButtonEditBookingDetails();
        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }


    //change this name later
    private SendMessage editStartMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String bookedRoomDetails = databasaQuery.checkBooking(userLoginMap.get(chatId).getStaffID());
        String editBooking = bookedRoomDetails + "\n" + """
                Please enter the booking ID in order to select the booking to edit
                Example of booking ID: 1
                \s""";


        InlineKeyboardMarkup markupInlineBack = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backMain = new InlineKeyboardButton();

        backMain.setText("Back Main");
        backMain.setCallbackData("backUserMain");


        row1.add(backMain);

        inlineButton.add(row1);

        markupInlineBack.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(editBooking);
        sendMessage.setReplyMarkup(markupInlineBack);
        userStateMap.put(chatId, displayBookingSelection);


        return sendMessage;
    }

    private SendMessage displayBookingSelectionMessage(Message message, long chatId) {
        int selectBooking = Integer.parseInt(message.getText());
        userBookingMap.put(chatId, databasaQuery.getBooking(selectBooking));
        String selectBookedDetails = databasaQuery.displaySelectedBooked(selectBooking);
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markupInlineBooking;
        markupInlineBooking = setInlineButtonEditBookingDetails();
        sendMessage.setChatId(chatId);
        sendMessage.setText(selectBookedDetails);
        sendMessage.setReplyMarkup(markupInlineBooking);
        return sendMessage;
    }

    //new code by woon
    private SendMessage editAdminRoomMessage(Message message, long chatId) {
        InlineKeyboardMarkup markupInlineRoom;
        SendMessage sendMessage = new SendMessage();
        markupInlineRoom = displayAdminRoom(chatId);

        // displayEditRoom
        sendMessage.setReplyMarkup(markupInlineRoom);
        String editMessage = "Please select the new room";
        sendMessage.setChatId(chatId);
        sendMessage.setText(editMessage);
        return sendMessage;
    }


    /////new code by wong

    private SendMessage editRoomMessage(Message message, long chatId) {

        InlineKeyboardMarkup markupInlineRoom;
        SendMessage sendMessage = new SendMessage();

        markupInlineRoom = setInlineButtonEditRoom(chatId);

        sendMessage.setReplyMarkup(markupInlineRoom);
        String editMessage = "Please select the new room";
        sendMessage.setChatId(chatId);
        sendMessage.setText(editMessage);
        return sendMessage;
    }
    /////new code by wong

    private InlineKeyboardMarkup setInlineButtonEditRoom(long chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        Room roomlist[] = databasaQuery.displayEditRoom();

        userRoomMap.get(chatId).setRoomlist(roomlist);



//        roomlist = databasaQuery.displayEditRoom();
        int length = 0; //check the length of listbutton in order to know create how much row of List<InlineKeyboardButton>
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        InlineKeyboardButton listButton[] = new InlineKeyboardButton[userRoomMap.get(chatId).getRoomlist().length];

        if (userRoomMap.get(chatId).getRoomlist().length % 2 == 0) {
            length = userRoomMap.get(chatId).getRoomlist().length / 2;
        } else if (userRoomMap.get(chatId).getRoomlist().length % 2 != 0) {
            length = (userRoomMap.get(chatId).getRoomlist().length / 2) + 1;

        }
        List<InlineKeyboardButton> row[] = (ArrayList<InlineKeyboardButton>[]) new ArrayList[length];


        for (int i = 0; i < userRoomMap.get(chatId).getRoomlist().length; i++) {
            listButton[i] = new InlineKeyboardButton();

            listButton[i].setText(userRoomMap.get(chatId).getRoomlist()[i].getRoomId());

            listButton[i].setCallbackData(userRoomMap.get(chatId).getRoomlist()[i].getRoomId() + 1);
        }

        int count = 0;

        for (int j = 0; j < row.length; j++) {
            row[j] = new ArrayList<>();

            row[j].add(listButton[count + j]);

            count += 1;
            if (!(count + j >= listButton.length)) {

                row[j].add(listButton[count + j]);
            }
            inlineButton.add(row[j]);
        }


        markupInline.setKeyboard(inlineButton);
        return markupInline;
    }

    /////new code by wong
//    private SendMessage displayNewRoom(Message message, String roomID,long chatId) {
//        SendMessage sendMessage = new SendMessage();
//        int bookID = userBookingMap.get(chatId).getBookingID();
//
//        databasaQuery.updateRoom(bookID, roomID);
//        InlineKeyboardMarkup markupInlineBack;
//        markupInlineBack = setInlineButtonGoBack();
//        sendMessage.setReplyMarkup(markupInlineBack);
//        String editMessage = "Data edited!";
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(editMessage);
//        return sendMessage;
//    }
    /////new code by wong

    private InlineKeyboardMarkup setInlineButtonGoBack() {

        InlineKeyboardMarkup markupInlineBack = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backEdit = new InlineKeyboardButton();
        InlineKeyboardButton backMain = new InlineKeyboardButton();

        backEdit.setText("Back Edit");
        backEdit.setCallbackData("backEditMenu");

        backMain.setText("Back Main");
        backMain.setCallbackData("backUserMain");

        row1.add(backEdit);
        row1.add(backMain);

        inlineButton.add(row1);

        markupInlineBack.setKeyboard(inlineButton);

        return markupInlineBack;
    }
    /////new code by wong

    private SendMessage editPurposeMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String newPurposeMessage = """
                You are required to insert new information regarding to the booking\s

                Please provide the booking purpose""";

        sendMessage.setText(newPurposeMessage);
        sendMessage.setChatId(chatId);
        userStateMap.put(chatId, updatePurpose);

        return sendMessage;
    }

    private SendMessage editdateMessage(Message message, String roomID, long chatId) {
        userBookingMap.get(chatId).setRoomID(roomID);
        //newBooking.setRoomID(roomID);

        SendMessage sendMessage = new SendMessage();
        String dateMessage = """
                Please choose the booking date:\s
                """;

        // date formatter get current local date
        // +1 until +7, 7 days to choose excluding today
        // ddf : date & day of week format (user-friendly)
        // dof : date only format (SQLite)

        DateTimeFormatter ddf = DateTimeFormatter.ofPattern("dd/MM/yyyy, EEEE");
        DateTimeFormatter dof = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime day;

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton>[] row = (ArrayList<InlineKeyboardButton>[]) new ArrayList[4];
        InlineKeyboardButton[] listButton = new InlineKeyboardButton[7];

        for (int i = 0; i < listButton.length; i++) {
            day = LocalDateTime.now().plusDays(i + 1);
            listButton[i] = new InlineKeyboardButton();
            listButton[i].setText(ddf.format(day));
            listButton[i].setCallbackData(dof.format(day) + 1);
            dateFormat[i] = dof.format(day);

        }


        int count = 0;

        for (int j = 0; j < row.length; j++) {
            row[j] = new ArrayList<>();
            row[j].add(listButton[count + j]);
            count += 1;

            if (!(count + j >= listButton.length)) {
                row[j].add(listButton[count + j]);
            }

            inlineButton.add(row[j]);
        }

        markupInline.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(dateMessage);
        sendMessage.setReplyMarkup(markupInline);
        userStateMap.put(chatId, sessionState);


        return sendMessage;
    }

    private SendMessage editsessionMessage(Message message, long chatId) {

        SendMessage sendMessage = new SendMessage();
        String sessionMessage = """
                Please select the session you wish to book the meeting room:
                """;

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton morningButton = new InlineKeyboardButton();
        InlineKeyboardButton afternoonButton = new InlineKeyboardButton();
        InlineKeyboardButton eveningButton = new InlineKeyboardButton();

        morningButton.setText("Morning Session");
        morningButton.setCallbackData("morning");

        afternoonButton.setText("Afternoon Session");
        afternoonButton.setCallbackData("afternoon");

        eveningButton.setText("Evening Session");
        eveningButton.setCallbackData("evening");

        row1.add(morningButton);
        row2.add(afternoonButton);
        row3.add(eveningButton);

        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        inlineMarkup.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(sessionMessage);
        sendMessage.setReplyMarkup(inlineMarkup);
        userStateMap.put(chatId, editSessionState);

        return sendMessage;
    }

    //    private SendMessage edittimeMessage(Message message, String session,long chatId) {
//
//// change 15/1/22
//        userBookingMap.get(chatId).setTimeSlot(databasaQuery.getTimeSlot(session));
//
//        SendMessage sendMessage = new SendMessage();
//        String timeMessage = "Please select the booking time\n";
//
//
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
//        List<InlineKeyboardButton>[] row = (ArrayList<InlineKeyboardButton>[]) new ArrayList[4];
//        InlineKeyboardButton[] listButton = new InlineKeyboardButton[4];
//
//
//        for (int i = 0; i < listButton.length; i++) {
//            listButton[i] = new InlineKeyboardButton();
//            listButton[i].setText(userBookingMap.get(chatId).getTimeSlot()[i]);
//            listButton[i].setCallbackData(userBookingMap.get(chatId).getTimeSlot()[i]+1);
//        }
//
//
//        for (int j = 0; j < row.length; j++) {
//
//            row[j] = new ArrayList<>();
//            row[j].add(listButton[j]);
//            inlineButton.add(row[j]);
//        }
//
//        markupInline.setKeyboard(inlineButton);
//
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(timeMessage);
//        sendMessage.setReplyMarkup(markupInline);
//
//
//        return sendMessage;
//    }
    private SendMessage edittimeMessage(Message message, String session, long chatId) {

        // change 15/1/22
        // new code by

        int index = 0;
        String booked[] = null;

        booked = databasaQuery.getBookedDate(userBookingMap.get(chatId).getRoomID(), userBookingMap.get(chatId).getDate());

        userBookingMap.get(chatId).setTimeSlot(databasaQuery.getTimeSlot(session));


        SendMessage sendMessage = new SendMessage();
        String timeMessage = "Please select the booking time\n";


        String b = null;


        for (String elements : userBookingMap.get(chatId).getTimeSlot()) {
            for (String a : booked) {
                if (elements.contains(a)) {
                    b = elements;

                    index = ArrayUtils.indexOf(userBookingMap.get(chatId).getTimeSlot(), b);
                }
            }
        }
        String[] result = new String[0];
        if (b != null) {
            result = ArrayUtils.remove(userBookingMap.get(chatId).getTimeSlot(), index);
            ;
        } else if (b == null) {
            result = userBookingMap.get(chatId).getTimeSlot();
        }
        //Checking section


        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton>[] row = (ArrayList<InlineKeyboardButton>[]) new ArrayList[result.length];
        InlineKeyboardButton[] listButton = new InlineKeyboardButton[result.length];

        for (int i = 0; i < result.length; i++) {
            listButton[i] = new InlineKeyboardButton();
            listButton[i].setText(result[i]);
            listButton[i].setCallbackData(result[i] + 1);
        }
        for (int j = 0; j < row.length; j++) {

            row[j] = new ArrayList<>();
            row[j].add(listButton[j]);
            inlineButton.add(row[j]);
        }

        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton backEdit = new InlineKeyboardButton();
        InlineKeyboardButton backMain = new InlineKeyboardButton();

        backEdit.setText("Back Edit");
        backEdit.setCallbackData("backEditMenu");

        backMain.setText("Back Main");
        backMain.setCallbackData("backUserMain");

        row1.add(backEdit);
        row1.add(backMain);

        inlineButton.add(row1);

        markupInline.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(timeMessage);
        sendMessage.setReplyMarkup(markupInline);

        userStateMap.put(chatId, confirmBookState);

        return sendMessage;
    }

    private SendMessage confirmEditMessage(Message message, long chatId) {

        SendMessage sendMessage = new SendMessage();
        String confirmationMessage =
                "Your Booking Details:\n\n" +
                        "Booking ID: " + userBookingMap.get(chatId).getBookingID() + "\n" +
                        "Room ID: " + userBookingMap.get(chatId).getRoomID() + "\n" +
                        "Booking Purpose: " + userBookingMap.get(chatId).getPurpose() + "\n" +
                        "Booking Date: " + userBookingMap.get(chatId).getDate() + "\n" +
                        "Booking Time: " + userBookingMap.get(chatId).getTime() + "\n\n" +
                        "Are you sure to edit?" ;

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        InlineKeyboardButton noButton = new InlineKeyboardButton();

        yesButton.setText("yes");
        yesButton.setCallbackData("yesedit");

        noButton.setText("no");
        noButton.setCallbackData("noedit");

        row1.add(yesButton);
        row2.add(noButton);

        inlineButton.add(row1);
        inlineButton.add(row2);

        inlineMarkup.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(confirmationMessage);
        sendMessage.setReplyMarkup(inlineMarkup);

        return sendMessage;
    }

    private SendMessage successEditMessage(Message message, String answer, long chatId) {

        SendMessage sendMessage = new SendMessage();
        String reply = "";

        if (answer.equals("yesedit")) {
            reply = """
                    Edit successful!
                              
                        """;

            databasaQuery.editBooking(userBookingMap.get(chatId));

        } else if (answer.equals("noedit")) {
            reply = """
                    Edit cancelled.
                                       

                    """;
        }

        sendMessage.setChatId(chatId);
        sendMessage.setText(reply);
        InlineKeyboardMarkup markupInlineBack;
        markupInlineBack = setInlineButtonGoBack();
        sendMessage.setReplyMarkup(markupInlineBack);

        userStateMap.put(chatId, decisionBackMainState);


        return sendMessage;
    }

    /////new code by wong

    private SendMessage updatePurposeMessage(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String newPurpose = message.getText();
        databasaQuery.udpatePurpose(userBookingMap.get(chatId).getBookingID(), newPurpose);
        InlineKeyboardMarkup markupInlineBack;
        markupInlineBack = setInlineButtonGoBack();
        sendMessage.setReplyMarkup(markupInlineBack);
        String editMessage = "Data successfully edited!";
        sendMessage.setChatId(chatId);
        userStateMap.put(chatId, decisionBookState);
        sendMessage.setText(editMessage);

        return sendMessage;
    }


    // All Yeap Works start from here
    private SendMessage bookingMessage(Message message, long chatId) {
        SendMessage sendMessage;
        switch (userStateMap.get(chatId)) {
            case purposeState:
                sendMessage = bookingPurpose(message, chatId);
                break;
            case roomIDState:

                sendMessage = roomMessage(message, chatId);
                break;
            case dateState:
                sendMessage = dateMessage(message, userRoomMap.get(chatId).getCurrentRoom().getRoomId(), chatId);
                break;
            case sessionState:
                sendMessage = sessionMessage(message, chatId);
                break;
            case timeState:
                sendMessage = timeMessage(message, userBookingMap.get(chatId).getSession(), chatId);
                break;
            case confirmBookState:
                sendMessage = confirmBookMessage(message, chatId);
                break;
            case successBookState:
                sendMessage = successBookMessage(message, answer, chatId);
                break;
            default:
                sendMessage = sendErrorMessage(message, chatId);
                break;

        }
        return sendMessage;

    }

    private SendMessage bookingPurpose(Message message, long chatId) {
        SendMessage sendMessage = new SendMessage();
        String purposeMessage = """
                You are required to insert some information in order to book a room\s

                Please provide the booking purpose
                """;

        sendMessage.setChatId(chatId);
        sendMessage.setText(purposeMessage);
        userStateMap.put(chatId, roomIDState);


        return sendMessage;
    }

    //HereWOon
    private SendMessage roomMessage(Message message, long chatId) {


        userBookingMap.get(chatId).setPurpose(message.getText());

        userBookingMap.get(chatId).setStaffID(userLoginMap.get(chatId).getStaffID());


        SendMessage sendMessage = new SendMessage();
        String roomMessage = "Please select the room based on the options below:";

        sendMessage.setText(roomMessage);
        sendMessage.setChatId(chatId);

        InlineKeyboardMarkup markupInline;
        markupInline = displayBookingRoom(chatId);

        sendMessage.setReplyMarkup(markupInline);
        userStateMap.put(chatId, dateState);

        return sendMessage;
    }

    private InlineKeyboardMarkup displayBookingRoom(long chatId) {
        userRoomMap.get(chatId).setRoomlist(databasaQuery.displayRoom());
//        roomlist = databasaQuery.displayRoom();

        //check the length of listbutton in order to know create how much row of List<InlineKeyboardButton>
        int length = 0;

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();

        InlineKeyboardButton[] listButton = new InlineKeyboardButton[userRoomMap.get(chatId).getRoomlist().length];

        if (userRoomMap.get(chatId).getRoomlist().length % 2 == 0) {
            length = userRoomMap.get(chatId).getRoomlist().length / 2;
        } else {
            length = (userRoomMap.get(chatId).getRoomlist().length / 2) + 1;
        }

        List<InlineKeyboardButton>[] row = (ArrayList<InlineKeyboardButton>[]) new ArrayList[length];

        for (int i = 0; i < userRoomMap.get(chatId).getRoomlist().length; i++) {
            listButton[i] = new InlineKeyboardButton();
            listButton[i].setText(userRoomMap.get(chatId).getRoomlist()[i].getRoomId());
            listButton[i].setCallbackData(userRoomMap.get(chatId).getRoomlist()[i].getRoomId() + 2);
        }

        int count = 0;

        for (int j = 0; j < row.length; j++) {
            row[j] = new ArrayList<>();
            row[j].add(listButton[count + j]);
            count += 1;

            if (!(count + j >= listButton.length)) {
                row[j].add(listButton[count + j]);
            }

            inlineButton.add(row[j]);
        }

        markupInline.setKeyboard(inlineButton);

        return markupInline;
    }

    private SendMessage dateMessage(Message message, String roomID, long chatId) {
        userBookingMap.get(chatId).setRoomID(roomID);
        //newBooking.setRoomID(roomID);

        SendMessage sendMessage = new SendMessage();
        String dateMessage = """
                Please choose the booking date:\s
                """;

        // date formatter get current local date
        // +1 until +7, 7 days to choose excluding today
        // ddf : date & day of week format (user-friendly)
        // dof : date only format (SQLite)

        DateTimeFormatter ddf = DateTimeFormatter.ofPattern("dd/MM/yyyy, EEEE");
        DateTimeFormatter dof = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime day;

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton>[] row = (ArrayList<InlineKeyboardButton>[]) new ArrayList[4];
        InlineKeyboardButton[] listButton = new InlineKeyboardButton[7];

        for (int i = 0; i < listButton.length; i++) {
            day = LocalDateTime.now().plusDays(i + 1);
            listButton[i] = new InlineKeyboardButton();
            listButton[i].setText(ddf.format(day));
            listButton[i].setCallbackData(dof.format(day));
            dateFormat[i] = dof.format(day);

        }


        int count = 0;

        for (int j = 0; j < row.length; j++) {
            row[j] = new ArrayList<>();
            row[j].add(listButton[count + j]);
            count += 1;

            if (!(count + j >= listButton.length)) {
                row[j].add(listButton[count + j]);
            }

            inlineButton.add(row[j]);
        }

        markupInline.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(dateMessage);
        sendMessage.setReplyMarkup(markupInline);
        userStateMap.put(chatId, sessionState);


        return sendMessage;
    }

    private SendMessage sessionMessage(Message message, long chatId) {

        SendMessage sendMessage = new SendMessage();
        String sessionMessage = """
                Please select the session you wish to book the room:
                """;

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton morningButton = new InlineKeyboardButton();
        InlineKeyboardButton afternoonButton = new InlineKeyboardButton();
        InlineKeyboardButton eveningButton = new InlineKeyboardButton();

        morningButton.setText("Morning Session");
        morningButton.setCallbackData("morning");

        afternoonButton.setText("Afternoon Session");
        afternoonButton.setCallbackData("afternoon");

        eveningButton.setText("Evening Session");
        eveningButton.setCallbackData("evening");

        row1.add(morningButton);
        row2.add(afternoonButton);
        row3.add(eveningButton);

        inlineButton.add(row1);
        inlineButton.add(row2);
        inlineButton.add(row3);

        inlineMarkup.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(sessionMessage);
        sendMessage.setReplyMarkup(inlineMarkup);
        userStateMap.put(chatId, timeState);


        return sendMessage;
    }


    //print the only time available for user to choose
    private SendMessage timeMessage(Message message, String session, long chatId) {

// change 15/1/22
// new code by

        int index = 0;
        String booked[] = null;

        booked = databasaQuery.getBookedDate(userBookingMap.get(chatId).getRoomID(), userBookingMap.get(chatId).getDate());

        userBookingMap.get(chatId).setTimeSlot(databasaQuery.getTimeSlot(session));


        SendMessage sendMessage = new SendMessage();
        String timeMessage = "Please select the booking time\n";


        String b = null;


        for (String elements : userBookingMap.get(chatId).getTimeSlot()) {

            for (String a : booked) {
                if (elements.contains(a)) {

                    b = elements;

                    index = ArrayUtils.indexOf(userBookingMap.get(chatId).getTimeSlot(), b);

                }
            }
        }
        String[] result = new String[0];
        if (b != null) {

            result = ArrayUtils.remove(userBookingMap.get(chatId).getTimeSlot(), index);
            ;
        } else if (b == null) {
            result = userBookingMap.get(chatId).getTimeSlot();
        }

        // display Timeslot result
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton>[] row = (ArrayList<InlineKeyboardButton>[]) new ArrayList[result.length];
        InlineKeyboardButton[] listButton = new InlineKeyboardButton[result.length];

        for (int i = 0; i < result.length; i++) {
            listButton[i] = new InlineKeyboardButton();
            listButton[i].setText(result[i]);
            listButton[i].setCallbackData(result[i]);
        }
        for (int j = 0; j < row.length; j++) {

            row[j] = new ArrayList<>();
            row[j].add(listButton[j]);
            inlineButton.add(row[j]);
        }
        List<InlineKeyboardButton> row1 = new ArrayList<>();


        InlineKeyboardButton backMain = new InlineKeyboardButton();

        backMain.setText("Back Main");
        backMain.setCallbackData("backUserMain");

        row1.add(backMain);

        inlineButton.add(row1);

        markupInline.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(timeMessage);
        sendMessage.setReplyMarkup(markupInline);

        userStateMap.put(chatId, confirmBookState);

        return sendMessage;
    }


    private SendMessage confirmBookMessage(Message message, long chatId) {

        SendMessage sendMessage = new SendMessage();
        String confirmationMessage =
                "Your Booking Details:\n\n" +
                        "Room ID: " + userBookingMap.get(chatId).getRoomID() + "\n" +
                        "Booking Purpose: " + userBookingMap.get(chatId).getPurpose() + "\n" +
                        "Booking Date: " + userBookingMap.get(chatId).getDate() + "\n" +
                        "Booking Time: " + userBookingMap.get(chatId).getTime() + "\n\n" +
                        "Are you sure to book?" ;

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        InlineKeyboardButton noButton = new InlineKeyboardButton();

        yesButton.setText("yes");
        yesButton.setCallbackData("yes");

        noButton.setText("no");
        noButton.setCallbackData("no");

        row1.add(yesButton);
        row2.add(noButton);

        inlineButton.add(row1);
        inlineButton.add(row2);

        inlineMarkup.setKeyboard(inlineButton);

        sendMessage.setChatId(chatId);
        sendMessage.setText(confirmationMessage);
        sendMessage.setReplyMarkup(inlineMarkup);
        userStateMap.put(chatId, successBookState);


        return sendMessage;
    }

    private SendMessage successBookMessage(Message message, String answer, long chatId) {

        SendMessage sendMessage = new SendMessage();
        String reply = "";

        if (answer.equals("yes")) {
            reply = """
                    Booking successful!
                    Thank you for booking the room.
                                        
                    Type 0 to return to main menu
                        """;

            databasaQuery.saveBooking(userBookingMap.get(chatId));
        } else if (answer.equals("no")) {
            reply = """
                    Booking cancelled.
                    Your did not book the room.
                                        
                    Type 0 to return to main menu
                    """;
        }

        sendMessage.setChatId(chatId);
        sendMessage.setText(reply);

        userStateMap.put(chatId, decisionBackMainState);


        return sendMessage;
    }




}
