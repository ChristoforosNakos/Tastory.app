# Tastory 🍕

Διαδικτυακή εφαρμογή παραγγελίας φαγητού, όπου οι πελάτες περιηγούνται στα εστιατόρια, βλέπουν τα μενού τους και κάνουν παραγγελίες, ενώ οι ιδιοκτήτες εστιατορίων διαχειρίζονται τα προϊόντα τους και παρακολουθούν τις εισερχόμενες παραγγελίες. Υλοποιημένη με Spring Boot και server-side rendering (Thymeleaf).

Αναπτύχθηκε ως τελική εργασία για το **Coding Factory 10 (ΟΠΑ)**.

---

## Λειτουργίες

- **Authentication & Authorization** με Spring Security και τρεις ρόλους: `CUSTOMER`, `RESTAURANT_OWNER`, `ADMIN`.
- Οι **πελάτες** μπορούν να εγγραφούν, να συνδεθούν, να δουν τη λίστα εστιατορίων, να ανοίξουν το μενού ενός εστιατορίου, να κάνουν παραγγελίες και να δουν το ιστορικό των παραγγελιών τους με το σύνολο ανά παραγγελία.
- Οι **ιδιοκτήτες εστιατορίων** μπορούν να προσθέτουν, να επεξεργάζονται και να διαγράφουν προϊόντα, καθώς και να βλέπουν / αλλάζουν το status των παραγγελιών που γίνονται στα εστιατόριά τους.
- **Grid εστιατορίων** με ζωντανό φίλτρο αναζήτησης (client-side).
- Πλοήγηση και προστασία διαδρομών ανάλογα με τον ρόλο (τα στατικά αρχεία και οι δημόσιες σελίδες είναι ανοιχτά· οι ενέργειες owner/admin είναι περιορισμένες).
- Αυτόματα **αρχικά δεδομένα (seed data)** στην πρώτη εκκίνηση (χρήστης admin, ένας ιδιοκτήτης και 6 δείγματα εστιατορίων με προϊόντα).

---

## Τεχνολογίες

| Επίπεδο | Τεχνολογία |
|---------|------------|
| Γλώσσα | Java 21 (Amazon Corretto) |
| Framework | Spring Boot 4.1.0 (Spring MVC, Spring Data JPA, Spring Security) |
| View | Thymeleaf (Server-Side Rendering) + Thymeleaf Extras Spring Security |
| Persistence | Hibernate / JPA |
| Βάση δεδομένων | MySQL 8 |
| Build tool | Maven |
| Styling | Custom CSS (γραμματοσειρά Poppins, εικονίδια Font Awesome) |

---

## Αρχιτεκτονική

Η εφαρμογή ακολουθεί κλασική στρωματοποιημένη (layered) αρχιτεκτονική:

- **Controllers** – διαχειρίζονται τα HTTP requests και επιστρέφουν τα Thymeleaf views.
- **Service Layer** – επιχειρησιακή λογική (δημιουργία παραγγελίας, διαχείριση προϊόντων κ.λπ.).
- **Repositories** – interfaces Spring Data JPA για την πρόσβαση στα δεδομένα.
- **Domain Model / Entities** – `User`, `Restaurant`, `Product`, `Order`, `OrderItem`, καθώς και τα enums `Role` και `OrderStatus`.
- **DTOs** – χρησιμοποιούνται στις φόρμες εγγραφής και προϊόντος.
- **Security config** – η κλάση `SecurityConfig` ορίζει το filter chain, την κρυπτογράφηση κωδικών και τους κανόνες authorization.

---

## Προαπαιτούμενα

Βεβαιώσου ότι έχεις εγκατεστημένα:

- **JDK 21** (π.χ. Amazon Corretto 21)
- **Maven 3.9+** (ή το ενσωματωμένο Maven wrapper `./mvnw`)
- **MySQL 8** που τρέχει τοπικά στη θύρα `3306`

---

## Ρύθμιση βάσης δεδομένων

Η εφαρμογή συνδέεται σε μια βάση MySQL με όνομα `tastory_db` και έναν ειδικό χρήστη. Δημιούργησέ τα μία φορά, π.χ. από το MySQL shell:

