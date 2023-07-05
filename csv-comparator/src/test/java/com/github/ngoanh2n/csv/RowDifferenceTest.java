package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resources;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ngoanh2n
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RowDifferenceTest {
    @Test
    @Order(1)
    void kept() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/inserted.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/exp/inserted.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertFalse(result.hasDeletion());
        assertFalse(result.hasInsertion());
        assertFalse(result.hasModification());
        assertFalse(result.hasDiff());
        assertEquals(1, result.getKeptRows().size());
    }

    @Test
    @Order(2)
    void deleted() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/deleted.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/act/deleted.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertTrue(result.hasDeletion());
        assertTrue(result.hasDiff());
        assertEquals(1, result.getKeptRows().size());
        assertEquals(1, result.getDeletedRows().size());
    }

    @Test
    @Order(3)
    void inserted() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/inserted.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/act/inserted.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertTrue(result.hasInsertion());
        assertTrue(result.hasDiff());
        assertEquals(1, result.getKeptRows().size());
        assertEquals(2, result.getInsertedRows().size());
    }

    @Test
    @Order(4)
    void modified() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/modified.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/act/modified.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertTrue(result.hasModification());
        assertTrue(result.hasDiff());
        assertEquals(2, result.getKeptRows().size());
        assertEquals(1, result.getModifiedRows().size());
    }
}
