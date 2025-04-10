package ru.otus.services.processors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final List<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new ArrayList<>();
    }

    @Override
    public void process(SensorData data) {
        synchronized (dataBuffer) {
            dataBuffer.add(data);
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        }
    }

    public void flush() {
        synchronized (dataBuffer) {
            if (dataBuffer.isEmpty()) {
                return;
            }
            try {
                List<SensorData> dataToFlush =
                        new ArrayList<>(dataBuffer.subList(0, Math.min(bufferSize, dataBuffer.size())));
                dataToFlush.sort(Comparator.comparing(SensorData::getMeasurementTime));
                writer.writeBufferedData(dataToFlush);
                dataBuffer.subList(0, Math.min(bufferSize, dataBuffer.size())).clear();
            } catch (Exception e) {
                log.error("Ошибка в процессе записи буфера", e);
            }
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
