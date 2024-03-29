package com.github.ngoanh2n.csv;

import com.github.ngoanh2n.Resources;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author ngoanh2n
 */
@Epic("CSV Comparison: Has Diff")
public class CsvComparisonReportTest {
    @Test
    @Feature("CSV Comparison: Has Diff")
    void hasDiff() {
        File exp = Resources.getFile("com/github/ngoanh2n/csv/exp/combination2.csv");
        File act = Resources.getFile("com/github/ngoanh2n/csv/act/combination2.csv");

        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(0)
                .build();
        CsvComparator.compare(exp, act, options);
    }
}
