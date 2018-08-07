package im.demo.common.value;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by taesu on 2018-08-07.
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicationDate {
    public static final String UNKNOWN = "UK";

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

        if (this.isYearOfMonthUnknown) {
            if (ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().contains("MMM")) {
                int dayIndex = ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().indexOf("MMM");
                StringBuilder builder = new StringBuilder(formatted);
                builder.replace(dayIndex, dayIndex + 3, "UK");
                formatted = builder.toString();
            } else if (ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().contains("MM")) {
                int dayIndex = ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().indexOf("MM");
                StringBuilder builder = new StringBuilder(formatted);
                builder.replace(dayIndex, dayIndex + 2, "UK");
                formatted = builder.toString();
            }
        }

        if (this.isDayOfMonthUnknown) {
            int dayIndex = ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().indexOf("dd");
            StringBuilder builder = new StringBuilder(formatted);
            builder.replace(dayIndex, dayIndex + 2, "UK");
            formatted = builder.toString();
        }

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
