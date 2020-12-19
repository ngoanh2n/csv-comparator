[![GitHub forks](https://img.shields.io/github/forks/ngoanh2n/csv-comparator.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/network/members/)
[![GitHub stars](https://img.shields.io/github/stars/ngoanh2n/csv-comparator.svg?style=social&label=Star&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/stargazers/)
[![GitHub watchers](https://img.shields.io/github/watchers/ngoanh2n/csv-comparator.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/watchers/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator)
[![Build Status](https://travis-ci.org/ngoanh2n/csv-comparator.svg?branch=master)](https://travis-ci.org/ngoanh2n/csv-comparator)
[![GitHub release](https://img.shields.io/github/release/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/releases/)
[![badge-jdk](https://img.shields.io/badge/jdk-8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-blueviolet.svg)](https://opensource.org/licenses/MIT)
[![javadoc](https://javadoc.io/badge2/com.github.ngoanh2n/csv-comparator/javadoc.svg)](https://javadoc.io/doc/com.github.ngoanh2n/csv-comparator)
[![GitHub contributors](https://img.shields.io/github/contributors/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/graphs/contributors/)
[![GitHub issues](https://img.shields.io/github/issues/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/issues/)
[![GitHub issues-closed](https://img.shields.io/github/issues-closed/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/issues?q=is%3Aissue+is%3Aclosed)
[![GitHub pull-requests](https://img.shields.io/github/issues-pr/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/pulls/)
[![GitHub pull-requests closed](https://img.shields.io/github/issues-pr-closed/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/pulls?q=is%3Apulls+is%3Aclosed)

# CSV Comparator

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

### Provide `CsvComparisonSource`
```java
CsvComparisonSource<File> source = CsvComparisonSource.create(expectedCsv, actualCsv);
```

### Build `CsvComparisonOptions`
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

### Do `CsvComparator`
```java
CsvComparisonResult result = new CsvComparator(source, options).compare();
```

### Asssert `CsvComparisonResult`
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
