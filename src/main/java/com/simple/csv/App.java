package com.simple.csv;

import com.simple.csv.io.CsvReader;
import com.simple.csv.io.CsvWriter;
import com.simple.csv.model.Currency;
import com.simple.csv.model.Matching;
import com.simple.csv.model.Product;
import com.simple.csv.model.TopProduct;
import com.simple.csv.service.TopProductCalculator;
import com.simple.csv.service.impl.TopProductCalculatorImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Illia Vinnichenko on 23.08.2016.
 */
public class App {

    public static final String CURRENCIES_CSV_FILE_PATH = "src/main/resources/currencies.csv";
    public static final String CURRENCY = "currency";
    public static final String RATIO = "ratio";

    public static final String MATCHING_CSV_FILE_PATH = "src/main/resources/matchings.csv";
    public static final String MATCHING_ID = "matching_id";

    public static final String DATA_CSV_FILE_PATH = "src/main/resources/data.csv";
    public static final String ID = "id";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";
    public static final String TOP_PRICED_COUNT = "top_priced_count";

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
        List<Currency> currencies = readCurrencies();
        currencies.forEach(currency -> currenciesMap.put(currency.getCode(), currency));

        Map<Long, Matching> matchingsMap = new HashMap<>();
        List<Matching> matchings = readMatchings();
        matchings.forEach(matching -> matchingsMap.put(matching.getId(), matching));

        List<Product> products = readProducts();
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


    private static List<Currency> readCurrencies() {
        List<Currency> result = null;
        try (CsvReader csvReader = new CsvReader(CURRENCIES_CSV_FILE_PATH)) {
            result = new ArrayList<>();
            while (csvReader.readRecord()) {
                Currency currency = new Currency();
                currency.setCode(csvReader.get(CURRENCY));
                Float code = Float.valueOf(csvReader.get(RATIO));
                currency.setRatio(code);
                result.add(currency);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<Matching> readMatchings() {
        List<Matching> result = null;
        try (CsvReader csvReader = new CsvReader(MATCHING_CSV_FILE_PATH)) {
            result = new ArrayList<>();
            while (csvReader.readRecord()) {
                Matching matching = new Matching();

                Long matchingId = Long.valueOf(csvReader.get(MATCHING_ID));
                matching.setId(matchingId);

                Long topPricedCount = Long.valueOf(csvReader.get(TOP_PRICED_COUNT));
                matching.setTopPricedCount(topPricedCount);

                result.add(matching);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<Product> readProducts() {
        List<Product> result = null;
        try (CsvReader csvReader = new CsvReader(DATA_CSV_FILE_PATH)) {
            result = new ArrayList<>();
            while (csvReader.readRecord()) {
                Product product = new Product();

                Long id = Long.valueOf(csvReader.get(ID));
                product.setId(id);

                Double price = Double.valueOf(csvReader.get(PRICE));
                product.setPrice(price);

                long quantity = Long.valueOf(csvReader.get(QUANTITY));
                product.setQuantity(quantity);

                String currencyCode = csvReader.get(CURRENCY);
                Currency currency = new Currency();
                currency.setCode(currencyCode);
                product.setCurrency(currency);

                Long matchingId = Long.valueOf(csvReader.get(MATCHING_ID));
                Matching matching = new Matching();
                matching.setId(matchingId);
                product.setMatching(matching);

                result.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
