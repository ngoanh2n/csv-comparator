package com.github.ngoanh2n.asserts.csv;

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

    private final static String KEPT = "kept";
    private final static String DELETED = "deleted";
    private final static String INSERTED = "inserted";
    private final static String MODIFIED = "modified";
    private final static Logger LOGGER = getLogger(DefaultCsvComparisonVisitor.class);

    private CsvWriter keptWriter;
    private CsvWriter deletedWriter;
    private CsvWriter insertedWriter;
    private CsvWriter modifiedWriter;
    private CsvWriterSettings settings;

    @Override
    public void rowKept(String[] row, CsvComparisonOptions options) {
        if (keptWriter == null) {
            File file = output(options, KEPT);
            keptWriter = new CsvWriter(file, options.encoding(), settings);
            LOGGER.debug("[comparator][csv] Kept row -> {}", Arrays.toString(row));
        }
        keptWriter.writeRow(row);
    }

    @Override
    public void rowDeleted(String[] row, CsvComparisonOptions options) {
        if (deletedWriter == null) {
            File file = output(options, DELETED);
            deletedWriter = new CsvWriter(file, options.encoding(), settings);
            LOGGER.debug("[comparator][csv] Deleted row -> {}", Arrays.toString(row));
        }
        deletedWriter.writeRow(row);
    }

    @Override
    public void rowInserted(String[] row, CsvComparisonOptions options) {
        if (insertedWriter == null) {
            File file = output(options, INSERTED);
            insertedWriter = new CsvWriter(file, options.encoding(), settings);
            LOGGER.debug("[comparator][csv] Inserted row -> {}", Arrays.toString(row));
        }
        insertedWriter.writeRow(row);
    }

    @Override
    public void rowModified(String[] row, CsvComparisonOptions options) {
        if (modifiedWriter == null) {
            File file = output(options, MODIFIED);
            modifiedWriter = new CsvWriter(file, options.encoding(), settings);
            LOGGER.debug("[comparator][csv] Modified row -> {}", Arrays.toString(row));
        }
        modifiedWriter.writeRow(row);
    }

    @Override
    public void visitStarted(ComparisonSource<?> source) {
        settings = new CsvWriterSettings();
    }

    @Override
    public void visitEnded(ComparisonSource<?> source) {
        if (keptWriter != null) keptWriter.close();
        if (insertedWriter != null) insertedWriter.close();
        if (modifiedWriter != null) modifiedWriter.close();
    }

    private File output(CsvComparisonOptions options, String folder) {
        Path location = options.resultLocation().resolve(folder);
        Utils.createsDirectory(location);
        return location.resolve(Utils.timeStamp() + ".csv").toFile();
    }
}
