package org.example.service.parser;

import org.example.model.ProjectRecord;
import org.example.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

import static org.example.util.TestHelper.FROM_1;
import static org.example.util.TestHelper.FROM_2;
import static org.example.util.TestHelper.FROM_3;
import static org.example.util.TestHelper.TO_1;
import static org.example.util.TestHelper.TO_2;
import static org.example.util.TestHelper.TO_3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvParserServiceTest {

    @Mock
    private DateParserService dateParserService;

    @InjectMocks
    private CsvParserService csvParserService;

    @Test
    @DisplayName("Throws exception if invalid file is sent.")
    public void whenInvalidFileIsSent_ThrowException() {
        final Path csvFile = Path.of("src/test/resources/missing_file.csv");

        final UncheckedIOException exception =
                assertThrows(UncheckedIOException.class, () -> csvParserService.parseRecords(csvFile));

        assertEquals("Failed to parse CSV", exception.getMessage());
    }

    @Test
    @DisplayName("Throws exception if invalid line is found.")
    public void whenInvalidLineIsFound_ThrowException() {
        final Path csvFile = Path.of("src/test/resources/employees_invalid_line.csv");
        mockFirstAndSecondRecordDates();

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> csvParserService.parseRecords(csvFile));

        assertEquals("Invalid number of arguments in line: 101, 13,", exception.getMessage());
    }

    @Test
    @DisplayName("Throws exception if invalid ID is found.")
    public void whenInvalidIdIsFound_ThrowException() {
        final Path csvFile = Path.of("src/test/resources/employees_invalid_id.csv");
        mockFirstAndSecondRecordDates();

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> csvParserService.parseRecords(csvFile));

        assertEquals("Invalid ID found: thirteen", exception.getMessage());
    }

    @Test
    @DisplayName("Throws exception if invalid period is found.")
    public void whenInvalidPeriodIsFound_ThrowException() {
        final Path csvFile = Path.of("src/test/resources/employees_invalid_period.csv");

        mockFirstAndSecondRecordDates();
        mockThirdRecordDates();

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> csvParserService.parseRecords(csvFile));

        assertEquals("Invalid date range found: from 2024-06-20 to 2023-11-01", exception.getMessage());
    }

    @Test
    @DisplayName("Correctly parses a valid CSV file.")
    public void whenValidFileIsSelected_ReturnCorrectList() {
        // GIVEN
        final Path csvFile = Path.of("src/test/resources/employees_valid.csv");
        final List<ProjectRecord> expected = TestHelper.getProjectRecords();

        mockFirstAndSecondRecordDates();
        mockThirdRecordDates();

        // WHEN
        final List<ProjectRecord> actual = csvParserService.parseRecords(csvFile);

        // THEN
        assertEquals(expected.size(), actual.size());
        expected.forEach(record -> assertTrue(actual.contains(record)));
    }

    private void mockFirstAndSecondRecordDates() {
        when(dateParserService.getDate("2023-11-01")).thenReturn(FROM_1);
        when(dateParserService.getDate("2024-01-05")).thenReturn(TO_1);
        when(dateParserService.getDate("2022-05-16")).thenReturn(FROM_2);
        when(dateParserService.getDate(null)).thenReturn(TO_2);
    }

    private void mockThirdRecordDates() {
        when(dateParserService.getDate("2024-06-20")).thenReturn(FROM_3);
        when(dateParserService.getDate("NULL")).thenReturn(TO_3);
    }
}