package im.demo.subject.domain;

import im.demo.common.event.ApplicationEventPublisherUtils;
import im.demo.common.util.ApplicationDateUtils;
import im.demo.common.value.ApplicationDate;
import im.demo.subject.domain.event.SubjectConsentEvent;
import im.demo.subject.domain.exception.IllegalSubjectStatusException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;

/**
 * Created by taesu on 2018-08-07.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
public class Subject extends AbstractAggregateRoot<Subject> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBJECT_SEQ")
    @SequenceGenerator(name = "SUBJECT_SEQ", sequenceName = "SUBJECT_SEQ")
    private Long subjectKey;

    private String subjectId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "CONSENT_DATE"))
            , @AttributeOverride(name = "isDayUnknown", column = @Column(name = "CONSENT_DATE_IS_DAY_UK"))
            , @AttributeOverride(name = "isMonthUnknown", column = @Column(name = "CONSENT_DATE_IS_MON_UK"))
    })
    private ApplicationDate consentDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "VISIT_DATE"))
            , @AttributeOverride(name = "isDayUnknown", column = @Column(name = "VISIT_DATE_IS_DAY_UK"))
            , @AttributeOverride(name = "isMonthUnknown", column = @Column(name = "VISIT_DATE_IS_MON_UK"))
    })
    private ApplicationDate visitDate;

    public Subject(String subjectId) {
        this.subjectId = subjectId;
    }

    public void registerVisit(String visitDate){
        this.visitDate = new ApplicationDate(visitDate);
    }

    public void signConsent() {
        if (ObjectUtils.isEmpty(visitDate)) {
            throw new IllegalSubjectStatusException("방문하지 않은 대상자는 동의서에 서명할 수 없습니다");
        }

        this.consentDate = new ApplicationDate(ApplicationDateUtils.getCurrentDate());
//        super.registerEvent(new SubjectConsentEvent(this));
        //registerEvent로 등록 할 경우 Transaction 관련하여 움직이는 것 같다
        //https://zoltanaltfatter.com/2017/06/09/publishing-domain-events-from-aggregate-roots/
        //기존 spring-domain-events 프로젝트 샘플은 잘 동작하지만
        //현재 케이스에선 잘 동작하지 않음...
        //따라서 별도의 event publisher를 통해 처리했다
        ApplicationEventPublisherUtils.publishEvent(new SubjectConsentEvent(this));
    }

}
