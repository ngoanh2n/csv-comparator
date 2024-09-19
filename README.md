[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net)
[![Maven](https://img.shields.io/maven-central/v/com.github.ngoanh2n/csv-comparator?label=Maven)](https://mvnrepository.com/artifact/com.github.ngoanh2n/csv-comparator)
[![GitHub Actions](https://img.shields.io/github/actions/workflow/status/ngoanh2n/csv-comparator/test.yml?logo=github&label=GitHub%20Actions)](https://github.com/ngoanh2n/csv-comparator/actions/workflows/test.yml)
[![CircleCI](https://img.shields.io/circleci/build/github/ngoanh2n/csv-comparator?token=CCIPRJ_V9AVYTzVyEF9A9GMsVD9oF_2ce0fb3410ce42dfee9d8d854bae69d56f206df6&logo=circleci&label=CircleCI)
](https://dl.circleci.com/status-badge/redirect/gh/ngoanh2n/csv-comparator/tree/master)

**Table of Contents**
<!-- TOC -->
* [Declaration](#declaration)
  * [Gradle](#gradle)
  * [Maven](#maven)
* [Usage](#usage)
  * [Comparison](#comparison)
  * [Result](#result)
  * [Visitor](#visitor)
  * [Output](#output)
  * [Allure Report](#allure-report)
<!-- TOC -->

# Declaration
## Gradle
Add dependency to `build.gradle`.
```gradle
implementation("com.github.ngoanh2n:csv-comparator:1.10.0")
```

## Maven
Add dependency to `pom.xml`.
```xml
<dependency>
  <groupId>com.github.ngoanh2n</groupId>
  <artifactId>csv-comparator</artifactId>
  <version>1.10.0</version>
</dependency>
```

# Usage
## Comparison
Example: CSV is formatted columns `[id,email,firstname,lastname,age,note]`.

1. Compare 2 CSV files
    ```java
    File expectedCsvFile = new File("data/expected/file.csv");
    File actualCsvFile = new File("data/actual/file.csv");
    
    CsvComparisonOptions options = CsvComparisonOptions
            .builder()
            .selectColumns("email", "firstname", "lastname")
            .selectColumnId("email")
            //.selectColumns(1, 2, 3)
            //.selectColumnId(1)
            .build();
    CsvComparisonResult result = CsvComparator.compare(expectedCsvFile, actualCsvFile, options);
    ```
2. Compare 2 CSV directories
    ```java
    Path expectedCsvDir = Paths.get("data/expected");
    Path actualCsvDir = Paths.get("data/actual");
    
    CsvComparisonOptions options = CsvComparisonOptions
            .builder()
            .selectColumns("email", "firstname", "lastname")
            .selectColumnId("email")
            //.selectColumns(1, 2, 3)
            //.selectColumnId(1)
            .build();
    CsvBulkComparisonResult result = CsvComparator.compare(expectedCsvDir, actualCsvDir, options);
    ```

## Result
`CsvComparisonResult` is the result of `CsvComparator.compare(expectedCsvFile, actualCsvFile, options)`.
```java
boolean hasDiff = CsvComparisonResult.hasDiff();
boolean hasDeletion = CsvComparisonResult.hasDeletion();
boolean hasInsertion = CsvComparisonResult.hasInsertion();
boolean hasModification = CsvComparisonResult.hasModification();
List<String[]> keptRows = CsvComparisonResult.getKeptRows();
List<String[]> deletedRows = CsvComparisonResult.getDeletedRows();
List<String[]> insertedRows = CsvComparisonResult.getInsertedRows();
List<String[]> modifiedRows = CsvComparisonResult.getModifiedRows();
```

`CsvBulkComparisonResult` is the result of `CsvComparator.compare(expectedCsvDir, actualCsvDir, options)`.
```java
boolean hasDiff = CsvBulkComparisonResult.hasDiff();
int diffTotal = CsvBulkComparisonResult.getDiffTotal();
List<CsvComparisonResult> diffResults = CsvBulkComparisonResult.getDiffResults();
```

## Visitor
`CsvComparisonVisitor` for walking through `CsvComparator`.
- `CsvComparisonVisitor#comparisonStarted(CsvComparisonOptions, File, File)`
- `CsvComparisonVisitor#rowKept(CsvComparisonOptions, String[], String[])`
- `CsvComparisonVisitor#rowDeleted(CsvComparisonOptions, String[], String[])`
- `CsvComparisonVisitor#rowInserted(CsvComparisonOptions, String[], String[])`
- `CsvComparisonVisitor#rowModified(CsvComparisonOptions, String[], String[], List)`
- `CsvComparisonVisitor#comparisonFinished(CsvComparisonOptions, File, File, CsvComparisonResult)`

## Output
`CsvComparisonOutput` for writing comparison output files to specified location.<br>
An implementation of `CsvComparisonVisitor`.
- The output is always created at `build/ngoanh2n/csv/{yyyyMMdd.HHmmss.SSS}` by default
- Use `CsvComparisonResultOptions` to adjust the output behaviors. And set to `CsvComparisonOptions`
  ```java
  CsvComparisonResultOptions resultOptions = CsvComparisonResultOptions
         .builder()
         .writeOutputs(false)                       // Default to true
         //.location(Paths.get("build/custom"))     // Default to build/ngoanh2n/csv
         .build();
  CsvComparisonOptions options = CsvComparisonOptions
          .builder()
          .resultOptions(resultOptions)             // Default to CsvComparisonResultOptions.defaults()
          .build();
  ```

## Allure Report
When using Allure as a report framework, should use
<a href="https://mvnrepository.com/artifact/com.github.ngoanh2n/csv-comparator-allure">com.github.ngoanh2n:csv-comparator-allure</a>.<br>
`csv-comparator-allure` [README](csv-comparator-allure#readme).
