package project;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.sql.SQLException;

public class main {
    
    public static void main(String[] args) {
        
        try {
            MyThread a = new MyThread();
            a.setDaemon(true);
            a.start();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new telegramBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }
    
}