```sql
CREATE DATABASE tastory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'tastory_user'@'localhost' IDENTIFIED BY 'tastory_pass';
GRANT ALL PRIVILEGES ON tastory_db.* TO 'tastory_user'@'localhost';
FLUSH PRIVILEGES;
```

Οι πίνακες δημιουργούνται αυτόματα από το Hibernate στην εκκίνηση (`spring.jpa.hibernate.ddl-auto=update`), οπότε δεν χρειάζεται να γράψεις DDL με το χέρι.

### Προαιρετικά: MySQL με Docker

Αντί για τοπική εγκατάσταση MySQL, μπορείς να τη σηκώσεις με Docker:

```bash
docker run --name tastory-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=tastory_db \
  -e MYSQL_USER=tastory_user \
  -e MYSQL_PASSWORD=tastory_pass \
  -p 3306:3306 -d mysql:8
```

---

## Ρυθμίσεις (Configuration)

Οι ρυθμίσεις σύνδεσης με τη βάση βρίσκονται στο `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tastory_db
spring.datasource.username=tastory_user
spring.datasource.password=tastory_pass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Αν το username / password της MySQL σου διαφέρουν, ενημέρωσε αντίστοιχα αυτές τις τιμές.

---

## Build & Εκτέλεση

Από τη ρίζα του project:

```bash
# 1. Build
mvn clean package        # ή: ./mvnw clean package

# 2. Εκτέλεση
mvn spring-boot:run      # ή: ./mvnw spring-boot:run
```

Εναλλακτικά, τρέξε το πακεταρισμένο JAR:

```bash
java -jar target/*.jar
```

Μπορείς επίσης να ανοίξεις το project στο **IntelliJ IDEA** και να τρέξεις κατευθείαν την κλάση `TastoryApplication`.

Μόλις ξεκινήσει, η εφαρμογή είναι διαθέσιμη στο:

```
http://localhost:8080
```

Στην **πρώτη** εκκίνηση εισάγονται αυτόματα τα αρχικά δεδομένα (admin, owner και 6 εστιατόρια). Στις επόμενες εκκινήσεις παραλείπονται.

---

## Προεπιλεγμένοι λογαριασμοί

Ο seeder δημιουργεί τους παρακάτω χρήστες (οι κωδικοί αποθηκεύονται κρυπτογραφημένοι με BCrypt στη βάση):

| Ρόλος | Email | Κωδικός |
|-------|-------|---------|
| Admin | `admin@tastory.gr` | `admin123` |
| Ιδιοκτήτης εστιατορίου | `owner@tastory.gr` | `owner123` |

Νέοι πελάτες μπορούν να δημιουργηθούν ανά πάσα στιγμή από τη σελίδα **Εγγραφής** (`/register`).

---

## Χρήση

1. Άνοιξε το `http://localhost:8080` και κάνε **εγγραφή** ως πελάτης (ή σύνδεση με τον έτοιμο λογαριασμό owner).
2. Πήγαινε στα **Εστιατόρια** για να δεις το grid.
3. Άνοιξε ένα εστιατόριο για να δεις το **μενού**, βάλε ποσότητες και κάνε **παραγγελία**.
4. Δες τις **Παραγγελίες μου** για το ιστορικό και τα σύνολα.
5. Συνδέσου ως **owner** για να προσθέσεις/επεξεργαστείς προϊόντα και να αλλάξεις το status των εισερχόμενων παραγγελιών από τη σελίδα παραγγελιών του εστιατορίου.

---

## Δομή του project (ενδεικτική)

```
src/main/java/gr/tastory/aueb/
├── TastoryApplication.java
├── config/          # SecurityConfig, DataSeeder
├── model/           # User, Restaurant, Product, Order, OrderItem, Role, OrderStatus
├── repository/      # UserRepo, RestaurantRepo, ProductRepo, OrderRepo, OrderItemRepo
├── service/         # επιχειρησιακή λογική
├── controller/      # MVC controllers
└── dto/             # RegisterDto, ProductDto

src/main/resources/
├── templates/       # Thymeleaf views (+ fragments/navbar.html)
├── static/css/      # style.css
└── application.properties
```

---

## Συντάκτης

Αναπτύχθηκε για το Coding Factory 10 — Οικονομικό Πανεπιστήμιο Αθηνών (ΟΠΑ).
