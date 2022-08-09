package com.github.ngoanh2n.comparator;

import com.github.ngoanh2n.Commons;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Default implementation for {@linkplain CsvComparisonVisitor}
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
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
        dir = Commons.timeStamp();
    }

    @Override
    public void rowKept(String[] row, String[] header, CsvComparisonOptions options) {
        keptWriter = writeHeaders(header, options, keptWriter, KEPT);
        keptWriter.writeRow(row);
        LOGGER.debug("Kept -> {}", Arrays.toString(row));
    }

    @Override
    public void rowDeleted(String[] row, String[] header, CsvComparisonOptions options) {
        deletedWriter = writeHeaders(header, options, deletedWriter, DELETED);
        deletedWriter.writeRow(row);
        LOGGER.debug("Deleted -> {}", Arrays.toString(row));
    }

    @Override
    public void rowInserted(String[] row, String[] header, CsvComparisonOptions options) {
        insertedWriter = writeHeaders(header, options, insertedWriter, INSERTED);
        insertedWriter.writeRow(row);
        LOGGER.debug("Inserted -> {}", Arrays.toString(row));
    }

    @Override
    public void rowModified(String[] row, String[] header, CsvComparisonOptions options) {
        modifiedWriter = writeHeaders(header, options, modifiedWriter, MODIFIED);
        modifiedWriter.writeRow(row);
        LOGGER.debug("Modified -> {}", Arrays.toString(row));
    }

    @Override
    public void comparisonStarted(CsvComparisonSource source) {
        settings = new CsvWriterSettings();
    }

    @Override
    public void comparisonFinished(CsvComparisonSource source) {
        if (keptWriter != null) keptWriter.close();
        if (deletedWriter != null) deletedWriter.close();
        if (insertedWriter != null) insertedWriter.close();
        if (modifiedWriter != null) modifiedWriter.close();
    }

    private Path output(CsvComparisonOptions options, String diffType) {
        Path location = options.resultOptions().location().resolve(dir);
        return Commons.createDirectory(location).resolve(diffType);
    }

    private CsvWriter writeHeaders(String[] header, CsvComparisonOptions options, CsvWriter csvWriter, String type) {
        if (csvWriter == null) {
            File file = output(options, type).toFile();
            csvWriter = new CsvWriter(file, options.encoding(), settings);

            if (header.length > 0 && options.resultOptions().includeHeader()) {
                csvWriter.writeRow(header);
            }
        }
        return csvWriter;
    }
}