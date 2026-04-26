package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.EmployeePair;
import org.example.model.EmployeePairStats;
import org.example.model.ProjectRecord;
import org.example.service.parser.CsvParserService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProjectAnalysisService {

    private final CsvParserService csvParserService;

    /**
     * Analyze the project records from a given CSV file and
     * calculate the pair of employees that have worked together the longest
     * with their respective common projects and days worked in each.
     *
     * @param csvFile the CSV file with the project records
     * @return EmployeePairStats object
     */
    public EmployeePairStats analyzeProjectRecords(Path csvFile) {
        final List<ProjectRecord> allProjectRecords = csvParserService.parseRecords(csvFile);

        // Group project records by project
        final Map<Long, List<ProjectRecord>> projectsMap =
                allProjectRecords.stream().collect(Collectors.groupingBy(ProjectRecord::getProjectID));

        final Map<EmployeePair, EmployeePairStats> pairStatsMap = new HashMap<>();

        // For every project check all the employees and form pairs
        for (List<ProjectRecord> projectRecords : projectsMap.values()) {

            for (int i = 0; i < projectRecords.size(); i++) {
                for (int j = i + 1; j < projectRecords.size(); j++) {

                    final ProjectRecord record1 = projectRecords.get(i);
                    final ProjectRecord record2 = projectRecords.get(j);

                    if (record1.getEmployeeID() == record2.getEmployeeID()) {
                        continue;
                    }

                    final long daysTogether = calculateOverlap(record1, record2);

                    // If there is overlap between the time periods of the two employees, document the data accordingly
                    if (daysTogether > 0) {
                        final EmployeePair pair = new EmployeePair(record1.getEmployeeID(), record2.getEmployeeID());
                        final EmployeePairStats pairStats =
                                pairStatsMap.computeIfAbsent(pair, _ -> new EmployeePairStats(pair));

                        pairStats.addOverlap(record1.getProjectID(), daysTogether);
                    }
                }
            }
        }

        // Find the longest working pair and return their stats
        final EmployeePairStats longestWorkingPair = pairStatsMap.values().stream()
                .max(Comparator.comparing(EmployeePairStats::getTotalDaysWorked))
                .orElseThrow(() -> new IllegalArgumentException("No overlapping employee pairs were found in the list."));
        longestWorkingPair.sortOverlapsByDaysDesc();
        return longestWorkingPair;
    }

    /**
     * Calculate the overlapping period between two records.
     *
     * @param record1 first record
     * @param record2 second record
     * @return overlapping days for the two records (inclusive)
     */
    private static long calculateOverlap(ProjectRecord record1, ProjectRecord record2) {
        final LocalDate start = record1.getDateFrom().isAfter(record2.getDateFrom()) ? record1.getDateFrom() : record2.getDateFrom();
        final LocalDate end = record1.getDateTo().isBefore(record2.getDateTo()) ? record1.getDateTo() : record2.getDateTo();
        return start.isAfter(end) ? 0 : ChronoUnit.DAYS.between(start, end) + 1;
    }
}
