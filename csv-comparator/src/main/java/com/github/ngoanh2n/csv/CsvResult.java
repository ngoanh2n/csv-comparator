package com.github.ngoanh2n.csv;

import java.util.ArrayList;
import java.util.HashMap;
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
class CsvResult implements CsvComparisonResult {
    private final Collector collector;

    CsvResult(Collector collector) {
        this.collector = collector;
    }

    //-------------------------------------------------------------------------------//

    @Override
    public boolean hasDeletion() {
        return collector.isDeleted;
    }

    @Override
    public boolean hasInsertion() {
        return collector.isInserted;
    }

    @Override
    public boolean hasModification() {
        return collector.isModified;
    }

    @Override
    public boolean hasDiff() {
        return hasDeletion() || hasInsertion() || hasModification();
    }

    @Override
    public List<String[]> getKeptRows() {
        return collector.rowsKept;
    }

    @Override
    public List<String[]> getDeletedRows() {
        return collector.rowsDeleted;
    }

    @Override
    public List<String[]> getInsertedRows() {
        return collector.rowsInserted;
    }

    @Override
    public List<String[]> getModifiedRows() {
        return collector.rowsModified;
    }

    @Override
    public String toString() {
        return new HashMap<String, Integer>() {{
            put("deletion", getDeletedRows().size());
            put("insertion", getInsertedRows().size());
            put("modification", getModifiedRows().size());
        }}.toString();
    }

    //===============================================================================//

    static class Collector implements CsvComparisonVisitor {
        final List<String[]> rowsKept = new ArrayList<>();
        final List<String[]> rowsDeleted = new ArrayList<>();
        final List<String[]> rowsInserted = new ArrayList<>();
        final List<String[]> rowsModified = new ArrayList<>();
        boolean isDeleted = false;
        boolean isInserted = false;
        boolean isModified = false;

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
