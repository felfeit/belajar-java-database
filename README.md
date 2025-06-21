![Java JDBC](https://nehajain216.github.io/img/jdbc.png)

# Pengenalan Java JDBC
**JDBC** adalah singkatan dari **Java Database Connectivity**, sebuah standard API yang disediakan oleh Java untuk memungkinkan aplikasi Java berkomunikasi dengan berbagai jenis database.

## Apa itu JDBC?
JDBC merupakan **spesifikasi standar** yang mendefinisikan sekumpulan antarmuka (interface) dan kelas yang digunakan untuk mengakses dan memanipulasi data di dalam database dari aplikasi Java.

Namun, penting untuk dipahami bahwa:
- JDBC **bukanlah implementasi langsung** yang bisa digunakan begitu saja.
- JDBC hanya menyediakan **interface-interface contract** yang mendefinisikan cara interaksi antara Java dan database.
- Agar JDBC dapat digunakan, kita memerlukan **implementasi dari driver database** tertentu.

Driver JDBC yang umum digunakan seperti:
- MySQL: `com.mysql.cj.jdbc.Driver`
- PostgreSQL: `org.postgresql.Driver`
- Oracle: `oracle.jdbc.driver.OracleDriver`
- Micosoft SQL Server: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
- dan lain-lainnya juga memiliki Driver masing masing.

## Struktur JDBC
Semua interface dan class utama dalam API JDBC dapat ditemukan dalam dua package berikut:
- `java.sql`: berisi antarmuka dasar seperti `Connection`, `Statement`, `ResultSet`, `Driver`, dll.
- `javax.sql`: berisi ekstensi JDBC seperti `DataSource`, `ConnectionPoolDataSource`, dan lainnya yang mendukung fitur lanjutan seperti *connection pooling*.

## Dokumentasi Resmi
Untuk dokumentasi lengkap dari API JDBC, kamu bisa merujuk ke dokumentasi resmi Oracle berikut:
- [java.sql package summary](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/package-summary.html)
- [javax.sql package summary](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/javax/sql/package-summary.html)

## Cara kerja JDBC
Cara kerja JDBC bisa dijelaskan seperti ini:

Aplikasi yang kita buat—baik itu aplikasi desktop, web, atau jenis lainnya yang dibangun dengan Java—tidak langsung terhubung ke database. Sebagai gantinya, aplikasi akan terhubung dulu ke **JDBC** (Java Database Connectivity).

Tapi, **JDBC sendiri bukan penghubung langsung ke database**, karena JDBC hanya menyediakan interface atau "kontrak" umum untuk komunikasi dengan berbagai jenis database. Supaya bisa benar-benar terkoneksi, JDBC memerlukan driver.

Nah, **driver JDBC inilah** yang bertugas menghubungkan ke database sebenarnya. Jenis driver yang digunakan akan menentukan jenis database yang bisa diakses. Misalnya:
- Kalau kamu pakai **MySQL JDBC Driver**, maka kamu hanya bisa connect ke database **MySQL**.
- Kalau pakai **PostgreSQL JDBC Driver**, maka hanya bisa connect ke database **PostgreSQL**.)

### Alur nya bisa digambarkan seperti ini

Jadi, yang perlu dipahami:
- JDBC adalah perantara.
- Driver adalah jembatan teknis sebenarnya yang tahu “bahasa” database-nya.
- Jenis driver harus cocok dengan jenis database yang ingin kamu akses.

