package im.demo.common.value;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by taesu on 2018-08-07.
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicationDate {
    public static final String UNKNOWN = "UK";

    private static final String DIGIT_3_MONTH = "MMM";
    private static final String DIGIT_2_DAY = "dd";
    private static final String DIGIT_2_MONTH = "MM";

    private String date;

    private Boolean isDayOfMonthUnknown;

    private Boolean isYearOfMonthUnknown;

    /**
     * @param date
     */
    public ApplicationDate(String date) {
        String[] ymd = date.split("-");
        if (ymd.length != 3) {
            throw new IllegalArgumentException();
        }

        this.isYearOfMonthUnknown = ObjectUtils.nullSafeEquals(ymd[1], UNKNOWN);
        if (this.isYearOfMonthUnknown) {
            ymd[1] = "01";
        }

        this.isDayOfMonthUnknown = ObjectUtils.nullSafeEquals(ymd[2], UNKNOWN);
        if (this.isDayOfMonthUnknown) {
            ymd[2] = "01";
        }

        this.date = Arrays.stream(ymd).collect(Collectors.joining("-"));
    }

    public String getFormattedDate(String pattern) {
        return getFormattedDate(pattern, Locale.US);
    }

    public String getFormattedDate(String pattern, Locale locale) {
        LocalDate localDate = LocalDate.parse(this.date, ApplicationDateFormat.ISO_LOCAL_DATE.getFormatter());

        String formatted = ApplicationDateFormat.getFormatterByPattern(pattern)
                .getFormatter()
                .withLocale(locale)
                .format(localDate);

        List<String> replacedTargetDigits = new ArrayList<>();
        if (this.isYearOfMonthUnknown) {
            final String targetDigit = getTargetMonthDigit(pattern);
            replacedTargetDigits.add(targetDigit);
            formatted = getReplacedFormattedString(formatted, pattern, targetDigit);
        }

        if (this.isDayOfMonthUnknown) {
            final String targetDigit = getTargetDayDigit(pattern);
            replacedTargetDigits.add(targetDigit);
            formatted = getReplacedFormattedString(formatted, pattern, targetDigit);
        }

        //길이가 긴 DIGIT 순으로 정렬
        replacedTargetDigits.sort(Comparator.comparingInt(String::length));

        for (String targetDigit : replacedTargetDigits) {
            formatted = formatted.replaceAll(targetDigit, UNKNOWN);
        }

        return formatted;
    }

    private String getTargetMonthDigit(String pattern) {
        if (ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().contains(DIGIT_3_MONTH)) {
            return DIGIT_3_MONTH;
        } else if (ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().contains(DIGIT_2_MONTH)) {
            return DIGIT_2_MONTH;
        } else {
            throw new IllegalFormatFlagsException(pattern);
        }
    }

    private String getTargetDayDigit(String pattern) {
        if (ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().contains(DIGIT_2_DAY)) {
            return DIGIT_2_DAY;
        } else {
            throw new IllegalFormatFlagsException(pattern);
        }
    }

    private String getReplacedFormattedString(String formatted, String pattern, String targetDigit) {
        final int dayIndex = pattern.indexOf(targetDigit);

        StringBuilder builder = new StringBuilder(formatted);
        builder.replace(dayIndex, dayIndex + targetDigit.length(), targetDigit);
        formatted = builder.toString();

        return formatted;
    }

    @Override
    public String toString() {
        return "ApplicationDate{" +
                "date='" + date + '\'' +
                ", isDayOfMonthUnknown=" + isDayOfMonthUnknown +
                ", isYearOfMonthUnknown=" + isYearOfMonthUnknown +
                '}';
    }

}
