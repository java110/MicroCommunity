package com.java110.dto.system;

/**
 * 系统日志
 */
public class SystemLogDto {

    private static String logSwatch = "logSwatch";
    private static String LOG_SWATCH_ON = "ON";

    public static boolean getLogSwatch() {
        return logSwatch == LOG_SWATCH_ON ? true : false;
    }

    public static void setLogSwatch(String logSwatch) {
        logSwatch = logSwatch;
    }
}
