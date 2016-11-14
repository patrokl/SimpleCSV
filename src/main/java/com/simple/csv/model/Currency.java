package com.simple.csv.model;

import com.simple.csv.annotation.CsvMapper;

/**
 * Created by Illia Vinnichenko on 23.08.2016.
 */
public class Currency {

    @CsvMapper(headerName = "currency")
    private String code;

    @CsvMapper(headerName = "ratio")
    private Float ratio;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        return !(code != null ? !code.equals(currency.code) : currency.code != null);

    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
