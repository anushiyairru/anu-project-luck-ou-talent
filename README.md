## Requirements for Group Project
[Read the instruction](https://github.com/STIW3054-A221/class-activity-soc/blob/main/GroupProject.md)

## Group Info:


| No | Full Name  | Matrix Number  | Phone Number  | Email  | Photo                                                               |
| ---- | --- | --- | --- | --- |---------------------------------------------------------------------| 
| 1 | Woon Zhun Ping (Leader) | 279144  | 017-4313479  | acping3@gmail.com  | <img height="120" src="/images/1.jpg" width="85" max-width="100%"/> |
| 2 | Ibtihal Hasmad  | 274790  | 011-28084876  | hal.hasmad@gmail.com  | <img height="95" src="/images/2.jpeg" width="75" max-width="100%"/> |
| 3 | Wong Zi Qing  | 279199  | 011-11463110  | 2020decwzq@gmail.com  | <img height="95" src="/images/3.jpg" width="75" max-width="100%"/>  |
| 4 | Yeap Shu Qi  | 280038  | 010-3171266  | shuqiyeap@gmail.com  | <img height="95" src="/images/4.jpg" width="75" max-width="100%"/>  |
| 5 | Anushiya A/P Irrulappen  | 280956  | 01116550117 | anushiya0117@gmail.com  | <img height="95" src="/images/5.jpeg" width="75" max-width="100%"/> |


## Title of your application
## Introduction

The group project’s main goal is to create a telegram bot, that aims to provide a convenient way for users to book and manage meeting rooms in University Utara Malaysia . The program stores all data in an SQLite database and automatically deletes data after a week. A daemon thread is also implemented to monitor the data every day. The telegram bot has been specifically designed for the purpose of booking rooms within the University Utara Malaysia (UUM), is directed towards two main user groups, namely school administrators and users.
The capacity to update existing rooms' unique identification numbers, descriptions, room types, and maximum capacities is given to school administrators in addition to the ability to build new rooms. The accommodation booking procedure is made simpler and more effective for frequent users thanks to this feature, which guarantees that the data provided by the bot is correct and current.
To further facilitate the booking process, the bot also offers a thorough list of rooms that are available and are accessible to users. Before confirming their bookings, users can also edit their personal information through the bot, as well as adjust the specifics of their reservations, like the type of rooms they're booking and the time and date of their reservation.This bot is designed to accommodate a wide range of room sizes, from small ones to large ones, meeting the various needs of the user community. The bot can also produce a list of every user's information who reserved a room.This feature allows for easy management and tracking of room bookings. This Telegram bot aims to improve the booking process and provide a more user-friendly experience for school administrators and users.


## Flow Diagram of the requirements

![flow diagram](https://user-images.githubusercontent.com/104670097/212495101-0b75d06d-e602-4782-a470-e42a6b3de159.jpg)

## User manual for installing your application on Microsoft Azure

The step-by-step guidelines to deploy a Java Telegram bot to Heroku:




## User manual/guideline for testing the system

**ADMIN**<br>

When an admin initiates a conversation with the bot by sending the command "/start", they will be greeted with a welcoming message. They will then be presented with two options to choose from: option 1 for registration, and option 2 for login. To proceed, the admin simply has to reply to the bot with their desired choice.

**If the admin chooses to reply with "1" for registration**<br>

1.First, they will be asked to enter their Identity card number.
2.After inputting their Identity card number, the admin will be asked to insert their staff id.
3.They will then be prompted to enter their name.
4.Once their name is entered, the admin will be asked to enter their phone number.
5.Finally, the admin will be asked to enter their email address.

**If the admin chooses to reply with "2" for login**<br>

1.The process for login is simpler compared to registration
2.The admin will be prompted to enter their staff ID as the only required information for login
3.After entering their staff ID and submitting the information, the system will verify the admin's credentials
4.If the information is accurate, the admin will be granted access
5.The admin can proceed with their intended task or request after successful login.<br>

Once the admin has completed the registration or login process successfully, the chatbot will send a welcome message and present a few options for the admin to choose from. These options include Register Room, Edit Room, Make a Booking, Cancel Booking,Display Booking, Display All Booking,Edit Booking and Update Personal Info.
When a user initiates a conversation with the bot by sending the command "/start", they will be greeted with a welcoming message. They will then be presented with two options to choose from: option 1 for registration, and option 2 for login. To proceed, the user simply has to reply to the bot with their desired choice.<br>

**If admin chooses Register Room**<br>
1.The system prompts for room description
2.Once done user will have to enter the maximun capacity for that particular room
3.Lastly they will have to select the type os room they will be, meeting room, training room or laboratory.
4.The chatbot will send a success message

<br>
If the admin wish to edit room, Admin is prompted to select the room they wish to edit
the admin is presented with five options: Edit Name, Edit Description, Edit Max Capacity, Edit room type, and Back to Main Menu.
If Admin chooses to Edit Name, they will be prompted to enter the new name for the room
If the user chooses to Make a Booking, they will be prompted to select a room, session and time
If the user chooses to Cancel Booking, they will be prompted to enter the room booking ID. Once entered, the system will delete the booking and sends a success message
If the user chooses to Display All Booking, it will show all the bookings made by others
If the user chooses to Edit Booking, they will be asked to choose from four options: Room, Date, Time and Purpose
Lastly, if the user chooses to update personal info, they can edit their ICNo, name, staff ID, phone number, email and office telephone number.<br>

<br>
**USER**

**If the user chooses to reply with "1" for registration** <br>

1.First, they will be asked to enter their Identity card number.<br> 
2.After inputting their Identity card number, the user will be asked to insert their staff id.<br> 
3.They will then be prompted to enter their name.<br> 
4.Once their name is entered, the user will be asked to enter their phone number.<br> 
5.Finally, the user will be asked to enter their email address.<br> 


**If the user chooses to reply with "2" for login**<br> <br> 

1.The process for login is simpler compared to registration<br> 
2.The user will be prompted to enter their staff ID as the only required information for login<br> 
3.After entering their staff ID and submitting the information, the system will verify the user's credentials<br> 
4.If the information is accurate, the user will be granted access<br> 
5.The user can proceed with their intended task or request after successful login.<br> <br> 


Once the user has completed the registration or login process successfully, the chatbot will send a welcome message and present a few options for the user to choose from. These options include Apply as School Admin, Make a Booking, Display Booking, Cancel Booking, Edit Booking and Update Personal Info.

**If the user chooses the option to "Make a Booking"**<br> 

1.They will be prompted to complete a series of steps to finalize their booking<br> 
2.The user will first be asked to enter the purpose for their booking<br> 
3.After entering the purpose, the user will be presented with a list of rooms to choose from<br> 
4.Once the user has chosen a room, they will be prompted to select a date for the booking<br> 
5.After selecting the date, the user will have the option to choose an available session<br> 
6.Finally, the user will be asked to select a specific time for the booking.<br> <br> 


Once the user has completed the steps for making a booking, the system will display the user's booking details and prompt the user to confirm their choice of booking based on the provided details.

**If the user chooses the option to "Display Booking"**<br> 

1.The system will display again the room that the user has booked<br> 
2.This will provide the user with an opportunity to verify and review the details of their booking, such as the date, time, and purpose of the booking<br> 
3.This feature will enable the user to have a clear understanding of their booking<br> 
4.Ensure that they have made the correct booking<br> 
5.Also give them the chance to edit or cancel their booking if they find any errors or decide to change their plans.<br> 



**If the user chooses the option to "Edit Booking"**<br> 

1.The system will show the booking details again and then require the user to enter their booking ID to edit.<br> 
2.Users will have to enter their Booking ID.<br> 
3.The system will display four options to edit, Room, Date, Time, and Purpose.<br> 
4.User can choose any of the options, for example, if they choose Purpose.<br> 
5.The system will prompt the user to re-state their purpose of booking the room.<br> 
6.Then the system will re-display their booking for that Booking ID.<br> 


**If the user chooses the option to "Booking Cancel"**<br> 

1.The system will show the booking details again and then require the user to enter their booking ID to cancel.<br> 
2.Users will have to enter their Booking ID.<br> 
3.Once the user enters the booking ID, the system will delete the booking and send a message to confirm that the booking has been successfully deleted.<br> 

This feature will enable the user to cancel a booking that they no longer require or change plans. It will also give them the chance to verify and review the details of the booking that they want to cancel and ensure that it is the correct booking before they confirm the cancellation.


**If the user chooses the option to "Update Personal Info"**<br> 

1.The system will display four options for the user to choose from, which include ICNo, Name, StaffId, and Phone No.<br> 
2.The user can click on any of the options. For example, if the user clicks on Name.<br> 
3.The system will prompt the user to re-enter their name.<br> 

This feature will allow the user to update their personal information, such as their name, staff ID, IC number, or phone number. It will also give them the chance to verify and review their personal information and ensure that it is accurate before they confirm the changes. It will also give them the chance to update their contact information in case they have a new phone number or email address.


**If the user chooses the option to "Apply as School Admin"**<br> 

1.The system will require the user to insert their office number.<br> 
2.The user will then be prompted to choose the school they are from.<br> 
3.The user's email will be processed and approved later by the system or the relevant authorities.<br> 

This feature will allow the user to apply for the role of a school administrator by providing their office number and the school they are from. The system or the relevant authorities will then process the user's email for approval. This will enable the user to have access to the school administrator's portal and perform administrative tasks such as creating and managing bookings, managing users and other administrative tasks.


## Result/Output (Screenshot of the output)

**USER**<br>
![Screenshot_20230120_044401](https://user-images.githubusercontent.com/104670097/213655543-1cbc92a3-6eef-4475-9d48-4138c0c19c2a.png)<br>
![Screenshot_20230120_044417](https://user-images.githubusercontent.com/104670097/213655643-f1660e0b-c6b2-40fa-8deb-00d776cdd4bf.png)<br>
![Screenshot_20230120_044443](https://user-images.githubusercontent.com/104670097/213655673-2d1e7448-edf7-42a2-9a3a-5c7fa010b3ca.png)<br>
![Screenshot_20230120_044501](https://user-images.githubusercontent.com/104670097/213655707-21f4d966-21db-4140-b650-e5382cca31d9.png)<br>
![Screenshot_20230120_044523](https://user-images.githubusercontent.com/104670097/213655745-3a71f6b6-04a8-4497-8c97-d74c279d7d8b.png)<br>

![Screenshot_20230120_044547](https://user-images.githubusercontent.com/104670097/213655757-2ca3e1a0-7ba0-4f33-85c1-9ff4204e17cb.png)<br>
![Screenshot_20230120_044557](https://user-images.githubusercontent.com/104670097/213655771-4464a6f7-b93b-441c-8ccb-f88f071dea67.png)<br>
![Screenshot_20230120_044606](https://user-images.githubusercontent.com/104670097/213655781-243f41de-4d94-4d02-a0f9-c9b5e033b119.png)<br>
![Screenshot_20230120_044620](https://user-images.githubusercontent.com/104670097/213655794-025e1759-db4c-4089-935d-ba9fd72e487a.png)<br>
![Screenshot_20230120_044730](https://user-images.githubusercontent.com/104670097/213655806-7601f4b6-ae06-4053-b682-b01ec985c0f5.png)<br>
![Screenshot_20230120_044750](https://user-images.githubusercontent.com/104670097/213655824-42baa4e4-f2b3-434d-9ab2-d0bc99756559.png)<br>
![Screenshot_20230120_044759](https://user-images.githubusercontent.com/104670097/213655849-1e0fad5d-33d3-41a2-bbe5-390add8bf5a8.png)<br>
![Screenshot_20230120_044808](https://user-images.githubusercontent.com/104670097/213655873-cbcfc89e-a7c0-417d-b59f-382616560394.png)<br>

![Screenshot_20230120_044816](https://user-images.githubusercontent.com/104670097/213655885-f8e495d6-2e8f-4e21-809e-0f2b080eb2ef.png)<br>
![Screenshot_20230120_044834](https://user-images.githubusercontent.com/104670097/213656437-37dbd903-7e15-42e4-b28e-df01249ba6e3.png)<br>

![Screenshot_20230120_044851](https://user-images.githubusercontent.com/104670097/213656444-141472c0-e8fc-44d3-8f69-45c069aaeebd.png)<br>

![Screenshot_20230120_045103](https://user-images.githubusercontent.com/104670097/213656498-735eeb0e-edad-4726-a67d-c1b70e97ab30.png)<br>

![Screenshot_20230120_045110](https://user-images.githubusercontent.com/104670097/213656512-96f603d0-a8af-4428-ab62-72156a11fb8f.png)<br>
![Screenshot_20230120_045119](https://user-images.githubusercontent.com/104670097/213656524-f9f187d3-3a90-44ca-87c4-6ef0ca84c58e.png)<br>

![Screenshot_20230120_045129](https://user-images.githubusercontent.com/104670097/213656598-124128f7-9682-4b4a-9427-4bee5e1851b6.png)<br>

![Screenshot_20230120_045141](https://user-images.githubusercontent.com/104670097/213656627-2faee448-49e4-4955-8b8b-ffd272b2a596.png)<br>

![Screenshot_20230120_045150](https://user-images.githubusercontent.com/104670097/213656643-c178a335-6b80-40b2-93a2-5f197f236cbb.png)<br>

![Screenshot_20230120_045159](https://user-images.githubusercontent.com/104670097/213656666-8da4f35e-eae2-47c1-b6b4-48e5c05f694d.png)<br>
![Screenshot_20230120_045206](https://user-images.githubusercontent.com/104670097/213656709-6118c753-d728-4a17-91d1-42a4ae646b9b.png)<br>

![Screenshot_20230120_045215](https://user-images.githubusercontent.com/104670097/213656730-1a9df17b-97b2-4d7f-bf7a-2805014171de.png)<br>
![Screenshot_20230120_045223](https://user-images.githubusercontent.com/104670097/213656740-89e7c4f4-6151-4b83-b072-9627b0a759df.png)<br>
![Screenshot_20230120_045231](https://user-images.githubusercontent.com/104670097/213656750-44b0bdd2-1d04-4784-8d8c-023e15a04aeb.png)<br>
![Screenshot_20230120_045239](https://user-images.githubusercontent.com/104670097/213656758-34d7a665-7d93-4f1b-b9db-552517b46b75.png)<br>
**ADMIN**<br>
![Screenshot_20230120_052215](https://user-images.githubusercontent.com/104670097/213661369-5a7ca776-89ba-48c3-9036-95dac5a2a902.png)<br>

![Screenshot_20230120_052238](https://user-images.githubusercontent.com/104670097/213661399-995bd38b-9ad0-4be0-9005-87387c33dad2.png)<br>
![Screenshot_20230120_052253](https://user-images.githubusercontent.com/104670097/213661405-3f4a7cd6-ecb7-4edc-be43-5e2a5212a66b.png)<br>

![Screenshot_20230120_052304](https://user-images.githubusercontent.com/104670097/213661417-e4614471-8f8a-46c8-847a-1f06a2267103.png)<br>

![Screenshot_20230120_052314](https://user-images.githubusercontent.com/104670097/213661429-d3ca4e1c-850f-4235-941e-fe6512141902.png)<br>
![Screenshot_20230120_052341](https://user-images.githubusercontent.com/104670097/213661440-31ceef4d-0865-45f1-9760-24daac4745f8.png)<br>

![Screenshot_20230120_052402](https://user-images.githubusercontent.com/104670097/213661455-b2a98eec-d39d-4e9e-b139-7820845c09a7.png)<br>
![Screenshot_20230120_052414](https://user-images.githubusercontent.com/104670097/213661535-24f5c7a4-00f3-4e0b-ab47-bdbe7301d058.png)<br>

![Screenshot_20230120_052425](https://user-images.githubusercontent.com/104670097/213661556-3b0da9ab-0124-4b5f-902b-fd14ef4f7978.png)<br>

![Screenshot_20230120_052436](https://user-images.githubusercontent.com/104670097/213661570-251e4c41-7c7f-4bf9-b99c-2b355cc67655.png)<br>
![Screenshot_20230120_052447](https://user-images.githubusercontent.com/104670097/213661587-902914f8-2150-4814-aef5-a3dcc9d44162.png)<br>

![Screenshot_20230120_052456](https://user-images.githubusercontent.com/104670097/213661639-02cb259d-74d7-448a-8a29-665cc6f6c488.png)<br>

![Screenshot_20230120_052505](https://user-images.githubusercontent.com/104670097/213661652-ba9a47e4-5f0e-4d9a-b0b2-67d5f2a3f8ff.png)<br>


![Screenshot_20230120_052515](https://user-images.githubusercontent.com/104670097/213661655-cec18942-8754-4e24-ae00-2b31ae586da6.png)<br><br>


## Use Case Diagram
<img src="/images/UseCaseDiagram.png"/>

## UML Class Diagram
<img src="/images/umlClassDiagram.png"/>

## Database Design
<img src="/images/DatabaseDesign.png"/>

## Youtube Presentation
## References (Not less than 20)

Vaghela, V. (2020, December 22). How to create a Telegram bot using java? - Viral Vaghela - Medium. Medium; Medium. https://vaghelaviral.medium.com/how-to-create-a-telegram-bot-using-java-5710bed16c0f

‌
rubenlagus. (2022, November 8). Getting Started · rubenlagus/TelegramBots Wiki. GitHub. https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started

‌
sprkv5. (2014, November 14). JDBC SQLite PreparedStatement update and delete. Stack Overflow. https://stackoverflow.com/questions/26940149/jdbc-sqlite-preparedstatement-update-and-delete

‌
SQLite Java: Deleting Data. (2022, August 28). SQLite Tutorial. https://www.sqlitetutorial.net/sqlite-java/delete/

‌
cc. (2009, October 3). JDBC - prepareStatement - How should I use it? Stack Overflow. https://stackoverflow.com/questions/1515043/jdbc-preparestatement-how-should-i-use-it

‌
Sanjar Suvonov. (2020). Java Telegram Bot. Lesson 3: InlineKeyboardButton [YouTube Video]. In YouTube. https://www.youtube.com/watch?v=jUiHPVR-IYg&list=PLAU9HciqfTIn1UuSYKqo0-UKqvAltacoG&index=4&ab_channel=SanjarSuvonov

‌
Fizz. (2020). Configuration JDBC drive with intlij IDEA || No suitable driver found for jdbc [YouTube Video]. In YouTube. https://www.youtube.com/watch?v=duHgwpYLKZE&ab_channel=Fizz

‌
sairumi. (2022). STIW3054 Assignment 2 - 277838 (Telegram Bot) [YouTube Video]. In YouTube. https://www.youtube.com/watch?v=_EK5Xd8rkgM&ab_channel=sairumi

‌
Azamat Ordabekov. (2021). How to create Telegram Bot in Java [YouTube Video]. In YouTube. https://www.youtube.com/watch?v=XjOnp8TVNSQ&t=146s&ab_channel=AzamatOrdabekov


Lesson 1. Simple echo bot - Java Telegram Bot Tutorial. (2022). Gitbook.io. https://monsterdeveloper.gitbook.io/java-telegram-bot-tutorial/chapter1


Choo, A. (2019, August 14). Build a Serverless Telegram Bot with Firebase Functions. Medium; Medium. https://medium.com/@aaroncql/build-a-serverless-telegram-bot-with-firebase-functions-267d251e4e46

‌
Akhromieiev, R. (2021, January 14). Building a Telegram Bot With Firebase Cloud Functions and Telegraf.js. Akhromieiev.com. https://akhromieiev.com/building-telegram-bot-with-firebase-cloud-functions-and-telegrafjs/


Comparing Telegram Bot Hosting Providers - Code Capsules. (2023). Codecapsules.io. https://codecapsules.io/docs/comparisons/comparing-telegram-bot-hosting-providers/

‌

Deploying with Git | Heroku Dev Center. (2022). Heroku.com. https://devcenter.heroku.com/articles/git

‌

Isakovinc. (2022, June 22). Deploy Java Telegram Bot on Heroku Server - Isakovinc - Medium. Medium; Medium. https://medium.com/@learntodevelop2020/deploy-java-telegram-bot-on-heroku-server-42bfcfc311d3


IntelliJ IDEA Help. (2022). IntelliJ IDEA Help; JetBrains. https://www.jetbrains.com/help/idea/connecting-to-a-database.html


rubenlagus. (2018, August 9). TelegramBotsExample/WeatherHandlers.java at 1b558e6f87645d7ab0d66fa966bae5dbd8b2998a · rubenlagus/TelegramBotsExample. GitHub. https://github.com/rubenlagus/TelegramBotsExample/blob/1b558e6f87645d7ab0d66fa966bae5dbd8b2998a/src/main/java/org/telegram/updateshandlers/WeatherHandlers.java#L571


Daemon Thread in Java. (2017, May 15). GeeksforGeeks; GeeksforGeeks. https://www.geeksforgeeks.org/daemon-thread-java/


Using the Timer and TimerTask Classes. (2023). Iitk.ac.in. https://www.iitk.ac.in/esc101/05Aug/tutorial/essential/threads/timer.html


‌Jannik. (2015, August 26). Run java function every hour. Stack Overflow. https://stackoverflow.com/questions/32228345/run-java-function-every-hour


Check if Email Address is Valid or not in Java. (2017, October 6). GeeksforGeeks; GeeksforGeeks. https://www.geeksforgeeks.org/check-email-address-valid-not-java/

‌
admin. (2018, April 10). Java regex validate 10 digit number. W3schools. https://www.w3schools.blog/validate-10-digit-number-regular-expression-regex-java

‌
How to validate a Username using Regular Expressions in Java. (2020, March 11). GeeksforGeeks; GeeksforGeeks. https://www.geeksforgeeks.org/how-to-validate-a-username-using-regular-expressions-in-java/

‌

‌
## JavaDoc
