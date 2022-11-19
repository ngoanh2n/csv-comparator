package com.github.ngoanh2n.comparator;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.github.ngoanh2n.Resource.getFile;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CsvComparatorTest {
    @Test
    @Order(1)
    void kept() {
        CsvComparisonSource source = CsvComparisonSource.create(
                getFile("com/github/ngoanh2n/comparator/exp/inserted.csv"),
                getFile("com/github/ngoanh2n/comparator/exp/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns(1, 2, 3)
                .setIdentityColumn(0)
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
                getFile("com/github/ngoanh2n/comparator/exp/deleted.csv"),
                getFile("com/github/ngoanh2n/comparator/act/deleted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns("email", "firstname", "lastname")
                .setIdentityColumn("email")
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
                getFile("com/github/ngoanh2n/comparator/exp/inserted.csv"),
                getFile("com/github/ngoanh2n/comparator/act/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns(1, 2, 3)
                .setIdentityColumn(0)
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
                getFile("com/github/ngoanh2n/comparator/exp/modified.csv"),
                getFile("com/github/ngoanh2n/comparator/act/modified.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns("email", "firstname", "lastname")
                .setIdentityColumn(0)
                .build();
        CsvComparisonResult result = CsvComparator.compare(source, options);

        assertTrue(result.isModified());
        assertTrue(result.isDifferent());
        assertEquals(2, result.rowsKept().size());
        assertEquals(1, result.rowsModified().size());
    }
}
