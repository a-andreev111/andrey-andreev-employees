package org.example.service.parser;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@NoArgsConstructor
public class DateParserService {

    private static final String EMPTY_DATE = "NULL";

    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_DATE, DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"), DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"), DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH),
            DateTimeFormatter.BASIC_ISO_DATE
    );

    /**
     * Parse a LocalDate object from a String.
     * If no date is passed (empty, blank or 'null'), then current date is returned.
     *
     * @param date the String to be parsed
     * @return LocalDate object
     */
    public LocalDate getDate(String date) {
        return isDateEmpty(date) ? LocalDate.now() : parseDateString(date);
    }

    /**
     * Checks if the date field is empty, blank or 'null'.
     * This could happen for the date_to field.
     *
     * @param date the String to be checked
     * @return whether the String date is empty, blank or 'null'
     */
    private static boolean isDateEmpty(String date) {
        return StringUtils.isBlank(date) || EMPTY_DATE.equalsIgnoreCase(date);
    }

    /**
     * Parse a String date into a LocalDate using a predefined list of formatters.
     *
     * @param date the String to be parsed
     * @return LocalDate object
     */
    private static LocalDate parseDateString(String date) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException exception) {
                // Swallow exception, try next formatter
            }
        }
        throw new IllegalArgumentException(String.format("Date %s cannot be parsed.", date));
    }
}
