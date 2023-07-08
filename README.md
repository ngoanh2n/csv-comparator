[![GitHub forks](https://img.shields.io/github/forks/ngoanh2n/csv-comparator.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/network/members/)
[![GitHub stars](https://img.shields.io/github/stars/ngoanh2n/csv-comparator.svg?style=social&label=Star&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/stargazers/)
[![GitHub watchers](https://img.shields.io/github/watchers/ngoanh2n/csv-comparator.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/watchers/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator)
[![javadoc](https://javadoc.io/badge2/com.github.ngoanh2n/csv-comparator/javadoc.svg)](https://javadoc.io/doc/com.github.ngoanh2n/csv-comparator)
[![GitHub release](https://img.shields.io/github/release/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/releases/)
[![badge-jdk](https://img.shields.io/badge/jdk-8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-blueviolet.svg)](https://opensource.org/licenses/MIT)

# CSV Comparator
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
implementation("com.github.ngoanh2n:csv-comparator:1.7.0")
```

## Maven
Add dependency to `pom.xml`.
```xml
<dependency>
  <groupId>com.github.ngoanh2n</groupId>
  <artifactId>csv-comparator</artifactId>
  <version>1.7.0</version>
</dependency>
```

# Usage
## Comparison
Example: CSV is formatted columns `[id,email,firstname,lastname,age,note]`.

1. Compare 2 CSV file
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
2. Compare 2 CSV directory
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
         //.setLocation(Paths.get("build/custom"))  // Default to build/ngoanh2n/csv
         .build();
  CsvComparisonOptions options = CsvComparisonOptions
          .builder()
          .setResultOptions(resultOptions)          // Default to CsvComparisonResultOptions.defaults()
          .build();
  ```

## Allure Report
When using Allure as a report framework, should use
<a href="https://mvnrepository.com/artifact/com.github.ngoanh2n/csv-comparator-allure">com.github.ngoanh2n:csv-comparator-allure</a>.<br>
`csv-comparator-allure` [README](csv-comparator-allure#readme).
