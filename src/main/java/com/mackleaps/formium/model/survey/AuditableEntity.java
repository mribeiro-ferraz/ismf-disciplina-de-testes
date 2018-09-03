package com.mackleaps.formium.model.survey;

import com.mackleaps.formium.model.auth.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity with the fields of a auditable object
 * In other words, this entity will not be used directly (hence the use of the <code>abstract</code> keyword),
 * but rather as a concentration of this capacity to retain the meta data concerning the auditing of the fields
 * */

@EntityListeners({AuditingEntityListener.class})
@MappedSuperclass
public abstract class AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastEditedDate;

    @CreatedBy
    @JoinColumn(updatable = false, nullable = false)
    @ManyToOne
    private User createdBy;

    @LastModifiedBy
    @JoinColumn(nullable = false)
    @ManyToOne
    private User lastModifiedBy;

    @java.beans.ConstructorProperties({"id", "createdDate", "lastEditedDate", "createdBy", "lastModifiedBy"})
    public AuditableEntity(Long id, LocalDateTime createdDate, LocalDateTime lastEditedDate, User createdBy, User lastModifiedBy) {
        this.id = id;
        this.createdDate = createdDate;
        this.lastEditedDate = lastEditedDate;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public AuditableEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public LocalDateTime getLastEditedDate() {
        return this.lastEditedDate;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public User getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastEditedDate(LocalDateTime lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
