# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository....

whenever searching for any documentation, you must delegate to the DocsExplorer subagent first. Do not look up documentation yourself.

## Commands

```bash
# Build and run all tests (also generates JaCoCo coverage report in target/site/jacoco/)
./mvnw clean verify

# Run tests only
./mvnw test

# Run a single test class
./mvnw test -Dtest=GamesServiceTest

# Build the jar (skip tests)
./mvnw clean package -DskipTests

# Run locally (uses H2 in-memory DB via the 'local' profile)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Build Docker image
docker build -t games-api:ci .
```

## Architecture

This is a Spring Boot / Java 17 REST API for managing games and their versions, organized in a layered architecture.

### Request Flow

`GamesController` → `GamesService` → `*AuroraDelegate` (JDBC) → DB

- **Controller** (`controller/`): Thin REST layer. `GamesController` exposes:
  - `GET /games/by-brand?brandId=` → list of games for a brand
  - `GET /games/by-id?id=` → single game (throws `ResourceNotFoundException` → 404 if not found)
  - `POST /games/save-game` → create a game (no `id` in body)
  - `PUT /games/update-game` → update a game (`id` required in body)

  Both POST and PUT route through `GamesService.saveGame()` — the presence or absence of `id` in `SaveGameRequest` determines whether `AddGameAuroraDelegate` or `UpdateGameAuroraDelegate` is called. Note: `saveGame()` returns `null` (not a 404) when an update affects 0 rows. `AppHealthController` provides a simple `/health` endpoint separate from Actuator.

- **Service** (`service/GamesService`): Orchestrates delegates and applies post-processing. After fetching games, it calls `decorateLocations()` to prefix all `GameVersion.location` values with the CloudFront base URL from `AWSConfigurationProperties`.

- **Delegate pattern** (`auroradb/`): Each database operation is its own `@Component` extending `AbstractAuroraDelegate` (which holds a `JdbcTemplate`). SQL is written inline as text blocks. `GetGamesAuroraDelegate` uses a `ResultSetExtractor` to manually map a joined `game`+`game_version` result set into `Game` objects with nested `List<GameVersion>`.

- **Error handling** (`exception/`): `GlobalExceptionHandler` returns structured JSON for `ResourceNotFoundException` (404) and bean validation failures `MethodArgumentNotValidException` (400 with per-field error map).

### Data Model

Three tables: `brand` → `game` → `game_version`. A `Game` has a `List<GameVersion>`. `SaveGameRequest` (DTO) is used for both create and update — `brandId`, `name`, and `engine` are `@NotNull`/`@NotBlank`; `id` is optional and its presence drives create-vs-update logic.

### Local Development

The `local` profile bootstraps an H2 in-memory DB using `schema.sql` and `data.sql` (in `src/main/resources/`). `data.sql` seeds two brands (`newport`, `camel`), two games, and three game versions. H2 console is available at `/h2-console` when running locally.

### Configuration Profiles

| Profile | DB | Trigger |
|---|---|---|
| `local` | H2 in-memory (MySQL mode) | `-Dspring-boot.run.profiles=local` |
| `prod` | MySQL via `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` env vars | Default in deployed environment |

`AWSConfigurationProperties` (prefix `aws`) binds `region`, `s3-bucket`, and `games-cloudfront` from `application.yml`. The `gamesCloudfront` value is prepended to all `GameVersion.location` paths to construct full asset URLs.

### Testing Patterns

Tests are pure unit tests — no `@SpringBootTest`, no Spring context. Dependencies are mocked with `Mockito.mock()` and wired directly via constructor injection. `GamesServiceTest` covers the service logic including `decorateLocations()` edge cases (blank locations are left unchanged). `GamesControllerTest` verifies the controller delegates correctly to the service.

### CI/CD Pipeline

`.github/workflows/ci.yml` runs on pushes/PRs to `main`:
1. Runs `./mvnw clean verify` (tests + build)
2. Logs into AWS ECR (account `004878717876`, region `us-east-1`)
3. Builds the Docker image and tags it as both `:latest` and `:<7-char-SHA>`
4. Pushes both tags to ECR repository `games-api`
5. Forces a new deployment on ECS cluster `games-api-cluster`, service `games-api-task-service-2016y2ts`

The Dockerfile copies `target/games-api-0.0.1-SNAPSHOT.jar` — the jar must be built before `docker build` runs (CI does this in step 1).
