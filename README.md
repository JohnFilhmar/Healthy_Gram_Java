# Healthy Gram

A Java Swing desktop application for a health food shop. It has three screens: a login window, a point of sale terminal, and an inventory manager. Data lives in a local MySQL database reached over JDBC.

The project is a work in progress. Login and user registration talk to the database and work end to end. The point of sale and inventory screens are built but not yet wired to the database, so see [Current status](#current-status) before assuming a feature works.

## Requirements

- A JDK. The Eclipse project files target Java 1.8, but the source compiles cleanly on JDK 17.
- MySQL running on `localhost:3306`.
- MySQL Connector/J 8.0.33. The Eclipse classpath expects the jar at `C:\Users\olajo\mysql-connector-j-8.0.33.jar`. Download it from [dev.mysql.com](https://dev.mysql.com/downloads/connector/j/) or Maven Central and put it there, or edit `.classpath` to point at your own copy.

## Database setup

The connection settings are hardcoded in `src/healthy_gram/Dbm.java`:

```
url:      jdbc:mysql://localhost:3306/healthy_gram
user:     root
password: (empty)
```

Change them there if your local MySQL differs.

No schema dump ships with this repository. The DDL below was reconstructed by reading which columns the code writes and reads, so treat it as a starting point rather than the authoritative schema. If you have the original dump, use that instead.

```sql
CREATE DATABASE IF NOT EXISTS healthy_gram;
USE healthy_gram;

CREATE TABLE item (
  Item_id         INT AUTO_INCREMENT PRIMARY KEY,
  Item_name       VARCHAR(255) NOT NULL,
  Description     TEXT,
  Price           DECIMAL(10,2) NOT NULL,
  Weight          DECIMAL(10,3),
  Expiration_date DATETIME
);

CREATE TABLE cashier (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(255) NOT NULL,
  passcode   CHAR(64) NOT NULL,
  Contact_no VARCHAR(20),
  Sex        VARCHAR(10)
);

CREATE TABLE preparation (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(255) NOT NULL,
  passcode   CHAR(64) NOT NULL,
  Contact_no VARCHAR(20),
  Sex        VARCHAR(10)
);
```

Two things about this schema are load bearing:

Column order in `item` matters. `InventoryController.fetchItems` reads each row by position (`row[0]` through `row[5]`) and maps them onto the names above. Reordering the columns silently mislabels every field.

Staff are split across two tables. `cashier` and `preparation` hold the same shape, and login checks both with a `UNION ALL`. `UserController.newUser` picks the table from its `userType` flag: `true` writes to `cashier`, `false` to `preparation`. `passcode` stores an unsalted SHA-256 hash as 64 hex characters.

## Build and run

There is no Maven or Gradle build. The Eclipse JDT builder compiles `src/` into `bin/`. Import the folder as an existing Eclipse project and it builds on its own.

To build from a terminal instead, from the repository root:

```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | % { $_.FullName })
```

Compiling needs no driver jar. `Dbm` loads the driver reflectively by name, so the classpath only matters at runtime.

Three classes have a `main` method, one per screen:

```powershell
java -cp "bin;C:\Users\olajo\mysql-connector-j-8.0.33.jar" healthy_gram.Main
java -cp "bin;C:\Users\olajo\mysql-connector-j-8.0.33.jar" healthy_gram.PointOfSale
java -cp "bin;C:\Users\olajo\mysql-connector-j-8.0.33.jar" healthy_gram.Inventory
```

On Windows the classpath separator is `;`, not `:`.

`healthy_gram.Main` is a scratch harness rather than a real entry point. Its `main` currently calls `tryItems()`, which dumps the item table to stdout, and the line that opens the login window is commented out. Uncomment it to launch the app properly. `DoTry()` and `getUsers()` in the same file are throwaway database checks, and `DoTry()` inserts, updates, and deletes rows in `item` when you run it.

## Registering a user

There is no seeded account, so create one through the login window. Type a username and passcode, both at least 8 characters, then press **Create New Account**. `Login.register` fills in the remaining fields with placeholders: contact number `0931231232`, sex `Male`, and `userType = true`, which files the account under `cashier`. Success and failure are printed to the console, not shown in the window.

Log in with the same credentials and the point of sale screen opens.

## Project layout

```
src/healthy_gram/
  Main.java            scratch harness, not the real entry point
  Login.java           login and registration window
  PointOfSale.java     POS terminal
  Inventory.java       inventory manager
  DatabaseModel.java   chainable query builder
  Dbm.java             JDBC connect, query, insert, close
  Users.java           unused POJO
  controllers/         UserController, InventoryController, PointOfSaleController
  helpers/             UsernameOrPasswordTooShortException
public/                logo1.png, logo2.jpg, jms.png
bin/                   compiled output
```

Requests flow from a Swing window to a controller, then to `DatabaseModel`, which builds SQL and hands it to `Dbm` for execution.

## Current status

Working:

- Login, including the SHA-256 passcode check across both staff tables
- User registration, with the placeholder fields noted above
- Reading the item table, via `InventoryController.fetchItems`

Not working yet:

- The Inventory screen's Add, Edit, and Delete buttons only change the on screen table. Nothing is saved to the database.
- The point of sale screen shows 25 placeholder buttons labelled "Product 1" through "Product 25". It never queries real items, and the Orders panel is empty.
- `PointOfSaleController` has `totalWeight` and `totalPrice` fields that nothing reads or writes.
- The expiry filter in `InventoryController` only triggers for `String` and `LocalDateTime` values. MySQL returns `java.sql.Timestamp`, so expired items are never filtered out.
- The Inventory table shows values in the wrong columns, because `fetchItems` returns an unordered `HashMap` and the table fills its columns by iteration order.
- `Login.java` loads the logo and window icon from an absolute path under `C:\Users\olajo\eclipse-workspace\HealthyGram\public\`. That path is not this checkout, so both images fail to load and the window renders without them.

## License

MIT. See [LICENSE](LICENSE).
