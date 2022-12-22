package com.dramanedev.buildrestservicesspring.model;

import java.util.Objects;

import com.dramanedev.buildrestservicesspring.constants.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CUSTOMER_ORDER")
public class OrderEntity {
    private @Id @GeneratedValue Long id;
    private String description;
    private Status status;

    OrderEntity() {}

    public OrderEntity(String description, Status status) {
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEntity orderEntity)) return false;

        return Objects.equals(this.id, orderEntity.id)
                && Objects.equals(this.description, orderEntity.description)
                && this.status == orderEntity.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.description, this.status);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id + ", description='" + this.description + '\'' + ", status=" + this.status + "}";
    }
}
