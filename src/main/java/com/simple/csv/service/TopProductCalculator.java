package com.simple.csv.service;

import com.simple.csv.model.Product;
import com.simple.csv.model.TopProduct;

import java.util.List;

/**
 * Created by Illia Vinnichenko on 25.08.2016.
 */
public interface TopProductCalculator {

     List<TopProduct> calculate(List<Product> products);

}
