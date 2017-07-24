package com.codenation.controller;

import com.codenation.models.ContactUs;
import com.codenation.repository.ContactUsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pulkit on 15/07/17.
 */

@RestController
@RequestMapping("/api/feedback")
public class ContactUsController {

    @Autowired
    ContactUsRepository contRepo;

    @PostMapping(value = "")
    public @ResponseBody
    ResponseEntity<Map> addUser (@RequestBody ContactUs custObj) {
        contRepo.save(custObj);
        Map<String,Object> mp = new HashMap<>();
        mp.put("id",custObj.getFeedbackId());
        return new ResponseEntity<>(mp, HttpStatus.CREATED);
    }

}
