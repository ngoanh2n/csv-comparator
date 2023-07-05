package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resources;
import org.junit.jupiter.api.*;

import java.io.File;

/**
 * @author ngoanh2n
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RowDifferenceTest {
    @Test
    @Order(1)
    void hasDiff() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/insertion.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/exp/insertion.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        Assertions.assertFalse(result.hasDiff());
        Assertions.assertFalse(result.hasDeletion());
        Assertions.assertFalse(result.hasInsertion());
        Assertions.assertFalse(result.hasModification());
        Assertions.assertEquals(1, result.getKeptRows().size());
    }

    @Test
    @Order(2)
    void hasDeletion() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/deletion.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/act/deletion.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        Assertions.assertTrue(result.hasDiff());
        Assertions.assertTrue(result.hasDeletion());
        Assertions.assertFalse(result.hasInsertion());
        Assertions.assertFalse(result.hasModification());
        Assertions.assertEquals(1, result.getKeptRows().size());
        Assertions.assertEquals(1, result.getDeletedRows().size());
    }

    @Test
    @Order(3)
    void hasInsertion() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/insertion.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/act/insertion.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        Assertions.assertTrue(result.hasDiff());
        Assertions.assertFalse(result.hasDeletion());
        Assertions.assertTrue(result.hasInsertion());
        Assertions.assertFalse(result.hasModification());
        Assertions.assertEquals(1, result.getKeptRows().size());
        Assertions.assertEquals(2, result.getInsertedRows().size());
    }

    @Test
    @Order(4)
    void hasModification() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/modification.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/act/modification.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        Assertions.assertTrue(result.hasDiff());
        Assertions.assertFalse(result.hasDeletion());
        Assertions.assertFalse(result.hasInsertion());
        Assertions.assertTrue(result.hasModification());
        Assertions.assertEquals(2, result.getKeptRows().size());
        Assertions.assertEquals(1, result.getModifiedRows().size());
    }
}
