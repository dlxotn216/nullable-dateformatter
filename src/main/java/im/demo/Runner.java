package im.demo;

import im.demo.common.util.ApplicationDateUtils;
import im.demo.common.value.ApplicationDate;
import im.demo.subject.application.dto.ConsentSignRequest;
import im.demo.subject.application.dto.VisitRequest;
import im.demo.subject.application.service.SubjectService;
import im.demo.subject.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by taesu on 2018-08-09.
 */
@Component
public class Runner implements ApplicationRunner{
    @Autowired
    private SubjectService subjectService;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        //Given
        Subject subject = new Subject("S-001-004");
        subject = subjectService.save(subject);
        VisitRequest visitRequest = new VisitRequest(subject.getSubjectKey(), "2018-08-UK");
        subjectService.visit(visitRequest);

        //When
        Subject visited = subjectService.findById(subject.getSubjectKey());
        subjectService.signConsent(new ConsentSignRequest(visited.getSubjectKey()));

        //Then
        ApplicationDate currentApplicationDate = new ApplicationDate(ApplicationDateUtils.getCurrentDate());
        Subject consented = subjectService.findById(subject.getSubjectKey());
    }
}
