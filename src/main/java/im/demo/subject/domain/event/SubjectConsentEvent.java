package im.demo.subject.domain.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by taesu on 2018-08-09.
 */
public class SubjectConsentEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SubjectConsentEvent(Object source) {
        super(source);
    }
}