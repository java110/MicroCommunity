package com.java110.boot.importData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultImportDataAdapt {


    protected boolean hasSpecialCharacters(String str) {
        if (str.contains("-") || str.contains("#") || str.contains("?") || str.contains("&")) {
            return true;
        }

        return false;
    }

    protected boolean hasRoomSpecialCharacters(String str) {
        if ( str.contains("#") || str.contains("?") || str.contains("&")) {
            return true;
        }

        return false;
    }


    //解析Excel日期格式
    public static String excelDoubleToDate(String strDate) {
        if (strDate.length() == 5) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date tDate = DoubleToDate(Double.parseDouble(strDate));
                return sdf.format(tDate);
            } catch (Exception e) {
                e.printStackTrace();
                return strDate;
            }
        }
        return strDate;
    }


    //解析Excel日期格式
    public static Date DoubleToDate(Double dVal) {
        Date tDate = new Date();
        long localOffset = tDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));

        return tDate;
    }

}
