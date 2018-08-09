package im.demo.common.util;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by taesu on 2018-08-09.
 */
public class ApplicationDateUtils {
    private static final ZoneId defaultZoneId = ZoneId.of("UTC");

    public static LocalDate getCurrentDate() {
        return LocalDate.now(defaultZoneId);
    }
}
