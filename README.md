[![GitHub stars](https://img.shields.io/github/stars/ngoanh2n/csv-comparator.svg?style=social&label=Star&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/stargazers/)
[![GitHub watchers](https://img.shields.io/github/watchers/ngoanh2n/csv-comparator.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/watchers/)
[![GitHub forks](https://img.shields.io/github/forks/ngoanh2n/csv-comparator.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/network/members/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator)
[![GitHub release](https://img.shields.io/github/release/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/releases/)
[![javadoc](https://javadoc.io/badge2/com.github.ngoanh2n/csv-comparator/javadoc.svg)](https://javadoc.io/doc/com.github.ngoanh2n/csv-comparator)
[![Build Status](https://travis-ci.org/ngoanh2n/csv-comparator.svg?branch=master)](https://travis-ci.org/ngoanh2n/csv-comparator)
[![License: MIT](https://img.shields.io/badge/License-MIT-blueviolet.svg)](https://opensource.org/licenses/MIT)
[![badge-jdk](https://img.shields.io/badge/jdk-8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![GitHub contributors](https://img.shields.io/github/contributors/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/graphs/contributors/)
[![GitHub issues](https://img.shields.io/github/issues/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/issues/)
[![GitHub issues-closed](https://img.shields.io/github/issues-closed/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/issues?q=is%3Aissue+is%3Aclosed)
[![GitHub pull-requests](https://img.shields.io/github/issues-pr/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/pulls/)
[![GitHub pull-requests closed](https://img.shields.io/github/issues-pr-closed/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/pulls?q=is%3Apulls+is%3Aclosed)

# CSV Comparator

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [How To Use](#how-to-use)
  - [Gradle Project](#gradle-project)
  - [Maven Project](#maven-project)
- [How To Apply](#how-to-apply)
  - [Provide *CsvComparisonSource*](#provide-csvcomparisonsource)
  - [Build *CsvComparisonOptions*](#build-csvcomparisonoptions)
  - [Do *CsvComparator*](#do-csvcomparator)
  - [Asssert *CsvComparisonResult*](#asssert-csvcomparisonresult)
  - [Use *CsvComparisonVisitor* To Walk Through *CsvComparator*](#use-csvcomparisonvisitor-to-walk-through-csvcomparator)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## How To Use
### Gradle Project
Add the `csv-comparator` dependency to your build.gradle
```gradle
dependencies {
    testImplementation("com.github.ngoanh2n:csv-comparator:1.2.0")
}
```

### Maven Project
Add the `csv-comparator` dependency to your pom
```xml
<dependencies>
    [...]
    <dependency>
        <groupId>com.github.ngoanh2n</groupId>
        <artifactId>csv-comparator</artifactId>
        <version>1.2.0</version>
        <scope>test</scope>
    </dependency>
    [...]
</dependencies>
```

## How To Apply
Compare 2 CSV files formatted columns:
```
id,email,firstname,lastname,age,note
```

### Provide *CsvComparisonSource*
```java
CsvComparisonSource<File> source = CsvComparisonSource.create(expectedCsv, actualCsv);
```

### Build *CsvComparisonOptions*
```java
CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns(1, 2, 3)
                .setIdentityColumn(0) // position starts with 0 in array [1, 2, 3]
                .build();
```

If you want to use column names:
```java
CsvComparisonOptions options = CsvComparisonOptions
                .builder()
                .setColumns("email", "firstname", "lastname")
                .setIdentityColumn("email")
                .build();
```

### Do *CsvComparator*
```java
CsvComparisonResult result = new CsvComparator(source, options).compare();
```

### Asssert *CsvComparisonResult*
```java
CsvComparisonResult.hasDiff()
CsvComparisonResult.hasDeleted()
CsvComparisonResult.hasInserted()
CsvComparisonResult.hasModified()
CsvComparisonResult.rowsKept()
CsvComparisonResult.rowsDeleted()
CsvComparisonResult.rowsInserted()
CsvComparisonResult.rowsModified()
```

_By default, result files which is created after comparing is located at `build/comparator/csv/{yyyyMMdd.HHmmss.SSS}/`_

### Use *CsvComparisonVisitor* To Walk Through *CsvComparator*
```java
CsvComparisonVisitor.visitStarted(CsvComparisonSource<?> source)
CsvComparisonVisitor.visitEnded(CsvComparisonSource<?> source)
CsvComparisonVisitor.rowKept(String[] row, String[] headers, CsvComparisonOptions options)
CsvComparisonVisitor.rowDeleted(String[] row, String[] headers, CsvComparisonOptions options)
CsvComparisonVisitor.rowInserted(String[] row, String[] headers, CsvComparisonOptions options)
CsvComparisonVisitor.rowModified(String[] row, String[] headers, CsvComparisonOptions options)
```

```java
CsvComparisonResult result = new CsvComparator(source, options, visitor).compare();
```
