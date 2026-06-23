package com.steamlibrary.repository;

import com.steamlibrary.model.Order;
import com.steamlibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
