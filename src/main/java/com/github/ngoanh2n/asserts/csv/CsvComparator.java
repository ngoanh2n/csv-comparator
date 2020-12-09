package com.github.ngoanh2n.asserts.csv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
public class CsvComparator {

    private final ComparisonSource<File> source;
    private final CsvComparisonOptions options;
    private final CsvComparisonVisitor visitor;

    public CsvComparator(@Nonnull ComparisonSource<File> source,
                         @Nonnull CsvComparisonOptions options,
                         @Nonnull CsvComparisonVisitor visitor) {
        this.source = checkNotNull(source, "source cannot be null");
        this.options = checkNotNull(options, "source cannot be null");
        this.visitor = checkNotNull(visitor, "source cannot be null");
    }

    @Nonnull
    public CsvComparisonResult compare() {
        visitor.visitStarted(source);
        Collector collector = new Collector();
        CsvParserSettings settings = createParserSettings();

        List<String[]> expRows = Utils.read(source.exp(), getEncoding(source.exp()), settings);
        Map<String, String[]> expMap = expRows.stream().collect(Collectors.toMap(expRow
                -> expRow[options.identityColumnIndex()], expRow -> expRow, (a, b) -> b));

        settings.setProcessor(new RowProcessor() {
            @Override
            public void processStarted(ParsingContext context) {
            }

            @Override
            public void rowProcessed(String[] row, ParsingContext context) {
                String[] expRow = expMap.get(row[options.identityColumnIndex()]);

                if (expRow == null) {
                    visitor.rowInserted(row, options);
                    collector.rowInserted(row, options);
                } else {
                    if (Arrays.equals(row, expRow)) {
                        visitor.rowKept(row, options);
                        collector.rowKept(row, options);
                    } else {
                        visitor.rowModified(row, options);
                        collector.rowModified(row, options);
                    }
                    expMap.remove(row[options.identityColumnIndex()]);
                }
            }

            @Override
            public void processEnded(ParsingContext context) {
            }
        });
        new CsvParser(settings).parse(source.act(), getEncoding(source.act()));

        if (expMap.size() > 0) {
            for (Map.Entry<String, String[]> left : expMap.entrySet()) {
                String[] row = left.getValue();
                visitor.rowDeleted(row, options);
                collector.rowDeleted(row, options);
            }
        }
        visitor.visitEnded(source);
        return new Result(collector);
    }

    private Charset getEncoding(File file) {
        try {
            return options.encoding() != null
                    ? options.encoding()
                    : Charset.forName(Utils.charsetOf(file));
        } catch (IOException ignored) {
            // Can't happen
            return StandardCharsets.UTF_8;
        }
    }

    private CsvParserSettings createParserSettings() {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(options.includedHeader());
        settings.getFormat().setLineSeparator(options.lineSeparator());

        if (options.selectedColumnNames().length > 0) {
            settings.selectFields(options.selectedColumnNames());
        } else if (options.selectedColumnIndexes().length > 0) {
            settings.selectIndexes(Arrays.stream(options.selectedColumnIndexes()).boxed().toArray(Integer[]::new));
        }
        Utils.createsDirectory(options.resultLocation());
        return settings;
    }

    private final static class Result implements CsvComparisonResult {

        private final Collector collector;

        private Result(Collector collector) {
            this.collector = collector;
        }

        @Override
        public boolean hasDeleted() {
            return collector.hasDeleted;
        }

        @Override
        public boolean hasInserted() {
            return collector.hasInserted;
        }

        @Override
        public boolean hasModified() {
            return collector.hasModified;
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
        public boolean hasDiff() {
            return hasDeleted() || hasInserted() || hasModified();
        }
    }

    private final static class Collector implements CsvComparisonVisitor {

        private boolean hasDeleted = false;
        private boolean hasInserted = false;
        private boolean hasModified = false;

        private final List<String[]> rowsKept = new ArrayList<>();
        private final List<String[]> rowsDeleted = new ArrayList<>();
        private final List<String[]> rowsInserted = new ArrayList<>();
        private final List<String[]> rowsModified = new ArrayList<>();

        @Override
        public void rowKept(String[] row, CsvComparisonOptions options) {
            rowsKept.add(row);
        }

        @Override
        public void rowDeleted(String[] row, CsvComparisonOptions options) {
            hasDeleted = true;
            rowsDeleted.add(row);
        }

        @Override
        public void rowInserted(String[] row, CsvComparisonOptions options) {
            hasInserted = true;
            rowsInserted.add(row);
        }

        @Override
        public void rowModified(String[] row, CsvComparisonOptions options) {
            hasModified = true;
            rowsModified.add(row);
        }

        @Override
        public void visitStarted(ComparisonSource<?> source) {

        }

        @Override
        public void visitEnded(ComparisonSource<?> source) {

        }
    }
}