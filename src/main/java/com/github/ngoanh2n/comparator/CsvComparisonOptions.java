package com.github.ngoanh2n.comparator;

import com.github.ngoanh2n.RuntimeError;
import com.univocity.parsers.csv.CsvParserSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class allows to adjust {@linkplain CsvComparator} by your expectation.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
public interface CsvComparisonOptions {
    /**
     * Get {@linkplain Builder} class where allows to build your {@linkplain CsvComparisonOptions}
     *
     * @return {@linkplain CsvComparisonOptions.Builder}
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Get {@linkplain CsvComparisonOptions} with default options
     *
     * @return {@linkplain CsvComparisonOptions}
     */
    static CsvComparisonOptions defaults() {
        return builder().build();
    }

    /**
     * This class allows to build {@linkplain CsvComparisonOptions}
     */
    final class Builder {
        private Charset encoding;
        private int selectedColumnCount;
        private int identityColumnIndex;
        private String[] selectedColumnNames;
        private final CsvParserSettings csvParser;
        private CsvComparisonResultOptions resultOptions;

        private Builder() {
            encoding = null;
            identityColumnIndex = 0;
            selectedColumnNames = new String[0];
            csvParser = new CsvParserSettings();
            csvParser.setHeaderExtractionEnabled(true);
            csvParser.getFormat().setLineSeparator("\n");
            resultOptions = CsvComparisonResultOptions.defaults();
        }

        /**
         * Set encoding to read and writing CSV files
         *
         * @param encoding is the {@code Charset} for reading and writing CSV files. <br>
         *                 <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html">encoding</a>
         * @return {@link Builder}
         * @see java.nio.charset.StandardCharsets
         */
        public Builder setEncoding(@Nullable Charset encoding) {
            this.encoding = encoding;
            return this;
        }

        /**
         * Defines the line separator sequence that should be used for parsing and writing
         *
         * @param lineSeparator is a sequence of 1 to 2 characters that identifies the end of a line
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder setLineSeparator(@Nonnull String lineSeparator) {
            checkNotNull(lineSeparator, "lineSeparator cannot not be null");
            csvParser.getFormat().setLineSeparator(lineSeparator);
            return this;
        }

        /**
         * Defines whether the first valid record parsed from
         * the input should be considered as the row containing the names of each column. <br>
         * This means, your input files are marked first row as header if {@code extracted} is {@code true}
         *
         * @param extracted is flag indicating whether the first valid record parsed from
         *                  the input should be considered as the row containing the names of each column
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder extractHeaders(boolean extracted) {
            csvParser.setHeaderExtractionEnabled(extracted);
            return this;
        }

        /**
         * Set to select columns to compare
         *
         * @param names is the expected column names which assert or compare
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder setColumns(@Nonnull String... names) {
            checkNotNull(names, "names cannot not be null");
            selectedColumnNames = names;
            selectedColumnCount = names.length;
            csvParser.selectFields(names);
            return this;
        }

        /**
         * Set to select columns to compare
         *
         * @param indexes is the expected column indexes which assert or compare
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder setColumns(@Nonnull Integer... indexes) {
            checkNotNull(indexes, "indexes cannot not be null");
            selectedColumnCount = indexes.length;
            csvParser.selectIndexes(indexes);
            return this;
        }

        /**
         * Set column name where has a data field unique
         *
         * @param name for indicating which is identity column name which data field is unique in a row. <br>
         *             You have to use {@linkplain #setColumns(String...)} first. <br>
         *             e.g. {@code #setColumns("email", "firstname", "lastname")}
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setIdentityColumn(@Nonnull String name) {
            if (selectedColumnNames.length != 0) {
                checkNotNull(name, "name cannot not be null");
                identityColumnIndex = Arrays.asList(this.selectedColumnNames).indexOf(name);
            } else {
                throw new RuntimeError("Please use #setColumns(String... names) first");
            }
            return this;
        }

        /**
         * Set column index where has a data field unique
         *
         * @param index for indicating which is identity column index which data field is unique in a row. <br>
         *              Starts with 0 in range of columns by indexes (0-based indexing) <br>
         *              e.g. CsvComparisonOptions.Builder.setColumns(1, 2, 5) = [username, address, prefs] <br>
         *              If you want to select field username as identity column <br>
         *              then set {@code #setIdentityColumn(0)}
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setIdentityColumn(int index) {
            if (index > -1 && index < this.selectedColumnCount) {
                identityColumnIndex = index;
            } else {
                throw new RuntimeError(String.format("index should be in range [%s, %s]", 0, selectedColumnCount));
            }
            return this;
        }

        /**
         * Set {@linkplain CsvComparisonResultOptions} to adjust {@linkplain CsvComparisonResult} output
         *
         * @param options to adjust output
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder setResultOptions(@Nonnull CsvComparisonResultOptions options) {
            resultOptions = options;
            return this;
        }

        /**
         * Build {@linkplain CsvComparisonOptions} based on {@linkplain CsvComparisonOptions.Builder}
         *
         * @return {@linkplain CsvComparisonOptions}
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

    /**
     * {@linkplain Charset} for reading and writing CSV files
     *
     * @return {@link Charset}
     */
    @Nullable
    Charset encoding();

    /**
     * Which column index where has a data field unique
     *
     * @return column index. 0-based indexing
     */
    int identityColumnIndex();

    /**
     * The configuration class used by the CSV parser
     *
     * @return {@linkplain CsvParserSettings}
     */
    @Nonnull
    CsvParserSettings parserSettings();

    /**
     * The comparison result options to adjust your {@linkplain CsvComparisonResult} output
     *
     * @return {@linkplain CsvComparisonResultOptions}
     */
    @Nonnull
    CsvComparisonResultOptions resultOptions();
}
