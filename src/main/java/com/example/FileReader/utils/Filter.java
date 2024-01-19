package com.example.FileReader.utils;

import java.util.Map;

public interface Filter
{
    boolean operation(Map<String,Object> item);
}
