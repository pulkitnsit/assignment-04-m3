package com.codenation.repository;

import com.codenation.models.*;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;


//@Autowired
public interface CustomersRepository extends CrudRepository<Customers, Long> {

    List<Customers> findByIsValid(Boolean valid);
}
