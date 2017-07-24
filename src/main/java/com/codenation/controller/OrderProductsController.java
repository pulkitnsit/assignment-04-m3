package com.codenation.controller;

import com.codenation.inputFormat.OrderInputFormat;
import com.codenation.models.Products;
import com.codenation.repository.*;
//import com.google.common.collect.Lists;
//import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codenation.models.*;

import java.util.*;

/**
 * Created by pulkit on 08/07/17.
 */

@RestController
@RequestMapping("/api/cart")
public class OrderProductsController {

    @Autowired
    ProductsRepository prodRepo;

    @Autowired
    OrdersRepository orderRepo;

    @Autowired
    CustomersRepository custRepo;

    @Autowired
    OrderProductsRepository orderProductsRepo;

    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);

    @PostMapping(value = "/{userId}")
    public @ResponseBody
    ResponseEntity<Map> addProductToCart(@RequestBody OrderInputFormat ordInpObj, @PathVariable long userId) {
        if (!ordInpObj.checkValidations(prodRepo)) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("status", "FAILURE");
            mp.put("reason", "Invalid Values");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }
        Long productId = ordInpObj.getProductId();
        Products product = prodRepo.findOne(productId);
        int availQuantity = product.getProductQuantity();
        int orderQuantity = ordInpObj.getQuantity();
        if (orderQuantity > availQuantity) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("status", "FAILURE");
            mp.put("reason", "Item out of stock");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }
        Customers customer = custRepo.findOne(userId);
        if (customer == null) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("status", "FAILURE");
            mp.put("reason", "Customer does not exist");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }
        Orders orderObj = orderRepo.findOneByCustomersAndStatus(customer, "cart");
        long orderId;
        if (orderObj == null) {
            Orders order = new Orders(new Date(), "cart", customer);
            orderRepo.save(order);
            orderId = order.getOrderId();
        } else
            orderId = orderObj.getOrderId();

        Orders order = orderRepo.findOne(orderId);
        OrderProducts orderProduct = new OrderProducts(product, order, orderQuantity, product.getSellPrice());
        orderProductsRepo.save(orderProduct);
        Map<String, Object> mp = new HashMap<>();
        return new ResponseEntity<>(mp, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{userId}/checkout")
    public @ResponseBody ResponseEntity<Map> checkout(@PathVariable long userId) {

        Customers customer = custRepo.findOne(userId);
        if (customer == null) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("status", "FAILURE");
            mp.put("reason", "Customer does not exits");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }
        Orders orderObj = orderRepo.findOneByCustomersAndStatus(customer, "cart");
        if (orderObj == null) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("status", "FAILURE");
            mp.put("reason", "Cannot checkout empty cart");
            return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
        }

        List<OrderProducts> orderProductsList = orderProductsRepo.findByOrders(orderObj);
        List<Products> productsList = prodRepo.findByIsValid(true);
        List<Integer> prodQuantityList = new ArrayList<>();
        for(Products products : productsList)
            prodQuantityList.add(products.getProductQuantity());

        Double totalPrice = 0.00;
        for(OrderProducts orderProducts : orderProductsList) {
            Products product = orderProducts.getProducts();

            int orderQuantity = orderProducts.getOrderQuantity();
            int availQuantity = product.getProductQuantity();
            totalPrice += orderProducts.getPriceEach()*orderQuantity;

            if (orderQuantity > availQuantity) {
                for(int i=0;i<prodQuantityList.size();++i)
                    productsList.get(i).setProductQuantity(prodQuantityList.get(i));
                prodRepo.save(productsList);
                orderObj.setStatus("cancelled");
                orderRepo.save(orderObj);

                Map<String, Object> mp = new HashMap<>();
                mp.put("status", "FAILURE");
                mp.put("reason", "Item out of stock");
                return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
            }
            product.setProductQuantity(availQuantity - orderQuantity);
            prodRepo.save(product);
        }
        orderObj.setStatus("placed");
        orderRepo.save(orderObj);

        Map<String, Object> mp = new HashMap<>();
        Map<String, Object> mpIn = new HashMap<>();
        mpIn.put("total_price",totalPrice);
        mp.put("data", mpIn);
        mp.put("status", "success");
        return new ResponseEntity<>(mp, HttpStatus.OK);
    }
}
