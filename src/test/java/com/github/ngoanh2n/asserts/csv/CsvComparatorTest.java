package com.github.ngoanh2n.asserts.csv;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.net.URL;

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
        ComparisonSource<File> source = ComparisonSource.create(
                resource("com/github/ngoanh2n/asserts/csv/exp/inserted.csv"),
                resource("com/github/ngoanh2n/asserts/csv/exp/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .columns(1, 2, 3)
                .identityColumn(0)
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
        ComparisonSource<File> source = ComparisonSource.create(
                resource("com/github/ngoanh2n/asserts/csv/exp/deleted.csv"),
                resource("com/github/ngoanh2n/asserts/csv/act/deleted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .columns("email", "firstname", "lastname")
                .identityColumn("email")
                .build();
        CsvComparisonResult result = new CsvComparator(source, options).compare();

        assertTrue(result.hasDeleted());
        assertTrue(result.hasDiff());
        assertTrue(result.rowsDeleted().size() > 0);
    }

    @Test
    @Order(3)
    void inserted() {
        ComparisonSource<File> source = ComparisonSource.create(
                resource("com/github/ngoanh2n/asserts/csv/exp/inserted.csv"),
                resource("com/github/ngoanh2n/asserts/csv/act/inserted.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .columns(1, 2, 3)
                .identityColumn(0)
                .build();
        CsvComparisonResult result = new CsvComparator(source, options).compare();

        assertTrue(result.hasInserted());
        assertTrue(result.hasDiff());
        assertEquals(2, result.rowsInserted().size());
    }

    @Test
    @Order(4)
    void modified() {
        ComparisonSource<File> source = ComparisonSource.create(
                resource("com/github/ngoanh2n/asserts/csv/exp/modified.csv"),
                resource("com/github/ngoanh2n/asserts/csv/act/modified.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .columns("email", "firstname", "lastname")
                .identityColumn(0)
                .build();
        CsvComparisonResult result = new CsvComparator(source, options).compare();

        assertTrue(result.hasModified());
        assertTrue(result.hasDiff());
        assertTrue(result.rowsModified().size() > 0);
    }

    static File resource(String name) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(name);
        if (resource == null) throw new IllegalArgumentException("File not found!");
        else return new File(resource.getFile());
    }
}
