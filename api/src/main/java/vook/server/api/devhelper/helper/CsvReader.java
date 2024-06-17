package vook.server.api.devhelper.helper;

import lombok.NoArgsConstructor;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class CsvReader {

    private String DELIMITER = ",";

    public CsvReader(String delimiter) {
        this.DELIMITER = delimiter;
    }

    public <T> List<T> readValue(InputStream inputStream, Class<T> clazz) {
        try {
            return readValue(new InputStreamReader(inputStream), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> readValue(File file, Class<T> clazz) {
        try {
            return readValue(new FileReader(file), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> readValue(String string, Class<T> clazz) {
        try {
            return readValue(new StringReader(string), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> readValue(Reader reader, Class<T> clazz) throws IOException {
        List<T> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            List<String> fieldNames = getFieldNames(br.readLine());
            while ((line = br.readLine()) != null) {
                records.add(createInstance(clazz, fieldNames, line));
            }
        }
        return records;
    }

    private List<String> getFieldNames(String line) {
        return Arrays.asList(line.split(DELIMITER));
    }

    private <T> T createInstance(Class<T> clazz, List<String> fieldNames, String line) {
        String[] values = line.split(DELIMITER);
        T instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
            for (int i = 0; i < fieldNames.size(); i++) {
                Field field = clazz.getDeclaredField(fieldNames.get(i));
                field.setAccessible(true);
                field.set(instance, values[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
