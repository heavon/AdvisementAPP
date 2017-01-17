package com.example.heavon.dao;

import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.utils.HttpUtils;
import com.example.heavon.vo.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yadong on 16/3/5.
 */
public class UserDao extends BaseDao{

    /**
     * 登录用户
     * @param username 用户名
     * @param password 密码
     * @param queue 网络请求队列
     * @param response 回调方法
     */
    public void login(String username, String password, RequestQueue queue, final HttpResponse<Map<String, Object>> response){
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        String loginUrl = HttpUtils.getHost() + "User/login";
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

    /**
     * 注册用户
     * @param user  用户名
     * @param verifyCode    验证码
     * @param queue 网络请求队列
     * @param response  回调方法
     */
    public void register(User user, String verifyCode, RequestQueue queue, final HttpResponse<Map<String, Object>> response){
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("verifyCode", verifyCode);
        String registerUrl = HttpUtils.getHost() + "User/register";
        //发送登录请求
        HttpUtils http = new HttpUtils(queue);
        http.postString(registerUrl, new Response.Listener<String>() {
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
                        int uid = json.getInt("data");
                        returnMap.put("uid", uid);
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, params);
    }

    public void findPassword(User user, String verifyCode, String passwordConfirm, RequestQueue queue, final HttpResponse<Map<String, Object>> response){
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("verifyCode", verifyCode);
        params.put("passwordConfirm", passwordConfirm);
        String registerUrl = HttpUtils.getHost() + "User/findPassword";
        //发送登录请求
        HttpUtils http = new HttpUtils(queue);
        http.postString(registerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                JSONObject json;
                Boolean responseError;
                try {
                    json = new JSONObject(s);
                    responseError = json.getBoolean("error");
                    returnMap.put("error", responseError);
                    String msg = json.getString("data");
                    if (responseError) {
                        if("必须用post方式".equals(msg)){
                            msg = "请求链接失效";
                        }
                    }
                    returnMap.put("msg", msg);
                } catch (Exception e) {
                    e.getStackTrace();
                }
                //将returnMap作为参数回调
                response.getHttpResponse(returnMap);
            }
        }, params);
    }

    /**
     * 邮箱格式有效
     * @param email 邮箱
     * @return 是否有效 true或false
     */
    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * 用户名格式有效
     * @param username 用户名
     * @return 是否有效 true或false
     */
    public boolean isUsernameValid(String username) {

        return true;//测试用
//        return username.length() > 4;
    }

    /**
     * 密码格式有效
     * @param password 密码
     * @return 是否有效 true或false
     */
    public boolean isPasswordValid(String password) {
        return true;//测试用
//        return password.length() > 4;
    }

    /**
     * 验证码格式有效
     * @param verifycode 验证码
     * @return 是否有效 true或false
     */
    public boolean isVerifyCodeValid(String verifycode){
        return TextUtils.isDigitsOnly(verifycode);
    }

    /**
     * 电话号码格式有效
     * @param phone 电话号码
     * @return 是否有效 true或false
     */
    public boolean isTelephone(String phone){

        return false;
    }
    //-------------未完成----------------
    public boolean verifyCode(String code, RequestQueue queue, final HttpResponse<Map<String, Object>> response){

        return false;
    }

}
