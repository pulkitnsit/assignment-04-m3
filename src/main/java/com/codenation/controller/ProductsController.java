package com.codenation.controller;
import com.codenation.outputFormat.ProductOutputFormat;
import com.codenation.models.Categories;
import com.codenation.models.Products;
//import com.codenation.repository.ProductsRepository;
import com.codenation.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codenation.inputFormat.ProductInputFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
//import com.codenation.repository.*;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    ProductsRepository prodRepo;

    @Autowired
    CategoriesRepository catRepo;

    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);

    @PostMapping(value = "")
    public @ResponseBody ResponseEntity<Map>  addNewProduct(@RequestBody ProductInputFormat prodInpObj) {

        log.info(prodInpObj.getCategory());
        if(!prodInpObj.checkValidations()) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("status", "FAILURE");
            mp.put("reason", "Invalid Values");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }
        String catDes = prodInpObj.getCategory();
        List<Categories> catList = catRepo.findByDescription(catDes);
        Categories category;
        if (catList.isEmpty())
            category = new Categories(catDes);
        else
            category = catList.get(0);
        Products product = new Products(prodInpObj, category);
        Map<String, Object> mp = new HashMap<>();
        prodRepo.save(product);
        mp.put("id", product.getProductId());

        return new ResponseEntity<>(mp, HttpStatus.CREATED);
    }

    @GetMapping(value = "")
    public @ResponseBody ResponseEntity<Map>  getAllProducts() {
        Map<String,Object> mp = new HashMap<>();
        List<Products> prodList = prodRepo.findByIsValid(true);
        List<ProductOutputFormat> prodOutList = new ArrayList<>();

        for(int i=0;i<prodList.size();++i)
            prodOutList.add(new ProductOutputFormat(prodList.get(i)));
        mp.put("data",prodOutList);
        return new ResponseEntity<>(mp, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Map> getProduct(@PathVariable long id) {
        log.info("Checking get ");
        Map<String, Object> mp = new HashMap<>();
        log.info("id is " + id);
        if(prodRepo.exists(id)) {
            Products prodObj = prodRepo.findOne(id);
            if(prodObj.getValid()) {
                ProductOutputFormat prodFormatObj = new ProductOutputFormat(prodObj);
                //Map<String, Object> mp = new HashMap<>();
                //mp.put("status", "SUCCESS");
                mp.put("data", prodFormatObj);
                return new ResponseEntity<>(mp, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Map> updateProduct(@PathVariable long id, @RequestBody ProductInputFormat prodInpObj) {
        Map<String, Object> mp = new HashMap<>();
        log.info("id is " + id);
        if(!prodInpObj.checkValidations()) {
            mp.put("status", "FAILURE");
            mp.put("reason", "Invalid Values");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }
        if(prodRepo.exists(id)) {
            Products product = prodRepo.findOne(id);
            String catDes = prodInpObj.getCategory();
            List<Categories> catList = catRepo.findByDescription(catDes);
            Categories category;
            if (catList.isEmpty())
                category = new Categories(catDes);
            else
                category = catList.get(0);

            product.updateProduct(prodInpObj,category);
            prodRepo.save(product);
            mp.put("status","success");
            return new ResponseEntity<>(mp,HttpStatus.OK);
        }
        else {
            mp.put("status","failure");
            mp.put("reason","Product does not exist");
            return new ResponseEntity<>(mp,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Map> deleteProduct(@PathVariable long id) {
    Map<String, Object> mp = new HashMap<>();
    log.info("id is " + id);
        if(prodRepo.exists(id)) {
            Products prodObj = prodRepo.findOne(id);
            if(prodObj.getValid()) {
                prodObj.setValid(false);
                prodRepo.save(prodObj);

                mp.put("id",prodObj.getProductId());
                return new ResponseEntity<Map>(mp,HttpStatus.OK);
            }
            else
                mp.put("status","failure");
                mp.put("reason","Product already deleted");
                return new ResponseEntity<Map>(mp,HttpStatus.NOT_FOUND);
        }
        else {
            mp.put("status", "FAILURE");
            mp.put("reason", "Product does not exist");
            return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
        }
    }
}
