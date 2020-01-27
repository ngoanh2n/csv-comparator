package com.github.ngoanh2n.asserts.csv;

/**
 * @author ngoanh2n@gmail.com (Ho Huu Ngoan)
 */

class CsvComparisonResultImpl implements CsvComparisonResult {

    private String diffPath;
    private boolean hasRowAdded;
    private boolean hasRowDeleted;
    private boolean hasRowModified;

    @Override
    public boolean hasDiff() {
        return this.hasRowAdded
                || this.hasRowDeleted
                || this.hasRowModified;
    }

    @Override
    public boolean hasRowAdded() {
        return this.hasRowAdded;
    }

    @Override
    public boolean hasRowDeleted() {
        return this.hasRowDeleted;
    }

    @Override
    public boolean hasRowModified() {
        return this.hasRowModified;
    }

    void setDiffPath(String path) {
        this.diffPath = path;
    }

    void setHasRowAdded(boolean hasRowAdded) {
        this.hasRowAdded = hasRowAdded;
    }

    void setHasRowDeleted(boolean hasRowDeleted) {
        this.hasRowDeleted = hasRowDeleted;
    }

    void setHasRowModified(boolean hasRowModified) {
        this.hasRowModified = hasRowModified;
    }
}
