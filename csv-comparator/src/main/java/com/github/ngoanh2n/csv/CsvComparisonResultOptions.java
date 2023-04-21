package com.github.ngoanh2n.csv;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class allows to adjust {@link CsvComparisonResult} output by your expectation.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 */
public interface CsvComparisonResultOptions {
    /**
     * Gets {@link CsvComparisonResultOptions.Builder} class
     * where allows to build your {@link CsvComparisonResultOptions}.
     *
     * @return {@link CsvComparisonResultOptions.Builder}.
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Gets {@link CsvComparisonResultOptions} with default options.
     *
     * @return {@link CsvComparisonResultOptions}.
     */
    static CsvComparisonResultOptions defaults() {
        return builder().build();
    }

    //-------------------------------------------------------------------------------//

    /**
     * Where you want to store comparison result output files.
     *
     * @return path to location.
     */
    @Nonnull
    Path location();

    /**
     * Whether wrote output files.
     *
     * @return Indicate to write output files.
     */
    boolean writeOutputs();

    /**
     * Whether included headers row in output files.
     *
     * @return Indicate to add headers row to results in output files.
     */
    boolean includeHeaders();

    //===============================================================================//

    /**
     * This class allows to build {@link CsvComparisonResultOptions}.
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
         * Set location where you want to store comparison result output files.
         *
         * @param path to location.
         * @return {@link CsvComparisonOptions.Builder}.
         */
        public Builder setLocation(@Nonnull Path path) {
            location = checkNotNull(path, "path cannot not be null");
            return this;
        }

        /**
         * Indicate which writes output files.
         *
         * @param enabled is a flag whether that includes or not.
         * @return {@link Builder}.
         */
        public Builder writeOutputs(boolean enabled) {
            writeOutputs = enabled;
            return this;
        }

        /**
         * Indicate which includes headers row to comparison result output files.
         *
         * @param enabled is a flag whether that includes or not.
         * @return {@link Builder}.
         */
        public Builder includeHeaders(boolean enabled) {
            includeHeaders = enabled;
            return this;
        }

        /**
         * Build {@link CsvComparisonResultOptions} based on {@link Builder}.
         *
         * @return {@link CsvComparisonResultOptions}.
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
