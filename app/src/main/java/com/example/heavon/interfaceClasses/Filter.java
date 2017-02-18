package com.example.heavon.interfaceClasses;

import java.util.Map;

/**
 * Created by heavon on 2017/2/15.
 */

public interface Filter<T> {
    void setFilter(Map<String, T> filter);
    void addFilter(String k, T v);
    Map<String, T> getFilter();
}
