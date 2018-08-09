package im.demo.subject.domain.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by taesu on 2018-08-09.
 */
@Component
@Slf4j
public class SubjectConsentEventListener implements ApplicationListener<SubjectConsentEvent> {
    @Override
    public void onApplicationEvent(SubjectConsentEvent event) {
        log.info("Send mail to Subject {}", event.getSource());
    }
}
