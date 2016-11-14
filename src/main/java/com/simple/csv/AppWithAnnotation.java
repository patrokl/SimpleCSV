package com.simple.csv;

import com.simple.csv.io.CsvAnnotationReader;
import com.simple.csv.io.CsvWriter;
import com.simple.csv.model.Currency;
import com.simple.csv.model.Matching;
import com.simple.csv.model.Product;
import com.simple.csv.model.TopProduct;
import com.simple.csv.service.TopProductCalculator;
import com.simple.csv.service.impl.TopProductCalculatorImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Illia Vinnichenko on 28.08.2016.
 */
public class AppWithAnnotation {

    public static final String CURRENCIES_CSV_FILE_PATH = "src/main/resources/currencies.csv";

    public static final String MATCHING_CSV_FILE_PATH = "src/main/resources/matchings.csv";

    public static final String DATA_CSV_FILE_PATH = "src/main/resources/data.csv";

    public static final String TOP_PRODUCTS_CSV_FILE_PATH = "top_products.csv";

    private static TopProductCalculator topProductCalculator = new TopProductCalculatorImpl();

    public static void main(String[] args) throws IOException {
        List<Product> products = initProducts();
        List<TopProduct> topProducts = topProductCalculator.calculate(products);
        CsvWriter csvWriter = new CsvWriter(TOP_PRODUCTS_CSV_FILE_PATH);
        csvWriter.write(TopProduct.class, topProducts);
    }

    private static List<Product> initProducts() {
        Map<String, Currency> currenciesMap = new HashMap<>();
        List<Currency> currencies = readAllRecordsFromFile(CURRENCIES_CSV_FILE_PATH, Currency.class);
        currencies.forEach(currency -> currenciesMap.put(currency.getCode(), currency));

        Map<Long, Matching> matchingsMap = new HashMap<>();
        List<Matching> matchings = readAllRecordsFromFile(MATCHING_CSV_FILE_PATH, Matching.class);
        matchings.forEach(matching -> matchingsMap.put(matching.getId(), matching));

        List<Product> products = readAllRecordsFromFile(DATA_CSV_FILE_PATH, Product.class);
        products = mergeProducts(products, matchingsMap, currenciesMap);
        return products;
    }

    private static List<Product> mergeProducts(List<Product> products, Map<Long, Matching> matchingsMap, Map<String, Currency> currenciesMap) {

        Consumer<Product> mergeCurrency = product -> {
            String code = product.getCurrency().getCode();
            Currency currency = currenciesMap.get(code);
            product.setCurrency(currency);
        };

        Consumer<Product> mergeMatching = product -> {
            Long id = product.getMatching().getId();
            Matching matching = matchingsMap.get(id);
            product.setMatching(matching);
        };

        return products.stream().peek(mergeCurrency).peek(mergeMatching).collect(Collectors.toList());
    }

    private static <T> List<T> readAllRecordsFromFile(String fileName, Class<T> tClass) {
        List<T> result = null;
        try {
            CsvAnnotationReader<T> csvReader = new CsvAnnotationReader<>(fileName, tClass);
            result = csvReader.readAllRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
