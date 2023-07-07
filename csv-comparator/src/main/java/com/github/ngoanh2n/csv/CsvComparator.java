package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Commons;
import com.github.ngoanh2n.RuntimeError;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Compare 2 CSV files or 2 CSV directories.<br><br>
 *
 * <b>Comparison</b><br>
 * Example: CSV is formatted columns {@code [id,email,firstname,lastname,age,note]}.
 *
 * <ol>
 *     <li>Compare 2 CSV file
 *          <pre>{@code
 *              File expectedCsvFile = new File("data/expected/file.csv");
 *              File actualCsvFile = new File("data/actual/file.csv");
 *
 *              CsvComparisonOptions options = CsvComparisonOptions
 *                      .builder()
 *                      .selectColumns("email", "firstname", "lastname")
 *                      .selectColumnId("email")
 *                      //.selectColumns(1, 2, 3)
 *                      //.selectColumnId(1)
 *                      .build();
 *              CsvComparisonResult result = CsvComparator.compare(expectedCsvFile, actualCsvFile, options);
 *          }</pre>
 *     </li>
 *     <li>Compare 2 CSV directory
 *          <pre>{@code
 *              Path expectedCsvDir = Paths.get("data/expected");
 *              Path actualCsvDir = Paths.get("data/actual");
 *
 *              CsvComparisonOptions options = CsvComparisonOptions
 *                      .builder()
 *                      .selectColumns("email", "firstname", "lastname")
 *                      .selectColumnId("email")
 *                      //.selectColumns(1, 2, 3)
 *                      //.selectColumnId(1)
 *                      .build();
 *              CsvBulkComparisonResult result = CsvComparator.compare(expectedCsvDir, actualCsvDir, options);
 *          }</pre>
 *     </li>
 * </ol>
 *
 * <b>Result</b><br>
 * {@link CsvComparisonResult} is the result of {@link CsvComparator#compare(File, File, CsvComparisonOptions) CsvComparator.compare(expectedCsvFile, actualCsvFile, options)}.
 * <pre>{@code
 *      boolean hasDiff = CsvComparisonResult.hasDiff();
 *      boolean hasDeletion = CsvComparisonResult.hasDeletion();
 *      boolean hasInsertion = CsvComparisonResult.hasInsertion();
 *      boolean hasModification = CsvComparisonResult.hasModification();
 *      List<String[]> keptRows = CsvComparisonResult.getKeptRows();
 *      List<String[]> deletedRows = CsvComparisonResult.getDeletedRows();
 *      List<String[]> insertedRows = CsvComparisonResult.getInsertedRows();
 *      List<String[]> modifiedRows = CsvComparisonResult.getModifiedRows();
 * }</pre><br>
 * {@link CsvBulkComparisonResult} is the result of {@link CsvComparator#compare(Path, Path, CsvComparisonOptions) CsvComparator.compare(expectedCsvDir, actualCsvDir, options)}.
 * <pre>{@code
 *      boolean hasDiff = CsvBulkComparisonResult.hasDiff();
 *      int diffTotal = CsvBulkComparisonResult.getDiffTotal();
 *      List<CsvComparisonResult> diffResults = CsvBulkComparisonResult.getDiffResults();
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

    /**
     * Compare 2 CSV directories.<br>
     * All CSV files in directories must be same column format.
     *
     * @param exp The expected CSV directory.
     * @param act The actual CSV directory needs to compare.
     * @return A {@link CsvBulkComparisonResult} after bulk comparison process ended.
     */
    public static CsvBulkComparisonResult compare(Path exp, Path act) {
        return compare(exp, act, CsvComparisonOptions.defaults());
    }

    /**
     * Compare 2 CSV directories.<br>
     * All CSV files in directories must be same column format.
     *
     * @param exp     The expected CSV directory.
     * @param act     The actual CSV directory needs to compare.
     * @param options The {@link CsvComparisonOptions} to adjust behaviors of {@link CsvComparator}.
     * @return A {@link CsvBulkComparisonResult} after bulk comparison process ended.
     */
    public static CsvBulkComparisonResult compare(Path exp, Path act, CsvComparisonOptions options) {
        log.debug("//-----Bulk CSV Comparison-----//");
        log.debug("Exp CSV directory: {}", Commons.getRelative(exp));
        log.debug("Act CSV directory: {}", Commons.getRelative(act));

        CsvBulkResult result = new CsvBulkResult();
        List<Map.Entry<Path, Path>> sources = getSources(exp, act);

        for (Map.Entry<Path, Path> source : sources) {
            File actCSV = source.getKey().toFile();
            File expCSV = source.getValue().toFile();
            result.put(compare(expCSV, actCSV, options));
        }
        return result;
    }

    //-------------------------------------------------------------------------------//

    private static CsvResult.Collector doComparison(File exp, File act, CsvComparisonOptions options, List<CsvComparisonVisitor> visitors) {
        CsvParserSettings settings = getSettings(options);
        CsvSource source = CsvSource.parse(options, exp);
        CsvResult.Collector collector = new CsvResult.Collector();

        Map<String, String[]> expMap = source.getRows().stream().collect(
                Collectors.toMap(rk -> rk[source.getColumnId()], rv -> rv, (rk, rv) -> rv));
        settings.setProcessor(new CsvProcessor(options, visitors, collector, expMap, source));
        new CsvParser(settings).parse(act, CsvSource.getCharset(options, act));

        if (expMap.size() > 0) {
            for (Map.Entry<String, String[]> left : expMap.entrySet()) {
                String[] row = left.getValue();
                collector.rowDeleted(options, source.getHeaders(), row);
                visitors.forEach(visitor -> visitor.rowDeleted(options, source.getHeaders(), row));
            }
        }
        return collector;
    }

    private static CsvParserSettings getSettings(CsvComparisonOptions options) {
        Commons.createDir(options.resultOptions().location());
        return options.parserSettings().clone();
    }

    private static List<Map.Entry<Path, Path>> getSources(Path exp, Path act) {
        try {
            List<Path> expFiles = getCSVFiles(exp);
            List<Path> actFiles = getCSVFiles(act);

            log.debug("Exp CSV files: {}", expFiles.size());
            log.debug("Act CSV files: {}", actFiles.size());
            List<Map.Entry<Path, Path>> sources = new ArrayList<>(actFiles.size());

            for (Path actFile : actFiles) {
                String actTarget = String.valueOf(act.relativize(actFile));

                for (Path expFile : expFiles) {
                    String expTarget = String.valueOf(exp.relativize(expFile));

                    if (actTarget.equals(expTarget)) {
                        sources.add(new AbstractMap.SimpleEntry<>(actFile, expFile));
                        break;
                    }
                }
            }
            return sources;
        } catch (IOException ex) {
            String msg = "Error occurred while reading CSV files in directory";
            log.error(msg);
            throw new RuntimeError(msg, ex);
        }
    }

    private static List<Path> getCSVFiles(Path path) throws IOException {
        try (Stream<Path> stream = Files.walk(path)) {
            return stream.filter(Files::isRegularFile).collect(Collectors.toList());
        }
    }

    //-------------------------------------------------------------------------------//

    private CsvComparisonResult compare() {
        CsvComparisonResult result;
        long starting = System.currentTimeMillis();
        List<CsvComparisonVisitor> visitors = getVisitors();

        try {
            visitors.forEach(visitor -> visitor.comparisonStarted(options, exp, act));
            CsvResult.Collector collector = doComparison(exp, act, options, visitors);
            result = new CsvResult(collector);
            visitors.forEach(visitor -> visitor.comparisonFinished(options, exp, act, result));
            log.debug("CSV comparison result: {}", result);
        } catch (Exception ex) {
            String msg = "Error occurred while comparing";
            log.error(msg);
            throw new RuntimeError(msg, ex);
        } finally {
            long ending = System.currentTimeMillis();
            String format = "[HH 'hours', mm 'minutes', ss 'seconds', SS 'milliseconds']";
            log.info("CSV comparison finished in {}\n", DurationFormatUtils.formatDuration(ending - starting, format, true));
        }
        return result;
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
