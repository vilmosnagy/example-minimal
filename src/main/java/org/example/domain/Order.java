package org.example.domain;

import javax.persistence.*;

/**
 * @author Vilmos Nagy  <vilmos.nagy@outlook.com>
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    Long id;

    String name;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;

    public Order(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
