package org.example.service.parser;

import lombok.AllArgsConstructor;
import org.example.model.ProjectRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CsvParserService {

    private final DateParserService dateParserService;

    /**
     * Parse data from a selected CSV file with line format
     * [employee_id, project_id, date_from, date_to].
     *
     * @param csvFile the CSV file to be parsed
     * @return a list of all project records from the file
     */
    public List<ProjectRecord> parseRecords(Path csvFile) {
        try (BufferedReader reader = Files.newBufferedReader(csvFile)) {
            return reader.lines()
                    .map(this::mapProjectRecord)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse CSV", e);
        }
    }

    private ProjectRecord mapProjectRecord(String line) {
        final List<String> fields = Arrays.stream(line.split(","))
                .map(String::trim)
                .toList();

        if (fields.size() < 3) {
            throw new IllegalArgumentException(String.format("Invalid number of arguments in line: %s", line));
        }

        final long employeeID = extractID(fields.get(0));
        final long projectID = extractID(fields.get(1));
        final LocalDate from = dateParserService.getDate(fields.get(2));
        final LocalDate to = dateParserService.getDate(fields.size() == 4 ? fields.get(3) : null);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    String.format("Invalid date range found: from %s to %s", from, to));
        }

        return ProjectRecord.builder().employeeID(employeeID).projectID(projectID).dateFrom(from).dateTo(to).build();
    }

    private static long extractID(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(String.format("Invalid ID found: %s", id));
        }
    }
}
