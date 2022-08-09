package com.github.ngoanh2n.comparator;

import com.univocity.parsers.csv.CsvParserSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
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

    int identityColumnIndex();

    @Nonnull
    CsvParserSettings parserSettings();

    @Nonnull
    CsvComparisonResultOptions resultOptions();

    final class Builder {

        private Charset encoding;
        private int selectedColumnCount;
        private int identityColumnIndex;
        private String[] selectedColumnNames;
        private final CsvParserSettings csvParser;
        private CsvComparisonResultOptions resultOptions;

        private Builder() {
            this.encoding = null;
            this.identityColumnIndex = 0;
            this.selectedColumnNames = new String[0];
            this.csvParser = new CsvParserSettings();
            this.csvParser.setHeaderExtractionEnabled(true);
            this.csvParser.getFormat().setLineSeparator("\n");
            this.resultOptions = CsvComparisonResultOptions.defaults();
        }

        /**
         * @param encoding the encoding of the CSV file <br>
         *                 https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
         * @return {@link CsvComparisonOptions.Builder}
         * @see java.nio.charset.StandardCharsets
         */
        public Builder setEncoding(@Nullable Charset encoding) {
            this.encoding = encoding;
            return this;
        }

        /**
         * Defines the line separator sequence that should be used for parsing and writing
         * <p>
         *
         * @param lineSeparator a sequence of 1 to 2 characters that identifies the end of a line
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setLineSeparator(@Nonnull String lineSeparator) {
            checkNotNull(lineSeparator, "lineSeparator cannot not be null");
            this.csvParser.getFormat().setLineSeparator(lineSeparator);
            return this;
        }

        /**
         * Defines whether or not the first valid record parsed from
         * the input should be considered as the row containing the names of each column.<br>
         * This means, your input files are marked first row as header if {@code extracted} is {@code true}
         * <p>
         *
         * @param extracted A flag indicating whether the first valid record parsed from
         *                  the input should be considered as the row containing the names of each column
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder extractHeader(boolean extracted) {
            this.csvParser.setHeaderExtractionEnabled(extracted);
            return this;
        }

        /**
         * @param name indicate which is identity column
         *             determine which field is unique in a row. <br>
         *             e.g. id | email | username
         *             {@link #setColumns(String...)}
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setIdentityColumn(@Nonnull String name) {
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
         *              {@link #setColumns(Integer...)}
         *              <p>
         *              e.g. columns(1, 2, 5) <=> [username, address, prefs] <br>
         *              If you want to select field username as identity column <br>
         *              then set {@code CsvComparisonOptions.Builder.identityColumn(0)}
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setIdentityColumn(int index) {
            if (index > -1 && index < this.selectedColumnCount) {
                this.identityColumnIndex = index;
            } else {
                throw new RuntimeException(String.format("index should be in range [%s, %s]", 0, selectedColumnCount));
            }
            return this;
        }

        /**
         * @param names the expected column names which assert or compare
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setColumns(@Nonnull String... names) {
            checkNotNull(names, "names cannot not be null");
            this.selectedColumnNames = names;
            this.selectedColumnCount = names.length;
            this.csvParser.selectFields(names);
            return this;
        }

        /**
         * @param indexes the expected column indexes which assert or compare
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setColumns(@Nonnull Integer... indexes) {
            checkNotNull(indexes, "indexes cannot not be null");
            this.selectedColumnCount = indexes.length;
            this.csvParser.selectIndexes(indexes);
            return this;
        }

        public Builder setResultOptions(@Nonnull CsvComparisonResultOptions options) {
            this.resultOptions = options;
            return this;
        }

        /**
         * Build CsvComparisonOptions based on CsvComparisonOptions.Builder
         *
         * @return {@link CsvComparator}
         */
        public CsvComparisonOptions build() {
            return new CsvComparisonOptions() {

                @Override
                public Charset encoding() {
                    return encoding;
                }

                @Override
                public int identityColumnIndex() {
                    return identityColumnIndex;
                }

                @Nonnull
                @Override
                public CsvParserSettings parserSettings() {
                    return csvParser;
                }

                @Nonnull
                @Override
                public CsvComparisonResultOptions resultOptions() {
                    return resultOptions;
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
