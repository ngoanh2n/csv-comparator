package com.github.ngoanh2n.csv;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Use to walk through {@link CsvComparator}.<br>
 * How to build the service provider:<br>
 * <ul>
 *      <li>1. Create a class that implements SPI {@link CsvComparisonVisitor}
 *      <pre>{@code
 *      package com.company.project.impl;
 *
 *      import com.github.ngoanh2n.csv.CsvComparisonVisitor;
 *
 *      public class MyComparisonVisitor implements CsvComparisonVisitor {
 *          //
 *          // IMPLEMENTED METHODS
 *          //
 *      }
 *      }</pre>
 *      <li>2. Create a provider configuration file:
 *      <ul>
 *          <li>Location: {@code resources/META-INF/services}
 *          <li>Name: {@code com.github.ngoanh2n.csv.CsvComparisonVisitor}
 *          <li>Content: {@code com.company.project.impl.MyComparisonVisitor}
 *      </ul>
 * </ul>
 *
 * @author ngoanh2n
 */
public interface CsvComparisonVisitor {
    /**
     * Callback before {@link CsvComparator#compare(File, File, CsvComparisonOptions)}.
     *
     * @param exp     The expected CSV file.
     * @param act     The actual CSV file needs to compare.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     */
    default void comparisonStarted(CsvComparisonOptions options, File exp, File act) {/**/}

    /**
     * Callback for when {@link CsvComparator} detected a row kept.
     *
     * @param row     The row kept.
     * @param headers Extracted from the CSV file you have provided.<br>
     *                Headers row is existed or not depends on {@link CsvComparisonOptions.Builder#withoutHeaders()}.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     */
    default void rowKept(CsvComparisonOptions options, String[] headers, String[] row) {/**/}

    /**
     * Callback for when {@link CsvComparator} detected a row deleted.
     *
     * @param row     The row deleted.
     * @param headers Extracted from the CSV file you have provided.<br>
     *                Headers row is existed or not depends on {@link CsvComparisonOptions.Builder#withoutHeaders()}.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     */
    default void rowDeleted(CsvComparisonOptions options, String[] headers, String[] row) {/**/}

    /**
     * Callback for when {@link CsvComparator} detected a row inserted.
     *
     * @param row     The row inserted.
     * @param headers Extracted from the CSV file you have provided.<br>
     *                Headers row is existed or not depends on {@link CsvComparisonOptions.Builder#withoutHeaders()}.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     */
    default void rowInserted(CsvComparisonOptions options, String[] headers, String[] row) {/**/}

    /**
     * Callback for when {@link CsvComparator} detected a row modified.
     *
     * @param row     The row modified.
     * @param headers Extracted from the CSV file you have provided.<br>
     *                Headers row is existed or not depends on {@link CsvComparisonOptions.Builder#withoutHeaders()}.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     * @param diffs   List of difference at specific cells: [column, expCell, actCell]<br>
     *                - column: name of column (header) <br>
     *                - expCell: cell value of expected CSV <br>
     *                - actCell: cell value of actual CSV <br>
     */
    default void rowModified(CsvComparisonOptions options, String[] headers, String[] row, List<HashMap<String, String>> diffs) {/**/}

    /**
     * Callback after {@link CsvComparator#compare(File, File, CsvComparisonOptions)}.
     *
     * @param exp     The expected CSV file.
     * @param act     The actual CSV file needs to compare.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     * @param result  A {@link CsvComparisonResult} after comparison process ended.
     */
    default void comparisonFinished(CsvComparisonOptions options, File exp, File act, CsvComparisonResult result) {/**/}
}
