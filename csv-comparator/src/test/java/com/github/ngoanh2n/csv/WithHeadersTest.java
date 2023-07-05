package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resources;
import com.github.ngoanh2n.RuntimeError;
import org.junit.jupiter.api.*;

import java.io.File;

/**
 * @author ngoanh2n
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WithHeadersTest {
    private final File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/combination2.csv");
    private final File act = Resources.getFile("com/github/ngoanh2n/csv/act/combination2.csv");

    @Test
    @Order(1)
    void unselectColumns_WithColumnIdName_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId("email")
                .build();
        Assertions.assertDoesNotThrow(() -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(2)
    void unselectColumns_WithColumnIdName_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId("unknown")
                .build();
        Assertions.assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(3)
    void unselectColumns_WithColumnIdIndex_Valid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(0)
                .build();
        Assertions.assertDoesNotThrow(() -> CsvComparator.compare(exp, act, options));
    }

    @Test
    @Order(4)
    void unselectColumns_WithColumnIdIndex_Invalid() {
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(4)
                .build();
        Assertions.assertThrows(RuntimeError.class, () -> CsvComparator.compare(exp, act, options));
    }
}
