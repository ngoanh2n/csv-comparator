package com.github.ngoanh2n.asserts.csv;

/**
 * This interface should be used to walk through {@link CsvComparator}
 * <br>
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
public interface CsvComparisonVisitor {

    /**
     * This method is called before {@link CsvComparator#compare()} method
     *
     * @param source {@link ComparisonSource} is being to compare
     */
    void visitStarted(ComparisonSource<?> source);

    /**
     * This method is called after {@link CsvComparator#compare()} method
     *
     * @param source {@link ComparisonSource} is being to compare
     */
    void visitEnded(ComparisonSource<?> source);

    void rowKept(String[] row, String[] headers, CsvComparisonOptions options);

    void rowDeleted(String[] row, String[] headers, CsvComparisonOptions options);

    void rowInserted(String[] row, String[] headers, CsvComparisonOptions options);

    void rowModified(String[] row, String[] headers, CsvComparisonOptions options);
}
