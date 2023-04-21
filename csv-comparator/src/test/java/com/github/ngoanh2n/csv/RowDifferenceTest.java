package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resource;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RowDifferenceTest {
    @Test
    @Order(1)
    void kept() {
        File exp = Resource.getFile("com/github/ngoanh2n/csv/exp/inserted.csv");
        File act = Resource.getFile("com/github/ngoanh2n/csv/exp/inserted.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertFalse(result.isDeleted());
        assertFalse(result.isInserted());
        assertFalse(result.isModified());
        assertFalse(result.isDifferent());
        assertEquals(1, result.rowsKept().size());
    }

    @Test
    @Order(2)
    void deleted() {
        File exp = Resource.getFile("com/github/ngoanh2n/csv/exp/deleted.csv");
        File act = Resource.getFile("com/github/ngoanh2n/csv/act/deleted.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertTrue(result.isDeleted());
        assertTrue(result.isDifferent());
        assertEquals(1, result.rowsKept().size());
        assertEquals(1, result.rowsDeleted().size());
    }

    @Test
    @Order(3)
    void inserted() {
        File exp = Resource.getFile("com/github/ngoanh2n/csv/exp/inserted.csv");
        File act = Resource.getFile("com/github/ngoanh2n/csv/act/inserted.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertTrue(result.isInserted());
        assertTrue(result.isDifferent());
        assertEquals(1, result.rowsKept().size());
        assertEquals(2, result.rowsInserted().size());
    }

    @Test
    @Order(4)
    void modified() {
        File exp = Resource.getFile("com/github/ngoanh2n/csv/exp/modified.csv");
        File act = Resource.getFile("com/github/ngoanh2n/csv/act/modified.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(exp, act, options);

        assertTrue(result.isModified());
        assertTrue(result.isDifferent());
        assertEquals(2, result.rowsKept().size());
        assertEquals(1, result.rowsModified().size());
    }
}
