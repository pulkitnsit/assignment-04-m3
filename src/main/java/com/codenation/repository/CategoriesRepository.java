package com.codenation.repository;

import java.util.List;

import com.codenation.models.Categories;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pulkit on 06/07/17.
 */
public interface CategoriesRepository extends CrudRepository<Categories, Long> {
    List<Categories> findByCategoryId(long id);
    List<Categories> findByDescription(String description);
    Categories findOneByDescription(String description);
}