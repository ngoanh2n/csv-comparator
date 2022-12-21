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
    private final static Logger LOGGER = getLogger(DefaultCsvComparisonVisitor.class);

    private final String dir;
    private CsvWriter keptWriter;
    private CsvWriter deletedWriter;
    private CsvWriter insertedWriter;
    private CsvWriter modifiedWriter;
    private CsvWriterSettings settings;

    public DefaultCsvComparisonVisitor() {
        dir = Commons.timestamp();
    }

    @Override
    public void comparisonStarted(CsvComparisonOptions options, CsvComparisonSource source) {
        settings = new CsvWriterSettings();
    }

    @Override
    public void rowKept(CsvComparisonOptions options, String[] headers, String[] row) {
        if (options.resultOptions().writesOutputs()) {
            keptWriter = writeHeaders(headers, options, keptWriter, "kept.csv");
            keptWriter.writeRow(row);
        }
        LOGGER.debug("K -> {}", Arrays.toString(row));
    }

    @Override
    public void rowDeleted(CsvComparisonOptions options, String[] headers, String[] row) {
        if (options.resultOptions().writesOutputs()) {
            deletedWriter = writeHeaders(headers, options, deletedWriter, "deleted.csv");
            deletedWriter.writeRow(row);
        }
        LOGGER.debug("D -> {}", Arrays.toString(row));
    }

    @Override
    public void rowInserted(CsvComparisonOptions options, String[] headers, String[] row) {
        if (options.resultOptions().writesOutputs()) {
            insertedWriter = writeHeaders(headers, options, insertedWriter, "inserted.csv");
            insertedWriter.writeRow(row);
        }
        LOGGER.debug("I -> {}", Arrays.toString(row));
    }

    @Override
    public void rowModified(CsvComparisonOptions options, String[] headers, String[] row, List<HashMap<String, String>> diffs) {
        if (options.resultOptions().writesOutputs()) {
            modifiedWriter = writeHeaders(headers, options, modifiedWriter, "modified.csv");
            modifiedWriter.writeRow(row);
        }
        LOGGER.debug("M -> {}", Arrays.toString(row));
    }

    @Override
    public void comparisonFinished(CsvComparisonOptions options, CsvComparisonSource source, CsvComparisonResult result) {
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

            if (headers.length > 0 && options.resultOptions().includesHeaders()) {
                writer.writeRow(headers);
            }
        }
        return writer;
    }
}