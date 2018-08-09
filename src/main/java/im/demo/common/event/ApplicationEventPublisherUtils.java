package im.demo.common.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Created by taesu on 2018-08-09.
 */
@Service
public class ApplicationEventPublisherUtils {
    private static ApplicationEventPublisher publisher;

    public ApplicationEventPublisherUtils(ApplicationEventPublisher publisher) {
        ApplicationEventPublisherUtils.publisher = publisher;
    }


    public static void publishEvent(ApplicationEvent applicationEvent){
        publisher.publishEvent(applicationEvent);
    }

}
