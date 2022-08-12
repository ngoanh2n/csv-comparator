package com.github.ngoanh2n.comparator;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * This interface should be used to provide comparison source for {@linkplain CsvComparator}.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
public interface CsvComparisonSource {
    /**
     * Provide expected CSV file
     *
     * @return Expected CSV file
     */
    @Nonnull
    File exp();

    /**
     * Provide actual CSV file needs to compare
     *
     * @return Actual CSV file
     */
    @Nonnull
    File act();

    /**
     * Create a comparison source and make sure it is not null
     *
     * @param exp the expected CSV
     * @param act the actual CSV needs to compare
     * @return {@linkplain CsvComparisonSource}
     */
    static CsvComparisonSource create(@Nonnull File exp, @Nonnull File act) {
        return new CsvComparisonSource() {
            @Nonnull
            @Override
            public File exp() {
                return Preconditions.checkNotNull(exp, "Expected source cannot be null");
            }

            @Nonnull
            @Override
            public File act() {
                return Preconditions.checkNotNull(act, "Actual source cannot be null");
            }
        };
    }
}
