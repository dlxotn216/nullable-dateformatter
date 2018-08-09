package im.demo.subject.application.service.impl;

import im.demo.common.util.ApplicationDateUtils;
import im.demo.common.value.ApplicationDate;
import im.demo.subject.application.dto.ConsentSignRequest;
import im.demo.subject.application.dto.VisitRequest;
import im.demo.subject.application.service.SubjectService;
import im.demo.subject.domain.Subject;
import im.demo.subject.domain.exception.IllegalSubjectStatusException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by taesu on 2018-08-09.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SubjectServiceImplTest {
    @Autowired
    private SubjectService subjectService;

    @Test
    public void visitTest() {
        //Given
        Subject subject = new Subject("S-001-001");
        subject = subjectService.save(subject);

        //When
        VisitRequest visitRequest = new VisitRequest(subject.getSubjectKey(), "2018-08-UK");
        subjectService.visit(visitRequest);

        //Then
        Subject saved = subjectService.findById(subject.getSubjectKey());
        assertThat(saved.getVisitDate()).isNotNull();
        assertThat(saved.getVisitDate().getDate()).isEqualTo("2018-08-UK");
    }

    @Test(expected = IllegalSubjectStatusException.class)
    public void 방문일_이전_동의_테스트() {
        //Given
        Subject subject = new Subject("S-001-002");
        subject = subjectService.save(subject);

        //When
        ConsentSignRequest consentSignRequest = new ConsentSignRequest(subject.getSubjectKey());
        subjectService.signConsent(consentSignRequest);

        //Then
        Assert.fail("방문일 이전에 동의서 서명이 불가해야 하지만 통과하였음");
    }

    @Test
    public void 동의서_서명_테스트() {
        //Given
        Subject subject = new Subject("S-001-003");
        subject = subjectService.save(subject);
        VisitRequest visitRequest = new VisitRequest(subject.getSubjectKey(), "2018-08-UK");
        subjectService.visit(visitRequest);

        //When
        Subject visited = subjectService.findById(subject.getSubjectKey());
        subjectService.signConsent(new ConsentSignRequest(visited.getSubjectKey()));

        //Then
        ApplicationDate currentApplicationDate = new ApplicationDate(ApplicationDateUtils.getCurrentDate());
        Subject consented = subjectService.findById(subject.getSubjectKey());
        assertThat(consented.getConsentDate()).isNotNull();
        assertThat(consented.getConsentDate()).isEqualTo(currentApplicationDate);
    }

}