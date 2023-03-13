package com.github.ngoanh2n.comparator;

import com.univocity.parsers.csv.CsvParserSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkArgument;
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
     * Gets {@linkplain Builder} class where allows to build your {@linkplain CsvComparisonOptions}.
     *
     * @return {@linkplain CsvComparisonOptions.Builder}.
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Gets {@linkplain CsvComparisonOptions} with default options.
     *
     * @return {@linkplain CsvComparisonOptions}.
     */
    static CsvComparisonOptions defaults() {
        return builder().build();
    }

    //-------------------------------------------------------------------------------//

    /**
     * {@linkplain Charset} for reading and writing CSV files.
     *
     * @return {@link Charset}.
     */
    @Nullable
    Charset charset();

    /**
     * Which column index or name where has a data field unique.
     *
     * @return column id.
     */
    Object columnId();

    /**
     * The configuration class used by the CSV parser.
     *
     * @return {@linkplain CsvParserSettings}.
     */
    @Nonnull
    CsvParserSettings parserSettings();

    /**
     * The comparison result options to adjust your {@linkplain CsvComparisonResult} output.
     *
     * @return {@linkplain CsvComparisonResultOptions}.
     */
    @Nonnull
    CsvComparisonResultOptions resultOptions();

    //===============================================================================//

    /**
     * This class allows to build {@linkplain CsvComparisonOptions}.
     */
    final class Builder {
        private final CsvParserSettings parserSettings;
        private Charset charset;
        private Object columnId;
        private CsvComparisonResultOptions resultOptions;

        private Builder() {
            charset = null;
            columnId = null;
            parserSettings = new CsvParserSettings();
            parserSettings.setHeaderExtractionEnabled(true);
            resultOptions = CsvComparisonResultOptions.defaults();
        }

        /**
         * Set charset to read and writing CSV files.
         *
         * @param charset is the {@code Charset} for reading and writing CSV files. <br>
         *                <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html">encoding</a>
         * @return {@link Builder}
         * @see java.nio.charset.StandardCharsets
         */
        public Builder setCharset(@Nullable Charset charset) {
            this.charset = charset;
            return this;
        }

        /**
         * Defines the line separator sequence that should be used for parsing and writing
         *
         * @param lineSeparator is a sequence of 1 to 2 characters that identifies the end of a line
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder setLineSeparator(@Nonnull String lineSeparator) {
            checkNotNull(lineSeparator, "`lineSeparator` cannot not be null");
            parserSettings.getFormat().setLineSeparator(lineSeparator);
            return this;
        }

        /**
         * This means, CSV files hasn't headers
         *
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder withoutHeaders() {
            parserSettings.setHeaderExtractionEnabled(false);
            return this;
        }

        /**
         * Select columns to compare
         *
         * @param names is the expected column names which assert or compare
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder selectColumns(@Nonnull String... names) {
            checkNotNull(names, "ColumnNames cannot not be null");
            parserSettings.selectFields(names);
            return this;
        }

        /**
         * Select columns to compare (0-based)
         *
         * @param indexes is the expected column indexes which assert or compare
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public Builder selectColumns(@Nonnull Integer... indexes) {
            checkNotNull(indexes, "ColumnIndexes cannot not be null");
            parserSettings.selectIndexes(indexes);
            return this;
        }

        /**
         * Set column name where has a data field unique
         *
         * @param name for indicating which is identity column name which data field is unique in a row. <br>
         *             You have to use {@linkplain #selectColumns(String...)} first. <br>
         *             e.g. {@code #selectColumns("email", "firstname", "lastname")}
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder selectColumnId(@Nonnull String name) {
            checkNotNull(name, "ColumnId cannot not be null");
            checkArgument(!name.isEmpty(), "ColumnId cannot not be empty");
            columnId = name;
            return this;
        }

        /**
         * Set column index where has a data field unique
         *
         * @param index for indicating which is identity column index which data field is unique in a row. <br>
         *              You have to use {@linkplain #selectColumns(Integer...)} first. <br>
         *              e.g. #selectColumns(1, 2, 5)
         * @return {@link CsvComparisonOptions.Builder}
         */
        public Builder selectColumnId(int index) {
            checkArgument(index > -1, "ColumnId should be greater -1");
            columnId = index;
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
                public Charset charset() {
                    return charset;
                }

                @Override
                public Object columnId() {
                    return columnId;
                }

                @Nonnull
                @Override
                public CsvParserSettings parserSettings() {
                    return parserSettings;
                }

                @Nonnull
                @Override
                public CsvComparisonResultOptions resultOptions() {
                    return resultOptions;
                }
            };
        }
    }
}
