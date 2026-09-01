# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`HealthyGram` — a Java 8 Swing desktop app for a health-food shop: login, point of sale, and inventory, backed by a local MySQL database. Package root is `healthy_gram`.

## Build & run

There is **no Maven or Gradle**. This is a raw Eclipse JDT project (`.project`, `.classpath`) that compiles `src/` into `bin/`. Both are committed — there is no `.gitignore`, so `bin/*.class` files show up in every diff.

Toolchain reality check before doing anything build-related:

- `.settings/org.eclipse.jdt.core.prefs` pins source/target/compliance to **1.8**, and `.classpath` points at a `jre1.8.0_131` container.
- The JDK actually installed here is **Temurin 17**. Compiling with it works but is not what the project claims.
- `.classpath` references the MySQL driver at the absolute path `C:/Users/olajo/mysql-connector-j-8.0.33.jar`, **which does not exist on this machine**. Anything that touches the database will fail with a missing `com.mysql.cj.jdbc.Driver` until that jar is downloaded to that path (or the classpath entry is repointed).

Command-line build and run. The driver is loaded reflectively (`Class.forName("com.mysql.cj.jdbc.Driver")`), so **compilation succeeds without the jar** — only running against the DB needs it:

```powershell
# compile (verified clean on JDK 17, no driver jar required)
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | % { $_.FullName })

# run a screen (each of these has its own main())
java -cp "bin;C:\Users\olajo\mysql-connector-j-8.0.33.jar" healthy_gram.Main
java -cp "bin;C:\Users\olajo\mysql-connector-j-8.0.33.jar" healthy_gram.PointOfSale
java -cp "bin;C:\Users\olajo\mysql-connector-j-8.0.33.jar" healthy_gram.Inventory
```

Classpath separator is `;` on Windows even from the bash tool.

**There are no tests and no test framework on the classpath.** `Main.main` is a scratchpad — it currently calls `tryItems()` with the real `Login` launch commented out, and `DoTry()`/`getUsers()` are ad-hoc DB smoke tests that insert, update, and delete rows in `item`. Treat `Main` as a manual harness, not an entry point, and expect to edit which method it calls when checking something by hand.

### Database

`Dbm.java` hardcodes the connection: `jdbc:mysql://localhost:3306/healthy_gram`, user `root`, empty password. A MySQL server with a `healthy_gram` schema must be running locally. **No schema dump or migration exists in the repo** — the table shapes are only inferable from the code:

- `item` — `Item_id`, `Item_name`, `Description`, `Price`, `Weight`, `Expiration_date` (column order matters, see below)
- `cashier` and `preparation` — two separate user tables, each with `name`, `passcode`, `Contact_no`, `Sex`

## Architecture

Four layers, view → controller → query builder → JDBC:

```
Login / PointOfSale / Inventory   (Swing JFrames, healthy_gram)
        ↓
UserController / InventoryController / PointOfSaleController   (healthy_gram.controllers)
        ↓
DatabaseModel   (fluent query builder, healthy_gram)
        ↓
Dbm   (raw JDBC: connect / getQuery / insertQuery / close)
```

Screen flow: `Login` → on successful `verifyUser`, opens `PointOfSale` and disposes itself. `PointOfSale`'s Logout button reverses it. `Inventory` is currently reachable only via its own `main()` — nothing navigates to it.

All Swing frames use `setLayout(null)` with absolute `setBounds` pixel coordinates. Adding a widget means picking coordinates by hand; there is no layout manager to reflow anything.

### `DatabaseModel` — the fluent builder, and its traps

`DatabaseModel` accumulates a `StringBuilder query` and a `List<Object> parameters` across chained calls. Understand these before writing any data access, because the API looks like a normal ORM and does not behave like one:

