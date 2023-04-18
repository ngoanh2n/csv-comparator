package com.github.ngoanh2n.csv;

import java.util.List;

/**
 * The result of {@link CsvComparator}.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 */
public interface CsvComparisonResult {
    /**
     * Whether there is any row deleted in
     * the {@link CsvComparisonSource#act()} CSV file against
     * to {@link CsvComparisonSource#exp()} CSV file.
     *
     * @return Indicate {@link CsvComparisonSource#act()} CSV file is deleted any row or not.
     */
    boolean isDeleted();

    /**
     * Whether there is any row inserted in
     * the {@link CsvComparisonSource#act()} CSV file against
     * to {@link CsvComparisonSource#exp()} CSV file.
     *
     * @return Indicate {@link CsvComparisonSource#act()} CSV file is inserted any row or not.
     */
    boolean isInserted();

    /**
     * Whether there is any row modified in
     * the {@link CsvComparisonSource#act()} CSV file against
     * to {@link CsvComparisonSource#exp()} CSV file.
     *
     * @return Indicate {@link CsvComparisonSource#act()} CSV file is modified any row or not.
     */
    boolean isModified();

    /**
     * Whether there is any difference between
     * {@link CsvComparisonSource#act()} file and
     * {@link CsvComparisonSource#exp()} file.
     *
     * @return Indicate {@link CsvComparisonSource#act()} file is different
     * against to {@link CsvComparisonSource#exp()}.
     */
    boolean isDifferent();

    /**
     * Get all kept rows in
     * the {@link CsvComparisonSource#act()} CSV file against
     * to {@link CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were kept.
     */
    List<String[]> rowsKept();

    /**
     * Get all deleted rows in
     * the {@link CsvComparisonSource#act()} CSV file against
     * to {@link CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were deleted.
     */
    List<String[]> rowsDeleted();

    /**
     * Get all inserted rows in
     * the {@link CsvComparisonSource#act()} CSV file against
     * to {@link CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were inserted.
     */
    List<String[]> rowsInserted();

    /**
     * Get all modified rows in
     * the {@link CsvComparisonSource#act()} CSV file against
     * to {@link CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were modified.
     */
    List<String[]> rowsModified();
}
