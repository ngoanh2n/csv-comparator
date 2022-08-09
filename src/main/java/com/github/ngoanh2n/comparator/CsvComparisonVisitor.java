package com.github.ngoanh2n.comparator;

/**
 * This interface should be used to walk through {@linkplain CsvComparator}.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
public interface CsvComparisonVisitor {
    /**
     * Callback before {@linkplain CsvComparator#compare()}
     *
     * @param source is {@linkplain CsvComparisonSource} is being to compare
     */
    void comparisonStarted(CsvComparisonSource source);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row kept
     *
     * @param row     is the row kept
     * @param header  is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Header row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeader(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowKept(String[] row, String[] header, CsvComparisonOptions options);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row deleted
     *
     * @param row     is the row deleted
     * @param header  is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Header row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeader(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowDeleted(String[] row, String[] header, CsvComparisonOptions options);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row inserted
     *
     * @param row     is the row inserted
     * @param header  is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Header row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeader(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowInserted(String[] row, String[] header, CsvComparisonOptions options);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row modified
     *
     * @param row     is the row modified
     * @param header  is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Header row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeader(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowModified(String[] row, String[] header, CsvComparisonOptions options);

    /**
     * Callback after {@linkplain CsvComparator#compare()}
     *
     * @param source is {@linkplain CsvComparisonSource} is being to compare
     */
    void comparisonFinished(CsvComparisonSource source);
}
