package gr.tastory.aueb.service;

import gr.tastory.aueb.model.*;
import gr.tastory.aueb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final RestaurantRepo restaurantRepo;
    private final UserRepo userRepo;

    public OrderService(OrderRepo orderRepo, OrderItemRepo orderItemRepo,
                        ProductRepo productRepo, RestaurantRepo restaurantRepo,
                        UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
        this.restaurantRepo = restaurantRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Order placeOrder(Long restaurantId, Map<Long, Integer> quantities, String customerEmail) {

        User customer = userRepo.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalStateException("Δεν βρέθηκε ο χρήστης"));

        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε το εστιατόριο"));


        Order order = new Order();
        order.setUser(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        orderRepo.save(order);

        int savedItems = 0;

        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            if (quantity == null || quantity <= 0) {
                continue; // αυτό το προϊόν δεν το πήρε — προχώρα στο επόμενο
            }

            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε το προϊόν"));

            if (!product.getRestaurant().getId().equals(restaurantId)) {
                throw new SecurityException("Το προϊόν δεν ανήκει σε αυτό το εστιατόριο!");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(product.getPrice());
            orderItemRepo.save(item);
            savedItems++;
        }

        if (savedItems == 0) {
            throw new IllegalArgumentException("Η παραγγελία είναι άδεια — διάλεξε τουλάχιστον ένα προϊόν.");
        }

        return order;
    }

    public List<Order> findOrdersForRestaurant(Long restaurantId, String ownerEmail) {

        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε το εστιατόριο"));

        if (!restaurant.getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("Δεν είσαι ο ιδιοκτήτης αυτού του εστιατορίου!");
        }

        return orderRepo.findByRestaurantId(restaurantId);
    }

    @Transactional
    public void updateStatus(Long restaurantId, Long orderId, OrderStatus newStatus, String ownerEmail) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Δεν βρέθηκε η παραγγελία"));

        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new SecurityException("Η παραγγελία δεν ανήκει σε αυτό το εστιατόριο!");
        }

        if (!order.getRestaurant().getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("Δεν είσαι ο ιδιοκτήτης αυτού του εστιατορίου!");
        }

        // ⚙️ State machine: επιτρέπεται αυτή η μετάβαση;
        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    "Μη επιτρεπτή μετάβαση: " + order.getStatus() + " → " + newStatus);
        }

        order.setStatus(newStatus);
        orderRepo.save(order);
    }

    public List<Order> findMyOrders(String customerEmail) {

        User customer = userRepo.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalStateException("Δεν βρέθηκε ο χρήστης"));

        return orderRepo.findByUserId(customer.getId());
    }

}