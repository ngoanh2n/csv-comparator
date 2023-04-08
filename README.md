[![GitHub forks](https://img.shields.io/github/forks/ngoanh2n/csv-comparator.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/network/members/)
[![GitHub stars](https://img.shields.io/github/stars/ngoanh2n/csv-comparator.svg?style=social&label=Star&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/stargazers/)
[![GitHub watchers](https://img.shields.io/github/watchers/ngoanh2n/csv-comparator.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/watchers/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator)
[![javadoc](https://javadoc.io/badge2/com.github.ngoanh2n/csv-comparator/javadoc.svg)](https://javadoc.io/doc/com.github.ngoanh2n/csv-comparator)
[![GitHub release](https://img.shields.io/github/release/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/releases/)
[![badge-jdk](https://img.shields.io/badge/jdk-8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-blueviolet.svg)](https://opensource.org/licenses/MIT)

# CSV Comparator

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [CSV Comparator](#csv-comparator)
- [Declarations](#declarations)
  - [Gradle](#gradle)
  - [Maven](#maven)
- [Usages](#usages)
  - [Comparison](#comparison)
  - [Assertion](#assertion)
  - [Allure Report](#allure-report)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Declarations
## Gradle
_Add dependency to `build.gradle`_
```gradle
implementation("com.github.ngoanh2n:csv-comparator:1.5.1")
```

## Maven
_Add dependency to `pom.xml`_
```xml
<dependency>
    <groupId>com.github.ngoanh2n</groupId>
    <artifactId>csv-comparator</artifactId>
    <version>1.5.1</version>
</dependency>
```

# Usages
Compare 2 CSV files formatted columns:
```
id,email,firstname,lastname,age,note
```

## Comparison
```java
// Create comparison source from actual and expected file
CsvComparisonSource source = CsvComparisonSource.create(expectedCsv, actualCsv);

// Build comparison options to navigate behaviors of comparison process
CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .selectColumns("email", "firstname", "lastname")
                .selectColumnId("email")
                //.selectColumns(1, 2, 3)
                //.selectColumnId(1)
                .build();

// Do comparison
CsvComparisonResult result = CsvComparator.compare(source, options);
```

## Assertion
```java
CsvComparisonResult.isDeleted()
CsvComparisonResult.isInserted()
CsvComparisonResult.isModified()
CsvComparisonResult.isDifferent()
CsvComparisonResult.rowsKept()
CsvComparisonResult.rowsDeleted()
CsvComparisonResult.rowsInserted()
CsvComparisonResult.rowsModified()
```

_By default, result files which are created after comparing is located at `build/ngoanh2n/csv/{yyyyMMdd.HHmmss.SSS}/`_

## Allure Report
Your project is using Allure as a report framework, `csv-comparator-allure` should be used. ([README](csv-comparator-allure))
