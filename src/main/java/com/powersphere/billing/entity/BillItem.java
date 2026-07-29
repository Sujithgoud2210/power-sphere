package com.powersphere.billing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Entity representing individual line items on a bill. Each item captures a
 * specific charge component such as energy charge, fixed charge, tax, discount,
 * or late fee with its description and amount.
 */
@Entity
@Table(name = "bill_items")
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(name = "item_type", nullable = false, length = 50)
    private String itemType;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "quantity", precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "rate", precision = 12, scale = 4)
    private BigDecimal rate;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "sequence", nullable = false)
    private int sequence;

    // --- Constructors ---

    public BillItem() {}

    public BillItem(String itemType, String description, BigDecimal amount, int sequence) {
        this.itemType = itemType;
        this.description = description;
        this.amount = amount;
        this.sequence = sequence;
    }

    public BillItem(String itemType, String description, BigDecimal quantity, BigDecimal rate, BigDecimal amount, int sequence) {
        this.itemType = itemType;
        this.description = description;
        this.quantity = quantity;
        this.rate = rate;
        this.amount = amount;
        this.sequence = sequence;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public int getSequence() { return sequence; }
    public void setSequence(int sequence) { this.sequence = sequence; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItem billItem = (BillItem) o;
        return Objects.equals(id, billItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BillItem.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("itemType='" + itemType + "'")
            .add("amount=" + amount)
            .toString();
    }
}
