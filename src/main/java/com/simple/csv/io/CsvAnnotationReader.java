package com.simple.csv.io;

import com.simple.csv.annotation.CsvMapper;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Illia Vinnichenko on 25.08.2016.
 */
public class CsvAnnotationReader<T> implements Closeable {

    private static final String LINE_SPLITTER = ",";
    public static final String EMPTY_STRING = "";

    private final BufferedReader reader;
    private Map<Integer, String> headersPositions;
    private Map<String, Field> headerFieldMap;
    private Class<T> tClass;

    public CsvAnnotationReader(String filePath, Class<T> tClass) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        this.tClass = tClass;

        init();
    }

    private void init() throws IOException {
        readHeadersPositions();
        createHeaderFieldMap();
    }

    private void readHeadersPositions() throws IOException {
        String line = reader.readLine();
        String[] headers = line.split(LINE_SPLITTER);
        headersPositions = new HashMap<>(headers.length, 1);
        for (int i = 0; i < headers.length; i++) {
            headersPositions.put(i, headers[i]);
        }
    }


    private void createHeaderFieldMap() throws IOException {
        headerFieldMap = new HashMap<>();
        Field[] declaredFields = tClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(CsvMapper.class)) {
                CsvMapper annotation = declaredField.getAnnotation(CsvMapper.class);
                String headerName = annotation.headerName();
                if (headerName.equals(EMPTY_STRING)) {
                    headerName = declaredField.getName();
                }
                Field field = headerFieldMap.get(headerName);
                if (field == null) {
                    headerFieldMap.put(headerName, declaredField);
                } else {
                    throw new IOException("Header name " + headerName + " mast be unique.");
                }
            }
        }
    }


    public T readRecord() throws IOException {
        String line = reader.readLine();
        T newInstance = null;
        if (line != null) {
            try {
                newInstance = tClass.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IOException("Cannot instantiate object of class : " + tClass.getName(), e);
            }
            String[] currentLine = line.split(LINE_SPLITTER);

            for (int i = 0; i < currentLine.length; i++) {
                String headerName = headersPositions.get(i);
                Field field = headerFieldMap.get(headerName);
                if (field != null) {
                    setValueToObjectField(newInstance, field, currentLine[i]);
                }
            }
        } else {
            close();
        }
        return newInstance;
    }

    public List<T> readAllRecords() throws IOException {
        List<T> result = new ArrayList<>();
        T record;
        while ((record = readRecord()) != null) {
            result.add(record);
        }
        close();
        return result;
    }

    private void setValueToObjectField(T object, Field field, String value) throws IOException {
        String mappingFieldName = field.getAnnotation(CsvMapper.class).mappingField();
        try {
            Object instance;
            if (mappingFieldName.equals(EMPTY_STRING)) {
                Class<?> type = field.getType();
                instance = type.getConstructor(String.class).newInstance(value);
            } else {
                Class<?> type = field.getType();
                instance = type.newInstance();

                Field mappingField = type.getDeclaredField(mappingFieldName);
                Object mappingFieldValue = mappingField.getType().getConstructor(String.class).newInstance(value);
                mappingField.setAccessible(true);
                mappingField.set(instance, mappingFieldValue);
            }
            field.set(object, instance);
        } catch (ReflectiveOperationException e) {
            throw new IOException("Can't read object from CSV file. Object class : " + tClass.getName(), e);
        }

    }


    public void close() throws IOException {
        headersPositions = null;
        headerFieldMap = null;
        reader.close();
    }
}
