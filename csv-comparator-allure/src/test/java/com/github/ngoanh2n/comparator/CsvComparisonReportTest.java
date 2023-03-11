package com.github.ngoanh2n.comparator;

import com.github.ngoanh2n.Resource;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

/**
 * @author Ho Huu Ngoan (ngoanh2n@gmail.com)
 * @version 1.5.0
 * @since 2022-12-29
 */
@Epic("CSV Comparator: Has Differences")
@Feature("CSV Comparator: Demo Test")
public class CsvComparisonReportTest {
    @Test
    void test() {
        CsvComparisonSource source = CsvComparisonSource.create(
                Resource.getFile("com/github/ngoanh2n/comparator/exp/combine2.csv"),
                Resource.getFile("com/github/ngoanh2n/comparator/act/combine2.csv")
        );
        CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumnId(0)
                .build();
        CsvComparator.compare(source, options);
    }
}
