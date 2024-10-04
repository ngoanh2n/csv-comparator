[![Java](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net)
[![Maven](https://img.shields.io/maven-central/v/com.github.ngoanh2n/csv-comparator-allure?label=Maven)](https://mvnrepository.com/artifact/com.github.ngoanh2n/csv-comparator-allure)
[![GitHub Actions](https://img.shields.io/github/actions/workflow/status/ngoanh2n/csv-comparator/test.yml?logo=github&label=GitHub%20Actions)](https://github.com/ngoanh2n/csv-comparator/actions/workflows/test.yml)
[![CircleCI](https://img.shields.io/circleci/build/github/ngoanh2n/csv-comparator?token=CCIPRJ_V9AVYTzVyEF9A9GMsVD9oF_2ce0fb3410ce42dfee9d8d854bae69d56f206df6&logo=circleci&label=CircleCI)
](https://dl.circleci.com/status-badge/redirect/gh/ngoanh2n/csv-comparator/tree/master)

# CSV Comparator for Allure
**Table of Contents**
<!-- TOC -->
* [Declaration](#declaration)
  * [Gradle](#gradle)
  * [Maven](#maven)
* [Description](#description)
* [System Property](#system-property)
<!-- TOC -->
When using Allure as a report framework, `csv-comparator-allure` should be used.

![](images/allure-report.png)

# Declaration
## Gradle
Add to `build.gradle`.
```gradle
implementation("com.github.ngoanh2n:csv-comparator-allure:1.10.0")
```

## Maven
Add to `pom.xml`.
```xml
<dependency>
    <groupId>com.github.ngoanh2n</groupId>
    <artifactId>csv-comparator-allure</artifactId>
    <version>1.10.0</version>
</dependency>
```

# Description
You can change comparison description on Allure by creating file `csv-comparator-allure.properties` in folder `resources`.<br>
Default description as below.
```properties
subject=CSV Comparison
expFile=exp
actFile=act
expCsv=Expected CSV
actCsv=Actual CSV
settings=Parser Settings
```

# System Property
| Name                            | Description                                                   | Default  |
|:--------------------------------|:--------------------------------------------------------------|:---------|
| `ngoanh2n.csv.includeSource`    | Indicate which attaches CSV sources to Allure report.         | `true`   |
| `ngoanh2n.csv.includeSettings`  | Indicate which attaches CSV parser settings to Allure report. | `true`   |
