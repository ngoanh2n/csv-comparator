package com.github.ngoanh2n.asserts.csv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * @author ngoanh2n@gmail.com (Ho Huu Ngoan)
 */

public final class CsvComparator {

    private final static Logger logger = LoggerFactory.getLogger(lookup().lookupClass());

    private Charset encoding;
    private String diffPath;
    private File actualCsv;
    private File expectedCsv;
    private int identifierColumn;
    private CsvParserSettings parserSettings;

    private CsvComparator(Builder builder) {
        this.encoding = builder.encoding;
        this.identifierColumn = builder.identityColumn;
        this.buildParserSettings(
                builder.lineSeparator,
                builder.includedHeader,
                builder.selectedColumnNames,
                builder.selectedColumnIndexes
        );
        this.actualCsv = builder.actualCsv;
        this.expectedCsv = builder.expectedCsv;
    }

    /*
     * Parsing Builder
     * */
    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public final static class Builder extends ParsingStrategy<Builder> {

        private File actualCsv;
        private File expectedCsv;

        public Builder onCsvFiles(File actual, File expected) {
            Utils.checkNotNull(actual, "actual == null");
            Utils.checkNotNull(expected, "expected == null");

            this.actualCsv = actual;
            this.expectedCsv = expected;
            return this;
        }

        public Builder onCsvFiles(String actualResourcePath, String expectedResourcePath) {
            Utils.checkNotNull(actualResourcePath, "actualResourcePath == null");
            Utils.checkNotNull(expectedResourcePath, "expectedResourcePath == null");

            this.onCsvFiles(
                    Utils.getFileFromResources(actualResourcePath),
                    Utils.getFileFromResources(expectedResourcePath)
            );
            return this;
        }

        public CsvComparator build() {
            return new CsvComparator(this);
        }
    }

    public CsvComparator saveDiffAt(String path) {
        File diffDir = new File(path);
        if (!diffDir.exists()) diffDir.mkdirs();
        diffPath = path;
        return this;
    }

    public CsvComparisonResult perform() {
        CsvComparisonResultImpl result = new CsvComparisonResultImpl();
        result.setDiffPath(diffPath);

        List<String[]> expectedRows = new CsvParser(parserSettings).parseAll(expectedCsv, this.encoding);
        Map<String, String[]> mapOfExpectedRecords = new HashMap<>();

        for (String[] row : expectedRows) {
            mapOfExpectedRecords.put(row[identifierColumn], row);
        }

        CsvWriterSettings writerSettings = new CsvWriterSettings();

        parserSettings.setProcessor(new RowProcessor() {
            CsvWriter additionWriter;
            CsvWriter modificationWriter;

            @Override
            public void processStarted(ParsingContext context) {
                additionWriter = new CsvWriter(
                        new File(Paths.get(diffPath, Constants.ADDITION_FILE_NAME).toString()),
                        encoding, writerSettings);

                modificationWriter = new CsvWriter(
                        new File(Paths.get(diffPath, Constants.MODIFICATION_FILE_NAME).toString()),
                        encoding, writerSettings);
            }

            @Override
            public void rowProcessed(String[] row, ParsingContext context) {
                String[] expectedRow = mapOfExpectedRecords.get(row[identifierColumn]);

                if (expectedRow != null) {
                    if (!Arrays.equals(row, expectedRow)) {
                        result.setHasRowModified(true);
                        modificationWriter.writeRow(row);
                        logger.info("Modification row -> {}", Arrays.toString(row));

                    } else {
                        logger.info("Equality row -> {}", Arrays.toString(row));
                    }

                    mapOfExpectedRecords.remove(row[identifierColumn]);

                } else {
                    result.setHasRowAdded(true);
                    additionWriter.writeRow(row);
                    logger.info("Addition row -> {}", Arrays.toString(row));
                }
            }

            @Override
            public void processEnded(ParsingContext context) {
                modificationWriter.close();
                additionWriter.close();
            }
        });

        new CsvParser(parserSettings).parse(actualCsv, encoding);

        if (mapOfExpectedRecords.size() > 0) {
            CsvWriter deletionWriter = new CsvWriter(
                    new File(Paths.get(diffPath, Constants.DELETION_FILE_NAME).toString()),
                    encoding, writerSettings);

            for (Map.Entry<String, String[]> rowOfRemainder : mapOfExpectedRecords.entrySet()) {
                result.setHasRowDeleted(true);
                deletionWriter.writeRow(rowOfRemainder.getValue());
                logger.info("Deletion row -> {}", Arrays.toString(rowOfRemainder.getValue()));
            }
            deletionWriter.close();
        }

        return result;
    }

    private void buildParserSettings(String lineSeparator, boolean includedHeader,
                                     String[] selectedColumnNames, Integer[] selectedColumnIndexes) {
        this.parserSettings = new CsvParserSettings();
        this.parserSettings.getFormat().setLineSeparator(lineSeparator);
        this.parserSettings.setHeaderExtractionEnabled(includedHeader);

        if (selectedColumnNames.length > 0)
            this.parserSettings.selectFields(selectedColumnNames);

        else if (selectedColumnIndexes.length > 0)
            this.parserSettings.selectIndexes(selectedColumnIndexes);
    }
}
