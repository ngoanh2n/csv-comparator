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
    void comparisonStarted(CsvComparisonSource source, CsvComparisonOptions options);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row kept
     *
     * @param row     is the row kept
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeaders(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowKept(String[] row, String[] headers, CsvComparisonOptions options);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row deleted
     *
     * @param row     is the row deleted
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeaders(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowDeleted(String[] row, String[] headers, CsvComparisonOptions options);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row inserted
     *
     * @param row     is the row inserted
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeaders(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void rowInserted(String[] row, String[] headers, CsvComparisonOptions options);

    /**
     * Callback for when {@linkplain CsvComparator} detected a row modified
     *
     * @param row     is the row modified
     * @param headers is extracted from {@linkplain CsvComparisonSource} you have provided <br>
     *                Headers row is existed or not depends on {@linkplain CsvComparisonOptions.Builder#extractHeaders(boolean)}
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     * @param diffs   is list of difference at specific cells -> [column, expCell, actCell] <br>
     *                - column: name of column (header) <br>
     *                - expCell: cell value of expected CSV <br>
     *                - actCell: cell value of actual CSV <br>
     */
    void rowModified(String[] row, String[] headers, CsvComparisonOptions options, List<HashMap<String, String>> diffs);

    /**
     * Callback after {@linkplain CsvComparator#compare(CsvComparisonSource, CsvComparisonOptions)} )}
     *
     * @param source  is {@linkplain CsvComparisonSource} is being to compare
     * @param options is {@linkplain CsvComparisonOptions} you have provided
     */
    void comparisonFinished(CsvComparisonSource source, CsvComparisonOptions options, CsvComparisonResult result);
}
