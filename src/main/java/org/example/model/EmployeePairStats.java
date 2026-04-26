package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class EmployeePairStats {

    private EmployeePair pair;
    private long totalDaysWorked;
    private List<ProjectOverlap> overlaps;

    public EmployeePairStats(EmployeePair pair) {
        this.pair = pair;
        this.overlaps = new ArrayList<>();
    }

    /**
     * Add days worked together for the pair on a given project.
     *
     * @param projectId the common project ID
     * @param days the total days worked together on the project
     */
    public void addOverlap(long projectId, long days) {
        this.totalDaysWorked += days;
        this.overlaps.add(ProjectOverlap.builder().projectId(projectId).daysWorked(days).build());
    }

    /**
     * Sort the project overlaps by days worked on each project.
     * This is done strictly for better UI visualization.
     */
    public void sortOverlapsByDaysDesc() {
        this.overlaps.sort(Comparator.comparing(ProjectOverlap::getDaysWorked).reversed());
    }
}
