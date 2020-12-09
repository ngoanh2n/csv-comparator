package com.github.ngoanh2n.asserts.csv;

import com.univocity.parsers.common.Format;
import com.univocity.parsers.csv.CsvParserSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
public interface CsvComparisonOptions {

    @Nullable
    Charset encoding();

    @Nonnull
    Path resultLocation();

    @Nonnull
    String lineSeparator();

    boolean includedHeader();

    int identityColumnIndex();

    int[] selectedColumnIndexes();

    @Nonnull
    String[] selectedColumnNames();

    final class Builder {

        private Charset encoding;
        private Path resultLocation;
        private String lineSeparator;
        private boolean includeHeader;
        private int selectedColumnCount;
        private int identityColumnIndex;
        private String[] selectedColumnNames;
        private Integer[] selectedColumnIndexes;

        private Builder() {
            this.encoding = null;
            this.lineSeparator = "\n";
            this.includeHeader = true;
            this.identityColumnIndex = 0;
            this.selectedColumnNames = new String[0];
            this.selectedColumnIndexes = new Integer[0];
            this.resultLocation = Paths.get("build/blur/comparator/csv");
        }

        /**
         * @param encoding the encoding of the CSV file <br>
         *                 https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
         * @return {@link CsvComparisonOptions.Builder}
         * @see java.nio.charset.StandardCharsets
         */
        public Builder encoding(@Nonnull Charset encoding) {
            this.encoding = encoding;
            return this;
        }

        /**
         * @param path where you store the results
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder resultLocation(@Nonnull Path path) {
            this.resultLocation = checkNotNull(path, "path cannot not be null");
            return this;
        }

        /**
         * Defines the line separator sequence that should be used for parsing and writing
         * <p>
         * {@link Format#setLineSeparator(String)}
         * {@link CsvParserSettings#getFormat()#lineSeparator(String)}
         *
         * @param lineSeparator a sequence of 1 to 2 characters that identifies the end of a line
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder lineSeparator(@Nonnull String lineSeparator) {
            this.lineSeparator = checkNotNull(lineSeparator, "lineSeparator cannot not be null");
            return this;
        }

        /**
         * Defines whether or not the first valid record parsed from the input should be considered
         * as the row containing the names of each column
         * <p>
         * {@link CsvParserSettings#setHeaderExtractionEnabled(boolean)}
         *
         * @param wanna A flag indicating whether the first valid record parsed from the input
         *              should be asserted or compared
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder includeHeader(boolean wanna) {
            this.includeHeader = wanna;
            return this;
        }

        /**
         * @param name indicate which is identity column
         *             determine which field is unique in a row. <br>
         *             e.g. id | email | username
         *             {@link #columns(String...)}
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder identityColumn(@Nonnull String name) {
            if (this.selectedColumnNames.length != 0) {
                checkNotNull(name, "name cannot not be null");
                this.identityColumnIndex = Arrays.asList(this.selectedColumnNames).indexOf(name);
            } else {
                throw new RuntimeException("Please use CsvComparisonOptions.builder().columns(String... names)");
            }
            return this;
        }

        /**
         * @param index indicate which is identity column
         *              determine which field is unique in a row.
         *              starts with 0 in range of columns by indexes
         *              <p>
         *              {@link #columns(Integer...)}
         *              <p>
         *              e.g. columns(1, 2, 5) <=> [username, address, prefs] <br>
         *              If you want to select field username as identity column <br>
         *              then set {@code CsvComparisonOptions.Builder.identityColumn(0)}
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder identityColumn(int index) {
            if (index > -1 && index < this.selectedColumnCount) {
                this.identityColumnIndex = index;
            } else {
                throw new RuntimeException(String.format("index should be in range [%s, %s]", 0, selectedColumnCount));
            }
            return this;
        }

        /**
         * {@link CsvParserSettings#selectFields(String...)}
         *
         * @param names the expected column names which assert or compare
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder columns(@Nonnull String... names) {
            checkNotNull(names, "names cannot not be null");
            this.selectedColumnNames = names;
            this.selectedColumnCount = names.length;
            return this;
        }

        /**
         * {@link CsvParserSettings#selectIndexes(Integer...)}
         *
         * @param indexes the expected column indexes which assert or compare
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder columns(@Nonnull Integer... indexes) {
            checkNotNull(indexes, "indexes cannot not be null");
            this.selectedColumnIndexes = indexes;
            this.selectedColumnCount = indexes.length;
            return this;
        }

        /**
         * Build CsvComparator based on CsvComparator.Builder
         *
         * @return {@link CsvComparator}
         */
        public CsvComparisonOptions build() {
            return new CsvComparisonOptions() {

                @Override
                public Charset encoding() {
                    return encoding;
                }

                @Nonnull
                @Override
                public Path resultLocation() {
                    return resultLocation;
                }

                @Nonnull
                @Override
                public String lineSeparator() {
                    return lineSeparator;
                }

                @Override
                public boolean includedHeader() {
                    return includeHeader;
                }

                @Override
                public int identityColumnIndex() {
                    return identityColumnIndex;
                }

                @Nonnull
                @Override
                public String[] selectedColumnNames() {
                    return selectedColumnNames;
                }

                @Override
                public int[] selectedColumnIndexes() {
                    return Arrays
                            .stream(selectedColumnIndexes)
                            .mapToInt(Integer::intValue).toArray();
                }
            };
        }
    }

    static Builder builder() {
        return new Builder();
    }

    static CsvComparisonOptions defaults() {
        return builder().build();
    }
}
