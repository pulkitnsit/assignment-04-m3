package com.codenation.repository;

import java.util.List;

import com.codenation.models.Customers;
import com.codenation.models.Orders;
import org.springframework.data.repository.CrudRepository;

public interface OrdersRepository extends CrudRepository<Orders, Long> {

    //List<Orders> findByName(String name);
    Orders findOneByCustomersAndStatus(Customers customer, String status);
}