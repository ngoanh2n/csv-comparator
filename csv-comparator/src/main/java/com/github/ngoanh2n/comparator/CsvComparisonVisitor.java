package com.github.ngoanh2n.comparator;

import java.util.HashMap;
import java.util.List;

/**
 * This interface should be used to walk through {@linkplain CsvComparator}.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
public interface CsvComparisonVisitor {
    /**
     * Callback before {@linkplain CsvComparator#compare(CsvComparisonSource, CsvComparisonOptions)}
     *
     * @param source  is {@linkplain CsvComparisonSource} is being to compare
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void comparisonStarted(CsvComparisonOptions options, CsvComparisonSource source);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row kept
     *
     * @param row     is the row kept
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#withoutHeaders()}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowKept(CsvComparisonOptions options, String[] headers, String[] row);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row deleted
     *
     * @param row     is the row deleted
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#withoutHeaders()}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowDeleted(CsvComparisonOptions options, String[] headers, String[] row);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row inserted
     *
     * @param row     is the row inserted
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#withoutHeaders()}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowInserted(CsvComparisonOptions options, String[] headers, String[] row);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row modified
     *
     * @param row     is the row modified
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#withoutHeaders()} (boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     * @param diffs   is list of difference at specific cells: [column, expCell, actCell] <br>
     *                - column: name of column (header) <br>
     *                - expCell: cell value of expected CSV <br>
     *                - actCell: cell value of actual CSV <br>
     */
    void rowModified(CsvComparisonOptions options, String[] headers, String[] row, List<HashMap<String, String>> diffs);

    /**
     * Callback after {@linkplain CsvComparator#compare(CsvComparisonSource, CsvComparisonOptions)} )}
     *
     * @param source  is {@linkplain CsvComparisonSource} is being to compare
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     * @param result  is {@linkplain CsvComparisonResult} after comparison process ended
     */
    void comparisonFinished(CsvComparisonOptions options, CsvComparisonSource source, CsvComparisonResult result);
}
