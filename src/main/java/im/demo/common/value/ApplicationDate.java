package im.demo.common.value;

import im.demo.common.exception.ApplicationDateIsNotDefinedDateFormat;
import im.demo.common.exception.IllegalApplicationDateFormatException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by taesu on 2018-08-07.
 */
@Getter
@NoArgsConstructor
@Embeddable
public class ApplicationDate {
    public static final String UNKNOWN = "UK";

    private static final String DIGIT_3_MONTH = "MMM";
    private static final String DIGIT_2_DAY = "dd";
    private static final String DIGIT_2_MONTH = "MM";

    private String date;

    private Boolean isDayUnknown;

    private Boolean isMonthUnknown;

    public ApplicationDate(LocalDate localDate) {
        this(String.format("%s-%s-%s", localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()));
    }

    /**
     * date는 아래와 같이 UK를 포함할 수 있다.
     * 2018-UK-UK
     * 2018-08-UK
     * <p>
     * UK인 경우엔 날짜 검색 필터 등에서 별도의 처리 없이 사용될 수 있도록
     * 가장 작은 값으로 처리한다. (1월과 1일)
     * <p>
     * ex) 2018-07-01 <= 2018-07-UK <= 2018-07-31
     * ex) 2018-01-01 <= 2018-UK-UK <= 2018-12-31
     * <p>
     * 따라서 {@link LocalDate#parse(CharSequence)} 메소드를 통해 처리할 경우 exception 발생 할 수 있으므로
     * 직접 split하여 처리한다.
     *
     * @param date
     */
    public ApplicationDate(String date) {
        Objects.requireNonNull(date, "date는 null이 될 수 없습니다");
        String[] ymd = date.split("-");
        if (ymd.length != 3) {
            throw new IllegalArgumentException();
        }

        this.isMonthUnknown = ObjectUtils.nullSafeEquals(ymd[1], UNKNOWN);
        if (this.isMonthUnknown) {
            ymd[1] = "01";
        }

        this.isDayUnknown = ObjectUtils.nullSafeEquals(ymd[2], UNKNOWN);
        if (this.isDayUnknown) {
            ymd[2] = "01";
        }

        this.date = Arrays.stream(ymd).collect(Collectors.joining("-"));
    }

    /**
     * UK 관련 정보를 포함하는 Date 문자열이 필요할 때 사용한다.
     * @return
     */
    public String getDate(){
        return this.getFormattedDate(ApplicationDateFormat.ISO_LOCAL_DATE);
    }

    /**
     * UK를 월, 일의 가장 작은 값(01월, 01일)으로 치환된
     * Date 문자열을 반환한다.
     *
     * Query 등 필터링이 필요할 때 주로 사용한다.
     *
     * @return UK가 가장 작은 값으로 치환 된 날짜
     */
    public String getDateForFiltering(){
        return this.date;
    }

    public String getFormattedDate(ApplicationDateFormat format) {
        return getFormattedDate(format, Locale.US);
    }

    /**
     * 현재 날짜를 전달된 <code>format</code>으로 포매팅한 문자열을 반환한다.
     * <p>
     * 각각 Unknown 관련 플래그에 따라 {@link #isDayUnknown}, {@link #isMonthUnknown}
     * 포매팅 된 문자열에서 Month, Day의 값을 "UK"로 치환한다.
     *
     * @param format ApplicationDateFormat Applicatoin에서 지원하는 DateFormat enum
     * @param locale Locale (yyyy-MMM-dd 포맷과 같이 8월, Aug로 표현이 필요한 경우를 위한 파라미터)
     * @return 포매팅 된 날짜 문자열
     * @throws ApplicationDateIsNotDefinedDateFormat 현재 날짜가 System에서 지원하는 형식(yyyy-MM-dd)이 아닐 경우
     */
    public String getFormattedDate(ApplicationDateFormat format, Locale locale) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(this.date, ApplicationDateFormat.ISO_LOCAL_DATE.getFormatter());
        } catch (DateTimeParseException | NullPointerException e) {
            throw new ApplicationDateIsNotDefinedDateFormat(this.date);
        }

        String formatted = format.getFormatter().withLocale(locale).format(localDate);

        List<String> replacedTargetDigits = new ArrayList<>();
        if (this.isMonthUnknown) {
            final String targetDigit = getTargetMonthDigit(format.getPattern());
            replacedTargetDigits.add(targetDigit);
            formatted = replaceToTargetDigit(formatted, format.getPattern(), targetDigit);
        }

        if (this.isDayUnknown) {
            final String targetDigit = getTargetDayDigit(format.getPattern());
            replacedTargetDigits.add(targetDigit);
            formatted = replaceToTargetDigit(formatted, format.getPattern(), targetDigit);
        }

        /*
        길이가 긴 Digit 부터 replace 호출이 필요하다
        formatted 문자열이 "2018-MMM-MM"이라고 가정할 때
        길이가 짧은 Digit "MM"부터 replace 할 경우
        "2018-0UK-MM"의 형태가 될 수 있다.
         */
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
            throw new IllegalApplicationDateFormatException(pattern);
        }
    }

    private String getTargetDayDigit(String pattern) {
        if (ApplicationDateFormat.getFormatterByPattern(pattern).getPattern().contains(DIGIT_2_DAY)) {
            return DIGIT_2_DAY;
        } else {
            throw new IllegalApplicationDateFormatException(pattern);
        }
    }

    private String replaceToTargetDigit(String formatted, String pattern, String targetDigit) {
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
                ", isDayUnknown=" + isDayUnknown +
                ", isMonthUnknown=" + isMonthUnknown +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ApplicationDate)) return false;

        ApplicationDate that = (ApplicationDate) object;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (isDayUnknown != null ? !isDayUnknown.equals(that.isDayUnknown) : that.isDayUnknown != null)
            return false;
        return isMonthUnknown != null ? isMonthUnknown.equals(that.isMonthUnknown) : that.isMonthUnknown == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (isDayUnknown != null ? isDayUnknown.hashCode() : 0);
        result = 31 * result + (isMonthUnknown != null ? isMonthUnknown.hashCode() : 0);
        return result;
    }
}
