package com.example.FileReader.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class KeywordSearchUtil {

    public static List<Map<String, Object>> findByKeyword(List<Map<String, Object>> items, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return items;
        }
        return findByKeyword(items, keyword, new DefaultKeywordMatcher());
    }

    public static List<Map<String, Object>> findByKeyword(List<Map<String, Object>> items, String keyword, KeywordMatcher matcher) {
        List<Map<String, Object>> matchingItems = matcher.findMatchingItems(items, keyword);
        return matchingItems;
    }

    public interface KeywordMatcher {
        List<Map<String, Object>> findMatchingItems(List<Map<String, Object>> items, String keyword);
    }

    public static class DefaultKeywordMatcher implements KeywordMatcher {
        @Override
        public List<Map<String, Object>> findMatchingItems(List<Map<String, Object>> items, String keyword) {
            return genericFindByKeyword(items, keyword);
        }

        private List<Map<String, Object>> genericFindByKeyword(List<Map<String, Object>> items, String keyword) {
            List<Map<String, Object>> matchingItems = new ArrayList<>();

            for (Map<String, Object> item : items) {
                if (containsKeywordInMap(item, keyword)) {
                    matchingItems.add(item);
                }
            }

            return matchingItems;
        }

        private boolean containsKeywordInMap(Map<String, Object> map, String keyword) {
            if (map == null || keyword == null) {
                return false;
            }

            for (Object value : map.values()) {
                if (value instanceof String) {
                    String stringValue = (String) value;
                    if (stringValue.matches(".*" + Pattern.quote(keyword) + ".*")) {
                        return true;
                    }
                } else if (value instanceof Number) {
                    String stringValue = String.valueOf(value);
                    if (stringValue.matches(".*" + Pattern.quote(keyword) + ".*")) {
                        return true;
                    }
                } else if (value instanceof List) {
                    List<?> subList = (List<?>) value;
                    if (containsKeywordInList(subList, keyword)) {
                        return true;
                    }
                } else if (value instanceof Map) {
                    Map<?, ?> subMap = (Map<?, ?>) value;
                    if (containsKeywordInMap((Map<String, Object>) subMap, keyword)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean containsKeywordInList(List<?> list, String keyword) {
            if (list == null || keyword == null) {
                return false;
            }

            for (Object listItem : list) {
                if (listItem instanceof Map) {
                    Map<String, Object> mapItem = (Map<String, Object>) listItem;
                    if (containsKeywordInMap(mapItem, keyword)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}