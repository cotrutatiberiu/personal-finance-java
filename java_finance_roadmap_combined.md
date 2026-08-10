# Java + Spring Boot — Personal Finance App Roadmap (Combined Detailed Version)

> **App concept:** A personal finance tracker where users manage accounts, track income/expenses, set budgets, and export reports.  
> Each phase adds features to this one app — so you always have context for *why* you're building something.  
> **Note:** No frontend. Use Postman / Insomnia / Bruno for all testing and demos.

This version combines:
- The **detailed style** of your original roadmap (annotations, patterns, pitfalls, interview notes)
- The **improved structure** (testing earlier, stronger concurrency & BigDecimal focus, clearer priorities)

---

## Core Goals
- Cover the most important real-world backend concerns (Auth, Authorization, Concurrency, Transactions, Testing, Caching, etc.)
- Build something deep enough to talk confidently about in interviews
- Stay realistic so you can actually finish a strong version

---

## Phase 1 — Auth & Security Foundations
**Status: Already done by you**

### What the user can do
- Register an account with email + password
- Log in and receive an access token (short-lived) + refresh token (longer-lived, preferably via HttpOnly cookie)
- Call a protected endpoint to verify authentication
- Refresh the access token without logging in again
- Logout (invalidate refresh token)

### Backend subjects covered
- `BCryptPasswordEncoder` — password hashing
- JWT generation + validation with separate signing keys per token type (access vs refresh)
- `OncePerRequestFilter` — custom filter in the Spring Security chain
- `SecurityFilterChain` — configuring which routes are public vs protected
- `SessionCreationPolicy.STATELESS` — why JWTs don’t need server sessions
- CSRF protection considerations with JWT (often disabled for pure APIs, or handled carefully with cookies)
- `@ControllerAdvice` + `@ExceptionHandler` — global exception handling
- Custom exception hierarchy (`EmailAlreadyUsedException`, `RefreshTokenExpiredException`, etc.)
- Java Records as DTOs (`RegisterRequest`, `LoginResponse`, etc.)
- Flyway versioned migrations — never let Hibernate manage schema in production
- Docker + docker-compose for Postgres + Redis

### Interview notes
- Be ready to explain the full login → access token → refresh flow
- Why separate keys for access and refresh tokens
- Difference between authentication and authorization (you will expand authorization later)

---

## Phase 2 — Accounts — CRUD, Ownership & Proper REST
**Goal:** Learn how to build a complete, correct REST resource with ownership checks

### What the user can do
- Create an account (e.g. "Main Bank", "Cash Wallet", "Savings")
- List all their accounts
- Update an account’s name or currency
- Archive an account (soft delete — never hard delete financial data)
- Get a single account by ID (404 if not found **or not theirs**)

### Backend subjects covered
- `ResponseEntity<T>` — returning proper HTTP status codes (201, 200, 204, 404, 403)
- `@Valid` + Jakarta Bean Validation annotations (`@NotBlank`, `@NotNull`, `@Size`, `@Min`, etc.)
- Service interface + implementation pattern — always code to an interface
- Mapper pattern — converting between Entity ↔ DTO manually (do it by hand first, MapStruct later if you want)
- Soft delete pattern — `archived` / `archivedAt` instead of deleting rows
- **Ownership validation** — every operation must check that the resource belongs to the current user
- Custom exceptions per case (`AccountNotFoundException`, `AccountNameTakenException`, `AccessDeniedException`)
- `@ControllerAdvice` handling multiple exception types with correct HTTP status per type
- SLF4J logging — replace every `System.out.println`

```java
// Never do this:
System.out.println("Creating account: " + name);

// Always do this:
private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
log.info("Creating account '{}' for userId={}", name, userId);
```

### Key pattern: Ownership check
```java
private Account getAccountForUser(Long accountId, Long userId) {
    return accountRepository.findByIdAndUserId(accountId, userId)
        .orElseThrow(() -> new AccountNotFoundException(accountId));
}
```
Returning 404 (instead of 403) when the resource exists but belongs to someone else is a common security practice (don’t leak existence).

---

## Phase 3 — Categories — Recursive Tree Structure
**Goal:** Self-referencing relationships and recursive data modeling

### What the user can do
- Create a top-level category (e.g. "Food", "Transport", "Income")
- Create a sub-category under any existing category
- List all categories in a flat list
- Get categories as a tree (parent → children → grandchildren)
- System prevents duplicate names within the same parent
- System prevents circular references (A → B → A)

