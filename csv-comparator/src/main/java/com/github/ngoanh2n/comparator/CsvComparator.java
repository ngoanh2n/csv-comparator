package com.github.ngoanh2n.comparator;

import com.github.ngoanh2n.Commons;
import com.google.common.collect.ImmutableList;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;

/**
 * This class handles to compare {@linkplain CsvComparisonSource#exp()} and {@linkplain CsvComparisonSource#act()}.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
public class CsvComparator {
    private final CsvComparisonSource source;
    private final CsvComparisonOptions options;
    private final List<CsvComparisonVisitor> visitors;

    //-------------------------------------------------------------------------------//

    private CsvComparator(@Nonnull CsvComparisonSource source) {
        this(source, CsvComparisonOptions.defaults());
    }

    private CsvComparator(@Nonnull CsvComparisonSource source,
                          @Nonnull CsvComparisonOptions options) {
        this.source = checkNotNull(source, "source cannot be null");
        this.options = checkNotNull(options, "options cannot be null");
        this.visitors = getVisitors();
    }

    //-------------------------------------------------------------------------------//

    public static CsvComparisonResult compare(@Nonnull CsvComparisonSource source) {
        return new CsvComparator(source).compare();
    }

    public static CsvComparisonResult compare(@Nonnull CsvComparisonSource source,
                                              @Nonnull CsvComparisonOptions options) {
        return new CsvComparator(source, options).compare();
    }

    //-------------------------------------------------------------------------------//

    @Nonnull
    private CsvComparisonResult compare() {
        visitors.forEach(visitor -> visitor.comparisonStarted(options, source));
        Collector collector = new Collector();
        CsvParserSettings settings = getSettings();
        CsvSource cs = CsvSource.parse(options, source.exp());

        int columnId = cs.getColumnId();
        String[] headers = cs.getHeaders();
        Map<String, String[]> expMap = cs.getRows().stream()
                .collect(toMap(rk -> rk[columnId], rv -> rv, (rk, rv) -> rv));

        settings.setProcessor(new RowProcessor() {
            @Override
            public void rowProcessed(String[] actRow, ParsingContext context) {
                String[] expRow = expMap.get(actRow[columnId]);

                if (expRow == null) {
                    visitors.forEach(visitor -> visitor.rowInserted(options, headers, actRow));
                    collector.rowInserted(options, headers, actRow);
                } else {
                    if (Arrays.equals(actRow, expRow)) {
                        visitors.forEach(visitor -> visitor.rowKept(options, headers, actRow));
                        collector.rowKept(options, headers, actRow);
                    } else {
                        List<HashMap<String, String>> diffs = getDiffs(headers, expRow, actRow);
                        visitors.forEach(visitor -> visitor.rowModified(options, headers, actRow, diffs));
                        collector.rowModified(options, headers, actRow, diffs);
                    }
                    expMap.remove(actRow[columnId]);
                }
            }

            @Override
            public void processStarted(ParsingContext context) { /* No implementation necessary */ }

            @Override
            public void processEnded(ParsingContext context) { /* No implementation necessary */ }
        });
        new CsvParser(settings).parse(source.act(), CsvSource.getEncoding(options, source.act()));

        if (expMap.size() > 0) {
            for (Map.Entry<String, String[]> left : expMap.entrySet()) {
                String[] row = left.getValue();
                visitors.forEach(visitor -> visitor.rowDeleted(options, headers, row));
                collector.rowDeleted(options, headers, row);
            }
        }

        Result result = new Result(collector);
        visitors.forEach(visitor -> visitor.comparisonFinished(options, source, result));
        return result;
    }

    private CsvParserSettings getSettings() {
        Commons.createDir(options.resultOptions().location());
        return options.parserSettings();
    }

    private List<CsvComparisonVisitor> getVisitors() {
        ServiceLoader<CsvComparisonVisitor> serviceLoader = ServiceLoader.load(CsvComparisonVisitor.class);
        List<CsvComparisonVisitor> visitors = ImmutableList.copyOf(serviceLoader.iterator());
        visitors.forEach(visitor -> LOGGER.debug("{}", visitor.getClass().getName()));
        return visitors;
    }

    private List<HashMap<String, String>> getDiffs(String[] headers, String[] expRow, String[] actRow) {
        List<HashMap<String, String>> diffs = new ArrayList<>();

        for (int index = 0; index < headers.length; index++) {
            String header = headers[index];
            String expCell = expRow[index];
            String actCell = actRow[index];

            if (!expCell.equals(actCell)) {
                diffs.add(new HashMap<String, String>() {{
                    put("column", header);
                    put("expCell", expCell);
                    put("actCell", actCell);
                }});
            }
        }
        return diffs;
    }

    //===============================================================================//

    private final static class Result implements CsvComparisonResult {

        private final Collector collector;

        private Result(Collector collector) {
            this.collector = collector;
        }

        @Override
        public boolean isDeleted() {
            return collector.isDeleted;
        }

        @Override
        public boolean isInserted() {
            return collector.isInserted;
        }

        @Override
        public boolean isModified() {
            return collector.isModified;
        }

        @Override
        public List<String[]> rowsKept() {
            return collector.rowsKept;
        }

        @Override
        public List<String[]> rowsDeleted() {
            return collector.rowsDeleted;
        }

        @Override
        public List<String[]> rowsInserted() {
            return collector.rowsInserted;
        }

        @Override
        public List<String[]> rowsModified() {
            return collector.rowsModified;
        }

        @Override
        public boolean isDifferent() {
            return isDeleted() || isInserted() || isModified();
        }
    }

    private final static class Collector implements CsvComparisonVisitor {
        private final List<String[]> rowsKept = new ArrayList<>();
        private final List<String[]> rowsDeleted = new ArrayList<>();
        private final List<String[]> rowsInserted = new ArrayList<>();
        private final List<String[]> rowsModified = new ArrayList<>();
        private boolean isDeleted = false;
        private boolean isInserted = false;
        private boolean isModified = false;

        @Override
        public void comparisonStarted(CsvComparisonOptions options, CsvComparisonSource source) { /* No implementation necessary */ }

        @Override
        public void rowKept(CsvComparisonOptions options, String[] headers, String[] row) {
            rowsKept.add(row);
        }

        @Override
        public void rowDeleted(CsvComparisonOptions options, String[] headers, String[] row) {
            isDeleted = true;
            rowsDeleted.add(row);
        }

        @Override
        public void rowInserted(CsvComparisonOptions options, String[] headers, String[] row) {
            isInserted = true;
            rowsInserted.add(row);
        }

        @Override
        public void rowModified(CsvComparisonOptions options, String[] headers, String[] row, List<HashMap<String, String>> diffs) {
            isModified = true;
            rowsModified.add(row);
        }

        @Override
        public void comparisonFinished(CsvComparisonOptions options, CsvComparisonSource source, CsvComparisonResult result) { /* No implementation necessary */ }
    }

    //-------------------------------------------------------------------------------//

    private final static Logger LOGGER = LoggerFactory.getLogger(CsvComparator.class);
}
