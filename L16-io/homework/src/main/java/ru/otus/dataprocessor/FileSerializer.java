package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Преобразуем Map в JSON-строку
        String json = objectMapper.writeValueAsString(data);

        // Записываем JSON в файл
        try (OutputStream outputStream = new FileOutputStream(fileName);
            Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(json);
        }
    }
}
