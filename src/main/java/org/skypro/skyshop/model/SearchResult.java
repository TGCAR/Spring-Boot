package org.skypro.skyshop.model;

import org.skypro.skyshop.model.search.Searchable;

import java.util.UUID;

public final class SearchResult {
    private final UUID id;
    private final String name;
    private final String contentType;

    private SearchResult(UUID id, String name, String contentType) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
    }

    public static SearchResult fromSearchable(Searchable searchable) {
        return new SearchResult(
                searchable.getId(),
                searchable.getSearchTerm(),
                searchable.getContentType()
        );
    }


    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getContentType() { return contentType; }
}
