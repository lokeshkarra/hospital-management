package com.hospital.dao;

import com.hospital.model.Invoice;
import com.hospital.model.InvoiceItem;
import com.hospital.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BillingDAO {

    /**
     * Saves an invoice and all its items to the database in a single transaction.
     * @param invoice The complete Invoice object to save.
     * @return true if the save was successful, false otherwise.
     */
    public boolean saveInvoice(Invoice invoice) {
        String sqlInvoice = "INSERT INTO invoices (patient_id, total_amount) VALUES (?, ?)";
        String sqlItem = "INSERT INTO invoice_items (invoice_id, category, description, amount) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert the main invoice record and get the generated ID
            try (PreparedStatement pstmtInvoice = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS)) {
                pstmtInvoice.setInt(1, invoice.getPatient().getPatientId());
                pstmtInvoice.setBigDecimal(2, invoice.getTotalAmount());
                pstmtInvoice.executeUpdate();

                try (ResultSet generatedKeys = pstmtInvoice.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        invoice.setInvoiceId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating invoice failed, no ID obtained.");
                    }
                }
            }

            // Insert each invoice item
            try (PreparedStatement pstmtItem = conn.prepareStatement(sqlItem)) {
                for (InvoiceItem item : invoice.getItems()) {
                    pstmtItem.setInt(1, invoice.getInvoiceId());
                    pstmtItem.setString(2, item.getCategory());
                    pstmtItem.setString(3, item.getDescription());
                    pstmtItem.setBigDecimal(4, item.getAmount());
                    pstmtItem.addBatch();
                }
                pstmtItem.executeBatch();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}