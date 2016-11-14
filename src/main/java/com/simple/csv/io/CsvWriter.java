package com.simple.csv.io;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Illia Vinnichenko on 24.08.2016.
 */
public class CsvWriter implements Closeable {

    public static final String UNDERSCORE = "_";
    public static final String COMMA = ",";
    public static final String DELIMITER = "\n";

    private final BufferedWriter bufferedWriter;

    public CsvWriter(String filePath) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(filePath));
    }

    public <T> void write(Class<T> tClass, List<T> values) throws IOException {
        String header = createHeader(tClass);
        bufferedWriter.write(header);
        bufferedWriter.write(DELIMITER);

        String body = createBody(values);
        bufferedWriter.write(body);

        bufferedWriter.close();
    }

    private <T> String createHeader(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        return Stream.of(fields)
                .map(field -> convertToCsvFormat(field.getName()))
                .collect(Collectors.joining(COMMA));
    }

    private <T> String createBody(List<T> values) {
        return values.stream()
                .map(T::toString)
                .collect(Collectors.joining(DELIMITER));
    }

    private String convertToCsvFormat(String header) {
        StringBuilder builder = new StringBuilder(header.length());
        char[] chars = header.toCharArray();
        for (char vChar : chars) {
            if (Character.isUpperCase(vChar)) {
                builder.append(UNDERSCORE);
            }
            builder.append(Character.toLowerCase(vChar));
        }
        return builder.toString();
    }


    @Override
    public void close() throws IOException {
        bufferedWriter.close();
    }
}
