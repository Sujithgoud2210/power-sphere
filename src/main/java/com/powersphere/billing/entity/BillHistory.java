package com.powersphere.billing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Entity capturing the audit trail of status changes and actions performed on a bill.
 * Each record documents what changed, when, and by whom.
 */
@Entity
@Table(name = "bill_histories")
public class BillHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "previous_status", length = 20)
    private String previousStatus;

    @Column(name = "new_status", length = 20)
    private String newStatus;

    @Column(name = "changed_by", length = 255)
    private String changedBy;

    @Column(name = "change_description", length = 1000)
    private String changeDescription;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now();
    }

    // --- Builder ---

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final BillHistory history = new BillHistory();

        public Builder action(String action) { history.action = action; return this; }
        public Builder previousStatus(String previousStatus) { history.previousStatus = previousStatus; return this; }
        public Builder newStatus(String newStatus) { history.newStatus = newStatus; return this; }
        public Builder changedBy(String changedBy) { history.changedBy = changedBy; return this; }
        public Builder changeDescription(String changeDescription) { history.changeDescription = changeDescription; return this; }

        public BillHistory build() {
            Objects.requireNonNull(history.action, "action must not be null");
            return history;
        }
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public String getChangeDescription() { return changeDescription; }
    public void setChangeDescription(String changeDescription) { this.changeDescription = changeDescription; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillHistory that = (BillHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BillHistory.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("action='" + action + "'")
            .add("newStatus='" + newStatus + "'")
            .add("changedAt=" + changedAt)
            .toString();
    }
}
