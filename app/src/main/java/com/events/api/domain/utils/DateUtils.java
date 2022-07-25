package com.events.api.domain.utils;

import java.time.LocalDateTime;

public enum DateUtils {
    INSTANCE;

    public static LocalDateTime dateParamToTime(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));

        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    public static boolean dateIsToday(LocalDateTime date) {
        return LocalDateTime.now().getDayOfYear() == date.getDayOfYear();
    }
}
