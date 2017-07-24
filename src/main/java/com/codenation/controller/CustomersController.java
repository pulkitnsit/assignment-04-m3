package com.codenation.controller;

import com.codenation.models.Customers;
import com.codenation.repository.CustomersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pulkit on 08/07/17.
 */

@RestController
@RequestMapping("/api/users")
public class CustomersController {

    @Autowired
    CustomersRepository custRepo;

    private static final Logger log = LoggerFactory.getLogger(CustomersController.class);

    @PostMapping(value = "")
    public @ResponseBody ResponseEntity<Map> addUser (@RequestBody Customers custObj) {
        custRepo.save(custObj);
        Map<String,Object> mp = new HashMap<>();
        mp.put("id",custObj.getCustomerId());
        return new ResponseEntity<>(mp, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Map> deleteUsers(@PathVariable long id) {
        Map<String, Object> mp = new HashMap<>();
        log.info("id is " + id);
        if(custRepo.exists(id)) {
            Customers custObj = custRepo.findOne(id);
            if(custObj.getValid()) {
                custObj.setValid(false);
                custRepo.save(custObj);

                //mp.put("id",prodObj.getProductId());
                return new ResponseEntity<Map>(mp,HttpStatus.NO_CONTENT);
            }
            else {
                mp.put("status", "failure");
                mp.put("reason", "Product already deleted");
                return new ResponseEntity<Map>(mp, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            mp.put("status", "FAILURE");
            mp.put("reason", "Product does not exist");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "")
    public @ResponseBody ResponseEntity<Map> getUsers() {
        Map<String,Object> mp = new HashMap<>();
        mp.put("data",custRepo.findByIsValid(true));
        return new ResponseEntity<>(mp,HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Map> getCustomer(@PathVariable long id) {
        log.info("Checking get ");
        Map<String, Object> mp = new HashMap<>();
        log.info("id is " + id);
        if(custRepo.exists(id)) {
            Customers custObj = custRepo.findOne(id);
            if(custObj.getValid()) {
                mp.put("data", custObj);
                return new ResponseEntity<>(mp, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
    }
}
