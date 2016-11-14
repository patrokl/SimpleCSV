package com.simple.csv.service.impl;

import com.simple.csv.model.Matching;
import com.simple.csv.model.Product;
import com.simple.csv.model.TopProduct;
import com.simple.csv.service.TopProductCalculator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Illia Vinnichenko on 25.08.2016.
 */
public class TopProductCalculatorImpl implements TopProductCalculator {

    public static final String PLN = "PLN";

    Comparator<Product> productComparatorDesc = (product1, product2) -> {
        Double totalPrice1 = countTotalProductPrice(product1);
        Double totalPrice2 = countTotalProductPrice(product2);
        return totalPrice2.compareTo(totalPrice1);
    };

    Double countTotalProductPrice(Product product) {
        com.simple.csv.model.Currency currency = product.getCurrency();
        double priceInPLN = product.getPrice() * currency.getRatio();
        return priceInPLN * product.getQuantity();
    }


    @Override
    public List<TopProduct> calculate(List<Product> products) {
        List<TopProduct> result = new ArrayList<>();
        Map<Matching, List<Product>> productsByMatching = products
                .stream()
                .collect(Collectors.groupingBy(Product::getMatching, Collectors.toList()));
        for (Map.Entry<Matching, List<Product>> matchingListEntry : productsByMatching.entrySet()) {
            TopProduct topProduct = new TopProduct();

            Matching matching = matchingListEntry.getKey();
            topProduct.setMatchingId(matching.getId());

            List<Product> productList = matchingListEntry.getValue();

            List<Product> topPricedProducts = findTopPricedProducts(productList, matching.getTopPricedCount());

            Double averagingPrice = countAveragingPrice(topPricedProducts);
            topProduct.setAvgPrice(averagingPrice);

            Double totalPrice = countTotalTopProductPrice(topPricedProducts);
            topProduct.setTotalPrice(totalPrice);

            long ignoredProductsCount = productList.size() - matching.getTopPricedCount();
            topProduct.setIgnoredProductsCount(ignoredProductsCount < 0 ? 0 : ignoredProductsCount);
            topProduct.setCurrency(PLN);

            result.add(topProduct);
        }
        return result;
    }

    private List<Product> findTopPricedProducts(List<Product> products, Long limit) {
        return products
                .stream()
                .sorted(productComparatorDesc)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Double countAveragingPrice(List<Product> products) {
        return products
                .stream()
                .collect(Collectors.averagingDouble(Product::getPrice));
    }

    private Double countTotalTopProductPrice(List<Product> products) {
        return products
                .stream()
                .collect(Collectors.summingDouble(Product::getPrice));
    }


}
