package com.codenation.repository;

import java.util.List;
import com.codenation.models.OrderProducts;
import com.codenation.models.Orders;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductsRepository extends CrudRepository<OrderProducts, Long> {

    List<OrderProducts> findByOrders(Orders order);
}