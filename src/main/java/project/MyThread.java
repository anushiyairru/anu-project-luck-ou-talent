package project;

import project.database.DatabasaQuery;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MyThread extends Thread{

    Timer timer;
    DatabasaQuery databasaQuery;



    @Override
    public void run() {

        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY,00);
        date.set(Calendar.MINUTE, 01);
        date.set(Calendar.SECOND,0);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    DateTimeFormatter dof = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime day;

                    day = LocalDateTime.now();

                    databasaQuery = new DatabasaQuery();
                    databasaQuery.timerUpdate(dof.format(day));

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        };

        timer.scheduleAtFixedRate(task, date.getTime(),TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));


    }

}
