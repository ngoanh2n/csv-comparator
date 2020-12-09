package com.github.ngoanh2n.asserts.csv;

import java.util.List;

/**
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
public interface CsvComparisonResult {

    /**
     * @return Whether has different or not between
     * {@link ComparisonSource#exp()} and {@link ComparisonSource#act()}
     */
    boolean hasDiff();

    boolean hasDeleted();

    boolean hasInserted();

    boolean hasModified();

    List<String[]> rowsKept();

    List<String[]> rowsInserted();

    List<String[]> rowsDeleted();

    List<String[]> rowsModified();
}
