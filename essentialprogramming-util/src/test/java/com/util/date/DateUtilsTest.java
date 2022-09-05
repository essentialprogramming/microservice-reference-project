package com.util.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class DateUtilsTest {

    @Test
    public void current_date() {

        final LocalDate date = LocalDate.now();

        OffsetDateTime startOfDay = DateUtil.startOfDay(date);
        System.out.println(startOfDay);


        OffsetDateTime endOfDay = DateUtil.endOfDay(date);
        System.out.println(endOfDay);

        OffsetDateTime startOfNextDay = DateUtil.startOfNextDay(date);
        System.out.println(startOfNextDay);


    }
}
