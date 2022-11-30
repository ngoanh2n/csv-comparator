package com.github.ngoanh2n.comparator;

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
public class WithHeadersTest {
    private final CsvComparisonSource source = CsvComparisonSource.create(
            Resource.getFile("com/github/ngoanh2n/comparator/exp/combine2.csv"),
            Resource.getFile("com/github/ngoanh2n/comparator/act/combine2.csv")
    );

    @Test
    @Order(1)
    void unselectColumns_WithColumnIdName_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId("email")
                .build();
        assertDoesNotThrow(() -> CsvComparator.compare(source, options));
    }

    @Test
    @Order(2)
    void unselectColumns_WithColumnIdName_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId("unknown")
                .build();
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(source, options));
    }

    @Test
    @Order(3)
    void unselectColumns_WithColumnIdIndex_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(0)
                .build();
        assertDoesNotThrow(() -> CsvComparator.compare(source, options));
    }

    @Test
    @Order(4)
    void unselectColumns_WithColumnIdIndex_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(4)
                .build();
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(source, options));
    }
}
