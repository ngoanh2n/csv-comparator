package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resource;
import com.github.ngoanh2n.RuntimeError;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WithoutHeadersTest {
    private final CsvComparisonSource source = CsvComparisonSource.create(
            Resource.getFile("com/github/ngoanh2n/csv/exp/combine1.csv"),
            Resource.getFile("com/github/ngoanh2n/csv/act/combine1.csv")
    );

    @Test
    @Order(1)
    void withColumns_WithColumnId_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumns(0, 1, 2)
                .selectColumnId(0)
                .build();
        assertDoesNotThrow(() -> CsvComparator.compare(source, options));
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
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(source, options));
    }

    @Test
    @Order(3)
    void withColumns_WithoutColumnId() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumns(0, 1, 2)
                .build();
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(source, options));
    }

    @Test
    @Order(4)
    void withoutColumns_WithColumnId_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumnId(0)
                .build();
        assertDoesNotThrow(() -> CsvComparator.compare(source, options));
    }

    @Test
    @Order(5)
    void withoutColumns_WithColumnId_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .selectColumnId(4)
                .build();
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(source, options));
    }

    @Test
    @Order(6)
    void withoutColumns_WithoutColumnId() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .withoutHeaders()
                .build();
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(source, options));
    }
}
