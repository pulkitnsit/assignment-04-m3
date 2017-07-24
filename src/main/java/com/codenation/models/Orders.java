package com.codenation.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name="orders")
public class Orders {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long orderId;
    private Date date;
    private String status;
    private Boolean isValid;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    private Customers customers;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<OrderProducts> orderProducts;

    public Orders(Date date, String status, Customers customers) {
        this.date = date;
        this.status = status;
        this.isValid = true;
        this.customers = customers;
    }

    protected Orders() {}

    public long getOrderId() {
        return orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Set<OrderProducts> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProducts> orderProducts) {
        this.orderProducts = orderProducts;
    }

}
