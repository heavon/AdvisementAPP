package com.example.heavon.dao;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.utils.HttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yadong on 16/3/5.
 */
public class UserDao extends BaseDao{

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param queue 网络请求队列
     * @param response 回调方法
     */
    public void login(String username, String password, RequestQueue queue, final HttpResponse<Map<String, Object>> response){
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        String loginUrl = "http://192.168.1.110/ad/index.php/Home/User/login";
        //发送登录请求
        HttpUtils http = new HttpUtils(queue);
        http.postString(loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                JSONObject json;
                Boolean responseError;
                try {
                    json = new JSONObject(s);
                    responseError = json.getBoolean("error");
                    returnMap.put("error", responseError);
                    if (responseError) {
                        String msg = json.getString("data");
                        if("必须用post方式".equals(msg)){
                            msg = "请求链接失效";
                        }
                        returnMap.put("msg", msg);
                    } else {
                        JSONObject data = json.getJSONObject("data");
                        int uid = data.getInt("id");
//                        String hashcode = data.getString("hashcode");
//                        String token = data.getString("token");
                        returnMap.put("uid", uid);
//                        returnMap.put("hashcode", hashcode);
//                        returnMap.put("token", token);
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, params);
    }

}
