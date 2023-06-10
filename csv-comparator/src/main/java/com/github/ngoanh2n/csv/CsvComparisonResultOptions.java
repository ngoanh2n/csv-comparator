package com.github.ngoanh2n.csv;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Adjust behaviors of {@link CsvComparisonResult}.
 *
 * @author ngoanh2n
 */
public interface CsvComparisonResultOptions {
    /**
     * Get {@link CsvComparisonResultOptions.Builder} class where allows to build your {@link CsvComparisonResultOptions}.
     *
     * @return A {@link CsvComparisonResultOptions.Builder}.
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Get {@link CsvComparisonResultOptions} with default options.
     *
     * @return A {@link CsvComparisonResultOptions}.
     */
    static CsvComparisonResultOptions defaults() {
        return builder().build();
    }

    //-------------------------------------------------------------------------------//

    /**
     * Where you want to store comparison result output files.
     *
     * @return The path to location.
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
     * Build a {@link CsvComparisonResultOptions}.
     */
    final class Builder {
        private Path location;
        private boolean writeOutputs;
        private boolean includeHeaders;

        private Builder() {
            this.location = Paths.get("build/ngoanh2n/csv");
            this.writeOutputs = true;
            this.includeHeaders = true;
        }

        /**
         * Set location where you want to store comparison result output files.
         *
         * @param path The path to location.
         * @return The current {@link CsvComparisonResultOptions.Builder}.
         */
        public Builder setLocation(@Nonnull Path path) {
            this.location = checkNotNull(path, "path cannot not be null");
            return this;
        }

        /**
         * Indicate which writes output files.
         *
         * @param enabled The flag whether that includes or not.
         * @return The current {@link CsvComparisonResultOptions.Builder}.
         */
        public Builder writeOutputs(boolean enabled) {
            this.writeOutputs = enabled;
            return this;
        }

        /**
         * Indicate which includes headers row to comparison result output files.
         *
         * @param enabled The flag whether that includes or not.
         * @return The current {@link CsvComparisonResultOptions.Builder}.
         */
        public Builder includeHeaders(boolean enabled) {
            this.includeHeaders = enabled;
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
