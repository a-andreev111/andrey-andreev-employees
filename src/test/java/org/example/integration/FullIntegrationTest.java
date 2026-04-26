package org.example.integration;

import org.example.model.EmployeePair;
import org.example.model.EmployeePairStats;
import org.example.model.ProjectOverlap;
import org.example.service.ProjectAnalysisService;
import org.example.service.parser.CsvParserService;
import org.example.service.parser.DateParserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FullIntegrationTest {

    final ProjectAnalysisService service = new ProjectAnalysisService(new CsvParserService(new DateParserService()));

    @Test
    @DisplayName("Returns correct pair from a full valid CSV file.")
    public void whenValidFileIsSent_shouldReturnCorrectPair() {
        // GIVEN
        final Path csvFile = Path.of("src/test/resources/employees_full.csv");

        // WHEN
        final EmployeePairStats actual = service.analyzeProjectRecords(csvFile);

        // THEN
        assertNotNull(actual);
        assertEquals(EmployeePair.builder().employee1_ID(101L).employee2_ID(103L).build(), actual.getPair());
        assertEquals(216, actual.getTotalDaysWorked());
        assertEquals(2, actual.getOverlaps().size());

        final ProjectOverlap project1 = actual.getOverlaps().get(0);
        assertNotNull(project1);
        assertEquals(11L, project1.getProjectId());
        assertEquals(122, project1.getDaysWorked());

        final ProjectOverlap project2 = actual.getOverlaps().get(1);
        assertNotNull(project2);
        assertEquals(13L, project2.getProjectId());
        assertEquals(94, project2.getDaysWorked());
    }

    @Test
    @DisplayName("Throws exception if invalid line is found in the CSV file.")
    public void whenInvalidFileIsSent_shouldThrowException() {
        final Path csvFile = Path.of("src/test/resources/missing.csv");

        final UncheckedIOException exception =
                assertThrows(UncheckedIOException.class, () -> service.analyzeProjectRecords(csvFile));

        assertEquals("Failed to parse CSV", exception.getMessage());
    }

    @Test
    @DisplayName("Throws exception if invalid line is found in the CSV file.")
    public void whenInvalidLineIsFound_shouldThrowException() {
        final Path csvFile = Path.of("src/test/resources/employees_invalid_line.csv");

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> service.analyzeProjectRecords(csvFile));

        assertEquals("Invalid number of arguments in line: 101, 13,", exception.getMessage());
    }

    @Test
    @DisplayName("Throws exception if invalid ID is found in the CSV file.")
    public void whenInvalidIdIsFound_shouldThrowException() {
        final Path csvFile = Path.of("src/test/resources/employees_invalid_id.csv");

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> service.analyzeProjectRecords(csvFile));

        assertEquals("Invalid ID found: thirteen", exception.getMessage());
    }

    @Test
    @DisplayName("Throws exception if invalid period is found in the CSV file.")
    public void whenInvalidPeriodIsFound_shouldThrowException() {
        final Path csvFile = Path.of("src/test/resources/employees_invalid_period.csv");

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> service.analyzeProjectRecords(csvFile));

        assertEquals("Invalid date range found: from 2024-06-20 to 2023-11-01", exception.getMessage());
    }

    @Test
    @DisplayName("Throws exception if no overlap is found.")
    public void whenNoOverlapIsFound_shouldThrowException() {
        final Path csvFile = Path.of("src/test/resources/employees_no_overlap.csv");

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> service.analyzeProjectRecords(csvFile));

        assertEquals("No overlapping employee pairs were found in the list.", exception.getMessage());
    }
}
