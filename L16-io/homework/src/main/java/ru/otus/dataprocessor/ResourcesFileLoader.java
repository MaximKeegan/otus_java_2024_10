package ru.otus.dataprocessor;

import java.util.Collections;
import java.util.List;
import ru.otus.model.Measurement;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.IOException;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        ObjectMapper objectMapper = new ObjectMapper();

        // Загружаем файл из ресурсов
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Файл не найден: " + fileName);
            }

            // Парсим JSON в список объектов Measurement
            return objectMapper.readValue(inputStream, new TypeReference<List<Measurement>>() {});
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
