package com.hospital.dao;

import com.hospital.model.Patient; // Make sure this import is present at the top
import java.util.ArrayList;        // Make sure this import is present
import java.util.List;           // Make sure this import is present
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
	    // MODIFIED SQL
	    String sqlInvoice = "INSERT INTO invoices (patient_id, total_amount, payment_method, status) VALUES (?, ?, ?, 'PAID')";
	    String sqlItem = "INSERT INTO invoice_items (invoice_id, category, description, amount) VALUES (?, ?, ?, ?)";
	    Connection conn = null;

	    try {
	        conn = DatabaseConnection.getConnection();
	        conn.setAutoCommit(false); // Start transaction

	        try (PreparedStatement pstmtInvoice = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS)) {
	            pstmtInvoice.setInt(1, invoice.getPatient().getPatientId());
	            pstmtInvoice.setBigDecimal(2, invoice.getTotalAmount());
	            pstmtInvoice.setString(3, invoice.getPaymentMethod()); // NEW PARAMETER
	            pstmtInvoice.executeUpdate();
	            
	            // ... rest of the method is the same ...
	            try (ResultSet generatedKeys = pstmtInvoice.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    invoice.setInvoiceId(generatedKeys.getInt(1));
	                } else {
	                    throw new SQLException("Creating invoice failed, no ID obtained.");
	                }
	            }
	        }

	        // ... rest of the method is the same ...
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

	        conn.commit();
	        return true;

	    } catch (SQLException e) {
	        // ... rest of the method is the same ...
	        e.printStackTrace();
	        if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
	        return false;
	    } finally {
	        // ... rest of the method is the same ...
	        if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
	    }
	}
    
    /**
     * Fetches a list of invoices, optionally filtered by patient ID.
     * If patientId is 0, it fetches all invoices.
     * @param patientId The ID of the patient to search for, or 0 to get all invoices.
     * @return A list of Invoice objects.
     */
    public List<Invoice> getInvoiceHistory(int patientId) {
        List<Invoice> invoiceHistory = new ArrayList<>();
        // Base SQL query with JOINs to get patient information
        String baseSql = """
            SELECT inv.*, p.patient_id, p.first_name, p.last_name
            FROM invoices inv
            JOIN patients p ON inv.patient_id = p.patient_id
        """;
        String finalSql = (patientId > 0)
            ? baseSql + " WHERE inv.patient_id = ? ORDER BY inv.invoice_date DESC"
            : baseSql + " ORDER BY inv.invoice_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(finalSql)) {

            if (patientId > 0) {
                pstmt.setInt(1, patientId);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoice_id"));
                invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
                invoice.setInvoiceDate(rs.getTimestamp("invoice_date"));
                invoice.setStatus(rs.getString("status"));

                // Create and set the associated Patient object
                Patient patient = new Patient();
                patient.setPatientId(rs.getInt("patient_id"));
                patient.setFirstName(rs.getString("first_name"));
                patient.setLastName(rs.getString("last_name"));
                invoice.setPatient(patient);

                invoiceHistory.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceHistory;
    }

}