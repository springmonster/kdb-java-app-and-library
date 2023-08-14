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

public class KdbConverter {
    private static final ConcurrentHashMap<Class<?>, List<Field>> columnsMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, String[]> tablesMap = new ConcurrentHashMap<>();

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
        if (tablesMap.containsKey(clazz)) {
            System.out.println("Returning from cache");
            return tablesMap.get(clazz);
        } else {
            List<String> objects = new ArrayList<>();
            Field[] fields = FieldUtils.getAllFields(clazz);
            for (Field field : fields) {
                Column annotation = field
                        .getAnnotation(Column.class);
                objects.add(annotation.value());
            }
            String[] array = objects.toArray(new String[0]);
            tablesMap.put(clazz, objects.toArray(array));
            return array;
        }
    }

    public static Object[] createRows(List<?> list, Class<?> clazz) {
        Field[] fields;

        if (columnsMap.containsKey(clazz)) {
            System.out.println("Returning from cache");
            fields = columnsMap.get(clazz).toArray(new Field[0]);
        } else {
            fields = FieldUtils.getFieldsWithAnnotation(clazz, Column.class);
            columnsMap.put(clazz, Arrays.asList(fields));
        }

        int size = list.size();

        List<Object> result = new ArrayList<>();

        for (Field field : fields) {
            Object array = ArrayUtils.newInstance(field.getType(), size);
            result.add(array);
        }

        for (int i = 0; i < list.size(); i++) {
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
