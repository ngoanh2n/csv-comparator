package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Commons;
import com.github.ngoanh2n.RuntimeError;
import com.univocity.parsers.common.fields.FieldIndexSelector;
import com.univocity.parsers.common.fields.FieldNameSelector;
import com.univocity.parsers.common.fields.FieldSelector;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * This class parse CSV file to rows and headers, columnId.
 *
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
class CsvSource {
    private final int columnId;
    private final String[] headers;
    private final List<String[]> rows;

    //-------------------------------------------------------------------------------//

    private CsvSource(CsvComparisonOptions options, File file, boolean includeHeaders) {
        String[] tmpHeaders = new String[]{};
        CsvParserSettings settings = options.parserSettings();
        CsvParser parser = new CsvParser(settings);

        if (!settings.isHeaderExtractionEnabled() || includeHeaders) {
            settings.setHeaderExtractionEnabled(false);
            rows = parser.parseAll(file, getCharset(options, file));
            settings.setHeaderExtractionEnabled(true);
        } else {
            settings.setHeaderExtractionEnabled(false);
            rows = parser.parseAll(file, getCharset(options, file));
            if (rows.size() > 0) {
                tmpHeaders = rows.remove(0);
            }
            settings.setHeaderExtractionEnabled(true);
        }
        headers = tmpHeaders;
        columnId = getColumnId(options);
    }

    //-------------------------------------------------------------------------------//

    static CsvSource parse(CsvComparisonOptions options, File file) {
        return parse(options, file, false);
    }

    static CsvSource parse(CsvComparisonOptions options, File file, boolean includeHeaders) {
        return new CsvSource(options, file, includeHeaders);
    }

    static Charset getCharset(CsvComparisonOptions options, File file) {
        try {
            return options.charset() != null
                    ? options.charset()
                    : Charset.forName(Commons.detectCharset(file));
        } catch (IOException ignored) {
            // Can't happen
            return StandardCharsets.UTF_8;
        }
    }

    //-------------------------------------------------------------------------------//

    int getColumnId() {
        return columnId;
    }

    String[] getHeaders() {
        return headers;
    }

    List<String[]> getRows() {
        return rows;
    }

    //-------------------------------------------------------------------------------//

    private int getColumnId(CsvComparisonOptions options) {
        CsvParserSettings settings = options.parserSettings();
        FieldSelector selector = getColumnSelector(settings);

        if (selector instanceof FieldNameSelector) {
            List<String> names = getColumnNames(settings);
            return getColumnIdByName(options, names);
        }
        if (selector instanceof FieldIndexSelector) {
            List<Integer> indexes = getColumnIndexes(settings);
            return getColumnIdByIndex(options, indexes);
        }
        if (selector == null) {
            if (options.columnId() instanceof String) {
                List<String> names = Arrays.asList(headers);
                return getColumnIdByName(options, names);
            } else {
                List<Integer> indexes = new ArrayList<>();

                if (rows.size() > 0) {
                    int columns = rows.get(0).length;
                    indexes = IntStream.range(0, columns).boxed().collect(toList());
                }
                return getColumnIdByIndex(options, indexes);
            }
        }
        throw new RuntimeError("Unknown column selector");
    }

    private List<String> getColumnNames(CsvParserSettings settings) {
        return ((FieldNameSelector) getColumnSelector(settings)).get();
    }

    private List<Integer> getColumnIndexes(CsvParserSettings settings) {
        return ((FieldIndexSelector) getColumnSelector(settings)).get();
    }

    private FieldSelector getColumnSelector(CsvParserSettings settings) {
        return Commons.readField(settings, "fieldSelector");
    }

    private int getColumnIdByName(CsvComparisonOptions options, List<String> names) {
        CsvParserSettings settings = options.parserSettings();
        if (settings.isHeaderExtractionEnabled()) {
            String columnName = String.valueOf(options.columnId());
            int columnId = names.indexOf(columnName);

            if (columnId != -1) {
                return columnId;
            } else {
                String msg = "ColumnId should be in selected columns %s";
                throw new RuntimeError(String.format(msg, names));
            }
        } else {
            String msg = "CSV without headers, select column indexes instead";
            throw new RuntimeError(msg);
        }
    }

    private int getColumnIdByIndex(CsvComparisonOptions options, List<Integer> indexes) {
        if (options.columnId() instanceof Integer) {
            int columnIndex = Integer.parseInt(options.columnId().toString());
            int columnId = indexes.indexOf(columnIndex);

            if (columnId != -1) {
                return columnId;
            } else {
                String msg = "ColumnId should be in selected columns %s";
                throw new RuntimeError(String.format(msg, indexes));
            }
        } else {
            String msg = "ColumnId=%s is not in selected columns %s";
            throw new RuntimeError(String.format(msg, options.columnId(), indexes));
        }
    }
}
