package com.example.FileReader.utils;



import com.example.FileReader.bean.CustomSort;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SortUtil {
    public static <T> void sortList(List<T> list, List<CustomSort> customSorts) {
        List<Comparator<T>> comparators = new ArrayList<>();

        for (CustomSort cs : customSorts) {
            String key = cs.getKey();
            int isAsc = cs.getIsAsc();

            Comparator<T> comparator = (o1, o2) -> {
                try {
                    Object value1 = getValueForKey(o1, key);
                    Object value2 = getValueForKey(o2, key);

                    if (value1 instanceof Comparable<?> && value2 instanceof Comparable<?>) {
                        @SuppressWarnings("unchecked")
                        Comparable<Object> comparableValue1 = (Comparable<Object>) value1;
                        int result = comparableValue1.compareTo(value2);

                        return (isAsc == 0) ? result : -result;
                    } else {
                        String strValue1 = String.valueOf(value1);
                        String strValue2 = String.valueOf(value2);
                        int result = strValue1.compareTo(strValue2);

                        return (isAsc == 0) ? result : -result;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            };

            comparators.add(comparator);
        }

        Comparator<T> combinedComparator = (o1, o2) -> {
            for (Comparator<T> comparator : comparators) {
                int result = comparator.compare(o1, o2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        };

        Collections.sort(list, combinedComparator);
    }

    private static Object getValueForKey(Object object, String key) throws IllegalAccessException, NoSuchFieldException {
        if (object instanceof Map) {
            return ((Map<?, ?>) object).get(key);
        } else {
            try {
                String getterMethodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
                Method getter = object.getClass().getMethod(getterMethodName);
                return getter.invoke(object);
            } catch (NoSuchMethodException | InvocationTargetException e) {
                Field field = object.getClass().getDeclaredField(key);
                field.setAccessible(true);
                return field.get(object);
            }
        }
    }
}