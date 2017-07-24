package com.codenation.controller;
import com.codenation.models.Categories;
import com.codenation.repository.CategoriesRepository;
//import org.eclipse.jetty.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
//import com.codenation.repository.*;

@RestController
public class CategoriesController {

    @Autowired
    CategoriesRepository catRepo;

    private static final Logger log = LoggerFactory.getLogger(CategoriesController.class);

    @PostMapping(value = "/categories")
    public @ResponseBody Map<String, String> addNewCategory(@RequestBody Categories categories) {
        log.info(categories.toString());
        catRepo.save(categories);

        Map<String, String> returnVal = new HashMap<>();
        returnVal.put("status","SUCCESS");

        return returnVal;
    }
}
