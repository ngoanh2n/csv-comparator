package com.github.ngoanh2n.asserts.csv;

import com.univocity.parsers.common.Format;
import com.univocity.parsers.csv.CsvParserSettings;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author ngoanh2n@gmail.com (Ho Huu Ngoan)
 */

@SuppressWarnings("unchecked")
class ParsingStrategy<T> {

    private int totalOfColumns;

    Charset encoding;
    int identityColumn;
    String lineSeparator;
    boolean includedHeader;
    String[] selectedColumnNames;
    Integer[] selectedColumnIndexes;

    ParsingStrategy() {
        this.encoding = Charset.defaultCharset();
        this.identityColumn = 0;
        this.lineSeparator = "\n";
        this.includedHeader = true;
        this.selectedColumnNames = new String[0];
        this.selectedColumnIndexes = new Integer[0];
    }

    /**
     * @param identityColumn indicate which is identity column
     *                       determine which field is unique in a row.
     *                       starts with 0 in range of columns by indexes
     *                       <p>
     *                       {@link #onColumns(Integer...)}
     *                       <p>
     *                       e.g. onColumns(1, 2, 5) <=> [username, address, prefs]
     *                       If you want to select field username as identity column
     *                       then set #byIdentityColumn(0)
     * @return this instance of T
     */
    public T byIdentityColumn(int identityColumn) {
        if (identityColumn > -1 && identityColumn < this.totalOfColumns)
            this.identityColumn = identityColumn;
        else throw new RuntimeException(
                String.format("identityColumn should be in range [%s, %s]", 0, totalOfColumns));

        return (T) this;
    }

    /**
     * @param identityColumn indicate which is identity column
     *                       determine which field is unique in a row.
     *                       e.g. id | email | username
     *                       {@link #onColumns(String...)}
     * @return this instance of T
     */
    public T byIdentityColumn(String identityColumn) {
        if (this.selectedColumnNames.length != 0)
            this.identityColumn = Arrays.asList(this.selectedColumnNames).indexOf(identityColumn);
        else throw new RuntimeException("Please use CsvComparator.builder().onColumns(String... columns)");

        return (T) this;
    }

    /**
     * Defines the line separator sequence that should be used for parsing and writing
     * <p>
     * {@link Format#setLineSeparator(String)}
     * {@link CsvParserSettings#getFormat()#setLineSeparator(String)}
     *
     * @param lineSeparator a sequence of 1 to 2 characters that identifies the end of a line
     * @return this instance of T
     */
    public T setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return (T) this;
    }

    /**
     * Defines whether or not the first valid record parsed from the input should be considered
     * as the row containing the names of each column
     * <p/>
     * {@link CsvParserSettings#setHeaderExtractionEnabled(boolean)}
     *
     * @param includedHeader A flag indicating whether the first valid record parsed from the input
     *                       should be asserted or compared
     * @return this instance of T
     */
    public T setIncludeHeader(boolean includedHeader) {
        this.includedHeader = includedHeader;
        return (T) this;
    }

    /**
     * {@link Charset#forName(String)}
     *
     * @param encoding the encoding of the CSV file
     * @return this instance of T
     * https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
     */
    public T setEncoding(String encoding) {
        this.encoding = Charset.forName(encoding);
        return (T) this;
    }

    /**
     * @param encoding the encoding of the CSV file
     * @return this instance of T
     * @see java.nio.charset.StandardCharsets
     * https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
     */
    public T setEncoding(Charset encoding) {
        this.encoding = encoding;
        return (T) this;
    }

    /**
     * {@link CsvParserSettings#selectFields(String...)}
     *
     * @param columns the expected column names which assert or compare
     * @return this instance of T
     */
    public T onColumns(String... columns) {
        this.selectedColumnNames = columns;
        this.totalOfColumns = columns.length;
        return (T) this;
    }

    /**
     * {@link CsvParserSettings#selectIndexes(Integer...)}
     *
     * @param columns the expected column indexes which assert or compare
     * @return this instance of T
     */
    public T onColumns(Integer... columns) {
        this.selectedColumnIndexes = columns;
        this.totalOfColumns = columns.length;
        return (T) this;
    }
}
