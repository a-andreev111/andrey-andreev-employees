package org.example.service;

import org.example.model.EmployeePair;
import org.example.model.EmployeePairStats;
import org.example.model.ProjectRecord;
import org.example.service.parser.CsvParserService;
import org.example.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.example.util.TestHelper.FROM_1;
import static org.example.util.TestHelper.TO_1;
import static org.example.util.TestHelper.TO_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectAnalysisServiceTest {

    @Mock
    private CsvParserService csvParserService;

    @InjectMocks
    private ProjectAnalysisService projectAnalysisService;

    @Test
    @DisplayName("Throws exception if no overlap is found.")
    public void whenNoOverlapIsFound_shouldThrowException() {
        when(csvParserService.parseRecords(any())).thenReturn(Collections.emptyList());

        final Exception exception =
                assertThrows(IllegalArgumentException.class, () -> projectAnalysisService.analyzeProjectRecords(null));

        assertEquals("No overlapping employee pairs were found in the list.", exception.getMessage());
    }

    @Test
    @DisplayName("Calculates correct overlap and pair stats based on valid data.")
    public void whenOverlapIsFound_shouldReturnCorrectPair() {
        when(csvParserService.parseRecords(any())).thenReturn(TestHelper.getProjectRecords());

        final EmployeePairStats actual = projectAnalysisService.analyzeProjectRecords(null);

        assertEquals(EmployeePair.builder().employee1_ID(101L).employee2_ID(102L).build(), actual.getPair());
        assertEquals(66, actual.getTotalDaysWorked());
        assertEquals(1, actual.getOverlaps().size());
        assertEquals(10L, actual.getOverlaps().getFirst().getProjectId());
        assertEquals(66, actual.getOverlaps().getFirst().getDaysWorked());
    }

    @Test
    @DisplayName("Calculates correct day count when overlap falls on the same day.")
    public void whenOverlapIsSameDay_shouldReturnOneDay() {
        when(csvParserService.parseRecords(any())).thenReturn(List.of(
                ProjectRecord.builder().employeeID(101L).projectID(10L).dateFrom(FROM_1).dateTo(TO_1).build(),
                ProjectRecord.builder().employeeID(102L).projectID(10L).dateFrom(TO_1).dateTo(TO_2).build()
        ));

        final EmployeePairStats actual = projectAnalysisService.analyzeProjectRecords(null);

        assertEquals(1, actual.getTotalDaysWorked());
    }
}