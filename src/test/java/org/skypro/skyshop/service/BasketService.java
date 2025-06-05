package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketServiceTest {

    private ProductBasket productBasket;
    private StorageService storageService;
    private BasketService basketService;

    @BeforeEach
    void setUp() {
        productBasket = mock(ProductBasket.class);
        storageService = mock(StorageService.class);
        basketService = new BasketService(productBasket, storageService);
    }

    @Test
    void addProduct_throwsException_whenProductNotFound() {
        UUID id = UUID.randomUUID();
        when(storageService.getProductById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> basketService.addProduct(id));
        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void addProduct_callsProductBasket_whenProductExists() {
        UUID id = UUID.randomUUID();
        Product product = mock(Product.class);
        when(storageService.getProductById(id)).thenReturn(Optional.of(product));

        basketService.addProduct(id);

        verify(productBasket, times(1)).addProduct(id);
    }

    @Test
    void getUserBasket_returnsEmpty_whenBasketIsEmpty() {
        // Если в ProductBasket нет товаров (пустая Map)
        when(productBasket.getBasket()).thenReturn(Collections.emptyMap());

        UserBasket result = basketService.getUserBasket();

        assertNotNull(result, "UserBasket не должен быть null");
        assertTrue(result.getItems().isEmpty(), "Список элементов корзины должен быть пустым");
        assertEquals(0, result.getTotal().intValue(), "Итоговая стоимость пустой корзины — 0");
    }

    @Test
    void getUserBasket_returnsCorrectBasketItems_whenProductsExist() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);
        // Mockito: заданное поведение для getProductById
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));
        // В «ProductBasket» находится 2 ед. какого-то товара
        Map<UUID, Integer> mockMap = new HashMap<>();
        mockMap.put(productId, 2);
        when(productBasket.getBasket()).thenReturn(mockMap);
        // Предположим, что product.getPrice() возвращает, скажем, 100
        when(product.getPrice()).thenReturn(100);

        UserBasket result = basketService.getUserBasket();

        // Проверяем, что внутри UserBasket ровно один элемент
        assertEquals(1, result.getItems().size(), "Должен быть ровно один элемент в UserBasket");
        BasketItem item = result.getItems().get(0);
        // Проверяем, что продукт — тот же, который вернул StorageService
        assertSame(product, item.getProduct(), "В BasketItem должен лежать тот же объект Product");
        // Проверяем количество
        assertEquals(2, item.getQuantity(), "Количество должно быть тем же, что и в mockMap");
        // И его стоимость: 100 * 2 = 200
        assertEquals(200, result.getTotal().intValue(), "Итоговая стоимость должна быть 200");
    }

    @Test
    void getUserBasket_throwsException_whenProductMissingInStorage() {
        UUID productId = UUID.randomUUID();
        // В корзине – один товар
        Map<UUID, Integer> mockMap = new HashMap<>();
        mockMap.put(productId, 1);
        when(productBasket.getBasket()).thenReturn(mockMap);
        // Но StorageService возвращает пустой Optional => товара нет
        when(storageService.getProductById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> basketService.getUserBasket(),
                "Если продукт из корзины не найден в хранилище, должно бросаться IllegalStateException");
    }

    // Можно добавить ещё дополнительные сценарии, например:
    @Test
    void getUserBasket_multipleProducts_correctTotalCalculation() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(product1.getPrice()).thenReturn(50);
        when(product2.getPrice()).thenReturn(30);

        Map<UUID, Integer> mockMap = new HashMap<>();
        mockMap.put(id1, 3); // 3 * 50 = 150
        mockMap.put(id2, 2); // 2 * 30 = 60

        when(productBasket.getBasket()).thenReturn(mockMap);
        when(storageService.getProductById(id1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(id2)).thenReturn(Optional.of(product2));

        UserBasket result = basketService.getUserBasket();

        // 2 элемента
        assertEquals(2, result.getItems().size());
        // Общая сумма: 150 + 60 = 210
        assertEquals(210, result.getTotal().intValue());
    }
}
