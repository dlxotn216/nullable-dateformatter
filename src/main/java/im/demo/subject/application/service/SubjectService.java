package im.demo.subject.application.service;

import im.demo.subject.application.dto.ConsentSignRequest;
import im.demo.subject.application.dto.VisitRequest;
import im.demo.subject.domain.Subject;

/**
 * Created by taesu on 2018-08-09.
 */
public interface SubjectService {

    Subject save(Subject subject);

    Subject findById(Long subjectKey);

    void visit(VisitRequest request);

    void signConsent(ConsentSignRequest request);
}
