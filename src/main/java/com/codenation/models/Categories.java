package com.codenation.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="categories")
public class Categories {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long categoryId;
    private String description;
    private Boolean isValid;
    //private Set<Products> products;

    @OneToMany(mappedBy = "categories",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Products> products;
    public Set<Products> getProducts() {
        return products;
    }

    protected Categories() {}

    public Categories(String description) {
        this.description = description;
        this.isValid = true;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //public void setProducts(Set<Products> products) {
        //this.products = products;
    //}

    @Override
    public String toString() {
        return "Categories{" +
                "categoryId=" + categoryId +
                ", description='" + description + '\'' +
                ", isValid=" + isValid +
                ", products=" + products +
                '}';
    }
}
