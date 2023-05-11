package com.java110.user;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class test {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2023);
        calendar.set(Calendar.MONTH,12);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.printf(dateFormat.format(calendar.getTime())+"");
    }
}
