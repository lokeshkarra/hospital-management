package com.hospital.model;

import java.math.BigDecimal;

public class InvoiceItem {
    private int itemId;
    private int invoiceId;
    private String category;
    private String description;
    private BigDecimal amount;

    public InvoiceItem(String category, String description, BigDecimal amount) {
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    // Getters and Setters...
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}