package org.skypro.skyshop.model.basket;

import java.math.BigDecimal;
import java.util.List;

public class UserBasket {
    private final List<BasketItem> items;
    private final BigDecimal total;

    public UserBasket(List<BasketItem> items) {
        this.items = List.copyOf(items);
        this.total = items.stream()
                .map(item -> {
                    BigDecimal price = BigDecimal.valueOf(item.getProduct().getPrice());
                    int quantity = item.getQuantity();
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add); // безопасно теперь
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}