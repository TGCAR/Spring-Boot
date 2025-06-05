package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с корзиной пользователя.
 * Содержит два метода:
 *   1. addProduct(UUID id) – добавляет товар в корзину (бросает NoSuchProductException, если товар не найден в StorageService).
 *   2. getUserBasket() – строит объект UserBasket по тому, что лежит в ProductBasket.
 */
@Service
public class BasketService {

    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    /**
     * Добавляет товар в корзину.
     * Если в storageService нет товара с таким id, бросает NoSuchProductException.
     */
    public void addProduct(UUID id) {
        Product product = storageService.getProductById(id)
                .orElseThrow(() ->
                        new NoSuchProductException("Product not found: " + id));
        // Если дошли до этого места, значит товар есть – добавляем в ProductBasket
        productBasket.addProduct(id);
    }

    /**
     * Формирует объект UserBasket на основании текущего содержимого ProductBasket.
     * Если в ProductBasket лежит id товара, которого уже нет в StorageService,
     * выбрасывается IllegalStateException.
     */
    public UserBasket getUserBasket() {
        // Получаем map<UUID, Integer> из сессионного хранилища ProductBasket
        Map<UUID, Integer> basketMap = productBasket.getBasket();

        // Для каждого (id, quantity) собираем BasketItem
        List<BasketItem> items = basketMap.entrySet().stream().map(entry -> {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();
            // Пытаемся получить Product из StorageService
            Product product = storageService.getProductById(productId)
                    .orElseThrow(() -> new IllegalStateException(
                            "Product with id " + productId + " not found in storage"));
            // Создаём BasketItem
            return new BasketItem(product, quantity);
        }).collect(Collectors.toList());

        // Собираем UserBasket из списка BasketItem (UserBasket сам посчитает total)
        return new UserBasket(items);
    }
}
