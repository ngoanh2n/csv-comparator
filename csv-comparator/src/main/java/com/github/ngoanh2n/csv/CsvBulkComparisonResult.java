package com.github.ngoanh2n.csv;

import java.util.List;

public interface CsvBulkComparisonResult {
    boolean hasDiff();

    int getDiffTotal();

    List<CsvComparisonResult> getDiffResults();
}
