package com.github.ngoanh2n.asserts.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ngoanh2n@gmail.com (Ho Huu Ngoan)
 */

class CsvComparatorTest {

    private final String diffPath = "build/csvDiff";

    @Test
    void checkAddedRows() {
        CsvComparator comparator = CsvComparator.builder()
                .onColumns(1, 2, 3)
                .onCsvFiles(
                        "com/github/ngoanh2n/asserts/csv/actual/addition-input.csv",
                        "com/github/ngoanh2n/asserts/csv/expected/addition-input.csv")
                .byIdentityColumn(0)
                .build();

        CsvComparisonResult result = comparator.saveDiffAt(diffPath).perform();
        Assertions.assertTrue(result.hasRowAdded());
        Assertions.assertTrue(result.hasDiff());
    }

    @Test
    void checkDeletedRows() {
        CsvComparator comparator = CsvComparator.builder()
                .onColumns("email", "firstname", "lastname")
                .onCsvFiles(
                        "com/github/ngoanh2n/asserts/csv/actual/deletion-input.csv",
                        "com/github/ngoanh2n/asserts/csv/expected/deletion-input.csv")
                .byIdentityColumn("email")
                .build();

        CsvComparisonResult result = comparator.saveDiffAt(diffPath).perform();
        Assertions.assertTrue(result.hasRowDeleted());
        Assertions.assertTrue(result.hasDiff());
    }

    @Test
    void checkModifiedRows() {
        CsvComparator comparator = CsvComparator.builder()
                .onColumns("email", "firstname", "lastname")
                .onCsvFiles(
                        "com/github/ngoanh2n/asserts/csv/actual/modification-input.csv",
                        "com/github/ngoanh2n/asserts/csv/expected/modification-input.csv")
                .byIdentityColumn(0)
                .build();

        CsvComparisonResult result = comparator.saveDiffAt(diffPath).perform();
        Assertions.assertTrue(result.hasRowModified());
        Assertions.assertTrue(result.hasDiff());
    }
}