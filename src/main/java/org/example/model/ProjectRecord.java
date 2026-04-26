package org.example.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ProjectRecord {

    private final long employeeID;
    private final long projectID;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
}
