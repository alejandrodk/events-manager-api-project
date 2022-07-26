package com.events.api.unit.utils;

import com.events.api.domain.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DateUtilsTest {
    @Test
    public void shouldParseStringParamToDate() {
        String param = "20220101";
        LocalDateTime result = DateUtils.dateParamToTime(param);

        assertThat(result.getYear(), equalTo(2022));
        assertThat(result.getMonthValue(), equalTo(1));
        assertThat(result.getDayOfMonth(), equalTo(1));
    }

    @Test
    public void shouldValidateIfProvidedDateIsToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        boolean isTodayTrue = DateUtils.dateIsToday(now);
        boolean isTodayFalse = DateUtils.dateIsToday(tomorrow);

        assertThat(isTodayTrue, equalTo(true));
        assertThat(isTodayFalse, equalTo(false));
    }
}
