package com.codenation.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="orders_products")
public class OrderProducts {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long orderProductId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Products products;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId")
    private Orders orders;

    private int orderQuantity;
    private Double priceEach;
    private Boolean isValid;

    protected OrderProducts() {}

    public OrderProducts(Products products, Orders orders, int orderQuantity, Double price_each) {
        this.products = products;
        this.orders = orders;
        this.orderQuantity = orderQuantity;
        this.priceEach = price_each;
        this.isValid = true;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Double getPriceEach() {
        return priceEach;
    }

}
