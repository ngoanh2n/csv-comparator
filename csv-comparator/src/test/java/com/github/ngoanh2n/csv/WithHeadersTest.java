package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resources;
import com.github.ngoanh2n.RuntimeError;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WithHeadersTest {
    private final File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/combine2.csv");
    private final File act = Resources.getFile("com/github/ngoanh2n/csv/act/combine2.csv");

    @Test
    @Order(1)
    void unselectColumns_WithColumnIdName_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId("email")
                .build();
        assertDoesNotThrow(() -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(2)
    void unselectColumns_WithColumnIdName_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId("unknown")
                .build();
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(3)
    void unselectColumns_WithColumnIdIndex_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(0)
                .build();
        assertDoesNotThrow(() -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(4)
    void unselectColumns_WithColumnIdIndex_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(4)
                .build();
        assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }
}
