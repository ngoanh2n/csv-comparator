package com.github.ngoanh2n.asserts.csv;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.2.0
 */
public interface CsvComparisonResultOptions {

    @Nonnull
    Path location();

    boolean includeHeaders();

    final class Builder {

        private Path location;
        private boolean includesHeader;

        private Builder() {
            this.includesHeader = true;
            this.location = Paths.get("build/comparator/csv");
        }

        /**
         * @param path where you store the results
         * @return {@link CsvComparisonOptions.Builder}
         */
        public CsvComparisonResultOptions.Builder location(@Nonnull Path path) {
            this.location = checkNotNull(path, "path cannot not be null");
            return this;
        }

        /**
         * @param included A flag indicating whether or not the header is included in result file
         * @return {@link CsvComparisonResultOptions.Builder}
         */
        public CsvComparisonResultOptions.Builder includeHeaders(boolean included) {
            this.includesHeader = included;
            return this;
        }

        /**
         * Build CsvComparisonResultOptions based on CsvComparisonResultOptions.Builder
         *
         * @return {@link CsvComparisonResultOptions}
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
                    return includesHeader;
                }
            };
        }
    }

    static CsvComparisonResultOptions.Builder builder() {
        return new CsvComparisonResultOptions.Builder();
    }

    static CsvComparisonResultOptions defaults() {
        return builder().build();
    }
}