### Backend subjects covered
- Self-referencing `@ManyToOne` / `@OneToMany` JPA relationship
- Partial unique indexes in PostgreSQL (`WHERE parent_id IS NULL` vs `WHERE parent_id IS NOT NULL`)
- Building a tree structure in Java — converting a flat list into a nested structure using a `Map`
- Cycle detection — traversing parent IDs and detecting loops
- Recursive DTOs — a `CategoryResponse` that contains `List<CategoryResponse> children`
- `@JsonInclude(JsonInclude.Include.NON_EMPTY)` — don’t serialize empty children arrays

### Interview notes
- Be able to explain how you detect cycles
- Trade-offs of storing trees (adjacency list vs nested sets vs materialized path)

---

## Phase 4 — Transactions — Business Logic + Concurrency + Money
**Goal:** `@Transactional`, optimistic locking, proper money handling, and why they matter

### What the user can do
- Record an income or expense transaction (amount, description, date, account, category)
- See their account balance update automatically after each transaction
- Edit a transaction — balance recalculates correctly (reverses old, applies new)
- Delete a transaction — balance is reversed
- List transactions with filters: date range, account, category, type (income/expense)
- Paginate results
- Add one or more tags to a transaction
- Filter transactions by tag

### Backend subjects covered
- `@Transactional` — what it actually does (unit of work, rollback on exception)
- `@Transactional` propagation levels — `REQUIRED` vs `REQUIRES_NEW`
- **`BigDecimal` for money** — never use `float`/`double`/`Double`
- `@Version` — optimistic locking to prevent lost updates on account balance
- Race condition simulation — write a test that fires two concurrent transactions and show how balance breaks without locking, then show it fixed
- `NUMERIC(19,4)` or similar in the database for money
- `Pageable` + `Page<T>` — Spring Data pagination
- `@Query` with JPQL or Specifications for filtered queries
- The N+1 query problem — you will hit this when loading transactions with account + category. Spot it in SQL logs, then fix with `@EntityGraph` or `JOIN FETCH`
- Many-to-many (tags) — first the naive `@ManyToMany`, then the explicit join entity when you need extra columns (`applied_at`, etc.)

### N+1 explained (interview favorite)
If you load 50 transactions and each one lazily fetches its `Account`, Hibernate fires 51 queries. Fix:
```java
@EntityGraph(attributePaths = {"account", "category"})
List<Transaction> findByUserId(Long userId);
```

### Critical concurrency test
Write a test that runs two threads simultaneously updating the same account balance and prove that without `@Version` (or proper locking) you lose updates, and with it you get `OptimisticLockException` (which you handle).

---

## Phase 5 — Testing (Moved Earlier — Do Not Skip)
**Goal:** Learn to test Spring applications properly. This is one of the biggest employability gaps.

### What you test
- Every important service method: happy path + error cases
- Every controller endpoint: correct status codes, response body, errors
- Auth flows
- Balance updates under concurrency
- Ownership checks

### Backend subjects covered
- **JUnit 5** — `@Test`, `@BeforeEach`, `@ParameterizedTest`
- **Mockito** — `@Mock`, `@InjectMocks`, `when().thenReturn()`, `verify()`, `assertThrows()`
- **`@WebMvcTest`** — loads only the web layer, mocks the service. Fast.
- **`MockMvc`** — perform fake HTTP requests and assert status + JSON
- **`@SpringBootTest`** — full application context for integration tests
- **Testcontainers** — real PostgreSQL container (modern standard)
- `@Transactional` on tests — rolls back after each test
- given / when / then naming

### Structure
```
src/test/java/
  unit/
    AccountServiceTest.java
    TransactionServiceTest.java
  integration/
    AccountControllerIT.java
    TransactionControllerIT.java
    AuthControllerIT.java
```

### What interviewers actually ask
- “How would you test a service that calls a repository?” → Mockito
- “How do you test an endpoint that requires auth?” → MockMvc + test JWT or `@WithMockUser`
- “Difference between `@WebMvcTest` and `@SpringBootTest`?” → slice vs full context

---

## Phase 6 — Budgets + Aggregations + Dynamic Filters
**Goal:** JPA query power and analytical endpoints

### What the user can do
- Set a monthly budget for any category
- See actual spend vs budget for a given month
- See remaining budget
- Monthly summary (income vs expenses)
- Spending breakdown by category
- Dynamic filtering of transactions (combine date + category + account + type + search)

