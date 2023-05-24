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
 * Starting point to compare 2 CSV files.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
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
