package com.simple.csv.model;

/**
 * Created by Illia Vinnichenko on 25.08.2016.
 */
public class TopProduct {

    private Long matchingId;
    private Double totalPrice;
    private Double avgPrice;
    private String currency;
    private Long ignoredProductsCount;

    public Long getMatchingId() {
        return matchingId;
    }

    public void setMatchingId(Long matchingId) {
        this.matchingId = matchingId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getIgnoredProductsCount() {
        return ignoredProductsCount;
    }

    public void setIgnoredProductsCount(Long ignoredProductsCount) {
        this.ignoredProductsCount = ignoredProductsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopProduct that = (TopProduct) o;

        return !(matchingId != null ? !matchingId.equals(that.matchingId) : that.matchingId != null);

    }

    @Override
    public int hashCode() {
        return matchingId != null ? matchingId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return matchingId +
                "," + totalPrice +
                "," + avgPrice +
                "," + currency +
                "," + ignoredProductsCount;
    }
}
