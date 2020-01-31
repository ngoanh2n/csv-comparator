[![Twitter Follow](https://img.shields.io/twitter/follow/ngoanh2n.svg?style=social)](https://twitter.com/ngoanh2n)
[![GitHub followers](https://img.shields.io/github/followers/ngoanh2n.svg?style=social&label=Follow&maxAge=2592000)](https://github.com/ngoanh2n?tab=followers)
[![GitHub forks](https://img.shields.io/github/forks/ngoanh2n/csv-comparator.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/network/members/)
[![GitHub stars](https://img.shields.io/github/stars/ngoanh2n/csv-comparator.svg?style=social&label=Star&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/stargazers/)
[![GitHub watchers](https://img.shields.io/github/watchers/ngoanh2n/csv-comparator.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/ngoanh2n/csv-comparator/watchers/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ngoanh2n/csv-comparator)
[![Build Status](https://travis-ci.org/ngoanh2n/csv-comparator.svg?branch=master)](https://travis-ci.org/ngoanh2n/csv-comparator)
[![GitHub release](https://img.shields.io/github/release/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/releases/)
[![badge-jdk](https://img.shields.io/badge/jdk-8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-blueviolet.svg)](https://opensource.org/licenses/MIT)
[![GitHub contributors](https://img.shields.io/github/contributors/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/graphs/contributors/)

[![GitHub issues](https://img.shields.io/github/issues/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/issues/)
[![GitHub issues-closed](https://img.shields.io/github/issues-closed/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/issues?q=is%3Aissue+is%3Aclosed)
[![GitHub pull-requests](https://img.shields.io/github/issues-pr/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/pulls/)
[![GitHub pull-requests closed](https://img.shields.io/github/issues-pr-closed/ngoanh2n/csv-comparator.svg)](https://github.com/ngoanh2n/csv-comparator/pulls?q=is%3Apulls+is%3Aclosed)

# CSV Comparator

### **Maven Project**
Add the `csv-comparator` dependency to your pom.
```xml
<dependencies>
    [...]
    <dependency>
        <groupId>com.github.ngoanh2n</groupId>
        <artifactId>csv-comparator</artifactId>
        <version>1.0.0</version>
    </dependency>
    [...]
</dependencies>
```

### **Gradle Project**
Add the `csv-comparator` dependency to your build.gradle.
```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.github.ngoanh2n:csv-comparator:1.0.0'
}
```

### **Examples**
Compare 2 CSV files formatted columns:
```
id,email,firstname,lastname,age,note
```

#### **Build CsvComparator**
```java
CsvComparator comparator = CsvComparator.builder()
                .onColumns(1, 2, 3) // 
                .onCsvFiles(
                        "path/to/actual.csv",
                        "path/to/expected.csv")
                .byIdentityColumn(0) // position starts with 0 in array [1, 2, 3]
                .build();
```

If you want to use column names:
```java
                .onColumns("email", "firstname", "lastname")
                .byIdentityColumn("email")
```

#### **Recieve CsvComparisonResult**
```java
CsvComparisonResult result = comparator
                .saveDiffAt("build/csv-results")
                .perform();
```

#### **Result Assertions**
**Check diff:**
```java
Assertions.assertTrue(result.hasDiff());
```

**Check addition:**
```java
Assertions.assertTrue(result.hasRowAdded());
```

**Check deletion:**
```java
Assertions.assertTrue(result.hasRowDeleted());
```

**Check modification:**
```java
Assertions.assertTrue(result.hasRowModified());
```