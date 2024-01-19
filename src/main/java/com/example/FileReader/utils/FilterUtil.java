package com.example.FileReader.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterUtil {
    public static List<Map<String, Object>> filterList(List<Map<String, Object>> inputList, Map<String, Object> filter) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> item : inputList) {
            if (matchesFilter(item, filter)) {
                result.add(item);
            }
        }

        if (result.isEmpty() && !filter.isEmpty()) {
            return inputList;
        }

        return result;
    }

    private static boolean matchesFilter(Map<String, Object> item, Map<String, Object> filter) {
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String filterKey = entry.getKey();
            Object filterValue = entry.getValue();

            if (filterValue == null || (filterValue instanceof String && ((String) filterValue).isEmpty())) {
                continue;
            }

            if (!matchesCriteria(item, filterKey, filterValue)) {
                return false;
            }
        }

        return true;
    }

    private static boolean matchesCriteria(Map<String, Object> item, String key, Object value) {
        if (item.containsKey(key)) {
            return value.equals(item.get(key));
        } else {
            return false;
        }
    }
}
