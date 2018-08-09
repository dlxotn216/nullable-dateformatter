package im.demo.subject.application.service.impl;

import im.demo.subject.application.dto.ConsentSignRequest;
import im.demo.subject.application.dto.VisitRequest;
import im.demo.subject.application.service.SubjectService;
import im.demo.subject.domain.Subject;
import im.demo.subject.domain.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by taesu on 2018-08-09.
 */
@Service
public class SubjectServiceImpl implements SubjectService {
    private SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject findById(Long subjectKey) {
        return subjectRepository.findById(subjectKey)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    @Override
    public void visit(VisitRequest request){
        findById(request.getSubjectKey()).registerVisit(request.getVisitDate());
    }

    @Transactional
    @Override
    public void signConsent(ConsentSignRequest request) {
        findById(request.getSubjectKey()).signConsent();
    }

}
