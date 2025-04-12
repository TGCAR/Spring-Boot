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

    // Инициализация тестовых данных
    private void initializeTestData() {
        // Пример добавления продукта
        UUID milkId = UUID.randomUUID();
        productStorage.put(
                milkId,
                new SimpleProduct(milkId, "Молоко", 120) // ID передается явно
        );

        productStorage.put(
                UUID.randomUUID(),
                new DiscountedProduct(
                        UUID.randomUUID(),
                        "Телевизор",
                        50000,
                        15
                )
        );

        productStorage.put(
                UUID.randomUUID(),
                new FixPriceProduct(UUID.randomUUID(), "Соль")
        );

        // Добавление тестовых статей
        articleStorage.put(
                UUID.randomUUID(),
                new Article(
                        UUID.randomUUID(),
                        "Выбор телевизора",
                        "Советы по выбору LED телевизора"
                )
        );

        articleStorage.put(
                UUID.randomUUID(),
                new Article(
                        UUID.randomUUID(),
                        "Польза молока",
                        "Молоко содержит кальций и витамины"
                )
        );
    }

    // Получение всех продуктов
    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(productStorage.values());
    }

    // Получение всех статей
    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articleStorage.values());
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> searchables = new ArrayList<>();
        searchables.addAll(productStorage.values());
        searchables.addAll(articleStorage.values());
        return Collections.unmodifiableCollection(searchables);
    }
}
