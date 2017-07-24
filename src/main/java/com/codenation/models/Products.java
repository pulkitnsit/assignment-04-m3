package com.codenation.models;
import com.codenation.inputFormat.ProductInputFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Set;

@Entity
@Table(name="products")
public class Products {

    private String productCode;
    private String name;
    private String description;
    private Integer productQuantity;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long productId;
    private Double buyPrice;
    private Double sellPrice;
    private Boolean isValid;
    //private Categories categories;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Categories categories;
    public Categories getCategories() {
        return categories;
    }

    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<OrderProducts> orderProducts;

    protected Products() {}

    public Products(String productCode, String name, String description, Integer productQuantity, Double buyPrice, Double sellPrice, Categories categories) {
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.productQuantity = productQuantity;
        this.buyPrice = buyPrice;
        this.categories = categories;
        this.sellPrice = sellPrice;
        this.isValid = true;
    }

    public Products(ProductInputFormat prodInpObj, Categories category) {
        this.productCode = null;
        this.name = prodInpObj.getName();
        this.description = prodInpObj.getDescription();
        this.productQuantity = prodInpObj.getQuantity();
        this.buyPrice = prodInpObj.getBuyPrice();
        this.categories = category;
        this.sellPrice = prodInpObj.getSellPrice();
        this.isValid = true;
    }

    public void updateProduct(ProductInputFormat prodObj, Categories category) {
        if(prodObj.getName() != null) this.name = prodObj.getName();
        if(prodObj.getDescription() != null) this.description = prodObj.getDescription();
        if(prodObj.getQuantity() != null) this.productQuantity = prodObj.getQuantity();
        if(prodObj.getBuyPrice() != null) this.buyPrice = prodObj.getBuyPrice();
        if(prodObj.getCategory() != null) this.categories = category;
        if(prodObj.getSellPrice() != null) this.sellPrice = prodObj.getSellPrice();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Set<OrderProducts> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProducts> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productCode='" + productCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productQuantity=" + productQuantity +
                ", productId=" + productId +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", isValid=" + isValid +
                ", categories=" + categories +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
