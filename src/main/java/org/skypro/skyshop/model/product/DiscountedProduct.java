package org.skypro.skyshop.model.product;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.UUID;

@JsonTypeName("simple")
public class DiscountedProduct extends Product {
    private final int basePrice; // Базовая цена
    private final int discount;  // Скидка в процентах (целое число от 0 до 100)


    public DiscountedProduct(UUID id, String name, int basePrice, int discount) {
        super(id, name);
        if (basePrice <= 0) throw new IllegalArgumentException("Базовая цена должна быть больше 0");
        if (discount < 0 || discount > 100) throw new IllegalArgumentException("Скидка должна быть от 0 до 100%");

        this.basePrice = basePrice;
        this.discount = discount;
    }


    @Override
    public int getPrice() {
        return (int) (basePrice * (100 - discount) / 100.0);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String toString() {
        return getName() + ": " + getPrice() + " (" + discount + "%)";
    }
}

