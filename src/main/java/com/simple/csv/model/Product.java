package com.simple.csv.model;

import com.simple.csv.annotation.CsvMapper;

/**
 * Created by Illia Vinnichenko on 23.08.2016.
 */
public class Product {

    @CsvMapper
    private Long id;

    @CsvMapper
    private Double price;

    @CsvMapper(mappingField = "code")
    private Currency currency;

    @CsvMapper
    private Long quantity;

    @CsvMapper(headerName = "matching_id", mappingField = "id")
    private Matching matching;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Matching getMatching() {
        return matching;
    }

    public void setMatching(Matching matching) {
        this.matching = matching;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return !(id != null ? !id.equals(product.id) : product.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
