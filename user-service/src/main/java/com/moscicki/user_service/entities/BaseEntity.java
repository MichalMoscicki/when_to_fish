package com.moscicki.user_service.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@MappedSuperclass
@SQLDelete(sql = "UPDATE user SET delete_date = NOW() WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    private LocalDateTime createDate;
    private LocalDateTime  lastChangeDate;
    private LocalDateTime deleteDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastChangeDate = LocalDateTime.now();
    }

}
