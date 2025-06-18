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