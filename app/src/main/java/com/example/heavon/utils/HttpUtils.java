package com.example.heavon.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.heavon.interfaceClasses.HttpResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yadong on 16/3/12.
 */
public class HttpUtils {

    private RequestQueue mQuene;
    private Map<String, String> mParams;
    private JSONObject mJsonParams;

    public HttpUtils(RequestQueue queue) {
        if(queue != null){
            mQuene = queue;
        }else{
            Log.e("http error", "Request queue is not init");
        }
    }

    /**
     * 获取服务器IP地址
     * @return 服务器IP地址
     */
    public static String getHostIP(){
        String hostIP = "127.0.0.1";

        return hostIP;
    }

    /**
     * 获取服务器后台地址
     * @return 服务器后台地址
     */
    public static String getHost(){
        String host = "http://192.168.1.104/ad/index.php/Home/";

        return host;
    }

    /**
     * GET直接请求
     * @param url 请求链接
     * @return
     */
    public boolean get(String url, final HttpResponse httpResponse){
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                httpResponse.getHttpResponse(s);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("http response error", volleyError.getMessage(), volleyError);
            }
        });
        mQuene.add(request);
        return true;
    }

    /**
     * GET请求字符数据
     *
     * @param url      请求链接
     * @param listener 回调监听器
     * @return
     */
    public boolean getString(String url, Response.Listener<String> listener) {
        StringRequest request = new StringRequest(url, listener
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("http response error", volleyError.getMessage(), volleyError);
            }
        });
        mQuene.add(request);
        return true;
    }

    /**
     * POST请求字符数据
     *
     * @param url      请求链接
     * @param listener 回调监听器
     * @param params 请求参数
     * @return
     */
    public boolean postString(String url, Response.Listener<String> listener, Map<String, String> params) {
        this.mParams = params;
        StringRequest request = new StringRequest(Request.Method.POST, url, listener
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("http response error", volleyError.getMessage(), volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //在这里设置需要post的参数
                return mParams;
            }
        };
        mQuene.add(request);
        return true;
    }

    //Json部分有Bug，需修复

    //

    /**
     * GET请求Json数据
     *
     * @param url      请求链接
     * @param listener 回调监听器
     * @return
     */
    public boolean getJson(String url, Response.Listener<JSONObject> listener) {
        JsonObjectRequest request = new JsonObjectRequest(url, null, listener
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("http response error", volleyError.getMessage(), volleyError);
            }
        });
        mQuene.add(request);
        return true;
    }

    /**
     * POST请求Json数据
     *
     * @param url      请求链接
     * @param listener 回调监听器
     * @param params    请求参数
     * @return
     */
    public boolean postJson(String url, Response.Listener<JSONObject> listener, JSONObject params) {
        this.mJsonParams = params;
        JsonObjectRequest request = new JsonObjectRequest(url, params, listener
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("http response error", volleyError.getMessage(), volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                return mParams;
            }
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };
        mQuene.add(request);
        return true;
    }

}
