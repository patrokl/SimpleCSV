package com.simple.csv.model;

import com.simple.csv.annotation.CsvMapper;

/**
 * Created by Illia Vinnichenko on 23.08.2016.
 */
public class Matching {

    @CsvMapper(headerName = "matching_id")
    private Long id;

    @CsvMapper(headerName = "top_priced_count")
    private Long topPricedCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTopPricedCount() {
        return topPricedCount;
    }

    public void setTopPricedCount(Long topPricedCount) {
        this.topPricedCount = topPricedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matching matching = (Matching) o;

        return !(id != null ? !id.equals(matching.id) : matching.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
