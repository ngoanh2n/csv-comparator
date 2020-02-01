package com.github.ngoanh2n.asserts.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author ngoanh2n@gmail.com (Ho Huu Ngoan)
 */

class CsvComparatorTest {

    @Test
    void checkAddedRows() throws IOException {
        CsvComparator comparator = CsvComparator.builder()
                .onColumns(1, 2, 3)
                .onCsvFiles(
                        "com/github/ngoanh2n/asserts/csv/actual/addition-input.csv",
                        "com/github/ngoanh2n/asserts/csv/expected/addition-input.csv")
                .byIdentityColumn(0)
                .build();

        CsvComparisonResult result = comparator.saveDiffAt("build/csv-diff/added").perform();
        Assertions.assertTrue(result.hasDiff());
        Assertions.assertTrue(result.hasRowAdded());
        Assertions.assertTrue(result.getAddedRows().exists());
    }

    @Test
    void checkDeletedRows() throws IOException {
        CsvComparator comparator = CsvComparator.builder()
                .onColumns("email", "firstname", "lastname")
                .onCsvFiles(
                        "com/github/ngoanh2n/asserts/csv/actual/deletion-input.csv",
                        "com/github/ngoanh2n/asserts/csv/expected/deletion-input.csv")
                .byIdentityColumn("email")
                .build();

        CsvComparisonResult result = comparator.saveDiffAt("build/csv-diff/deleted").perform();
        Assertions.assertTrue(result.hasDiff());
        Assertions.assertTrue(result.hasRowDeleted());
        Assertions.assertTrue(result.getDeletedRows().exists());
    }

    @Test
    void checkModifiedRows() throws IOException {
        CsvComparator comparator = CsvComparator.builder()
                .onColumns("email", "firstname", "lastname")
                .onCsvFiles(
                        "com/github/ngoanh2n/asserts/csv/actual/modification-input.csv",
                        "com/github/ngoanh2n/asserts/csv/expected/modification-input.csv")
                .byIdentityColumn(0)
                .build();

        CsvComparisonResult result = comparator.saveDiffAt("build/csv-diff/modified").perform();
        Assertions.assertTrue(result.hasDiff());
        Assertions.assertTrue(result.hasRowModified());
        Assertions.assertTrue(result.getModifiedRows().exists());
    }
}