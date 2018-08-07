package im.demo.common.value;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Created by taesu on 2018-08-07.
 */
public enum ApplicationDateFormat {
    ISO_LOCAL_DATE("yyyy-MM-dd", DateTimeFormatter.ISO_LOCAL_DATE),
    SYS_DATE_01("yyyy-MMM-dd", DateTimeFormatter.ofPattern("yyyy-MMM-dd")),
    SYS_DATE_02("MMM, dd, yyyy", DateTimeFormatter.ofPattern("MMM, dd, yyyy"));

    private String pattern;
    private DateTimeFormatter formatter;

    ApplicationDateFormat(String pattern, DateTimeFormatter formatter) {
        this.pattern = pattern;
        this.formatter = formatter;
    }

    public String getPattern() {
        return pattern;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public static ApplicationDateFormat getFormatterByPattern(String pattern) {
        return Arrays.stream(values())
                .filter(applicationDateFormat -> applicationDateFormat.getPattern().equals(pattern))
                .findFirst()
                .orElse(ISO_LOCAL_DATE);
    }
}
