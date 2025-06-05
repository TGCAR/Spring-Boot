package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private StorageService storageService;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        storageService = mock(StorageService.class);
        searchService = new SearchService(storageService);
    }

    @Test
    void search_returnsEmptyList_whenNoSearchablesInStorage() {
        // Сценарий 1: StorageService возвращает пустую коллекцию
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        List<Searchable> result = searchService.search("anything");

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Ожидается пустой список, если в хранилище нет объектов");
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    void search_returnsEmptyList_whenNoMatchingSearchablesFound() {
        // Сценарий 2: В хранилище есть объекты, но ни один не содержит ключевое слово
        Searchable s1 = mock(Searchable.class);
        when(s1.getTitle()).thenReturn("Apple");
        Searchable s2 = mock(Searchable.class);
        when(s2.getTitle()).thenReturn("Banana");

        List<Searchable> all = Arrays.asList(s1, s2);
        when(storageService.getAllSearchables()).thenReturn(all);

        List<Searchable> result = searchService.search("Cherry");

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Ожидается пустой список, если нет совпадений по ключевому слову");
        verify(storageService, times(1)).getAllSearchables();
        // убедимся, что getTitle() вызывался (достаточно хотя бы один раз в каждом объекте)
        verify(s1, atLeastOnce()).getTitle();
        verify(s2, atLeastOnce()).getTitle();
    }

    @Test
    void search_returnsMatchingItems_whenThereAreMatches() {
        // Сценарий 3: В хранилище есть объект, чьё название (title) содержит ключевое слово (без учёта регистра)
        Searchable found = mock(Searchable.class);
        when(found.getTitle()).thenReturn("SuperProduct");
        Searchable other = mock(Searchable.class);
        when(other.getTitle()).thenReturn("AnotherOne");

        List<Searchable> all = Arrays.asList(found, other);
        when(storageService.getAllSearchables()).thenReturn(all);

        List<Searchable> result = searchService.search("super");

        assertEquals(1, result.size(), "Должен вернуться ровно один объект, содержащий 'super'");
        assertSame(found, result.get(0), "Первый (и единственный) элемент должен быть тем, чей заголовок содержит 'super'");
        verify(storageService, times(1)).getAllSearchables();
        verify(found, atLeastOnce()).getTitle();
        verify(other, atLeastOnce()).getTitle();
    }

    @Test
    void search_isCaseInsensitive() {
        // Дополнительный сценарий: поиск без учёта регистра
        Searchable prod1 = mock(Searchable.class);
        when(prod1.getTitle()).thenReturn("TeLeVision");
        Searchable prod2 = mock(Searchable.class);
        when(prod2.getTitle()).thenReturn("Radio");

        when(storageService.getAllSearchables()).thenReturn(Arrays.asList(prod1, prod2));

        List<Searchable> result = searchService.search("teleVISion");

        assertEquals(1, result.size());
        assertSame(prod1, result.get(0));
        verify(prod1, atLeastOnce()).getTitle();
        verify(prod2, atLeastOnce()).getTitle();
    }
}
