package com.simple.csv.io;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Illia Vinnichenko on 23.08.2016.
 */
public class CsvReader implements Closeable {

    private static final String LINE_SPLITTER = ",";

    private final BufferedReader reader;
    private Map<String, Integer> headersPositions;
    private String[] currentLine;

    public CsvReader(String filePath) throws IOException {
        reader = new BufferedReader(new FileReader(filePath));
        readHeader();
    }


    private void readHeader() throws IOException {
        String line = reader.readLine();
        String[] headers = line.split(LINE_SPLITTER);
        headersPositions = new HashMap<>(headers.length, 1);
        for (int i = 0; i < headers.length; i++) {
            headersPositions.put(headers[i], i);
        }
    }


    public boolean readRecord() throws IOException {
        String line = reader.readLine();
        boolean notEmpty = (line != null);
        if (notEmpty) {
            currentLine = line.split(LINE_SPLITTER);
        }
        return notEmpty;
    }

    public String get(String headerName) throws IOException {
        Integer index = headersPositions.get(headerName);
        if (index == null) {
            throw new IOException("That CSV document doesn't have such header: " + headerName);
        }
        if (currentLine == null) {
            throw new IOException("You must call readRecord() method before you can get() anything");
        }
        return currentLine[index];
    }


    public void close() throws IOException {
        headersPositions = null;
        currentLine = null;
        reader.close();
    }
}
