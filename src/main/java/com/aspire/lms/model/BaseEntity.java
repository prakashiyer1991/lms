package com.aspire.lms.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            nullable = false,
            updatable = false,
            name = "created_at"
    )
    @CreatedDate
    private LocalDateTime createdAt;

    @Value("system")
    @CreatedBy
    @Column(
            nullable = false,
            updatable = false,
            name = "created_by"
    )
    private String createdBy;
    @Column(
            nullable = false,
            name = "updated_at"
    )
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @LastModifiedBy
    @Value("system")
    @Column(
            nullable = false,
            name = "last_modified_by"
    )
    private String lastModifiedBy;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (StringUtils.isBlank(this.lastModifiedBy)) {
            this.lastModifiedBy = "system";
        }

    }

    @PrePersist
    public void setAuditable() {
        LocalDateTime date = LocalDateTime.now();
        this.updatedAt = date;
        this.createdAt = date;
        if (StringUtils.isBlank(this.createdBy)) {
            this.createdBy = "system";
            this.lastModifiedBy = "system";
        }

        if (StringUtils.isBlank(this.lastModifiedBy)) {
            this.lastModifiedBy = "system";
        }

    }

}
