package com.shahed.taskmanagementsystem.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    void testConvertOffsetDtoToInternalDto_withNullInput() {
        assertNull(DateUtils.convertOffsetDtoToInternalDto(null));
    }

    @Test
    void testConvertOffsetDtoToInternalDto_withValidInput() {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2024, 3, 2, 10, 0, 0, 0, ZoneOffset.ofHours(6));
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2024, 3, 2, 4, 0, 0);

        LocalDateTime actualLocalDateTime = DateUtils.convertOffsetDtoToInternalDto(offsetDateTime);

        assertEquals(expectedLocalDateTime, actualLocalDateTime);
    }
}