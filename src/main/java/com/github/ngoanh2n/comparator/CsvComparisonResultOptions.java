package com.github.ngoanh2n.comparator;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class allows to adjust {@linkplain CsvComparisonResult} output by your expectation.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
public interface CsvComparisonResultOptions {
    /**
     * Get {@linkplain CsvComparisonResultOptions.Builder} class where allows to build your {@linkplain CsvComparisonResultOptions}
     *
     * @return {@linkplain CsvComparisonResultOptions.Builder}
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Get {@linkplain CsvComparisonResultOptions} with default options
     *
     * @return {@linkplain CsvComparisonResultOptions}
     */
    static CsvComparisonResultOptions defaults() {
        return builder().build();
    }

    /**
     * This class allows to build {@linkplain CsvComparisonResultOptions}
     */
    final class Builder {

        private Path location;
        private boolean includeHeaders;

        private Builder() {
            includeHeaders = true;
            location = Paths.get("build/ngoanh2n/csv");
        }

        /**
         * Set location where you want to store comparison result output files
         *
         * @param path to location
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public CsvComparisonResultOptions.Builder location(@Nonnull Path path) {
            location = checkNotNull(path, "path cannot not be null");
            return this;
        }

        /**
         * Indicate which includes headers row to comparison result output files
         *
         * @param included is a flag whether that includes or not
         * @return {@linkplain CsvComparisonResultOptions.Builder}
         */
        public CsvComparisonResultOptions.Builder includeHeaders(boolean included) {
            includeHeaders = included;
            return this;
        }

        /**
         * Build {@linkplain CsvComparisonResultOptions} based on {@linkplain CsvComparisonResultOptions.Builder}
         *
         * @return {@linkplain CsvComparisonResultOptions}
         */
        public CsvComparisonResultOptions build() {
            return new CsvComparisonResultOptions() {
                @Nonnull
                @Override
                public Path location() {
                    return location;
                }

                @Override
                public boolean includeHeaders() {
                    return includeHeaders;
                }
            };
        }
    }

    /**
     * Where you want to store comparison result output files
     *
     * @return path to location
     */
    @Nonnull
    Path location();

    /**
     * Whether included header row to comparison result output files
     *
     * @return Indicate to add header row to result from output files
     */
    boolean includeHeaders();
}
