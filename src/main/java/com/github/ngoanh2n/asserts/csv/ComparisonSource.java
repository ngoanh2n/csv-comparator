package com.github.ngoanh2n.asserts.csv;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

/**
 * Provide comparison source for {@link CsvComparator}
 * <br>
 * <h3>csv-comparator<h3>
 * <a href="https://github.com/ngoanh2n/csv-comparator">https://github.com/ngoanh2n/csv-comparator<a>
 * <br>
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @since 1.0.0
 */
public interface ComparisonSource<T> {

    @Nonnull
    T exp();

    @Nonnull
    T act();

    /**
     * Create a comparison source and make sure it is not null
     *
     * @param exp the expected source
     * @param act the actual source needs to compare
     * @param <T> type of source
     * @return comparison source
     */
    static <T> ComparisonSource<T> create(@Nonnull T exp, @Nonnull T act) {
        return new ComparisonSource<T>() {
            @Nonnull
            @Override
            public T exp() {
                return Preconditions.checkNotNull(exp, "Expected source cannot be null");
            }

            @Nonnull
            @Override
            public T act() {
                return Preconditions.checkNotNull(act, "Actual source cannot be null");
            }
        };
    }
}