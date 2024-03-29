package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Commons;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A default implementation for {@link CsvComparisonVisitor} for writing output files.<br><br>
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
public class CsvComparisonOutput implements CsvComparisonVisitor {
    private final static Logger log = LoggerFactory.getLogger(CsvComparisonOutput.class);
    private String dir;
    private CsvWriter keptWriter;
    private CsvWriter deletedWriter;
    private CsvWriter insertedWriter;
    private CsvWriter modifiedWriter;
    private CsvWriterSettings settings;

    //-------------------------------------------------------------------------------//

    /**
     * Default constructor.
     */
    public CsvComparisonOutput() { /**/ }

    /**
     * {@inheritDoc}
     */
    @Override
    public void comparisonStarted(CsvComparisonOptions options, File exp, File act) {
        dir = Commons.timestamp();
        settings = new CsvWriterSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rowKept(CsvComparisonOptions options, String[] headers, String[] row) {
        keptWriter = writeHeaders(headers, options, keptWriter, "kept.csv");
        keptWriter.writeRow(row);
        log.debug("{}", Arrays.toString(row));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rowDeleted(CsvComparisonOptions options, String[] headers, String[] row) {
        deletedWriter = writeHeaders(headers, options, deletedWriter, "deleted.csv");
        deletedWriter.writeRow(row);
        log.debug("{}", Arrays.toString(row));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rowInserted(CsvComparisonOptions options, String[] headers, String[] row) {
        insertedWriter = writeHeaders(headers, options, insertedWriter, "inserted.csv");
        insertedWriter.writeRow(row);
        log.debug("{}", Arrays.toString(row));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rowModified(CsvComparisonOptions options, String[] headers, String[] row, List<HashMap<String, String>> diffs) {
        modifiedWriter = writeHeaders(headers, options, modifiedWriter, "modified.csv");
        modifiedWriter.writeRow(row);
        log.debug("{}", Arrays.toString(row));
    }

    //-------------------------------------------------------------------------------//

    /**
     * {@inheritDoc}
     */
    @Override
    public void comparisonFinished(CsvComparisonOptions options, File exp, File act, CsvComparisonResult result) {
        if (keptWriter != null) keptWriter.close();
        if (deletedWriter != null) deletedWriter.close();
        if (insertedWriter != null) insertedWriter.close();
        if (modifiedWriter != null) modifiedWriter.close();
    }

    //-------------------------------------------------------------------------------//

    private CsvWriter writeHeaders(String[] headers, CsvComparisonOptions options, CsvWriter writer, String fileName) {
        if (writer == null) {
            Path location = options.resultOptions().location().resolve(dir);
            File file = Commons.createDir(location).resolve(fileName).toFile();
            writer = new CsvWriter(file, options.charset(), settings);

            if (headers.length > 0 && options.resultOptions().includeHeaders()) {
                writer.writeRow(headers);
            }
        }
        return writer;
    }
}