package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Commons;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * This class handles to compare {@linkplain CsvComparisonSource#exp()} and {@linkplain CsvComparisonSource#act()}.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 */
public class CsvComparator {
    private final CsvComparisonSource source;
    private final CsvComparisonOptions options;

    //-------------------------------------------------------------------------------//

    private CsvComparator(@Nonnull CsvComparisonSource source) {
        this(source, CsvComparisonOptions.defaults());
    }

    private CsvComparator(@Nonnull CsvComparisonSource source,
                          @Nonnull CsvComparisonOptions options) {
        this.source = Preconditions.checkNotNull(source, "source cannot be null");
        this.options = Preconditions.checkNotNull(options, "options cannot be null");
    }

    //-------------------------------------------------------------------------------//

    /**
     * Compare 2 CSV files.
     *
     * @param source {@linkplain CsvComparisonSource} will be compared.
     * @return {@linkplain CsvComparisonResult} after comparison process ended.
     */
    public static CsvComparisonResult compare(@Nonnull CsvComparisonSource source) {
        return new CsvComparator(source).compare();
    }

    /**
     * Compare 2 CSV files.
     *
     * @param source  {@linkplain CsvComparisonSource} will be compared.
     * @param options {@linkplain CsvComparisonOptions} you have provided.
     * @return {@linkplain CsvComparisonResult} after comparison process ended.
     */
    public static CsvComparisonResult compare(@Nonnull CsvComparisonSource source,
                                              @Nonnull CsvComparisonOptions options) {
        return new CsvComparator(source, options).compare();
    }

    //-------------------------------------------------------------------------------//

    @Nonnull
    private CsvComparisonResult compare() {
        List<CsvComparisonVisitor> visitors = getVisitors();
        visitors.forEach(v -> v.comparisonStarted(options, source));
        CsvParserSettings settings = getSettings();
        CsvSource cs = CsvSource.parse(options, source.exp());
        CsvResult.Collector collector = new CsvResult.Collector();

        Map<String, String[]> expMap = cs.getRows().stream().collect(
                Collectors.toMap(rk -> rk[cs.getColumnId()], rv -> rv, (rk, rv) -> rv));
        settings.setProcessor(new CsvProcessor(options, visitors, collector, expMap, cs));
        new CsvParser(settings).parse(source.act(), CsvSource.getCharset(options, source.act()));

        if (expMap.size() > 0) {
            for (Map.Entry<String, String[]> left : expMap.entrySet()) {
                String[] row = left.getValue();
                collector.rowDeleted(options, cs.getHeaders(), row);
                visitors.forEach(v -> v.rowDeleted(options, cs.getHeaders(), row));
            }
        }

        CsvComparisonResult result = new CsvResult(collector);
        visitors.forEach(v -> v.comparisonFinished(options, source, result));
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
                    .filter(visitors, v -> !v.getClass().getName().equals(CsvComparisonOutput.class.getName())));
        }

        visitors.forEach(v -> LOGGER.debug("{}", v.getClass().getName()));
        return visitors;
    }

    //-------------------------------------------------------------------------------//

    private final static Logger LOGGER = LoggerFactory.getLogger(CsvComparator.class);
}
