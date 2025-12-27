package com.matchmycoffee.model.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@EqualsAndHashCode
@Setter
@Getter
@ToString
public abstract class AbstractModel {

    private String uuid;

    private void ensureUuid() {
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
        }
    }

    @PrePersist
    public void prePersist() {
        ensureUuid();
    }
}
