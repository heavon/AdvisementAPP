package com.example.heavon.dao;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.vo.ShowFilter;
import com.example.heavon.utils.HttpUtils;
import com.example.heavon.vo.Show;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heavon on 2017/2/14.
 */

public class ShowDao extends BaseDao {


    /**
     * 根据筛选条件初始化节目列表
     * @param filter 过滤条件
     * @param queue 网络请求队列
     * @param response 回调方法
     * @return
     */
    public void initShowsByFilter(ShowFilter filter, RequestQueue queue, final HttpResponse<Map<String, Object>> response){

        List<Show> showList = new ArrayList<Show>();

        String getShowsUrl = HttpUtils.getHost() + "Show/getShows";
//        if(!filter.isEmpty()){
//            getShowsUrl += filter.parseFilterToURL();
//        }

        if(queue == null){
            Log.e("showDao", "queue is null");
            return ;
        }
        HttpUtils http = new HttpUtils(queue);
        http.postString(getShowsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                JSONObject json;
                Boolean responseError;
                try {
//                    json = new JSONObject(s);

                    json = JSON.parseObject(s);Log.e("showshow", "here");
                    responseError = json.getBoolean("error");
//                    responseError = json.getBoolean("error");
                    returnMap.put("error", responseError);
                    if (responseError) {
                        String msg = json.getString("data");
                        if("必须用post方式".equals(msg)){
                            msg = "请求链接失效";
                        }
                        returnMap.put("msg", msg);
                    } else {
                        //
                        JSONArray dataList = json.getJSONArray("data");
                        Log.e("showDao",dataList.toString());
                        String dataListString = JSON.toJSONString(dataList);
                        Log.e("showList", dataListString );
                        List<Show> showList = JSON.parseArray(dataListString, Show.class);

                        returnMap.put("showList", showList);
                        //
                    }
                } catch (Exception e) {
                    Log.e("showError", e.getMessage());
                    e.getStackTrace();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("error", true);
                    map.put("msg", e.getMessage());
                    returnMap = map;
                }
                Log.e("show", returnMap.toString());
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, filter.getFilter());

    }

    public Show getShow(){
        Show show = new Show();

        return show;
    }

    public List<Show> getFavoriteShows(){
        List<Show> favoriteList = new ArrayList<Show>();


        return favoriteList;
    }
}
