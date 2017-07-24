package com.codenation.repository;

import java.util.List;
import com.codenation.models.Products;
import org.springframework.data.repository.CrudRepository;

public interface ProductsRepository extends CrudRepository<Products, Long> {

    List<Products> findByName(String name);
    List<Products> findByIsValid(Boolean valid);
}