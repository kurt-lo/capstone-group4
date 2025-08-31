package com.capstone.cargo.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

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

    @Column(name="CREATION_DATE")
    private LocalDateTime createDate;

    @Column(name="UPDATE_DATE")
    private LocalDateTime updatedDate;
}