### Backend subjects covered
- Native or JPQL `@Query` with `GROUP BY`, `SUM`, date truncation
- JPA Projection interfaces — returning aggregated results without loading full entities
- `Specification<T>` API — building dynamic WHERE clauses without string concatenation
- `JpaSpecificationExecutor<T>`
- Index usage — understanding how indexes on `(user_id, occurred_at)` help these queries
- Date/month boundary handling (be careful with timezones)

---

## Phase 7 — Caching + Scheduled Jobs
**Goal:** `@Scheduled` and proper caching

### What the user can do
- Fast budget/summary responses (cached)
- Nightly job that checks for exceeded budgets and logs warnings

### Backend subjects covered
- `@EnableScheduling` + `@Scheduled(cron = "...")`
- Spring Cache abstraction + Redis
- `@Cacheable`, `@CacheEvict`, `@CachePut`
- Cache key design (`#userId + '-' + #month`)
- When to expire vs when to evict manually
- TTL considerations

---

## Phase 8 — File Export + Async Processing
**Goal:** File generation and background work

### What the user can do
- Export transactions for a date range as `.xlsx` or CSV
- Large exports can run asynchronously (return job ID, poll for completion)

### Backend subjects covered
- Apache POI (or alternative) for Excel generation
- `StreamingResponseBody` — stream without loading everything into memory
- `Content-Disposition` header for downloads
- `@Async` + `CompletableFuture`
- `@EnableAsync` + custom thread pool configuration
- Simple job status tracking

---

## Phase 9 — Authorization (RBAC) & Security Hardening
**Goal:** Proper authorization beyond simple ownership

### What the user can do
- Regular users can only access their own data
- Admin users can list all users / view any account (optional feature)
- Attempting to access another user’s data returns 403 (or 404)

### Backend subjects covered
- `@EnableMethodSecurity`
- `@PreAuthorize("hasRole('ADMIN')")` or `hasAuthority(...)`
- `SecurityContextHolder` — extracting current user ID and roles inside services
- Combining ownership checks + role checks
- 403 vs 404 decision (security best practice)
- Actuator endpoints (`/actuator/health`, `/actuator/info`) with proper security
- Additional hardening (headers, rate limiting ideas, etc.)

### Interview notes
- Be able to explain the difference between authentication (who you are) and authorization (what you are allowed to do)
- How method security works under the hood at a high level

---

## Phase 10 — API Documentation & Final Polish
**Goal:** Make the project portfolio-ready

### What you add
- SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
- `@Operation`, `@ApiResponse`, `@Parameter`, `@Schema` on endpoints and DTOs
- Consistent error response format across the whole API
- Structured logging
- Excellent README containing:
  - Architecture overview
  - How to run with Docker
  - Main business rules
  - How concurrency on balances is handled
  - How ownership and RBAC work
  - Postman collection (highly recommended)
- Final code cleanup and consistency

---

## Parallel Java Topics (Study Alongside)

| Topic              | Priority | Notes |
|--------------------|----------|-------|
| Stream API         | High     | `map`, `filter`, `collect`, `groupingBy`, `flatMap` |
| `Optional<T>`      | High     | Never call `.get()` blindly |
| Generics           | Medium   | Write a generic `PageResponse<T>` |
| `BigDecimal`       | High     | Critical for this domain |
| Concurrency basics | High     | Happens-before, visible side effects |
| Records            | Already using | Understand equals/hashCode/toString |

---

## Suggested Minimum Viable Portfolio Version

If time is limited, prioritize this:

1. Phase 1 – Auth (done)
2. Phase 2 – Accounts + ownership
3. Phase 3 – Categories
4. Phase 4 – Transactions + concurrency + `BigDecimal` (most important)
5. Phase 5 – Solid testing (especially concurrency and ownership)
6. Phase 6 – Budgets + basic reports
7. Phase 9 – At least basic RBAC / method security
8. Phase 10 – Swagger + excellent README + Postman collection

This combination already covers the majority of what mid-level Java backend interviews care about.

---

## Final Notes

- Depth beats breadth. A clean, well-tested implementation of Phases 1–6 + 9 is stronger than a shallow version of everything.
- Be very strict with `BigDecimal` and concurrency tests — these are high-signal topics.
- Document architectural decisions in the README. Interviewers love seeing intentional choices.
- You already have a meaningful head start (Docker, Flyway, Redis, JWT, React experience on the consumer side).

This roadmap keeps the detailed, practical style of your original while using a better phase order and stronger emphasis on the topics that matter most for employability.
