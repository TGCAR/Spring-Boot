package org.skypro.skyshop.model.product;

import org.skypro.skyshop.model.search.Searchable;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleProduct.class, name = "simple"),
        @JsonSubTypes.Type(value = DiscountedProduct.class, name = "discounted"),
        @JsonSubTypes.Type(value = FixPriceProduct.class, name = "fixprice")})

public abstract class Product implements Searchable {
    private final UUID id;
    private final String name;
    protected Product() {
        this.id = null;
        this.name = null;
    }

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
    public String getTitle() {
        return name;
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
