/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author darwin
 */
public class DateTime {
    private String dateFormated(){
        String currentDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        currentDate = dateFormat.format(date);
        return currentDate;
    }
    
    private String timeFormated(){
        String currentTime;
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        currentTime = timeFormat.format(time);
        return currentTime;
    }
    
    private long unixtime(){
        long currentTime;
        currentTime = System.currentTimeMillis();
        return currentTime;
    }
    
    
    public long getUnixTime(){
        return unixtime();
    }
    
    
    public String getDateFormated(){
        return dateFormated();
    }
    
    public String getTimeFormated(){
        return timeFormated();
    }
}
