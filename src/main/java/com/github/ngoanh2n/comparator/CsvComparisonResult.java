package com.github.ngoanh2n.comparator;

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
     * {@link CsvComparisonSource#exp()} and {@link CsvComparisonSource#act()}
     */
    boolean hasDiff();

    boolean hasDeleted();

    boolean hasInserted();

    boolean hasModified();

    List<String[]> rowsKept();

    List<String[]> rowsDeleted();

    List<String[]> rowsInserted();

    List<String[]> rowsModified();
}
