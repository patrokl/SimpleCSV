package com.simple.csv.service.impl;

import com.simple.csv.model.Currency;
import com.simple.csv.model.Product;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Illia Vinnichenko on 28.08.2016.
 */
public class TopProductCalculatorImplTest {

    public static final String EU = "EU";
    private static TopProductCalculatorImpl topProductCalculator;

    @BeforeClass
    public static void setUp() {
        topProductCalculator = new TopProductCalculatorImpl();
    }

    @Test
    public void canCountTotalProductPrice() {
        Currency currency = new Currency();
        currency.setCode(EU);
        currency.setRatio(2.5F);

        Product product = new Product();
        product.setCurrency(currency);
        product.setPrice(4D);
        product.setQuantity(10L);
        Double totalProductPrice = topProductCalculator.countTotalProductPrice(product);

        Assert.assertEquals(100D, totalProductPrice, 0.01);
    }


    @Test
    public void canCompareTwoProductCorrect() {
        Currency currency = new Currency();
        currency.setCode(EU);
        currency.setRatio(2.5F);

        Product productTotalPrice100 = new Product();
        productTotalPrice100.setCurrency(currency);
        productTotalPrice100.setPrice(4D);
        productTotalPrice100.setQuantity(10L);

        Product productTotalPrice500 = new Product();
        productTotalPrice500.setCurrency(currency);
        productTotalPrice500.setPrice(200D);
        productTotalPrice500.setQuantity(1L);

        Product productTotalPrice500_2 = new Product();
        productTotalPrice500_2.setCurrency(currency);
        productTotalPrice500_2.setPrice(50D);
        productTotalPrice500_2.setQuantity(4L);

        int actual = topProductCalculator.productComparatorDesc.compare(productTotalPrice500, productTotalPrice100);
        int expected = -1;
        Assert.assertEquals(expected,actual);

        actual = topProductCalculator.productComparatorDesc.compare(productTotalPrice100,productTotalPrice500);
        expected = 1;
        Assert.assertEquals(expected,actual);

        actual = topProductCalculator.productComparatorDesc.compare(productTotalPrice500_2,productTotalPrice500);
        expected = 0;
        Assert.assertEquals(expected,actual);
    }




}
