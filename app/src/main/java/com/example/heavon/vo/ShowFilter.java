package com.example.heavon.vo;

import android.util.Log;

import com.example.heavon.interfaceClasses.Filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heavon on 2017/2/15.
 */

public class ShowFilter implements Filter<String> {

    Map<String, String> mFilter;

    public ShowFilter() {
        this.mFilter = new HashMap<String, String>();
    }

    public ShowFilter(String name, String value){
        this.mFilter = new HashMap<String, String>();
        mFilter.put(name, value);
    }

    public ShowFilter(Map<String, String> mFilter) {
        this.mFilter = mFilter;
    }

    @Override
    public void setFilter(Map<String, String> filter) {
        this.mFilter = filter;
    }

    @Override
    public void addFilter(String k, String v) {
        if (mFilter != null) {
            mFilter.put(k, v);
        } else {
            Log.e("showFilter", "mFilter is null.");
        }
    }

    @Override
    public Map<String, String> getFilter() {
        return this.mFilter;
    }

    /**
     * 判断过滤条件是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        if (mFilter == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 转换过滤条件为url参数形式
     * @return 转换后url字符串
     */
    public String parseFilterToURL(){
        String filterUrl = "";
        for (Map.Entry entry : mFilter.entrySet()){
            filterUrl += "/" + entry.getKey() + "/" + entry.getValue();
        }
        return filterUrl;
    }
}
