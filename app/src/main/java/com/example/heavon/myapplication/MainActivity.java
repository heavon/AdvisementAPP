package com.example.heavon.myapplication;

//import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.example.heavon.fragment.MainFragment;
import com.example.heavon.fragment.PersonFragment;
import com.example.heavon.fragment.SearchFragment;
import com.example.heavon.fragment.TypeFragment;
import com.example.heavon.fragment.TypeShowFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends BasicActivity implements
        MainFragment.OnFragmentInteractionListener,
        PersonFragment.OnFragmentInteractionListener,
        TypeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        TypeShowFragment.OnFragmentInteractionListener,
        View.OnClickListener{

    private Button mIndicatorMain;
    private Button mIndicatorType;
    private Button mIndicatorPerson;

    private List<Fragment> mFragmentList;
    private android.support.v4.app.FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mCurFragment;

    private int mCurPos = 0;

//    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();   //初始化UI
    }

    //初始化UI
    public void initUI() {
        initIndicator();
        initFragment();
    }

    //初始化Fragment
    public void initFragment() {
        mFragmentManager = getSupportFragmentManager();

        TypeFragment typeFragment = new TypeFragment();
        PersonFragment personFragment = new PersonFragment();

        MainFragment mainFragment = new MainFragment();
        SearchFragment searchFragment = (SearchFragment) mFragmentManager.findFragmentById(R.id.fragment_search);

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(mainFragment);
        mFragmentList.add(typeFragment);
        mFragmentList.add(personFragment);
        mFragmentList.add(searchFragment);

        changeIndicator(0);
//        mFragmentBox = findViewById(R.id.fragment_box);
    }

    //初始化底部导航
    public void initIndicator() {
        mIndicatorMain = (Button) findViewById(R.id.indicator_main);
        mIndicatorType = (Button) findViewById(R.id.indicator_type);
        mIndicatorPerson = (Button) findViewById(R.id.indicator_person);

        mIndicatorMain.setOnClickListener(this);
        mIndicatorType.setOnClickListener(this);
        mIndicatorPerson.setOnClickListener(this);

        mIndicatorMain.setTag(0);
        mIndicatorType.setTag(1);
        mIndicatorPerson.setTag(2);
    }

    //初始化搜索栏
    public void initSearch(){

    }

    //实现Fragment事件监听
    @Override
    public void onFragmentInteraction(Uri uri) {
        super.enterSearch();
    }

    @Override
    public void onClick(View view) {
        changeIndicator((Integer) view.getTag());
    }

    private void changeIndicator(int index){
        mCurPos = index;
        mFragmentTransaction = mFragmentManager.beginTransaction();

        if(null != mCurFragment){
            mFragmentTransaction.hide(mCurFragment);
        }

        Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentList.get(mCurPos).getClass().getName());
        if(null == fragment){
            fragment = mFragmentList.get(index);
        }
        mCurFragment = fragment;

        if(!fragment.isAdded()){
            mFragmentTransaction.add(R.id.fragment_box, fragment, fragment.getClass().getName());
        }else{
            mFragmentTransaction.show(fragment);
        }

        mFragmentTransaction.commit();

    }
}
