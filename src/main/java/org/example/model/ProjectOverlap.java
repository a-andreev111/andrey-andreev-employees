package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ProjectOverlap {

    private final long projectId;
    private final long daysWorked;
}
