package org.example.util;

import org.example.model.ProjectRecord;

import java.time.LocalDate;
import java.util.List;

public class TestHelper {

    public static final LocalDate FROM_1 = LocalDate.of(2023, 11, 1);
    public static final LocalDate TO_1 = LocalDate.of(2024, 1, 5);
    public static final LocalDate FROM_2 = LocalDate.of(2022, 5, 16);
    public static final LocalDate TO_2 = LocalDate.now();
    public static final LocalDate FROM_3 = LocalDate.of(2024, 6, 20);
    public static final LocalDate TO_3 = LocalDate.now();

    public static List<ProjectRecord> getProjectRecords() {
        return List.of(
                ProjectRecord.builder().employeeID(101L).projectID(10L).dateFrom(FROM_1).dateTo(TO_1).build(),
                ProjectRecord.builder().employeeID(102L).projectID(10L).dateFrom(FROM_2).dateTo(TO_2).build(),
                ProjectRecord.builder().employeeID(101L).projectID(13L).dateFrom(FROM_3).dateTo(TO_3).build()
        );
    }
}
