package com.github.ngoanh2n.comparator;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;

import java.util.*;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.0.0
 * @since 2020-01-06
 */
class CsvProcessor implements RowProcessor {
    CsvComparisonOptions options;
    List<CsvComparisonVisitor> visitors;
    CsvResult.Collector collector;
    Map<String, String[]> expMap;
    CsvSource source;

    //-------------------------------------------------------------------------------//

    CsvProcessor(CsvComparisonOptions options, List<CsvComparisonVisitor> visitors,
                 CsvResult.Collector collector, Map<String, String[]> expMap, CsvSource source) {
        this.options = options;
        this.visitors = visitors;
        this.collector = collector;
        this.expMap = expMap;
        this.source = source;
    }

    //-------------------------------------------------------------------------------//

    @Override
    public void rowProcessed(String[] actRow, ParsingContext context) {
        String[] expRow = expMap.get(actRow[source.getColumnId()]);

        if (expRow == null) {
            collector.rowInserted(options, source.getHeaders(), actRow);
            visitors.forEach(v -> v.rowInserted(options, source.getHeaders(), actRow));
        } else {
            if (Arrays.equals(actRow, expRow)) {
                collector.rowKept(options, source.getHeaders(), actRow);
                visitors.forEach(v -> v.rowKept(options, source.getHeaders(), actRow));
            } else {
                List<HashMap<String, String>> diffs = getDiffs(source.getHeaders(), expRow, actRow);
                collector.rowModified(options, source.getHeaders(), actRow, diffs);
                visitors.forEach(v -> v.rowModified(options, source.getHeaders(), actRow, diffs));
            }
            expMap.remove(actRow[source.getColumnId()]);
        }
    }

    @Override
    public void processStarted(ParsingContext context) { /* No implementation necessary */ }

    @Override
    public void processEnded(ParsingContext context) { /* No implementation necessary */ }

    //-------------------------------------------------------------------------------//

    private List<HashMap<String, String>> getDiffs(String[] headers, String[] expRow, String[] actRow) {
        List<HashMap<String, String>> diffs = new ArrayList<>();

        for (int index = 0; index < headers.length; index++) {
            String header = headers[index];
            String expCell = expRow[index] == null ? "NULL" : expRow[index];
            String actCell = actRow[index] == null ? "NULL" : actRow[index];

            if (!expCell.equals(actCell)) {
                diffs.add(new HashMap<String, String>() {{
                    put("column", header);
                    put("expCell", expCell);
                    put("actCell", actCell);
                }});
            }
        }
        return diffs;
    }
}
