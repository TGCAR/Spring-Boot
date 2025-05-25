package org.skypro.skyshop.model.product;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.UUID;

@JsonTypeName("fixprice")
public class FixPriceProduct extends Product {
    // Приватная константа для фиксированной цены
    private final int FIXED_PRICE = 50; // фиксированная цена

    // Конструктор, принимающий только имя продукта
    public FixPriceProduct(UUID id, String name) {
        super(id, name);
    }

    // Переопределенный метод getPrice, возвращающий фиксированную цену
    @Override
    public int getPrice() {
        return 100;
    }

    @Override
    public boolean isSpecial() {
        return true; // Товар с фиксированной ценой является специальным
    }

    @Override
    public String toString() {
        return getName() + ": Фиксированная цена " + FIXED_PRICE;
    }
}
