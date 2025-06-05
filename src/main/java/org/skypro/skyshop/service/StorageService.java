package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> productStorage = new HashMap<>();
    private final Map<UUID, Article> articleStorage = new HashMap<>();


    public StorageService() {
        initializeTestData();
    }


    private void initializeTestData() {
        
        UUID milkId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID tvId = UUID.fromString("d648c957-cad1-4b84-b953-62849f7a3806");
        UUID saltId = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");


        productStorage.put(milkId, new SimpleProduct(milkId, "Молоко", 120));
        productStorage.put(tvId, new DiscountedProduct(tvId, "Телевизор", 50000, 15));
        productStorage.put(saltId, new FixPriceProduct(saltId, "Соль"));
    }


    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(productStorage.values());
    }


    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articleStorage.values());
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> searchables = new ArrayList<>();
        searchables.addAll(productStorage.values());
        searchables.addAll(articleStorage.values());
        return Collections.unmodifiableCollection(searchables);
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(productStorage.get(id));
    }


}
