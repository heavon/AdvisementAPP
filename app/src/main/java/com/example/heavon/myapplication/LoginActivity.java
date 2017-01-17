package com.example.heavon.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.dao.UserDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.utils.DlgUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    private RequestQueue mQueue;
    private SharedPreferences mSp;
    private DlgUtils mDlgUtils;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mLoginButton;
    private Button mRegisterButton;
    private Button mFindPasswordButton;
    private Button mGotoMainButton;
    private Dialog mLoginingDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mQueue = Volley.newRequestQueue(LoginActivity.this);
        mSp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mDlgUtils = new DlgUtils(this);

        //初始化UI
        initUI();
    }

    //初始化UI
    public void initUI(){
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
//        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton = (Button) findViewById(R.id.bt_login);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);

        mRegisterButton = (Button) findViewById(R.id.bt_link_register);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入到注册页面
                enterRegister();
            }
        });
        mGotoMainButton = (Button) findViewById(R.id.link_goto_main);
        mGotoMainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到主页面
                gotoMain();
            }
        });
        mFindPasswordButton = (Button) findViewById(R.id.bt_link_find_password);
        mFindPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入到找回密码页面
                enterFindPassword();
            }
        });
        //初始化正在登录框
        mDlgUtils.initDlg(R.style.loginingDlg, R.layout.logining_dlg);
    }
    //进入到注册页面
    public void enterRegister(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }
    //进入到忘记密码页面
    public void enterFindPassword(){
        Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
        LoginActivity.this.startActivity(intent);
    }
    //跳转到主页面
    public void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
//
//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        if (VERSION.SDK_INT >= 14) {
//            // Use ContactsContract.Profile (API 14+)
//            getLoaderManager().initLoader(0, null, this);
//        } else if (VERSION.SDK_INT >= 8) {
//            // Use AccountManager (API 8+)
//            new SetupEmailAutoCompleteTask().execute(null, null);
//        }
//    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        UserDao userDao = new UserDao();
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !userDao.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //正在登录
            mDlgUtils.showDlg();

            userDao.login(username, password, mQueue, new HttpResponse<Map<String, Object>>() {
                @Override
                public void getHttpResponse(Map<String, Object> result) {
                    if((Boolean)result.get("error")){
                        //登录失败
                        mDlgUtils.closeDlg();
                        Toast.makeText(LoginActivity.this, (String)result.get("msg"), Toast.LENGTH_SHORT).show();
                    }else{
                        int uid = (int) result.get("uid");
//                                String hashcode = result.get("hashcode").toString();
                        Log.i("login", String.valueOf(uid)+" login!");
                        //登录成功保存登录信息
                        SharedPreferences.Editor editor = mSp.edit();
                        editor.putInt("USER_ID", uid);
                        editor.putBoolean("AUTO_ISCHECK", true);
                        editor.putString("USER_NAME", username);
                        editor.putString("PASSWORD", password);
//                                editor.putString("HASHCODE", hashcode);
                        editor.commit();

                        mDlgUtils.closeDlg();
                        //进入主界面
                        gotoMain();
                    }
                }
            });
//            showProgress(true);
//            mAuthTask = new UserLoginTask(username, password);
//            mAuthTask.execute((Void) null);
        }
    }

//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }

//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        mUsernameView.setAdapter(adapter);
//    }
//
//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }
//
//    /**
//     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
//     * the email text field with results on the main UI thread.
//     */
//    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {
//
//        @Override
//        protected List<String> doInBackground(Void... voids) {
//            ArrayList<String> emailAddressCollection = new ArrayList<>();
//
//            // Get all emails from the user's contacts and copy them to a list.
//            ContentResolver cr = getContentResolver();
//            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
//                    null, null, null);
//            while (emailCur.moveToNext()) {
//                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
//                        .CommonDataKinds.Email.DATA));
//                emailAddressCollection.add(email);
//            }
//            emailCur.close();
//
//            return emailAddressCollection;
//        }
//
//        @Override
//        protected void onPostExecute(List<String> emailAddressCollection) {
//            addEmailsToAutoComplete(emailAddressCollection);
//        }
//    }
}

