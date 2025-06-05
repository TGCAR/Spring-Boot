package org.skypro.skyshop.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public interface Searchable {
    UUID getId();
    @JsonIgnore
    String getSearchTerm();
    @JsonIgnore
    String getContentType();

    default String getStringRepresentation() {
        return this.getClass().getSimpleName() + " — " + getContentType();
    }

    String getTitle();
}
