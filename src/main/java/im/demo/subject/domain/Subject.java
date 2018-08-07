package im.demo.subject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by taesu on 2018-08-07.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBJECT_SEQ")
    @SequenceGenerator(name = "SUBJECT_SEQ", sequenceName = "SUBJECT_SEQ")
    private Long subjectKey;

    private String subjectId;


}
