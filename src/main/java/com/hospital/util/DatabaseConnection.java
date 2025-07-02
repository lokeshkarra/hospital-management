package com.hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:derby:hospitaldb;create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    static {
        try {
            Class.forName(DRIVER);
            //setupDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("Derby driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * This method handles the entire database setup. It first tries to drop
     * old tables to ensure a clean slate, then creates the new, consolidated schema.
     * This is ideal for development.
     */
    // ... inside the DatabaseConnection class ...

    private static void setupDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // --- Step 1: Drop old tables in the CORRECT order to prevent conflicts ---
            // Drop tables with foreign keys first.
            System.out.println("Attempting to clean up old tables...");
            try { stmt.executeUpdate("DROP TABLE invoice_items"); System.out.println("...dropped invoice_items.");} catch (SQLException e) { /* ignore if not found */ }
            try { stmt.executeUpdate("DROP TABLE appointments"); System.out.println("...dropped appointments.");} catch (SQLException e) { /* ignore if not found */ }
            
            // Now drop the tables they referred to.
            try { stmt.executeUpdate("DROP TABLE invoices"); System.out.println("...dropped invoices.");} catch (SQLException e) { /* ignore if not found */ }
            try { stmt.executeUpdate("DROP TABLE patients"); System.out.println("...dropped patients.");} catch (SQLException e) { /* ignore if not found */ }
            try { stmt.executeUpdate("DROP TABLE users"); System.out.println("...dropped users.");} catch (SQLException e) { /* ignore if not found */ }
            // The 'staff' table is obsolete but we can leave the drop statement for safety.
            try { stmt.executeUpdate("DROP TABLE staff"); System.out.println("...dropped staff.");} catch (SQLException e) { /* ignore if not found */ }
            System.out.println("Cleanup complete.");


            // --- Step 2: Create the new, consolidated USERS table ---
            // This table now holds all information for staff members.
            String createUserTable = """
                CREATE TABLE users (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                    user_id VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    first_name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) NOT NULL,
                    role VARCHAR(50) NOT NULL,
                    specialization VARCHAR(100),
                    email VARCHAR(100) UNIQUE,
                    phone VARCHAR(20),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL
                )
            """;
            stmt.executeUpdate(createUserTable);
            System.out.println("SUCCESS: Consolidated 'users' table created.");

            // --- Step 3: Insert sample user data into the new table ---
            // Note: Passwords should be hashed in a real application.
            stmt.executeUpdate("INSERT INTO users (user_id, password, first_name, last_name, role, email, phone) VALUES ('admin123', 'Admin@123456', 'System', 'Admin', 'ADMIN', 'admin@hospital.com', '555-0100')");
            stmt.executeUpdate("INSERT INTO users (user_id, password, first_name, last_name, role, specialization, email, phone) VALUES ('dr.jones', 'password123', 'Indiana', 'Jones', 'DOCTOR', 'Cardiology', 'indy.jones@hospital.com', '555-0101')");
            System.out.println("SUCCESS: Sample users inserted.");


            // --- Step 4: Create the dependent tables (Patients and Appointments) ---
            createDependentTables(stmt);

        } catch (SQLException e) {
            // This will catch errors during the initial connection or statement creation.
            System.err.println("A critical error occurred during database setup.");
            e.printStackTrace();
        }
    }

    

    /**
     * Helper method to create tables that have foreign key dependencies.
     * @param stmt The active SQL statement object.
     * @throws SQLException
     */
    private static void createDependentTables(Statement stmt) throws SQLException {
        // --- PATIENTS TABLE ---
        String createPatientTable = """
            CREATE TABLE patients (
                patient_id INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
                first_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                email VARCHAR(100),
                phone VARCHAR(20) NOT NULL,
                address VARCHAR(255),
                date_of_birth DATE,
                gender VARCHAR(10),
                blood_group VARCHAR(5),
                emergency_contact VARCHAR(20),
                registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                status VARCHAR(20) DEFAULT 'ACTIVE'
            )
        """;
        stmt.executeUpdate(createPatientTable);
        System.out.println("SUCCESS: 'patients' table created.");

        // Insert sample patients
        stmt.executeUpdate("INSERT INTO patients (first_name, last_name, email, phone, date_of_birth, gender) VALUES ('John', 'Doe', 'john.doe@email.com', '9876543210', '1990-05-15', 'Male')");
        stmt.executeUpdate("INSERT INTO patients (first_name, last_name, email, phone, date_of_birth, gender) VALUES ('Jane', 'Smith', 'jane.smith@email.com', '9876543212', '1985-08-22', 'Female')");
        System.out.println("SUCCESS: Sample patients inserted.");

     // --- INVOICES TABLE ---
        // Stores the main record for each bill.
     // --- INVOICES TABLE (MODIFIED) ---
        String createInvoicesTable = """
            CREATE TABLE invoices (
                invoice_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                patient_id INT NOT NULL,
                total_amount DECIMAL(10, 2) NOT NULL,
                invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                status VARCHAR(20) DEFAULT 'PAID' NOT NULL, -- Changed default to PAID
                payment_method VARCHAR(50), -- NEW COLUMN
                CONSTRAINT fk_invoice_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
            )
        """;
        stmt.executeUpdate(createInvoicesTable);
        System.out.println("SUCCESS: 'invoices' table created.");

        // --- INVOICE_ITEMS TABLE ---
        // Stores each line item (charge) for a specific invoice.
        String createInvoiceItemsTable = """
            CREATE TABLE invoice_items (
                item_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                invoice_id INT NOT NULL,
                category VARCHAR(50) NOT NULL, -- 'Room Charges', 'Pharmacy', 'Diagnostics'
                description VARCHAR(255) NOT NULL,
                amount DECIMAL(10, 2) NOT NULL,
                CONSTRAINT fk_item_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
            )
        """;
        stmt.executeUpdate(createInvoiceItemsTable);
        System.out.println("SUCCESS: 'invoice_items' table created.");

     
        // --- APPOINTMENTS TABLE ---
        // This table now correctly references the new 'users' table for the doctor.
        String createAppointmentTable = """
            CREATE TABLE appointments (
                appointment_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                patient_id INT NOT NULL,
                doctor_id INT NOT NULL,
                appointment_date TIMESTAMP NOT NULL,
                reason VARCHAR(255),
                notes CLOB,
                status VARCHAR(50) DEFAULT 'SCHEDULED' NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
                CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES users(id) 
            )
        """;
        stmt.executeUpdate(createAppointmentTable);
        System.out.println("SUCCESS: 'appointments' table created with correct foreign keys.");

        // Sample appointment: patient_id=1, doctor_id=2 (Dr. Jones from the users table)
        stmt.executeUpdate("INSERT INTO appointments (patient_id, doctor_id, appointment_date, reason) VALUES (1, 2, '2024-10-26 10:00:00', 'Regular checkup')");
        System.out.println("SUCCESS: Sample appointment inserted.");
    }
    
    /**
     * Shuts down the Derby database.
     */
    public static void closeConnection() {
        try {
            // The shutdown=true attribute is the standard way to shut down Derby.
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            // A SQLException with SQLState 08006 is expected on a successful shutdown.
            // We check for this state to avoid printing an unnecessary error message.
            if (!e.getSQLState().equals("08006")) {
                System.err.println("Error during Derby shutdown.");
                e.printStackTrace();
            }
        }
    }
}