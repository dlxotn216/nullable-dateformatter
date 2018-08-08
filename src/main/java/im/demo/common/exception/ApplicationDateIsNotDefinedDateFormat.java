package im.demo.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by taesu on 2018-08-08.
 */
public class ApplicationDateIsNotDefinedDateFormat extends NestedRuntimeException {
    public ApplicationDateIsNotDefinedDateFormat(String date) {
        super(date +"는 정의된 형식이 아닙니다.");
    }
}
