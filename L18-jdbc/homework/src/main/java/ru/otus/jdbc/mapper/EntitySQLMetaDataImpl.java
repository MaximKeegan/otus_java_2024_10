package ru.otus.jdbc.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        String idField = entityClassMetaData.getIdField().getName();
        return String.format("SELECT * FROM %s WHERE %s = ?", entityClassMetaData.getName(), idField);
    }

    @Override
    public String getInsertSql() {
        List<String> fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        String columns = String.join(", ", fields);
        String placeholders = fields.stream().map(f -> "?").collect(Collectors.joining(", "));
        return String.format("INSERT INTO %s (%s) VALUES (%s)", entityClassMetaData.getName(), columns, placeholders);
    }

    @Override
    public String getUpdateSql() {
        String idField = entityClassMetaData.getIdField().getName();
        List<String> fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        String setClause = fields.stream().map(f -> f + " = ?").collect(Collectors.joining(", "));
        return String.format("UPDATE %s SET %s WHERE %s = ?", entityClassMetaData.getName(), setClause, idField);
    }
}
