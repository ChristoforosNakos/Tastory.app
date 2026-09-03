package gr.tastory.aueb.service;

import gr.tastory.aueb.exception.NotAuthorizedException;
import gr.tastory.aueb.model.*;
import gr.tastory.aueb.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import java.math.BigDecimal;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepo orderRepo;
    @Mock private OrderItemRepo orderItemRepo;
    @Mock private ProductRepo productRepo;
    @Mock private RestaurantRepo restaurantRepo;
    @Mock private UserRepo userRepo;

    @InjectMocks private OrderService orderService;

    @Test
    void updateStatus_throwsWhenUserIsNotOwner() {
        // Arrange
        User owner = new User();
        owner.setEmail("owner@test.com");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        Order order = new Order();
        order.setId(10L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepo.findById(10L)).thenReturn(Optional.of(order));

        // Act + Assert
        assertThrows(NotAuthorizedException.class, () ->
                orderService.updateStatus(1L, 10L, OrderStatus.CONFIRMED, "intruder@test.com"));
    }
    @Test
    void updateStatus_throwsOnIllegalTransition() {
        // Arrange
        User owner = new User();
        owner.setEmail("owner@test.com");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        Order order = new Order();
        order.setId(10L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepo.findById(10L)).thenReturn(Optional.of(order));

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                orderService.updateStatus(1L, 10L, OrderStatus.PENDING, "owner@test.com"));
    }
    @Test
    void placeOrder_savesUnitPriceSnapshotFromProduct() {
        // Arrange
        User customer = new User();
        customer.setEmail("customer@test.com");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Product product = new Product();
        product.setId(5L);
        product.setPrice(new BigDecimal("8.50"));
        product.setRestaurant(restaurant);

        when(userRepo.findByEmail("customer@test.com")).thenReturn(Optional.of(customer));
        when(restaurantRepo.findById(1L)).thenReturn(Optional.of(restaurant));
        when(productRepo.findById(5L)).thenReturn(Optional.of(product));

        // Act
        orderService.placeOrder(1L, Map.of(5L, 2), "customer@test.com");

        // Assert
        ArgumentCaptor<OrderItem> captor = ArgumentCaptor.forClass(OrderItem.class);
        verify(orderItemRepo).save(captor.capture());

        OrderItem saved = captor.getValue();
        assertEquals(new BigDecimal("8.50"), saved.getUnitPrice());
        assertEquals(2, saved.getQuantity());
    }
}