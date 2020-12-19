# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

----
## [Unreleased] (In Git)

### Added

### Changed

### Deprecated

### Removed

### Fixed
----
## [1.2.0] - (2020-12-19)
### Added
- `CsvComparisonResultOptions`
- Allow to add headers to result fies ([#2](https://github.com/ngoanh2n/csv-comparator/issues/2))
- `CsvComparisonResult#rowsKept()`
- `CsvComparisonResult#rowsDeleted()`
- `CsvComparisonResult#rowsInserted()`
- `CsvComparisonResult#rowsModified()`
- Dependency `com.google.guava:guava`
- Dependency `com.github.albfernandez:juniversalchardet`
### Changed
- Upgrade Gradle to `6.7.1`
- Upgrade JUnit5 Jupiter to `5.7.0`
- Change Builder method names for `CsvComparisonOptions`
- Apply `CsvComparisonVisitor` and `CsvComparisonSource` for `CsvComparator`
### Removed
- `CsvComparisonResult#getAddedRows()`
- `CsvComparisonResult#getDeletedRows()`
- `CsvComparisonResult#getModifiedRows()`

## [1.1.0] - (2020-02-01)
### Added
- Allow to get diff files from CsvComparisonResult ([#1](https://github.com/ngoanh2n/csv-comparator/issues/1))
### Changed
- [Gradle] Make failing task when the tests failed - `ignoreFailures = false` [build.gradle](build.gradle)

## 1.0.0 - (2020-01-29)
### Added
- [Core] Basic comparison and assertion between 2 CSV files
  + Check diff
  + Check rows added
  + Check rows deleted
  + Check rows modified
