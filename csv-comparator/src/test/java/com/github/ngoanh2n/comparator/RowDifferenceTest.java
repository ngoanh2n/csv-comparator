package com.github.ngoanh2n.comparator;

import com.github.ngoanh2n.Resource;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RowDifferenceTest {
    @Test
    @Order(1)
    void kept() {
        CsvComparisonSource source = CsvComparisonSource.create(
                Resource.getFile("com/github/ngoanh2n/comparator/exp/inserted.csv"),
                Resource.getFile("com/github/ngoanh2n/comparator/exp/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(source, options);

        assertFalse(result.isDeleted());
        assertFalse(result.isInserted());
        assertFalse(result.isModified());
        assertFalse(result.isDifferent());
        assertEquals(1, result.rowsKept().size());
    }

    @Test
    @Order(2)
    void deleted() {
        CsvComparisonSource source = CsvComparisonSource.create(
                Resource.getFile("com/github/ngoanh2n/comparator/exp/deleted.csv"),
                Resource.getFile("com/github/ngoanh2n/comparator/act/deleted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(source, options);

        assertTrue(result.isDeleted());
        assertTrue(result.isDifferent());
        assertEquals(1, result.rowsKept().size());
        assertEquals(1, result.rowsDeleted().size());
    }

    @Test
    @Order(3)
    void inserted() {
        CsvComparisonSource source = CsvComparisonSource.create(
                Resource.getFile("com/github/ngoanh2n/comparator/exp/inserted.csv"),
                Resource.getFile("com/github/ngoanh2n/comparator/act/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns(1, 2, 3)
                .selectColumnId(1)
                .build();
        CsvComparisonResult result = CsvComparator.compare(source, options);

        assertTrue(result.isInserted());
        assertTrue(result.isDifferent());
        assertEquals(1, result.rowsKept().size());
        assertEquals(2, result.rowsInserted().size());
    }

    @Test
    @Order(4)
    void modified() {
        CsvComparisonSource source = CsvComparisonSource.create(
                Resource.getFile("com/github/ngoanh2n/comparator/exp/modified.csv"),
                Resource.getFile("com/github/ngoanh2n/comparator/act/modified.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                .build();
        CsvComparisonResult result = CsvComparator.compare(source, options);

        assertTrue(result.isModified());
        assertTrue(result.isDifferent());
        assertEquals(2, result.rowsKept().size());
        assertEquals(1, result.rowsModified().size());
    }
}
