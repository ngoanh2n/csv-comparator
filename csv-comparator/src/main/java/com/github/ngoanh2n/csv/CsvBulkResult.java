package com.github.ngoanh2n.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * <em>Repository:</em>
 * <ul>
 *     <li><em>GitHub: <a href="https://github.com/ngoanh2n/csv-comparator">ngoanh2n/csv-comparator</a></em></li>
 *     <li><em>Maven: <a href="https://mvnrepository.com/artifact/com.github.ngoanh2n/csv-comparator">com.github.ngoanh2n:csv-comparator</a></em></li>
 * </ul>
 *
 * @author ngoanh2n
 * @since 2019
 */
class CsvBulkResult implements CsvBulkComparisonResult {
    private final List<CsvComparisonResult> results = new ArrayList<>();

    void put(CsvComparisonResult result) {
        results.add(result);
    }

    @Override
    public boolean hasDiff() {
        return getDiffTotal() > 0;
    }

    @Override
    public int getDiffTotal() {
        return (int) results.stream().filter(CsvComparisonResult::hasDiff).count();
    }

    @Override
    public List<CsvComparisonResult> getDiffResults() {
        return results;
    }
}
