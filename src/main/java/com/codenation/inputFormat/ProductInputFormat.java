package com.codenation.inputFormat;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by pulkit on 08/07/17.
 */
public class ProductInputFormat {

    private String name;
    private String category;
    private String description;
    private Double buyPrice;
    private Double sellPrice;
    private Integer quantity;
    //private static final Logger log = LoggerFactory.getLogger(ProductInputFormat.class);

    protected ProductInputFormat() {}
/*
    public ProductInputFormat(String name, String category, String description, double buyPrice, double sellPrice, long quantity) {
        log.info("Correct called");
        this.name = name;
        this.category = category;
        this.description = description;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
    }
    */

    public Boolean checkValidations() {
        if(this.buyPrice != null && this.buyPrice<0) return false;
        if(this.sellPrice != null && this.sellPrice<0) return false;
        if(this.quantity != null && this.quantity<0) return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
