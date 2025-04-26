package nep.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime{
    
    private LocalDateTime now;
    
    public CurrentTime(){
        updateTime();
    }

    private void updateTime(){
        now = LocalDateTime.now();
    }

    public int getCurrentYear() {
        updateTime();
        return now.getYear();
    }

    public int getCurrentMonth(){
        updateTime();
        return now.getMonthValue();
    }

    public int getCurrentDay(){
        updateTime();
        return now.getDayOfMonth();
    }

    public String getCurrentTime(){
        updateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
}
