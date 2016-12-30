package org.example.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Customer {

  @Id
  Long id;

  String name;

  String notes;

  @Version
  Long version;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  Set<Order> orders;

  public Customer(String name) {
    this.name = name;
  }

  public Customer() {
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

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }

}
