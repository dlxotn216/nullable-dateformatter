package im.demo.subject.domain.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by taesu on 2018-08-09.
 */
public class IllegalSubjectStatusException extends NestedRuntimeException {
    public IllegalSubjectStatusException(String msg) {
        super(msg);
    }
}
