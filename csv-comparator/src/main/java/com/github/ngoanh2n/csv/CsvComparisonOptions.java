package com.github.ngoanh2n.csv;

import com.univocity.parsers.csv.CsvParserSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Adjust behaviors of {@link CsvComparator}.<br><br>
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
public interface CsvComparisonOptions {
    /**
     * Get {@link Builder} class where allows to build your {@link CsvComparisonOptions}.
     *
     * @return A {@link Builder}.
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Get {@link CsvComparisonOptions} with default options.
     *
     * @return A {@link CsvComparisonOptions}.
     */
    static CsvComparisonOptions defaults() {
        return builder().build();
    }

    //-------------------------------------------------------------------------------//

    /**
     * {@link Charset} for reading and writing CSV files.
     *
     * @return A {@link Charset}.
     */
    @Nullable
    Charset charset();

    /**
     * Which column index or name where has a data field unique.
     *
     * @return The column id.
     */
    Object columnId();

    /**
     * The configuration class used by the CSV parser.
     *
     * @return A {@link CsvParserSettings}.
     */
    @Nonnull
    CsvParserSettings parserSettings();

    /**
     * The comparison result options to adjust behaviors of {@link CsvComparisonResult}.
     *
     * @return A {@link CsvComparisonResultOptions}.
     */
    @Nonnull
    CsvComparisonResultOptions resultOptions();

    //===============================================================================//

    /**
     * Build a {@link CsvComparisonOptions}.
     */
    final class Builder {
        private final CsvParserSettings parserSettings;
        private Charset charset;
        private Object columnId;
        private CsvComparisonResultOptions resultOptions;

        private Builder() {
            this.charset = null;
            this.columnId = null;
            this.parserSettings = new CsvParserSettings();
            this.parserSettings.setHeaderExtractionEnabled(true);
            this.resultOptions = CsvComparisonResultOptions.defaults();
        }

        /**
         * Set charset to read and writing CSV files.
         *
         * @param charset The {@code Charset} for reading and writing CSV files.<br>
         *                <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html">encoding</a>
         * @return The current {@link Builder}.
         * @see java.nio.charset.StandardCharsets
         */
        public Builder charset(@Nullable Charset charset) {
            this.charset = charset;
            return this;
        }

        /**
         * Define the line separator sequence that should be used for parsing and writing.
         *
         * @param lineSeparator The sequence of 1 to 2 characters that identifies the end of a line.
         * @return The current {@link Builder}.
         */
        public Builder lineSeparator(@Nonnull String lineSeparator) {
            checkNotNull(lineSeparator, "`lineSeparator` cannot not be null");
            this.parserSettings.getFormat().setLineSeparator(lineSeparator);
            return this;
        }

        /**
         * This means, CSV files hasn't headers.
         *
         * @return The current {@link Builder}.
         */
        public Builder withoutHeaders() {
            this.parserSettings.setHeaderExtractionEnabled(false);
            return this;
        }

        /**
         * Select columns to compare.
         *
         * @param names The expected column names which assert or compare.
         * @return The current {@link Builder}.
         */
        public Builder selectColumns(@Nonnull String... names) {
            checkNotNull(names, "ColumnNames cannot not be null");
            this.parserSettings.selectFields(names);
            return this;
        }

        /**
         * Select columns to compare (0-based).
         *
         * @param indexes The expected column indexes which assert or compare.
         * @return The current {@link Builder}.
         */
        public Builder selectColumns(@Nonnull Integer... indexes) {
            checkNotNull(indexes, "ColumnIndexes cannot not be null");
            this.parserSettings.selectIndexes(indexes);
            return this;
        }

        /**
         * Set column name where has a data field unique.
         *
         * @param name Indicate which is identity column name which data field is unique in a row.<br>
         *             You have to use {@link #selectColumns(String...)} first.<br>
         *             e.g. {@code #selectColumns("email", "firstname", "lastname")}.
         * @return The current {@link Builder}.
         */
        public Builder selectColumnId(@Nonnull String name) {
            checkNotNull(name, "ColumnId cannot not be null");
            checkArgument(!name.isEmpty(), "ColumnId cannot not be empty");
            this.columnId = name;
            return this;
        }

        /**
         * Set column index where has a data field unique.
         *
         * @param index Indicate which is identity column index which data field is unique in a row.<br>
         *              You have to use {@link #selectColumns(Integer...)} first.<br>
         *              e.g. #selectColumns(1, 2, 5).
         * @return The current {@link Builder}.
         */
        public Builder selectColumnId(int index) {
            checkArgument(index > -1, "ColumnId should be greater -1");
            this.columnId = index;
            return this;
        }

        /**
         * Set {@link CsvComparisonResultOptions} to adjust {@link CsvComparisonResult} output.
         *
         * @param options to adjust output.
         * @return The current {@link Builder}.
         */
        public Builder resultOptions(@Nonnull CsvComparisonResultOptions options) {
            this.resultOptions = options;
            return this;
        }

        /**
         * Build {@link CsvComparisonOptions} based on {@link Builder}.
         *
         * @return A {@link CsvComparisonOptions}.
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
