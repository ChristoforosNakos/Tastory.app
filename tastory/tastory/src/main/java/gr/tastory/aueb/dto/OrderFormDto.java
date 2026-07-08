package gr.tastory.aueb.dto;

import java.util.HashMap;
import java.util.Map;

public class OrderFormDto {

    private Map<Long, Integer> quantities = new HashMap<>();

    public Map<Long, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Long, Integer> quantities) {
        this.quantities = quantities;
    }
}