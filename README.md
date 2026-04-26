# Project Records Analyzer

## Overview
Desktop application that reads CSV files and identifies the pair of employees who have worked together on common projects for the longest total period of time.

## UI Preview
![App Screenshot](src/main/resources/screenshot.png)

## Features
- JavaFX UI for selecting CSV files
- Parses multiple date formats
- Empty / NULL DateTo values are treated as current date
- Displays common projects for the longest working employee pair
- Input validation with clear error messages
- Unit and integration tests

## Tech Stack
- Java 25
- Gradle
- JavaFX
- JUnit 5
- Mockito
- Lombok

## Run
``` ./gradlew run```

## Test
``` ./gradlew test```

## Assumptions
- If multiple pairs share the same maximum duration, a single pair is returned.
- Input project records are not pre-sorted.
- One employee appears at most once per project.
- Overlap is inclusive of the end date.
- Input size is moderate and fits in memory.

## Supported Date Formats
- yyyy-MM-dd
- dd-MM-yyyy
- MM/dd/yyyy
- dd/MM/yyyy
- yyyy/MM/dd
- dd MMM yyyy
- yyyyMMdd