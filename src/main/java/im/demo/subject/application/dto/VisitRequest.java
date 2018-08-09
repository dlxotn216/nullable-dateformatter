package im.demo.subject.application.dto;

import lombok.Value;

import java.time.LocalDate;

/**
 * Created by taesu on 2018-08-09.
 */
@Value
public class VisitRequest {
    private Long subjectKey;
    private String visitDate;
}
