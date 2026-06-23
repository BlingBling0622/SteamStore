package com.steamlibrary.service;

import com.steamlibrary.model.*;
import com.steamlibrary.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public List<Product> getAllProducts() { return productRepository.findAll(); }
    public List<Product> getFeaturedProducts() { return productRepository.findByFeaturedTrue(); }
    public Optional<Product> getProduct(Long id) { return productRepository.findById(id); }

    public List<CartItem> getCart(User user) { return cartRepository.findByUser(user); }

    public double getCartTotal(User user) {
        return getCart(user).stream().mapToDouble(CartItem::getEffectivePrice).sum();
    }

    @Transactional
    public void addToCart(User user, Long productId) {
        if (cartRepository.findByUserAndProductId(user, productId).isPresent()) return;
        Product product = productRepository.findById(productId).orElseThrow();
        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        cartRepository.save(item);
    }

    @Transactional
    public void removeFromCart(User user, Long productId) {
        cartRepository.findByUserAndProductId(user, productId)
                .ifPresent(cartRepository::delete);
    }

    @Transactional
    public Order checkout(User user) {
        List<CartItem> items = cartRepository.findByUser(user);
        if (items.isEmpty()) throw new IllegalStateException("Cart is empty");

        double total = items.stream().mapToDouble(CartItem::getEffectivePrice).sum();
        List<Product> products = items.stream().map(CartItem::getProduct).collect(Collectors.toList());

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setTotal(total);
        orderRepository.save(order);

        cartRepository.deleteByUser(user);
        return order;
    }

    /** IDs of products already owned by this user */
    public Set<Long> getOwnedProductIds(User user) {
        return orderRepository.findByUser(user).stream()
                .flatMap(o -> o.getProducts().stream())
                .map(Product::getId)
                .collect(Collectors.toSet());
    }
}
