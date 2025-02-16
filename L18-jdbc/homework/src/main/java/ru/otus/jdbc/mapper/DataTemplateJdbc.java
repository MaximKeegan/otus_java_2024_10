package ru.otus.jdbc.mapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import java.lang.reflect.Field;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String sql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, sql, List.of(id), this::mapRowToEntity);
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor.executeSelect(connection, sql, List.of(), this::mapRowsToEntities).orElse(new ArrayList<>());
    }

    @Override
    public long insert(Connection connection, T object) {
        String sql = entitySQLMetaData.getInsertSql();
        List<Object> params = getEntityParamsWithoutId(object);
        return dbExecutor.executeStatement(connection, sql, params);
    }

    @Override
    public void update(Connection connection, T object) {
        String sql = entitySQLMetaData.getUpdateSql();
        List<Object> params = getEntityParamsWithoutId(object);
        params.add(getIdValue(object));
        dbExecutor.executeStatement(connection, sql, params);
    }

    private T mapRowToEntity(ResultSet resultSet) {
        try {
            T entity = (T) entityClassMetaData.getConstructor().newInstance();
            List<Field> fields = entityClassMetaData.getAllFields();
            if (resultSet.next()) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldValue = resultSet.getObject(fieldName);
                    field.set(entity, fieldValue);
                }
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping row to entity", e);
        }
    }

    private List<T> mapRowsToEntities(ResultSet resultSet) {
        List<T> entities = new ArrayList<>();
        try {
            while (resultSet.next()) {
                entities.add(mapRowToEntity(resultSet));
            }
            return entities;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping rows to entities", e);
        }
    }

    private List<Object> getEntityParamsWithoutId(T entity) {
        List<Object> params = new ArrayList<>();
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                params.add(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error getting field value", e);
            }
        }
        return params;
    }

    private Object getIdValue(T entity) {
        Field idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        try {
            return idField.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error getting ID field value", e);
        }
    }
}
