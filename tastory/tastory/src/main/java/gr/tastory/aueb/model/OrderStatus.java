package gr.tastory.aueb.model;

import java.util.List;
import java.util.Set;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    // Ποια status επιτρέπονται ΜΕΤΑ από το τρέχον;
    public Set<OrderStatus> nextStates() {
        return switch (this) {
            case PENDING          -> Set.of(CONFIRMED, CANCELLED);
            case CONFIRMED        -> Set.of(PREPARING, CANCELLED);
            case PREPARING        -> Set.of(OUT_FOR_DELIVERY, CANCELLED);
            case OUT_FOR_DELIVERY -> Set.of(DELIVERED, CANCELLED);
            case DELIVERED        -> Set.of();   // τέλος — τίποτα μετά
            case CANCELLED        -> Set.of();   // τέλος — τίποτα μετά
        };
    }

    // Επιτρέπεται η μετάβαση από 'this' στο 'target';
    public boolean canTransitionTo(OrderStatus target) {
        return nextStates().contains(target);
    }
}