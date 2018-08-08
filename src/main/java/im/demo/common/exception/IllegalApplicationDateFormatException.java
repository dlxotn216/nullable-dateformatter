package im.demo.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by taesu on 2018-08-08.
 */
public class IllegalApplicationDateFormatException extends NestedRuntimeException {

    public IllegalApplicationDateFormatException() {
        super("지원하지 않는 Date format 입니다.");
    }

    public IllegalApplicationDateFormatException(String pattern) {
        super(pattern + "은 지원하지 않는 Date format 입니다.");
    }

    public IllegalApplicationDateFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
