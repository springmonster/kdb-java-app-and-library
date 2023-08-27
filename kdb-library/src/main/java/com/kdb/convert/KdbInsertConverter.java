package com.kdb.convert;

import com.kdb.annotation.Column;
import com.kdb.annotation.Table;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class KdbInsertConverter {
    private static final ConcurrentHashMap<Class<?>, List<Field>> fieldsMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, String[]> columnsMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Class<?>, String> tablesMap = new ConcurrentHashMap<>();

    public static String createTable(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class must be annotated with @Table");
        }
        if (tablesMap.containsKey(clazz)) {
            return tablesMap.get(clazz);
        } else {
            Table annotation = clazz.getAnnotation(Table.class);
            String value = annotation.value();
            tablesMap.put(clazz, value);
            return value;
        }
    }

    public static String[] createColumns(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class must be annotated with @Table");
        }
        if (!clazz.isRecord()) {
            throw new IllegalArgumentException("Class must be a record");
        }
        if (clazz.getRecordComponents().length == 0) {
            throw new IllegalArgumentException("Class must have at least one field");
        }
        if (clazz.getRecordComponents().length != FieldUtils.getFieldsListWithAnnotation(clazz,
                Column.class).size()) {
            throw new IllegalArgumentException("Class must have all fields annotated with @Column");
        }
        if (columnsMap.containsKey(clazz)) {
            System.out.println("Returning from cache");
            return columnsMap.get(clazz);
        } else {
            List<String> objects = new ArrayList<>();
            Field[] fields = FieldUtils.getAllFields(clazz);
            for (Field field : fields) {
                Column annotation = field
                        .getAnnotation(Column.class);
                if (null == annotation.value()) {
                    throw new IllegalArgumentException("Field with @Column must have value");
                }
                objects.add(annotation.value());
            }
            String[] array = objects.toArray(new String[0]);
            columnsMap.put(clazz, objects.toArray(array));
            fieldsMap.put(clazz, Arrays.asList(fields));
            return array;
        }
    }

    public static Object[] createRows(List<?> list, Class<?> clazz) {
        Field[] fields;

        if (fieldsMap.containsKey(clazz)) {
            fields = fieldsMap.get(clazz).toArray(new Field[0]);
        } else {
            fields = FieldUtils.getFieldsWithAnnotation(clazz, Column.class);
            fieldsMap.put(clazz, Arrays.asList(fields));
        }

        final int size = list.size();

        List<Object> result = new ArrayList<>();

        for (Field field : fields) {
            Object array = ArrayUtils.newInstance(field.getType(), size);
            result.add(array);
        }

        for (int i = 0; i < size; i++) {
            Object o = list.get(i);

            for (int j = 0; j < fields.length; j++) {
                try {
                    Object[] o1 = (Object[]) result.get(j);
                    o1[i] = FieldUtils.readField(o, fields[j].getName(), true);
                } catch (IllegalAccessException ignored) {
                }
            }
        }

        Object[] objects = new Object[result.size()];

        for (int i = 0; i < result.size(); i++) {
            objects[i] = result.get(i);
        }

        return objects;
    }
}
