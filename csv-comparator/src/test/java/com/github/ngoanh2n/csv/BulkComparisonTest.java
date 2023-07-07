package com.github.ngoanh2n.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ngoanh2n
 */
public class BulkComparisonTest {
    @Test
    void compare() {
        Path exp = Paths.get("src/test/resources/com/github/ngoanh2n/csv/exp/bulk_data");
        Path act = Paths.get("src/test/resources/com/github/ngoanh2n/csv/act/bulk_data");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(0)
                .build();
        CsvBulkComparisonResult result = CsvComparator.compare(exp, act, options);

        Assertions.assertTrue(result.hasDiff());
        Assertions.assertEquals(3, result.getDiffTotal());
        Assertions.assertEquals(3, result.getDiffResults().size());
    }
}
