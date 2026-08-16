package com.trier.trier_report.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_type_id", nullable = false)
    private Long accountTypeId;

    @Column(name = "currency_id", nullable = false)
    private Long currencyId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "archived", nullable = false)
    private boolean archived;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Account() {
    }

    public Account(Long userId, Long accountTypeId, Long currencyId, String name) {
        this.userId = userId;
        this.accountTypeId = accountTypeId;
        this.name = name;
        this.currencyId = currencyId;
        this.archived = false;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Long accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", userId=" + userId + ", accountTypeId=" + accountTypeId + ", name='" + name + '\'' + ", currencyId='" + currencyId + '\'' + ", archived=" + archived + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
