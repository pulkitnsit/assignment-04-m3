package com.codenation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pulkit on 15/07/17.
 */

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping(value = "")
    public @ResponseBody
    ResponseEntity<Map> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
