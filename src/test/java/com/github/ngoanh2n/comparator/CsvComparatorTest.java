package com.github.ngoanh2n.comparator;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static com.github.ngoanh2n.Resource.getFile;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CsvComparatorTest {

    @Test
    @Order(1)
    void kept() {
        CsvComparisonSource<File> source = CsvComparisonSource.create(
                getFile("com/github/ngoanh2n/comparator/exp/inserted.csv"),
                getFile("com/github/ngoanh2n/comparator/exp/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns(1, 2, 3)
                .setIdentityColumn(0)
                .build();
        CsvComparisonResult result = new CsvComparator(source, options).compare();

        assertFalse(result.hasDeleted());
        assertFalse(result.hasInserted());
        assertFalse(result.hasModified());
        assertFalse(result.hasDiff());
        assertEquals(1, result.rowsKept().size());
    }

    @Test
    @Order(2)
    void deleted() {
        CsvComparisonSource<File> source = CsvComparisonSource.create(
                getFile("com/github/ngoanh2n/comparator/exp/deleted.csv"),
                getFile("com/github/ngoanh2n/comparator/act/deleted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns("email", "firstname", "lastname")
                .setIdentityColumn("email")
                .build();
        CsvComparisonResult result = new CsvComparator(source, options).compare();

        assertTrue(result.hasDeleted());
        assertTrue(result.hasDiff());
        assertEquals(1, result.rowsKept().size());
        assertEquals(1, result.rowsDeleted().size());
    }

    @Test
    @Order(3)
    void inserted() {
        CsvComparisonSource<File> source = CsvComparisonSource.create(
                getFile("com/github/ngoanh2n/comparator/exp/inserted.csv"),
                getFile("com/github/ngoanh2n/comparator/act/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns(1, 2, 3)
                .setIdentityColumn(0)
                .build();
        CsvComparisonResult result = new CsvComparator(source, options).compare();

        assertTrue(result.hasInserted());
        assertTrue(result.hasDiff());
        assertEquals(1, result.rowsKept().size());
        assertEquals(2, result.rowsInserted().size());
    }

    @Test
    @Order(4)
    void modified() {
        CsvComparisonSource<File> source = CsvComparisonSource.create(
                getFile("com/github/ngoanh2n/comparator/exp/modified.csv"),
                getFile("com/github/ngoanh2n/comparator/act/modified.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns("email", "firstname", "lastname")
                .setIdentityColumn(0)
                .build();
        CsvComparisonResult result = new CsvComparator(source, options).compare();

        assertTrue(result.hasModified());
        assertTrue(result.hasDiff());
        assertEquals(2, result.rowsKept().size());
        assertEquals(1, result.rowsModified().size());
    }
}
