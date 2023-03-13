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
     * Gets {@linkplain CsvComparisonResultOptions.Builder} class where allows to build your {@linkplain CsvComparisonResultOptions}
     *
     * @return {@linkplain CsvComparisonResultOptions.Builder}
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Gets {@linkplain CsvComparisonResultOptions} with default options
     *
     * @return {@linkplain CsvComparisonResultOptions}
     */
    static CsvComparisonResultOptions defaults() {
        return builder().build();
    }

    //-------------------------------------------------------------------------------//

    /**
     * Where you want to store comparison result output files
     *
     * @return path to location
     */
    @Nonnull
    Path location();

    /**
     * Whether wrote output files
     *
     * @return Indicate to write output files
     */
    boolean writeOutputs();

    /**
     * Whether included headers row in output files
     *
     * @return Indicate to add headers row to results in output files
     */
    boolean includeHeaders();

    //===============================================================================//

    /**
     * This class allows to build {@linkplain CsvComparisonResultOptions}.
     */
    final class Builder {
        private Path location;
        private boolean writeOutputs;
        private boolean includeHeaders;

        private Builder() {
            writeOutputs = true;
            includeHeaders = true;
            location = Paths.get("build/ngoanh2n/csv");
        }

        /**
         * Set location where you want to store comparison result output files
         *
         * @param path to location
         * @return {@linkplain CsvComparisonOptions.Builder}
         */
        public CsvComparisonResultOptions.Builder setLocation(@Nonnull Path path) {
            location = checkNotNull(path, "path cannot not be null");
            return this;
        }

        /**
         * Indicate which writes output files
         *
         * @param enabled is a flag whether that includes or not
         * @return {@linkplain CsvComparisonResultOptions.Builder}
         */
        public CsvComparisonResultOptions.Builder writeOutputs(boolean enabled) {
            writeOutputs = enabled;
            return this;
        }

        /**
         * Indicate which includes headers row to comparison result output files
         *
         * @param enabled is a flag whether that includes or not
         * @return {@linkplain CsvComparisonResultOptions.Builder}
         */
        public CsvComparisonResultOptions.Builder includeHeaders(boolean enabled) {
            includeHeaders = enabled;
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

                @Override
                public boolean writeOutputs() {
                    return writeOutputs;
                }
            };
        }
    }
}