> [!NOTE] 
> Untuk menguji JDBC, kita akan menggunakan unit test, dan pastinya menambahkan dependency JUnit 5 di project-nya. Silahkan cari dependency `junit-jupiter` di [https://search.maven.org/](https://search.maven.org/)

# Arsitektur dan Komponen JDBC
Setelah tahu konsep dasar JDBC, sekarang kita bahas komponen-komponen penting yang jadi "pemain utama" di dalam proses koneksi dan komunikasi antara aplikasi Java dan database.

## 1. Driver
`Driver` adalah **jembatan** antara JDBC dan Database Management System (DBMS) yang kamu gunakan (seperti MySQL, PostgreSQL, dll).
Tanpa driver, **JDBC nggak bisa berkomunikasi langsung dengan database**.

Secara teknis, **driver adalah implementasi class-class dari interface JDBC**, terutama dari interface `java.sql.Driver`. Baca referensinya [Disini](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/Driver.html ).

### Contohnya: MySQL Driver
Kalau pakai database MySQL, maka perlu menggunakan **Driver khusus MySQL**. Untuk menambahkan Driver MySQL ke project Java menggunakan Maven, kalian bisa cari di [https://search.maven.org/](https://search.maven.org/) dengan keyword `mysql-connector-java`, lalu tambahkan konfigurasi dependency tersebut ke dalam `pom.xml` seperti ini:
```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.33</version>
</dependency>
```

### Registrasi Driver ke JDBC
Setelah drivernya ditambahkan ke dependency project, diperlukan untuk **mendaftarkan driver** ke JDBC. Ini supaya `DriverManager` mengetahui adanya driver yang bisa dipakai.
Caranya, cukup dengan memanggil seperti ini:

```
DriverManager.registerDriver(new com.mysql.jc.jdbc.Driver());
```
Tapi kabar baiknya, **sejak JDBC 4.0**, kalau kamu pakai dependency dengan benar (misalnya via Maven), maka **driver akan otomatis terdaftar lewat SPI** (Service Provider Interface).
Jadi **nggak harus register manual** kecuali memang dibutuhkan secara eksplisit.

### Unit Test: Registrai Driver Manual
Kalau kamu pengen ngetes apakah MySQL JDBC Driver bisa diregistrasi secara manual (meskipun JDBC 4.0+ biasanya udah otomatis), kamu bisa bikin unit test seperti ini:
```java
@Test
void testRegisterDriver() {
    try {
        Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(mysqlDriver);
    } catch (SQLException exception) {
        Assertions.fail(exception);
    }
}
```

## 2. Connection
Setelah driver sudah siap (dan terdaftar), langkah selanjutnya adalah membuat koneksi ke database.

`Connection` adalah objek yang merepresentasikan **hubungan langsung** dari aplikasi kita ke database. Kelas interface-nya berupa `java.sql.Connection`. Baca referensi-nya [disini](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/Connection.html).

### Membuat JDBC URL
Untuk bisa terkoneksi dengan database, dibutuhkan informasi seperti:
- Jenis database
- Host
- Port
- Nama database
- Username & Password

Semua info ini disatukan dalam yang namanya **JDBC URL**

#### Contoh format JDBC URL untuk MySQL
```text
jdbc:mysql://localhost:3306/mydb
```

Kalau menggunakan username dan password, biasanya digunakan seperti ini:
```java
String jdbcUrl = "jdbc:mysql://localhost:3306/mydb";
String username = "root";
String password = "";
```

### Membuat Koneksi dengan DriverManager
Untuk membuat koneksi, kita bisa pakai static method `DriverManager.getConnection(...)`.
Contohnya:
```java
import java.sql.Connection;
import java.sql.DriverManager;

Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
```
Semua proses ini bisa melempar **SQLException**, jadi sebaiknya ditangani dengan try-catch.

#### Unit Test: Membuat Koneksi
Contoh unit test untuk memastikan koneksi ke database berhasil dibuat:

```java
import java.sql.Connection;
import java.sql.SQLException;

@Test
void testConnection() {
    String jdbcUrl = "jdbc:mysql://localhost:3306/belajar_java_database";
    String username = "root";
    String password = "";

    try {
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        System.out.println("Sukses Terkoneksi ke Database");
        Assertions.assertNotNull(connection);
    } catch (SQLException exception) {
        Assertions.fail(exception);
    }
}    
```

### Menutup Koneksi Database
Setelah selesai digunakan, **Connection wajib ditutup**. Kalau tidak, koneksi akan tetap terbuka selama aplikasi hidup—dan ini bisa bahaya.

Contoh masalah:
- Database bisa kehabisan slot koneksi
- MySQL secara default hanya mengizinkan 151 koneksi aktif. Baca referensi berikut: [MySQL Default Max Connections](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_max_connections)

#### Unit Test: Menutup Koneksi

```java
import java.sql.Connection;
import java.sql.SQLException;

@Test
void testCloseConnection() {
    String jdbcUrl = "jdbc:mysql://localhost:3306/belajar_java_database";
    String username = "root";
    String password = "";

    try {
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        System.out.println("Sukses Terkoneksi ke Database");
        connection.close();
        System.out.println("Sukses menutup koneksi");
        Assertions.assertTrue(connection.isClosed());
    } catch (SQLException exception) {
        Assertions.fail(exception);
    }
}
```

## 3. DataSource

### Masalah dengan Connection
`Connection` di JDBC itu **resource yang sangat berat**.
Kalau kita buka-tutup koneksi setiap ada request, maka:
- Proses akan **lambat**, karena bikin koneksi baru itu mahal (butuh waktu + autentikasi)
- Memory aplikasi akan **boros**, apalagi kalau user-nya banyak

Jadi, **mengelola koneksi manual bukan solusi ideal** kalau kamu bangun aplikasi nyata.

### Connection Pool
**Connection Pool** adalah solusi paling umum dan efisien.

Cara kerjanya:
- Saat aplikasi pertama kali jalan, kita **buat beberapa koneksi ke database di awal** (misalnya 10 koneksi)
- Saat ada request, kita tinggal **pakai salah satu koneksi yang udah ada**
- Setelah selesai, **koneksi dikembalikan ke pool** dan bisa dipakai lagi
- Kalau semua koneksi sedang dipakai, request baru akan **menunggu sampai ada yang tersedia**

Manfaat:
- Koneksi hanya dibuat sekali di awal
- Performa jauh lebih baik
- Memory lebih stabil
- Penggunaan koneksi jadi lebih efisien dan terkontrol


### Konsep Connection Pool
![Connection Pool](https://miro.medium.com/v2/resize:fit:1100/format:webp/0*3C6pOIXlS0A_Mb9_.png)

### HikariCP - Connection Pool Cepat & Ringan
Bikin connection pool sendiri itu ribet dan rawan bug.
Daripada reinvent the wheel, mending pakai library connection pool yang udah terbukti cepat, ringan, dan stabil.

Nah, di antara banyak library, [HikariCP](https://github.com/brettwooldridge/HikariCP) adalah salah satu yang **paling populer dan direkomendasikan** di ekosistem Java, bahkan secara default digunakan oleh Spring Boot.

**Kelebihan HikariCP**:
- Sangat Cepat
- Ringan, minim thread overhead
- Konfigurasi Simpel dan fleksibel
- Cocok untuk aplikasi skala kecil sampai besar

### Kode: Konfigurasi HikariCP
1. Tambahkan dependency HikariCP ke `pom.xml`
```xml
<dependency>
   <groupId>com.zaxxer</groupId>
   <artifactId>HikariCP</artifactId>
   <version>6.3.0</version>
</dependency>
```

2. Buat konfigurasi HikariCP
```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionUtil {

  private static HikariDataSource dataSource;

  static {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName("com.mysql.cj.jdbc.Driver");
    config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
    config.setUsername("root");
    config.setPassword("");

    config.setMaximumPoolSize(10);
    config.setMinimumIdle(5);
    config.setIdleTimeout(60_000);
    config.setMaxLifetime(10 * 60_000);

    dataSource = new HikariDataSource(config);
  }

  public static HikariDataSource getDataSource() {
    return dataSource;
  }
}
```

3. Menggunakan Connection Pool
Unit Test: Menggunakan Connection Pool

```java
import com.zaxxer.hikari.HikariDataSource;
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;

@Test
void testConnectionPool() {
    try {
        HikariDataSource dataSource = ConnectionUtil.getDataSource();
        Connection connection = dataSource.getConnection();
        System.out.println("Sukses mengambil koneksi");

        connection.close();
        System.out.println("Sukses mengembalikan koneksi");

        dataSource.close();
        System.out.println("Sukses menutup pool");
    } catch (SQLException e) {
        Assertions.fail(e);
    }
}
```

## 4. Statement
Begitu kamu punya `Connection` ke database, langkah berikutnya pasti: **ngirim perintah SQL** — entah itu insert data, update, delete, atau query data.

Nah, di sinilah kita pakai `Statement`.

Statement adalah interface di JDBC (`java.sql.Statement`) yang digunakan untuk:
- Mengirim perintah SQL ke database
- Menerima hasil response dari database (jika ada)

Baca dokumentasi mengenai [java.sql.Statement](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/Statement.html).

Kita bisa bikin `Statement` dari method `createStatement()` milik `Connection` seperti ini:
```
Statement statement = connection.createStatement();
```

### Kode: Membuat Statement

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Test
void testCreateStatement() throws SQLException {

    Connection connection = ConnectionUtil.getDataSource().getConnection();
    Statement statement = connection.createStatement();

    statement.close();
    connection.close();
}
```

### Statement.executeUpdate(sql) - Menjalankan SQL tanpa Result
Method ini cocok untuk perintah SQL yang **tidak mengembalikan data**:
- `INSERT`
- `UPDATE`
- `DELETE`
- Bahkan `CREATE TABLE` atau perintah DDL lainnya

Metode ini mengembalikan `int` yang artinya berapa row yang terkena perubahan.

#### Kode: Membuat Table
```sql
CREATE TABLE customers
(
    id VARCHAR(100) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);
```

#### Kode: Mengirim perintah SQL

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Test
void testExecuteUpdate() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    Statement statement = connection.createStatement();
    String sql = """
            INSERT INTO customers(id, name, email) VALUES
            ('joko, 'Joko', 'joko@test.com');
            """;

    statement.executeUpdate(sql);

    statement.close();
    connection.close();
}

@Test
void testExecuteDelete() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    Statement statement = connection.createStatement();
    
    String sql = "DELETE * FROM customers";
    
    statement.executeUpdate(sql);
    
    statement.close();
    connection.close();
}
```

### Statement.executeQuery(sql) – Melakukan Query Data
Kalau kamu ingin mengambil data dari database (`SELECT`), bisa menggunakan:

**Kode: Melakukan Query Data**
```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Test
void testExecuteQuery() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    Statement statement = connection.createStatement();

    String sql = "SELECT * FROM customers";
    ResultSet resultSet = statement.executeQuery(sql);

    resultSet.close();
    statement.close();
    connection.close();
}
```
Hasilnya berupa `ResultSet`, yaitu objek yang bisa kamu gunakan untuk membaca baris demi baris dari hasil query. namun akan kita bahas nanti.

> [!CAUTION]
> **Hati-hati: SQL Injection**
> Kalau kamu pakai Statement dengan input dari user, kamu harus ekstra hati-hati. Contoh buruk:
> ```java
> String username = "admin";
> String password = "admin";
> String sql = "SELECT * FROM admin WHERE username = '" + username + "' AND password = '" + password + "'";
> ``` 
> Untuk solusinya akan kita bahas di bagian `PreparedStatement` nanti.


### SQL Injection
Dalam dunia nyata, kita gak mungkin hardcode semua perintah SQL di aplikasi. Biasanya, SQL akan dibentuk berdasarkan **input dari user**, misalnya dari form login, form input produk, dan sebagainya.

**SQL Injection** adalah teknik eksploitasi yang memanfaatkan celah dari penyusunan SQL yang tidak aman.

### Contoh Kasus SQL Injection
**Kode: Membuat Table Admin**
```sql
CREATE TABLE admin
(
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
)
```

**Kode: Membuat SQL dari Input User**
```sql
String username = "admin' --";
String password = "admin"

String sql = """
    SELECT * FROM admin WHERE username = '" + username + 
    "' AND password = '" + password + "'
    """; 
```

Perhatikan bagian -- di atas, itu adalah komentar di SQL. Artinya perintah setelahnya diabaikan. Sehingga password **tidak dicek sama sekali**, dan query tetap valid.

**Efeknya**? Bisa login tanpa password, drop table, bahkan eksekusi query liar lainnya.

**Solusi: Jangan Pakai Statement Biasa**
`Statement` biasa **tidak aman** untuk menerima input dari user. Gunakan `PreparedStatement` yang merupakan versi aman dari `Statement` yang otomatis akan escape dari input user, sehingga gak bisa disalahgunakan untuk SQL Injection.

Kita akan bahas `PreparedStatement` secara khusus di bagian setelah `ResultSet`.

## 5. ResultSet
Kalau sebelumnya pakai `Statement.executeQuery(sql)` untuk ngelakuin query ke database, maka hasilnya adalah sebuah objek `ResultSet`.

`ResultSet` ini ibarat kayak "hasil cetakan" dari database yang bisa kamu baca baris per baris. Baca dokumentasi mengenai ResultSet: [java.sql.ResultSet](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/ResultSet.html)

ResultSet itu mirip Iterator, kamu bisa pakai .next() untuk maju ke baris berikutnya. Awalnya, posisinya ada sebelum baris pertama, jadi kamu harus panggil .next() dulu sebelum bisa akses datanya. Setiap baris bisa dibaca menggunakan method getXxx(), sesuai tipe data di kolom.

### Kode: Iterasi di ResultSet

```java
Connection connection = ConnectionUtil.getDataSource().getConnection();
Statement statement = connection.createStatement();
String sql = "SELECT * FROM customers";
ResultSet resultSet = statement.executeQuery(sql);

while(resultSet.next()){
    // iterasi setiap data
}

resultSet.close();
statement.close();
```

### Mengambil Data Kolom di ResultSet
Kita bisa ambil data dengan dua cara:

1. Berdasarkan nama kolom
2. Berdasarkan urutan kolom (index dimulai dari 1, bukan 0)

Contoh method yang sering dipakai:
- getString("columnLabel)
- getInt("columnLabel")
- getBoolean("columnLabel")
- getDate("columnLabel") dan lain-lain

### Kode: Iterasi dan Mengambil Data di ResultSet

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Test
void testResultSetIteration() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    Statement statement = connection.createStatement();

    String sql = "SELECT * FROM customes";

    ResultSet resultSet = statement.executeQuery(sql);
    
    while(resultSet.next()){
        // iterasi setiap data
        String id = resultSet.getString("id"); // mengambil data id
        String name = resultSet.getString("name"); // mengambil data nama
        String email = resultSet.getString("email"); // mengambil data email

        System.out.println(String.join(", ", id, name, email));
    }
    
    resultSet.close();
    statement.close();
    connection.close();
}

```

## 6. PreparedStatement
`PreparedStatement` adalah turunan dari `Statement`, tapi dengan fitur tambahan yang **lebih aman** dan **lebih efisien**, terutama saat menangani input dari user.

Keunggulan utama:
- Menghindari SQL Injection
- Lebih mudah digunakan untuk input dinamis
- Bisa digunakan untuk query berulang secara efisien

Dokumentasi resminya: [java.sql.PreparedStatement](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/PreparedStatement.html)


### Membuat PreparedStatement
Berbeda dengan `Statement`, saat membuat `PreparedStatement`, kita langsung tentukan query SQL-nya. Biasanya kita pakai `?` (tanda tanya) sebagai placeholder untuk input dari user.

**Kode: Membuat PreparedStatement**

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;

Connection connection = ConnectionUtil.getDataSource().getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("SQL");

preparedStatement.close();
connection.close();
```

### Menerima Input dari User
Setelah bikin `PreparedStatement`, kita bisa masukin input user dengan method `setXxx(index, value)`.

**Kode: Menerima Input dari User**
```java
String username = "eko";
String password = "rahasia";

String sql = "INSERT INTO admin(username, password) VALUES (?, ?)";
PreparedStatement preparedStatement = connection.prepareStatement(sql);
preparedStatement.setString(1, username);
preparedStatement.setString(2, password);

preparedStatement.executeUpdate();
```

**Penjelasan**:
- `?` akan digantikan oleh input dari user
- `setString(1, ...)` berarti mengganti tanda tanya pertama
- `setString(2, ...)` mengganti tanda tanya kedua

Dengan ini, input dari user **tidak akan langsung dikonkat ke SQL**, sehingga jauh lebih aman.

**Kapan Harus pakai PreparedStatement?**
- Selalu gunakan PreparedStatement ketika ada input dari user
- Jangan pakai `Statement` jika query-nya dinamis
- Cocok juga buat aplikasi skala besar karena lebih efisien secara performa (database bisa caching query plan-nya)

## 7. Batch Process
Secara default, JDBC pakai pola **request dan response**.
Artinya:
- Setiap kali kirim 1 perintah SQL → harus nunggu database balas dulu.
- Kalau kita kirim ribuan perintah SQL (misalnya dari file Excel) satu-satu? **Lambat banget.**

**Solusi: Batch Process**

**Batch process** artinya kita kumpulkan _banyak_ perintah SQL dulu, lalu kirim sekaligus ke database.
Cocok untuk:
- Import data besar (ribuan atau jutaan record)
- Migrasi
- Sinkronisasi data

Manfaat:
- Lebih cepat dari kirim satu-satu
- Lebih hemat koneksi
- Mengurangi beban database dari sisi komunikasi

### Batch Process di JDBC
JDBC mendukung batch di dua cara:

**1. Statement**

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Test
void testBatchStatement() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    Statement statement = connection.createStatement();
    String sql = "INSERT INTO customers(id, name, email) VALUES ('eko', 'Eko', 'eko@test.com')";

    for(int i = 0; i< 1000;i++){
        statement.addBatch(sql);
    }

    statement.executeBatch();
    
    statement.close();
    connection.close();
}
```

**2. PreparedStatement**

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Test
void testBatchPreparedStatement() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    String sql = "INSERT INTO customers(id, name, email) VALUES (?, ?, ?)";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    for(int i = 0; i< 1000;i ++){
        preparedStatement.clearParameters();
        preparedStatement.setString(1,"joko");
        preparedStatement.setString(2,"Joko");
        preparedStatement.setString(3,"joko@test.com");
        preparedStatement.addBatch();
    }

    preparedStatement.executeBatch();
    
    preparedStatement.close();
    connection.close();
}
```
> [!WARNING]
> Batch itu disimpan dulu di memory sebelum dikirim ke database.
> Kalau terlalu banyak? Bisa bikin **OutOfMemoryError**

## 8. Auto Increment
Kadang setelah kita **insert data** ke tabel yang punya **primary key auto increment**, kita pengen tahu: "ID terakhir yang baru dimasukin itu berapa sih?"

Misalnya:
```
INSERT INTO customers(name) VALUES ('Budi');
```

Di MySQL, kita bisa pakai:
```
SELECT LAST_INSERT_ID();
```
Tapi ini ribet, karena harus query lagi, bikin `Statement` lagi, `ResultSet` lagi.

### Solusi JDBC: getGeneratedKeys()
Untungnya, **JDBC punya cara praktis** buat dapetin auto-generated key dari hasil `INSERT`, yaitu:
```java
ResultSet generatedKeys = statement.getGeneratedKeys();
```
### Step-By-Step
**1. Membuat Table**
```sql
CREATE TABLE comments
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    comment TEXT
)
```

**2. Insert Data + Ambil Auto Increment ID (Bisa menggunakan Statement ataupun PreparedStatement)**

```java

import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Test
void testAutoIncrement() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    String sql = "INSERT INTO comments(email, comment) VALUES (?, ?)";

    PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setString(1, "budi@test.com");
    preparedStatement.setString(2, "Hi");
    preparedStatement.executeUpdate();

    ResultSet resultSet = preparedStatement.getGeneratedKeys();

    if (resultSet.next()) {
        int generatedId = resultSet.getInt(1);
        System.out.println("Generated ID: " + generatedId);
    }
}
```
**Wajib Diperhatikan**
- Jangan lupa pakai `Statement.RETURN_GENERATED_KEYS`, baik saat buat Statement atau `PreparedStatement`.
- Kalau lupa, `getGeneratedKeys()` akan gagal atau kosong.

## 9. Date, Time, dan Timestamp
Di dunia database, kita sering banget berurusan dengan data waktu seperti:
- `DATE`: cuma tanggal (contoh: `2025-06-19`)
- `TIME`: cuma jam (contoh: `13:45:00`)
- `TIMESTAMP`: gabungan tanggal & waktu (contoh: `2025-06-19 13:45:00`)

Sedangkan di Java, kita dulu cuman punya
```java
java.util.Date
```
...dan itu gak cukup fleksibel untuk handle semua jenis waktu secara spesifik.

### Solusi: `java.sql` Time Classes
Makanya, JDBC menyediakan versi-versi khusus dari `Date`, yaitu: `java.sql.Date`, `java.sql.Time`, dan `java.sql.Timestamp`

Enaknya, JDBC secara otomatis akan mengkonversi data di database ke Java object yang sesuai.

**Kode: Membuat Table**
```sql
CREATE TABLE sample_time
(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    sample_date DATE,
    sample_time TIME,
    sample_timestamp TIMESTAMP
)
```

**Kode: Insert Data**

```java
import org.example.util.ConnectionUtil;

import java.sql.*;

@Test
void testDate() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    String sql = """
            INSERT INTO sample_time (sample_date, sample_time, sample_timestamp)
            VALUES (?, ?, ?)
            """;

    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
    preparedStatement.setTime(2, new Time(System.currentTimeMillis()));
    preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

    preparedStatement.executeUpdate();
    
    preparedStatement.close();
    connection.close();
}
```

**Kode: Select Data**

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Test
void testDateQuery() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    String sql = "SELECT * FROM sample_time";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ResultSet resultSet = preparedStatement.executeQuery();

    while (resultSet.next()) {
        System.out.println("ID: " + resultSet.getInt("id"));
        System.out.println("Date: " + resultSet.getDate("sample_date"));
        System.out.println("Time: " + resultSet.getTime("sample_time"));
        System.out.println("Timestamp: " + resultSet.getTimestamp("sample_timestamp"));
    }
    
    resultSet.close();
    preparedStatement.close();
    connection.close();
}
```

## 10. Database Transaction
Transaction itu sederhananya adalah **kumpulan perintah SQL** yang dieksekusi **sebagai satu kesatuan**.
Kalau satu perintah gagal, maka semua perintah lainnya ikut dibatalkan (rollback), supaya data tetap konsisten.

Bayangin aja kayak ATM:

- Transfer uang -> Kurangi saldo A dan Tambah saldo B
- Kalau salah satu gagal, dua-duanya harus dibatalkan

### Auto Commit
Secara default, JDBC menggunakan auto-commit = true, artinya:
- Setiap kali kirim SQL (misal INSERT, UPDATE), dia langsung di-commit otomatis.

Ini bisa bahaya kalau kamu sedang melakukan serangkaian proses yang harus jalan bareng (atomic).

### Cara Mengaktifkan Transaction Manual
Untuk menjalankan perintah SQL dalam transaksi manual, kamu harus:
- **Matikan auto commit**
- Jalankan semua perintah SQL
- Jika semua sukses: `commit()`
- Jika ada error: `rollback()`

**Kode: Database Transaction**

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.Prepared****Statement;
import java.sql.SQLException;

@Test
void testCommit() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    connection.setAutoCommit(false);

    // Proses transaksi disini
    String sql = "INSERT INTO comments (email, comments) VALUES (?, ?)";
    for (int i = 0; i < 10; i++) {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "udin@test.com");
        preparedStatement.setString(2, "Hi");
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    connection.commit();
    connection.close();
}
```

**Kode: Rollback Transaction**

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Test
void testRollback() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    connection.setAutoCommit(false);

    // Proses transaksi disini  
    String sql = "INSERT INTO comments (email, comments) VALUES (?, ?)";
    for (int i = 0; i < 10; i++) {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "udin@test.com");
        preparedStatement.setString(2, "Hi");
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    connection.rollback();
    connection.close();
}
```

## 10. MetaData
Kadang dalam pengembangan aplikasi, kita ingin mendapatkan **informasi tentang database, parameter, atau hasil query**. Informasi ini disebut **MetaData**.

JDBC menyediakan beberapa interface untuk mengakses informasi metadata:
- [DatabaseMetaData](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/DatabaseMetaData.html)
- [ParameterMetaData](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/ParameterMetaData.html)
- [ResultSetMetaData](https://docs.oracle.com/en/java/javase/15/docs/api/java.sql/java/sql/ResultSetMetaData.html)

### DatabaseMetaData
`DatabaseMetaData` adalah interface yang menyediakan informasi detail tentang database yang sedang terkoneksi, seperti:
- Nama database
- Versi database
- Nama-nama table
- Fitur-fitur yang didukung

Untuk mendapatkannya, kita cukup memanggil method `getMetaData()` dari object `Connection`.

**Kode: DatabaseMetaData**
```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Test
void testDatabaseMetaData() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    DatabaseMetaData databaseMetaData = connection.getMetaData();

    System.out.println(databaseMetaData.getDatabaseProductName());
    System.out.println(databaseMetaData.getDatabaseProductVersion());

    ResultSet resultSet = databaseMetaData.getTables("belajar_java_database", null, null, null);
    while (resultSet.next()) {
        String tableName = resultSet.getString("TABLE_NAME);
        System.out.println("Table Name" + tableName);
    }
    
    connection.close();
}
```

### ParameterMetaData
`ParameterMetaData` digunakan untuk mendapatkan **informasi tentang parameter yang digunakan dalam PreparedStatement**, seperti:
- Jumlah parameter
- Tipe data tiap parameter (jika didukung oleh driver)

Untuk mengaksesnya, gunakan method `getParameterMetaData()` dari object `PreparedStatement`.

> ![NOTE]
> **Tidak semua JDBC driver mendukung tipe data parameter secara detail.** 

**Kode: ParameterMetaData**

```java
import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Test
void testParameterMetaData() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    String sql = "INSERT INTO comments(email, comment) VALUES (?, ?)";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();

    System.out.println(parameterMetaData.getParameterCount());
    System.out.println(parameterMetaData.getParameterTypeName(1));
    
    preparedStatement.close();
    connection.close();
}
```

### ResultSetMetaData
`ResultSetMetaData` memberikan informasi tentang kolom-kolom di hasil query (`ResultSet`), misalnya:
- Jumlah kolom
- Nama kolom 
- Tipe data kolom
- Label alias (jika ada)

Kita bisa mengaksesnya melalui method `getMetaData()` dari object `ResultSet`.

**Kode: ResultSetMetaData**

```java
import org.example.util.ConnectionUtil;

import java.sql.*;

@Test
void testResultSetMetaData() throws SQLException {
    Connection connection = ConnectionUtil.getDataSource().getConnection();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM sample_time");

    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
        System.out.println("Name : " + resultSetMetaData.getColumnName(i));
        System.out.println("Type : " + resultSetMetaData.getColumnType(i));
        System.out.println("Type Name : " + resultSetMetaData.getColumnTypeName(i));

        if (resultSetMetaData.getColumnType(i) == Types.INTEGER) {
            System.out.println("INI INTEGER");
        }
    }

    resultSet.close();
    statement.close();
    connection.close();
}
```