package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resources;
import com.github.ngoanh2n.RuntimeError;
import org.junit.jupiter.api.*;

import java.io.File;

/**
 * @author ngoanh2n
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WithoutHeadersTest {
    private final File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/combination1.csv");
    private final File act = Resources.getFile("com/github/ngoanh2n/csv/act/combination1.csv");

    @Test
    @Order(1)
    void withColumns_WithColumnId_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumns(0, 1, 2)
                .selectColumnId(0)
                .build();
        Assertions.assertDoesNotThrow(() -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(2)
    void withColumns_WithColumnId_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumns(0, 1, 2)
                .selectColumnId(3)
                .build();
        Assertions.assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(3)
    void withColumns_WithoutColumnId() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumns(0, 1, 2)
                .build();
        Assertions.assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(4)
    void withoutColumns_WithColumnId_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumnId(0)
                .build();
        Assertions.assertDoesNotThrow(() -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(5)
    void withoutColumns_WithColumnId_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumnId(4)
                .build();
        Assertions.assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(6)
    void withoutColumns_WithoutColumnId() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .build();
        Assertions.assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }
}
