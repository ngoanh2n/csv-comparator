package com.github.ngoanh2n.asserts.csv;

/**
 * @author ngoanh2n@gmail.com (Ho Huu Ngoan)
 */

public interface CsvComparisonResult {

    boolean hasDiff();

    boolean hasRowAdded();

    boolean hasRowDeleted();

    boolean hasRowModified();
}
