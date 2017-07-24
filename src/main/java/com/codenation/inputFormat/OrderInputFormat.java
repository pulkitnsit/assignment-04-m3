package com.codenation.inputFormat;

import com.codenation.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pulkit on 10/07/17.
 */
public class OrderInputFormat {
    Long productId;
    int quantity;

    public Boolean checkValidations(ProductsRepository prodRepo) {
        if(prodRepo.findOne(this.productId) == null) return false;
        if(this.quantity < 1) return false;
        return true;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
