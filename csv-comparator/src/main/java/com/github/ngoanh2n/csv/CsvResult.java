package com.github.ngoanh2n.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
class CsvResult implements CsvComparisonResult {
    private final Collector collector;

    //-------------------------------------------------------------------------------//

    CsvResult(Collector collector) {
        this.collector = collector;
    }

    //-------------------------------------------------------------------------------//

    @Override
    public boolean isDeleted() {
        return collector.isDeleted;
    }

    @Override
    public boolean isInserted() {
        return collector.isInserted;
    }

    @Override
    public boolean isModified() {
        return collector.isModified;
    }

    @Override
    public boolean isDifferent() {
        return isDeleted() || isInserted() || isModified();
    }

    @Override
    public List<String[]> rowsKept() {
        return collector.rowsKept;
    }

    @Override
    public List<String[]> rowsDeleted() {
        return collector.rowsDeleted;
    }

    @Override
    public List<String[]> rowsInserted() {
        return collector.rowsInserted;
    }

    @Override
    public List<String[]> rowsModified() {
        return collector.rowsModified;
    }

    //===============================================================================//

    static class Collector implements CsvComparisonVisitor {
        boolean isDeleted = false;
        boolean isInserted = false;
        boolean isModified = false;
        final List<String[]> rowsKept = new ArrayList<>();
        final List<String[]> rowsDeleted = new ArrayList<>();
        final List<String[]> rowsInserted = new ArrayList<>();
        final List<String[]> rowsModified = new ArrayList<>();

        //-------------------------------------------------------------------------------//

        @Override
        public void rowKept(CsvComparisonOptions options, String[] headers, String[] row) {
            rowsKept.add(row);
        }

        @Override
        public void rowDeleted(CsvComparisonOptions options, String[] headers, String[] row) {
            isDeleted = true;
            rowsDeleted.add(row);
        }

        @Override
        public void rowInserted(CsvComparisonOptions options, String[] headers, String[] row) {
            isInserted = true;
            rowsInserted.add(row);
        }

        @Override
        public void rowModified(CsvComparisonOptions options, String[] headers, String[] row, List<HashMap<String, String>> diffs) {
            isModified = true;
            rowsModified.add(row);
        }
    }
}
