package com.codenation.outputFormat;

/**
 * Created by pulkit on 08/07/17.
 */

import com.codenation.models.Products;

public class ProductOutputFormat {

    private long id;
    private String name;
    private String categoryName;
    private String description;
    private Double buyPrice;
    private Double sellPrice;
    private Integer quantity;

    protected ProductOutputFormat() {}

    public ProductOutputFormat(Products product) {
        this.id = product.getProductId();
        this.name = product.getName();
        this.categoryName = product.getCategories().getDescription();
        this.description = product.getDescription();
        this.buyPrice = product.getBuyPrice();
        this.sellPrice = product.getSellPrice();
        this.quantity = product.getProductQuantity();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductOutputFormat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", quantity=" + quantity +
                '}';
    }
}
