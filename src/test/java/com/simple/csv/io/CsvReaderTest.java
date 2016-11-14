package com.simple.csv.io;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Illia Vinnichenko on 28.08.2016.
 */
public class CsvReaderTest {

    public static final String HEADER_1 = "header1";
    public static final String HEADER_2 = "header2";
    public static final String HEADER_3 = "header3";
    public static final String HEADER_4 = "header4";
    public static final String HEADER_5 = "header5";

    public static final String VALUE_1 = "1";
    public static final String VALUE_2 = "2";
    public static final String VALUE_3 = "3";
    public static final String VALUE_4 = "4";
    public static final String VALUE_5 = "5";

    public static final String TEST_FILE_PATH = "src/test/resources/test.csv";
    public static final String WRONG_HEADER = "wrong_header";

    private CsvReader csvReader;

    @Before
    public void setUp() throws IOException {
        this.csvReader = new CsvReader(TEST_FILE_PATH);
    }

    @Test
    public void canReadOneValue() throws IOException {
        csvReader.readRecord();
        assertOneValueInLine(HEADER_3, VALUE_3, 1);
    }

    @Test
    public void canReadOneRecord() throws IOException {
        csvReader.readRecord();
        int line = 1;
        assertOneValueInLine(HEADER_1, VALUE_1, line);
        assertOneValueInLine(HEADER_2, VALUE_2, line);
        assertOneValueInLine(HEADER_3, VALUE_3, line);
        assertOneValueInLine(HEADER_4, VALUE_4, line);
        assertOneValueInLine(HEADER_5, VALUE_5, line);
    }

    @Test
    public void canReadAllRecords() throws IOException {
        int expectedSize = 4;
        int actualSize = 0;
        while (csvReader.readRecord()) {
            actualSize++;
            assertOneValueInLine(HEADER_1, VALUE_1, actualSize);
            assertOneValueInLine(HEADER_2, VALUE_2, actualSize);
            assertOneValueInLine(HEADER_3, VALUE_3, actualSize);
            assertOneValueInLine(HEADER_4, VALUE_4, actualSize);
            assertOneValueInLine(HEADER_5, VALUE_5, actualSize);
        }
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test(expected = IOException.class)
    public void cannotGetValueByWrongHeader() throws IOException {
        csvReader.readRecord();
        csvReader.get(WRONG_HEADER);
    }

    @Test(expected = IOException.class)
    public void cannotGetValueBeforeReadRecord() throws IOException {
        csvReader.get(HEADER_1);
    }

    @After
    public void ternDown() throws IOException {
        csvReader.close();
    }

    private void assertOneValueInLine(String header, String value, int line) throws IOException {
        String actual = csvReader.get(header);
        String expected = line + "_" + value;
        Assert.assertEquals(expected, actual);
    }


}
