package org.skypro.skyshop.model.product;

import org.skypro.skyshop.model.search.Searchable;
import java.util.UUID;

public abstract class Product implements Searchable {
    private final UUID id;
    private final String name;

    public Product(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    // Реализация методов Searchable
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getSearchTerm() {
        return name;
    }

    @Override
    public String getContentType() {
        return "PRODUCT";
    }

    // Геттеры
    public String getName() {
        return name;
    }

    public abstract int getPrice();

    public abstract boolean isSpecial();
}
