# Games API

A Spring Boot backend project for managing games and game versions, built with a layered architecture similar to an enterprise-style codebase.

This project supports:
- reading games by brand
- reading a single game by id
- creating games
- updating games
- validation and exception handling
- local development with H2
- production-style deployment on AWS EC2
- production database with AWS RDS MySQL

---

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Web
- Spring JDBC
- H2 Database
- MySQL
- AWS EC2
- AWS RDS
- systemd
- Maven

---

## Project Structure

```text
src/main/java/com/example/gamesapi
├── auroradb
│   ├── AbstractAuroraDelegate.java
│   ├── AddGameAuroraDelegate.java
│   ├── GetGameByIdAuroraDelegate.java
│   ├── GetGamesAuroraDelegate.java
│   └── UpdateGameAuroraDelegate.java
├── config
│   └── AWSConfigurationProperties.java
├── controller
│   ├── AppHealthController.java
│   └── GamesController.java
├── dto
│   └── SaveGameRequest.java
├── exception
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model
│   ├── Game.java
│   └── GameVersion.java
├── service
│   └── GamesService.java
└── GamesApiApplication.java