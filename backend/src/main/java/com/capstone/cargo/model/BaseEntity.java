package com.capstone.cargo.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="UPDATED_BY")
    private String updatedBy;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name="CREATION_DATE")
    private LocalDateTime createDate;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name="UPDATE_DATE")
    private LocalDateTime updatedDate;
}
