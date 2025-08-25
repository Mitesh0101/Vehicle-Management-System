# 🚗 mVaahan: RTO Management System

mVaahan is a comprehensive vehicle and license management system designed to digitize and streamline the processes of a Regional Transport Office (RTO). Built with Java and backed by a robust PostgreSQL database, this project provides a full suite of features for both citizens and administrative officers.

---

## ✨ Core Features

-   **👤 User & Officer Roles:** Secure registration and login system with distinct permissions for regular users and RTO officers.
-   **🪪 Driving License Lifecycle:** Complete management of driving licenses, from application and issuance to renewal and status tracking.
-   **📝 Automated Examination System:** A fully-featured driving license examination module with a comprehensive question bank for various vehicle categories.
-   **🚗 Vehicle Registration:** Manages all vehicle details, ownership records, and tracks the expiry dates of critical documents like insurance and PUC.
-   **💳 E-Challan & Payments:** Functionality for issuing, tracking, and managing payments for traffic violations.
-   **🔐 Data Integrity & Logging:** Includes procedures for secure data handling and triggers for logging user deletions.

---

## 🛠️ Technology Stack

-   **Backend:** Java (JDK 22)
-   **UI Framework:** JavaFX (SDK 21.0.7)
-   **Database:** PostgreSQL
-   **Connectivity:** JDBC (Java Database Connectivity)
-   **Version Control:** Git

---

## 📂 Project Structure

The repository is organized to separate database logic from the application source code for clarity and maintainability.

```

mVaahan/
│
├── db/                      # Contains all database-related SQL scripts
│   ├── schema.sql           # The complete database schema (tables, functions, triggers)
│   └── seeds.sql            # Initial data for populating the database (test data, categories)
│
├── src/                     # Contains all Java source code
|   ├── main/
|   |   ├── config/          # Contains the configuration files
|   |   ├── controllers/     # Contains the controllers for the project fxml files
|   |   ├── model/           # Contains the entity models
|   |   ├── repository/      # Contains the JDBC code
|   |   ├── resources/       # Contains all the fxml files
|   |   ├── utils/           # Contains the utility classes
|   |   └── Main.java        # File to run
│
├── .gitignore               # Specifies files for Git to ignore
├── README.md                # You are here\!
└── mVaahan.iml              # IntelliJ IDEA project file (or equivalent for other IDEs)

````

---

## Prerequisites

Before you begin, ensure you have the following installed and downloaded:

1.  **Java Development Kit (JDK):** Version **22** is required.
2.  **PostgreSQL:** A running instance of the PostgreSQL database server.
3.  **PostgreSQL JDBC Driver:** Download the `.jar` file from the [official PostgreSQL website](https://jdbc.postgresql.org/download/).
4.  **JavaFX SDK:** Download the SDK **(21.0.7)** for your operating system and Java version from [GluonHQ](https://gluonhq.com/products/javafx/).

## 🗄️ Database Setup Guide

This project requires a PostgreSQL database. The following instructions use `psql`, the standard command-line interface for PostgreSQL.

### Step 1: Create the Database

First, connect to your PostgreSQL server and create a new, empty database for the project.

```sql
CREATE DATABASE mvaahan;
````

### Step 2: Build the Database Schema

Next, execute the `schema.sql` script. This file contains all the `CREATE TABLE` statements, functions, and triggers required to build the database structure from scratch.

```sh
# Navigate to your project directory in the terminal
psql -U your_postgres_username -d mvaahan -f db/schema.sql
```

### Step 3: Seed the Database with Initial Data

Finally, run the `seeds.sql` script. This will populate your new database with essential data, such as vehicle categories, exam questions, and a default officer account for testing purposes.

```sh
# Make sure you are still in your project directory
psql -U your_postgres_username -d mvaahan -f db/seeds.sql
```

Your database is now fully configured and ready to connect to the application.

-----

## 🚀 How to Run the Project

### Step 1: Clone the Repository

```sh
git clone https://github.com/Mitesh0101/mVaahan.git
cd mVaahan
```

### Step 2: Open in your Java IDE
Import the project as a new project in your preferred IDE, such as IntelliJ IDEA or Eclipse.

### Step 3: Configure Project Libraries (Crucial Step)
You must add the downloaded JavaFX and PostgreSQL JARs to your project's build path.
**In IntelliJ IDEA:**
-   Go to **File** -> **Project Structure...** (`Ctrl+Alt+Shift+S`).
-   Select **Libraries** from the left panel.
-   Click the **+** (Add) button and select **Java**.
-   Navigate to and select the **PostgreSQL JDBC driver `.jar` file** you downloaded.
-   Click **+** again, select **Java**, and navigate to the `lib` folder inside the **JavaFX SDK** you downloaded. Select all the `.jar` files.
-   Click **Apply** and then **OK**.

### Step 4: Configure Source & Resource Folders (Crucial Step)
To avoid a "Location is required" exception when loading FXML files, you must mark the resources directory as a Resource Root.

---

## Usage

- **User Registration:** Sign up as a citizen or officer, then log in.
- **Apply for License:** Fill out the application, take the online exam, and track status.
- **Register Vehicle:** Enter vehicle details and upload required documents.
- **Issue/View E-Challan:** Officers can issue challans; users can view and pay them.
- **Profile Management:** Update personal details and change password.

---

## Screenshots

> _Add screenshots of the login page, dashboard, license application, vehicle registration, and e-challan screens here._

---

## Contributing

Contributions are welcome! Please open issues or submit pull requests for improvements or bug fixes.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a pull request

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## Contact

For support or questions, please open an issue or contact the maintainer at [your-email@example.com].
