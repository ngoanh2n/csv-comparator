package com.github.ngoanh2n.comparator;

import java.util.List;

/**
 * The result of {@linkplain CsvComparator}.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
public interface CsvComparisonResult {
    /**
     * Whether there is any row deleted in
     * the {@linkplain CsvComparisonSource#act()} CSV file against
     * to {@linkplain CsvComparisonSource#exp()} CSV file.
     *
     * @return Indicate {@linkplain CsvComparisonSource#act()} CSV file is deleted any row or not.
     */
    boolean isDeleted();

    /**
     * Whether there is any row inserted in
     * the {@linkplain CsvComparisonSource#act()} CSV file against
     * to {@linkplain CsvComparisonSource#exp()} CSV file.
     *
     * @return Indicate {@linkplain CsvComparisonSource#act()} CSV file is inserted any row or not.
     */
    boolean isInserted();

    /**
     * Whether there is any row modified in
     * the {@linkplain CsvComparisonSource#act()} CSV file against
     * to {@linkplain CsvComparisonSource#exp()} CSV file.
     *
     * @return Indicate {@linkplain CsvComparisonSource#act()} CSV file is modified any row or not.
     */
    boolean isModified();

    /**
     * Whether there is any difference between
     * {@linkplain CsvComparisonSource#act()} file and
     * {@linkplain CsvComparisonSource#exp()} file.
     *
     * @return Indicate {@linkplain CsvComparisonSource#act()} file is different
     * against to {@linkplain CsvComparisonSource#exp()}.
     */
    boolean isDifferent();

    /**
     * Get all kept rows in
     * the {@linkplain CsvComparisonSource#act()} CSV file against
     * to {@linkplain CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were kept.
     */
    List<String[]> rowsKept();

    /**
     * Get all deleted rows in
     * the {@linkplain CsvComparisonSource#act()} CSV file against
     * to {@linkplain CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were deleted.
     */
    List<String[]> rowsDeleted();

    /**
     * Get all inserted rows in
     * the {@linkplain CsvComparisonSource#act()} CSV file against
     * to {@linkplain CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were inserted.
     */
    List<String[]> rowsInserted();

    /**
     * Get all modified rows in
     * the {@linkplain CsvComparisonSource#act()} CSV file against
     * to {@linkplain CsvComparisonSource#exp()} CSV file.
     *
     * @return Rows were modified.
     */
    List<String[]> rowsModified();
}
