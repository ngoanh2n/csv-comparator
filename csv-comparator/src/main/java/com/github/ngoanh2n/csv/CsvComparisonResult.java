package com.github.ngoanh2n.csv;

import java.util.List;

/**
 * The result of {@link CsvComparator}.<br><br>
 *
 * <em>Repository:</em>
 * <ul>
 *     <li><em>GitHub: <a href="https://github.com/ngoanh2n/csv-comparator">ngoanh2n/csv-comparator</a></em></li>
 *     <li><em>Maven: <a href="https://mvnrepository.com/artifact/com.github.ngoanh2n/csv-comparator">com.github.ngoanh2n:csv-comparator</a></em></li>
 * </ul>
 *
 * @author ngoanh2n
 * @since 2019
 */
public interface CsvComparisonResult {
    /**
     * Whether there is any difference between expected CSV file and actual CSV file.
     *
     * @return Indicate expected file is different against to actual.
     */
    boolean hasDiff();

    /**
     * Whether there is any row deleted in the expected CSV file against to actual CSV file.
     *
     * @return Indicate expected CSV file is deleted any row or not.
     */
    boolean hasDeletion();

    /**
     * Whether there is any row inserted in the expected CSV file against to actual CSV file.
     *
     * @return Indicate expected CSV file is inserted any row or not.
     */
    boolean hasInsertion();

    /**
     * Whether there is any row modified in the expected CSV file against to actual CSV file.
     *
     * @return Indicate expected CSV file is modified any row or not.
     */
    boolean hasModification();

    /**
     * Get all kept rows in the expected CSV file against to actual CSV file.
     *
     * @return Rows were kept.
     */
    List<String[]> getKeptRows();

    /**
     * Get all deleted rows in the expected CSV file against to actual CSV file.
     *
     * @return Rows were deleted.
     */
    List<String[]> getDeletedRows();

    /**
     * Get all inserted rows in the expected CSV file against to actual CSV file.
     *
     * @return Rows were inserted.
     */
    List<String[]> getInsertedRows();

    /**
     * Get all modified rows in the expected CSV file against to actual CSV file.
     *
     * @return Rows were modified.
     */
    List<String[]> rowsModified();
}
