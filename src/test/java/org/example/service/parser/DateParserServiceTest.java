package org.example.service.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DateParserServiceTest {

    private final DateParserService service = new DateParserService();

    @Test
    @DisplayName("Returns current date if no date is sent.")
    public void whenNoStringValueIsSent_shouldReturnCurrentDate() {
        final LocalDate expected = LocalDate.now();

        assertEquals(expected, service.getDate(null));
        assertEquals(expected, service.getDate(""));
        assertEquals(expected, service.getDate(" "));
        assertEquals(expected, service.getDate("null"));
        assertEquals(expected, service.getDate("NULL"));
    }

    @Test
    @DisplayName("Returns correct date when parsing using different formats.")
    public void whenDifferentValidStringValuesAreSent_shouldReturnCorrectDate() {
        final LocalDate expected = LocalDate.of(2026, 1, 20);

        assertEquals(expected, service.getDate("2026-01-20"));
        assertEquals(expected, service.getDate("20-01-2026"));
        assertEquals(expected, service.getDate("01/20/2026"));
        assertEquals(expected, service.getDate("20/01/2026"));
        assertEquals(expected, service.getDate("2026/01/20"));
        assertEquals(expected, service.getDate("20 Jan 2026"));
        assertEquals(expected, service.getDate("20260120"));
    }

    @Test
    @DisplayName("Throws exception if invalid date is sent.")
    public void whenInvalidStringValuesAreSent_shouldThrowException() {
        final String date = "2026-20-20";
        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> service.getDate(date));

        assertEquals(String.format("Date %s cannot be parsed.", date), exception.getMessage());
    }
}