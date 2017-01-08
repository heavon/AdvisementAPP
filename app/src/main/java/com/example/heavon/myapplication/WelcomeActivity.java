package com.example.heavon.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.dao.UserDao;
import com.example.heavon.interfaceClasses.HttpResponse;

import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {

    SharedPreferences mSp;
    RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //初始化配置

        //判断登录状态
        mQueue = Volley.newRequestQueue(WelcomeActivity.this);
        mSp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final Boolean autoLogin = mSp.getBoolean("AUTO_ISCHECK", false);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(autoLogin){
                    UserDao dao = new UserDao();
                    String username = mSp.getString("USER_NAME", "");
                    String password = mSp.getString("PASSWORD", "");
                    dao.login(username, password, mQueue, new HttpResponse<Map<String, Object>>() {
                        @Override
                        public void getHttpResponse(Map<String, Object> result) {
                            if((Boolean)result.get("error")){
                                gotoLogin();
                            }else{
                                int uid = (int) result.get("uid");
//                                String hashcode = result.get("hashcode").toString();
                                Log.i("login", String.valueOf(uid)+" login!");
                                //登录成功保存登录信息
                                SharedPreferences.Editor editor = mSp.edit();
                                editor.putInt("USER_ID", uid);
//                                editor.putString("HASHCODE", hashcode);
                                editor.commit();

                                //进入主界面
                                gotoMain();
                            }
                        }
                    });

                }else{
                    gotoLogin();
                }

            }
        }, 2000); //2000 for release
    }

    //跳转到主页面
    public void gotoMain(){
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    //跳转到登录页面
    public void gotoLogin(){
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }
}
