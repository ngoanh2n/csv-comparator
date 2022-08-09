package com.github.ngoanh2n.comparator;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
public class DefaultCsvComparisonVisitor implements CsvComparisonVisitor {

    private final static String KEPT = "kept.csv";
    private final static String DELETED = "deleted.csv";
    private final static String INSERTED = "inserted.csv";
    private final static String MODIFIED = "modified.csv";
    private final static Logger LOGGER = getLogger(DefaultCsvComparisonVisitor.class);

    private final String dir;
    private CsvWriter keptWriter;
    private CsvWriter deletedWriter;
    private CsvWriter insertedWriter;
    private CsvWriter modifiedWriter;
    private CsvWriterSettings settings;

    public DefaultCsvComparisonVisitor() {
        dir = Utils.timeStamp();
    }

    @Override
    public void rowKept(String[] row, String[] headers, CsvComparisonOptions options) {
        if (keptWriter == null) {
            File file = output(options, KEPT).toFile();
            keptWriter = new CsvWriter(file, options.encoding(), settings);
            writeHeader(headers, options, keptWriter);
        }
        keptWriter.writeRow(row);
        LOGGER.debug("Kept -> {}", Arrays.toString(row));
    }

    @Override
    public void rowDeleted(String[] row, String[] headers, CsvComparisonOptions options) {
        if (deletedWriter == null) {
            File file = output(options, DELETED).toFile();
            deletedWriter = new CsvWriter(file, options.encoding(), settings);
            writeHeader(headers, options, deletedWriter);
        }
        deletedWriter.writeRow(row);
        LOGGER.debug("Deleted -> {}", Arrays.toString(row));
    }

    @Override
    public void rowInserted(String[] row, String[] headers, CsvComparisonOptions options) {
        if (insertedWriter == null) {
            File file = output(options, INSERTED).toFile();
            insertedWriter = new CsvWriter(file, options.encoding(), settings);
            writeHeader(headers, options, insertedWriter);
        }
        insertedWriter.writeRow(row);
        LOGGER.debug("Inserted -> {}", Arrays.toString(row));
    }

    @Override
    public void rowModified(String[] row, String[] headers, CsvComparisonOptions options) {
        if (modifiedWriter == null) {
            File file = output(options, MODIFIED).toFile();
            modifiedWriter = new CsvWriter(file, options.encoding(), settings);
            writeHeader(headers, options, modifiedWriter);
        }
        modifiedWriter.writeRow(row);
        LOGGER.debug("Modified -> {}", Arrays.toString(row));
    }

    @Override
    public void visitStarted(CsvComparisonSource<?> source) {
        settings = new CsvWriterSettings();
    }

    @Override
    public void visitEnded(CsvComparisonSource<?> source) {
        if (keptWriter != null) keptWriter.close();
        if (deletedWriter != null) deletedWriter.close();
        if (insertedWriter != null) insertedWriter.close();
        if (modifiedWriter != null) modifiedWriter.close();
    }

    private void writeHeader(String[] headers, CsvComparisonOptions options, CsvWriter csvWriter) {
        if (headers.length > 0 && options.resultOptions().includeHeaders()) {
            csvWriter.writeRow(headers);
        }
    }

    private Path output(CsvComparisonOptions options, String diffType) {
        Path location = options.resultOptions().location().resolve(dir);
        return Utils.createsDirectory(location).resolve(diffType);
    }
}