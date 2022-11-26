package com.github.ngoanh2n.comparator;

import com.github.ngoanh2n.Commons;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    public void comparisonStarted(CsvComparisonSource source, CsvComparisonOptions options) {
        settings = new CsvWriterSettings();
    }

    @Override
    public void rowKept(String[] row, String[] headers, CsvComparisonOptions options) {
        keptWriter = writeHeaders(headers, options, keptWriter, KEPT);
        keptWriter.writeRow(row);
        LOGGER.debug("Kept -> {}", Arrays.toString(row));
    }

    @Override
    public void rowDeleted(String[] row, String[] headers, CsvComparisonOptions options) {
        deletedWriter = writeHeaders(headers, options, deletedWriter, DELETED);
        deletedWriter.writeRow(row);
        LOGGER.debug("Deleted -> {}", Arrays.toString(row));
    }

    @Override
    public void rowInserted(String[] row, String[] headers, CsvComparisonOptions options) {
        insertedWriter = writeHeaders(headers, options, insertedWriter, INSERTED);
        insertedWriter.writeRow(row);
        LOGGER.debug("Inserted -> {}", Arrays.toString(row));
    }

    @Override
    public void rowModified(String[] row, String[] headers, CsvComparisonOptions options, List<HashMap<String, String>> diffs) {
        modifiedWriter = writeHeaders(headers, options, modifiedWriter, MODIFIED);
        modifiedWriter.writeRow(row);
        LOGGER.debug("Modified -> {}", Arrays.toString(row));
    }

    @Override
    public void comparisonFinished(CsvComparisonSource source, CsvComparisonOptions options, CsvComparisonResult result) {
        if (keptWriter != null) keptWriter.close();
        if (deletedWriter != null) deletedWriter.close();
        if (insertedWriter != null) insertedWriter.close();
        if (modifiedWriter != null) modifiedWriter.close();
    }

    private CsvWriter writeHeaders(String[] headers, CsvComparisonOptions options, CsvWriter writer, String fileName) {
        if (writer == null) {
            Path location = options.resultOptions().location().resolve(dir);
            File file = Commons.createDir(location).resolve(fileName).toFile();
            writer = new CsvWriter(file, options.encoding(), settings);

            if (headers.length > 0 && options.resultOptions().includeHeaders()) {
                writer.writeRow(headers);
            }
        }
        return writer;
    }
}