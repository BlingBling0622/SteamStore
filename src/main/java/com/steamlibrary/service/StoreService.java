package com.steamlibrary.service;

import com.steamlibrary.model.*;
import com.steamlibrary.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public List<Product> getAllProducts() { return productRepository.findAll(); }
    public List<Product> getFeaturedProducts() { return productRepository.findByFeaturedTrue(); }
    public Optional<Product> getProduct(Long id) { return productRepository.findById(id); }

    public List<CartItem> getCart(User user) { return cartRepository.findByUser(user); }

    public double getCartTotal(List<CartItem> cartItems) {
        return cartItems.stream().mapToDouble(CartItem::getEffectivePrice).sum();
    }

    /** @deprecated Use getCartTotal(List<CartItem>) instead to avoid fetching cart twice */
    @Deprecated
    public double getCartTotal(User user) {
        return getCartTotal(getCart(user));
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

        Set<Long> owned = getOwnedProductIds(user);
        List<Product> newProducts = items.stream()
                .map(CartItem::getProduct)
                .filter(p -> !owned.contains(p.getId()))
                .collect(Collectors.toList());
        int skipped = items.size() - newProducts.size();

        double total = items.stream().mapToDouble(CartItem::getEffectivePrice).sum();
        List<Product> allProducts = items.stream().map(CartItem::getProduct).collect(Collectors.toList());

        Order order = new Order();
        order.setUser(user);
        order.setProducts(allProducts);
        order.setTotal(total);
        orderRepository.save(order);

        cartRepository.deleteByUser(user);

        log.info("Order created: user={}, products={}, new={}, skipped={}, total={}",
                user.getUsername(), allProducts.size(), newProducts.size(), skipped, total);
        return order;
    }

    /** Claim a free game directly */
    @Transactional
    public void claimFree(User user, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        if (product.getPrice() > 0) throw new IllegalStateException("Not a free game");
        if (getOwnedProductIds(user).contains(productId)) return; // already owned
        // Remove from cart if present
        cartRepository.findByUserAndProductId(user, productId).ifPresent(cartRepository::delete);
        Order order = new Order();
        order.setUser(user);
        order.setProducts(List.of(product));
        order.setTotal(0.0);
        orderRepository.save(order);
        log.info("Free game claimed: user={}, game={}", user.getUsername(), product.getName());
    }

    /** IDs of products already owned by this user */
    @Transactional(readOnly = true)
    public Set<Long> getOwnedProductIds(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        log.debug("Found {} orders for user {}", orders.size(), user.getUsername());

        Set<Long> ownedIds = orders.stream()
                .flatMap(o -> o.getProducts().stream())
                .map(Product::getId)
                .collect(Collectors.toSet());

        log.debug("User {} owns {} unique products", user.getUsername(), ownedIds.size());
        return ownedIds;
    }

    /** Total spent by this user across all orders */
    @Transactional(readOnly = true)
    public double getTotalSpent(User user) {
        return orderRepository.findByUser(user).stream()
                .mapToDouble(Order::getTotal).sum();
    }

    /** Get all purchased products for this user */
    @Transactional(readOnly = true)
    public List<Product> getPurchasedProducts(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        log.debug("Found {} orders for user {}", orders.size(), user.getUsername());

        List<Product> products = orders.stream()
                .flatMap(o -> o.getProducts().stream())
                .distinct()
                .collect(Collectors.toList());

        log.debug("User {} has purchased {} unique products", user.getUsername(), products.size());
        return products;
    }
}
