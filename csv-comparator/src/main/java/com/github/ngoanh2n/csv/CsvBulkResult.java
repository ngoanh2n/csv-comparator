package com.github.ngoanh2n.csv;

import java.util.ArrayList;
import java.util.List;

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
