package com.example.heavon.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.constant.Constant;
import com.example.heavon.dao.UserDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.utils.DlgUtils;
import com.example.heavon.vo.User;

import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class FindPasswordActivity extends BasicActivity {

    private RequestQueue mQueue;
    private SharedPreferences mSp;

    private DlgUtils mDlgUtils;
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mVerifyView;
    private EditText mPasswordConfirmView;

    private Button mButtonFindPassword;
    private Button mButtonVerify;

    // verify relation.
    private int time = 60;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        mQueue = Volley.newRequestQueue(FindPasswordActivity.this);
        mSp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mDlgUtils = new DlgUtils(this);

        //        注册验证码事件
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);

        //初始化UI
        initUI();
    }

    //初始化UI
    public void initUI(){
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mVerifyView = (EditText) findViewById(R.id.verify);
        mPasswordConfirmView = (EditText) findViewById(R.id.password_confirm);

        mButtonFindPassword = (Button) findViewById(R.id.bt_find_password);
        mButtonFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptFindPassword();
            }
        });

        mButtonVerify = (Button) findViewById(R.id.bt_verify);
        mButtonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查电话号码是否正确
                final String username = mUsernameView.getText().toString();
                if(checkUsername(username)){
                    //发送验证码
                    SMSSDK.getVerificationCode("86",username);
                    mVerifyView.requestFocus();
                    mButtonVerify.setClickable(false);
                    handlerText.sendEmptyMessage(Constant.MESSAGE_VERIFY_COUNTDOWN);//开始倒计时
                }else{
                    mUsernameView.requestFocus();
                }
            }
        });
        //初始化顶部导航栏
        initToolBar(getString(R.string.find_password_title));
        //初始化正在注册框
        mDlgUtils.initDlg(R.style.loginingDlg, R.layout.find_password_dlg);
    }

    //检查验证码
    public boolean checkVerifyCode(String verifycode){
        boolean cancel = false;
        View focusView = null;

        UserDao userDao = new UserDao();
        // Check for a valid verifycode.
        if (TextUtils.isEmpty(verifycode)) {
            mVerifyView.setError(getString(R.string.error_field_required));
            focusView = mVerifyView;
            cancel = true;
        } else if (!userDao.isVerifyCodeValid(verifycode)) {
            mVerifyView.setError(getString(R.string.error_invalid_verifycode));
            focusView = mVerifyView;
            cancel = true;
        }

        if(cancel){
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }else{
            return true;
        }
    }
    //检查用户名
    public boolean checkUsername(String username){
        boolean cancel = false;
        View focusView = null;

        UserDao userDao = new UserDao();
        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!userDao.isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if(cancel){
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }else{
            return true;
        }
    }
    //检查密码
    public boolean checkPassword(String password, String passwordConfirm){
        boolean cancel = false;
        View focusView = null;

        UserDao userDao = new UserDao();
        // Check for a valid password, if the user entered one.
        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if(!userDao.isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if(!password.equals(passwordConfirm)){
            mPasswordView.setError(getString(R.string.error_inconsistent_password));
            focusView = mPasswordConfirmView;
            cancel = true;
        }

        if(cancel){
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    //尝试找回密码
    public void attemptFindPassword(){

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the register attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String passwordConfirm = mPasswordConfirmView.getText().toString();
        final String verifycode = mVerifyView.getText().toString();

        if(checkUsername(username) && checkVerifyCode(verifycode) && checkPassword(password, passwordConfirm)){
            mDlgUtils.showDlg();//正在找回密码
            //验证码校对
            flag = false;
            SMSSDK.submitVerificationCode("86", username, verifycode);
        }
    }

    Handler handlerText =new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what== Constant.MESSAGE_VERIFY_COUNTDOWN){
                if(time>0){
                    mButtonVerify.setText(time + "秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(Constant.MESSAGE_VERIFY_COUNTDOWN, 1000);
                }else{
                    time = 60;
                    mButtonVerify.setText(R.string.link_verify_short);
                    mButtonVerify.setClickable(true);
                }
            }else{
                mVerifyView.setText("");
                time = 60;
                mButtonVerify.setText(R.string.link_verify_short);
                mButtonVerify.setClickable(true);
            }
        };
    };

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
//                    Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    findPassword();
                    handlerText.sendEmptyMessage(Constant.MESSAGE_VERIFY_SUCCESS);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(flag){
                    mButtonVerify.setText(R.string.link_verify_short);
                    mButtonVerify.setClickable(true);
                    Toast.makeText(FindPasswordActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    mUsernameView.requestFocus();
                }else{
                    ((Throwable) data).printStackTrace();
//                    int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
                    mDlgUtils.closeDlg();
                    Toast.makeText(FindPasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();

//                    mVerifyView.selectAll();
//                    if (resId > 0) {
//                        Toast.makeText(RegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        }
    };

    //
    public void findPassword() {
        // Store values at the time of the register attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String passwordCofirm = mPasswordConfirmView.getText().toString();
        final String verifycode = mVerifyView.getText().toString();

        //发送请求
        UserDao userDao = new UserDao();
        userDao.findPassword(new User(username, password), verifycode, passwordCofirm, mQueue, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if((Boolean)result.get("error")){
                    //找回密码失败
                    mDlgUtils.closeDlg();
                    Toast.makeText(FindPasswordActivity.this, (String)result.get("msg"), Toast.LENGTH_SHORT).show();
                }else{
//                    String uid = (int) result.get("uid");
//                                String hashcode = result.get("hashcode").toString();
                    Log.i("find_password", password+" password!");

                    mDlgUtils.closeDlg();
                    Toast.makeText(FindPasswordActivity.this, "找回密码成功", Toast.LENGTH_SHORT).show();
                    //进入登录界面
                    gotoLogin();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
