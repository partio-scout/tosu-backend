
package partio.service.validators;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestHelper {
    //helpers
    public static LocalDate DateNowPlusAmount(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.plusDays(days);
        date = date.plusMonths(months);
        date = date.plusYears(years);
        return date;
    }

    // watch out for goin over midnight
    public static LocalTime TimeNowPlusAmount(int hours, int minutes) {
        LocalTime time = LocalTime.now();
        time = time.plusMinutes(minutes);
        time = time.plusHours(hours);
        return time;
    }

    public static String makeStringLengthOf(int length, char toAppend) {
        StringBuilder sb = new StringBuilder(Math.max(length, 0));
        for (int i = 0; i < length; i++) {
            sb.append(toAppend);
        }
        return sb.toString();
    }


}
