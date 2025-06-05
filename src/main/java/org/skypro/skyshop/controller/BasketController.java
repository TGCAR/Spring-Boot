package org.skypro.skyshop.controller;

import org.skypro.skyshop.service.BasketService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    // Основной рабочий POST-метод
    @PostMapping("/{id}")
    public void addProductToBasket(@PathVariable UUID id) {
        basketService.addProduct(id);
    }

    // Временный GET-метод для теста из браузера
    @GetMapping("/{id}")
    public String addProductToBasketFromBrowser(@PathVariable UUID id) {
        basketService.addProduct(id);
        return "Товар успешно добавлен в корзину";
    }
}