1. **`select()` returns a *different* instance.** It constructs a new `DatabaseModel` whose `query` is `"SELECT * FROM " + tableName` but whose own `tableName` field is left `null`. So `.table("item").select().getAll()` works, but `.table("item").select().delete()` produces `DELETE FROM null ...`. Write terminators like `insert`/`update`/`delete` on the model that still holds the table name, and only `getAll()` after `select()`.

2. **Nothing resets between operations.** `query` and `parameters` are never cleared after a terminator runs. Reusing one `DatabaseModel` for two queries silently concatenates their `WHERE` clauses and parameter lists. Controllers work around this by holding one `private static DatabaseModel dbm` and only ever issuing one kind of read — do not extend that pattern without constructing a fresh model per query.

3. **`Dbm.getQuery` casts every bound parameter to `String`**: `pstmt.setObject(i + 1, (String) parameters[i])`. Passing a non-`String` to `where()` on a read path throws `ClassCastException` at runtime. `Dbm.insertQuery` does *not* have this cast, which is why writes accept typed values (`Timestamp`, `Double`) and reads do not. `Main.DoTry()` contains a live instance of this bug (`.where("Item_id", 4)` with an `int`).

4. **`Dbm` is a single static instance shared by every `DatabaseModel`**, and every terminator opens a connection and closes it in a `finally`. There is no pooling and no transaction support; a multi-statement operation is several independent connections.

5. Column and table names are string-concatenated into the SQL; only values are parameterized. `raw()` bypasses parameters entirely.

### Controller notes

- `UserController.verifyUser` fetches **every** user from both tables via a hardcoded `UNION ALL` raw query, then loops in Java comparing SHA-256 hashes (`hashPassword`, unsalted, no iteration count). `newUser(..., boolean userType)` routes `true` → `cashier`, `false` → `preparation`.
- `InventoryController.fetchItems` returns `List<Map<String, Object>>` built from **`HashMap`**, so key order is not insertion order. `Inventory.java` fills its `JTable` by iterating `entrySet()` positionally into a `String[6]` — column values land in scrambled columns. If you touch either side, switch the map to `LinkedHashMap` or index by the known column names rather than iteration order.
- Its expiry filter only fires when `Expiration_date` comes back as a `String` or `LocalDateTime`; the MySQL driver returns `java.sql.Timestamp`, so in practice **nothing is filtered out**.

### Known incomplete work

Do not assume these are wired up:

- `Inventory`'s Add/Edit/Delete buttons mutate only the in-memory `DefaultTableModel` — no `DatabaseModel` call, nothing persists. They also write 4 values into a 6-column table model.
- `PointOfSale` renders 25 placeholder `"Product N"` buttons in a loop; it never calls `PointOfSaleController.getItems()`, and the Orders panel is an empty `JPanel` with a label.
- `PointOfSaleController` holds `totalWeight`/`totalPrice` getters and setters that nothing reads.
- `Users.java` is an unused POJO; `UserController` duplicates its two fields.
- `Login.register` hardcodes contact number `"0931231232"`, sex `"Male"`, and `userType = true`.

### Assets

`public/` holds `logo1.png`, `logo2.jpg`, `jms.png`. `Login.java` loads two of them by **absolute path** pointing at `C:\Users\olajo\eclipse-workspace\HealthyGram\public\`, which is not this checkout — the logo and window icon silently fail to render. Prefer classpath or relative loading if you change that code.

## Conventions in this codebase

Java naming here is standard-Java, not the global snake_case default: `PascalCase` types, `camelCase` methods and fields. Database columns are inconsistent (`Item_name`, `Expiration_date` capitalized; `name`, `passcode` lowercase) — match whatever the existing table already uses rather than normalizing. Indentation is mixed tabs and spaces across files; match the surrounding file.

Checked exceptions are used sparingly: `healthy_gram.helpers.UsernameOrPasswordTooShortException` is the only custom one (8-character minimum, enforced in `Login.confirmLength`). Everywhere else errors are swallowed with `e.printStackTrace()` or `System.out.println`, and user-facing failures print to the console rather than a dialog.
