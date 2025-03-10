package ru.otus.dataprocessor;

import java.util.*;

import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        TreeMap<String, Double> result  = new TreeMap<>();

        for (Measurement measurement : data) {
            result.merge(measurement.name(), measurement.value(), Double::sum);
        }

        return result;
    }
}
