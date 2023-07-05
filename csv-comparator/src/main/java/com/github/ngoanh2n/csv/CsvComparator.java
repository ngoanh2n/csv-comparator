package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Commons;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Compare 2 CSV files.<br><br>
 *
 * <b>Comparison</b><br>
 * Example: CSV is formatted columns {@code [id,email,firstname,lastname,age,note]}.
 * <pre>{@code
 *     CsvComparisonOptions options = CsvComparisonOptions
 *             .builder()
 *             .selectColumns("email", "firstname", "lastname")
 *             .selectColumnId("email")
 *             //.selectColumns(1, 2, 3)
 *             //.selectColumnId(1)
 *             .build();
 *     CsvComparisonResult result = CsvComparator.compare(expectedCSV, actualCSV, options);
 * }</pre><br>
 *
 * <b>Result</b><br>
 * {@link CsvComparisonResult} is the result of {@link CsvComparator}.
 * <pre>{@code
 *      boolean isDeleted = CsvComparisonResult.isDeleted();
 *      boolean isInserted = CsvComparisonResult.isInserted();
 *      boolean isModified = CsvComparisonResult.isModified();
 *      boolean isDifferent = CsvComparisonResult.isDifferent();
 *      List<String[]> rowsKept = CsvComparisonResult.rowsKept();
 *      List<String[]> rowsDeleted = CsvComparisonResult.rowsDeleted();
 *      List<String[]> rowsInserted = CsvComparisonResult.rowsInserted();
 *      List<String[]> rowsModified = CsvComparisonResult.rowsModified();
 * }</pre><br>
 *
 * <b>Visitor</b><br>
 * {@link CsvComparisonVisitor} for walking through {@link CsvComparator}.
 * <ul>
 *     <li>{@link CsvComparisonVisitor#comparisonStarted(CsvComparisonOptions, File, File)}</li>
 *     <li>{@link CsvComparisonVisitor#rowKept(CsvComparisonOptions, String[], String[])}</li>
 *     <li>{@link CsvComparisonVisitor#rowDeleted(CsvComparisonOptions, String[], String[])}</li>
 *     <li>{@link CsvComparisonVisitor#rowInserted(CsvComparisonOptions, String[], String[])}</li>
 *     <li>{@link CsvComparisonVisitor#rowModified(CsvComparisonOptions, String[], String[], List)}</li>
 *     <li>{@link CsvComparisonVisitor#comparisonFinished(CsvComparisonOptions, File, File, CsvComparisonResult)}</li>
 * </ul>
 *
 * <b>Output</b><br>
 * {@link CsvComparisonOutput} for writing comparison output files to specified location.<br>
 * An implementation of {@link CsvComparisonVisitor}.
 * <ul>
 *     <li>The output is always created at {@code build/ngoanh2n/csv/{yyyyMMdd.HHmmss.SSS}} by default</li>
 *     <li>Use {@link CsvComparisonResultOptions} to adjust the output behaviors. And set to {@link CsvComparisonOptions}
 *         <pre>{@code
 *              CsvComparisonResultOptions resultOptions = CsvComparisonResultOptions
 *                     .builder()
 *                     .writeOutputs(false)                         // Default to true
 *                     //.setLocation(Paths.get("build/custom"))    // Default to build/ngoanh2n/csv
 *                     .build();
 *              CsvComparisonOptions options = CsvComparisonOptions
 *                      .builder()
 *                      .setResultOptions(resultOptions)            // Default to CsvComparisonResultOptions.defaults()
 *                      .build();
 *         }</pre>
 *     </li>
 * </ul>
 *
 * <b>Allure Report</b><br>
 * When using Allure as a report framework, should use
 * <a href="https://mvnrepository.com/artifact/com.github.ngoanh2n/csv-comparator-allure">com.github.ngoanh2n:csv-comparator-allure</a>.<br><br>
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
@ParametersAreNonnullByDefault
public class CsvComparator {
    private final static Logger log = LoggerFactory.getLogger(CsvComparator.class);
    private final File exp;
    private final File act;
    private final CsvComparisonOptions options;

    private CsvComparator(File exp, File act, CsvComparisonOptions options) {
        this.exp = Preconditions.checkNotNull(exp, "exp CSV cannot be null");
        this.act = Preconditions.checkNotNull(act, "act CSV cannot be null");
        this.options = Preconditions.checkNotNull(options, "options cannot be null");
    }

    //-------------------------------------------------------------------------------//

    /**
     * Compare 2 CSV files.
     *
     * @param exp The expected CSV file.
     * @param act The actual CSV file needs to compare.
     * @return A {@link CsvComparisonResult} after comparison process ended.
     */
    public static CsvComparisonResult compare(File exp, File act) {
        return compare(exp, act, CsvComparisonOptions.defaults());
    }

    /**
     * Compare 2 CSV files.
     *
     * @param exp     The expected CSV file.
     * @param act     The actual CSV file needs to compare.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     * @return A {@link CsvComparisonResult} after comparison process ended.
     */
    public static CsvComparisonResult compare(File exp, File act, CsvComparisonOptions options) {
        log.debug("//-----CSV Comparison-----//");
        log.debug("Exp CSV file: {}", Commons.getRelative(exp));
        log.debug("Act CSV file: {}", Commons.getRelative(act));
        return new CsvComparator(exp, act, options).compare();
    }

    //-------------------------------------------------------------------------------//

    private CsvComparisonResult compare() {
        List<CsvComparisonVisitor> visitors = getVisitors();
        visitors.forEach(visitor -> visitor.comparisonStarted(options, exp, act));
        CsvParserSettings settings = getSettings();
        CsvSource cs = CsvSource.parse(options, exp);
        CsvResult.Collector collector = new CsvResult.Collector();

        Map<String, String[]> expMap = cs.getRows().stream().collect(
                Collectors.toMap(rk -> rk[cs.getColumnId()], rv -> rv, (rk, rv) -> rv));
        settings.setProcessor(new CsvProcessor(options, visitors, collector, expMap, cs));
        new CsvParser(settings).parse(act, CsvSource.getCharset(options, act));

        if (expMap.size() > 0) {
            for (Map.Entry<String, String[]> left : expMap.entrySet()) {
                String[] row = left.getValue();
                collector.rowDeleted(options, cs.getHeaders(), row);
                visitors.forEach(visitor -> visitor.rowDeleted(options, cs.getHeaders(), row));
            }
        }

        CsvComparisonResult result = new CsvResult(collector);
        visitors.forEach(visitor -> visitor.comparisonFinished(options, exp, act, result));
        return result;
    }

    private CsvParserSettings getSettings() {
        Commons.createDir(options.resultOptions().location());
        return options.parserSettings();
    }

    private List<CsvComparisonVisitor> getVisitors() {
        ServiceLoader<CsvComparisonVisitor> serviceLoader = ServiceLoader.load(CsvComparisonVisitor.class);
        ImmutableList<CsvComparisonVisitor> visitors = ImmutableList.copyOf(serviceLoader.iterator());

        if (!options.resultOptions().writeOutputs()) {
            visitors = ImmutableList.copyOf(Collections2
                    .filter(visitors, visitor -> !visitor.getClass().getName().equals(CsvComparisonOutput.class.getName())));
        }

        visitors.forEach(visitor -> log.debug("{}", visitor.getClass().getName()));
        return visitors;
    }
}
