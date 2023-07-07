package com.github.ngoanh2n.csv;

import java.nio.file.Path;
import java.util.List;

/**
 * The result of {@link CsvComparator#compare(Path, Path, CsvComparisonOptions) CsvComparator.compare(expectedCsvDir, actualCsvDir, options)}.<br><br>
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
public interface CsvBulkComparisonResult {
    /**
     * Whether there is any different file between expected CSV directory and actual CSV directory.
     *
     * @return Indicate expected file is different against to actual.
     */
    boolean hasDiff();

    /**
     * Get number of diff results after bulk comparison between 2 CSV directories.<br>
     * A diff result is a {@link CsvComparisonResult} after comparing between 2 CSV files.
     *
     * @return Diff result total.
     */
    int getDiffTotal();

    /**
     * Get all diff results after bulk comparison between 2 CSV directories.<br>
     * A diff result is a {@link CsvComparisonResult} after comparing between 2 CSV files.
     *
     * @return Diff result list.
     */
    List<CsvComparisonResult> getDiffResults();
}
