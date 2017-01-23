package com.example.heavon.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.heavon.fragment.MainFragment;
import com.example.heavon.fragment.PersonFragment;
import com.example.heavon.fragment.SearchFragment;
import com.example.heavon.fragment.TypeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, PersonFragment.OnFragmentInteractionListener, TypeFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    private Button mIndicatorMain;
    private Button mIndicatorType;
    private Button mIndicatorPerson;

    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragmentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

    }

    //初始化Fragment
    public void initFragment() {
        MainFragment mainFragment = new MainFragment();
        TypeFragment typeFragment = new TypeFragment();
        PersonFragment personFragment = new PersonFragment();

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(mainFragment);
        mFragmentList.add(typeFragment);
        mFragmentList.add(personFragment);

        mFragmentManager = getFragmentManager();

//        mFragmentBox = findViewById(R.id.fragment_box);

    }

    //初始化UI
    public void initUI() {
        initIndicator();
        initFragment();
    }

    //初始化底部导航
    public void initIndicator() {
        mIndicatorMain = (Button) findViewById(R.id.indicator_main);
        mIndicatorType = (Button) findViewById(R.id.indicator_type);
        mIndicatorPerson = (Button) findViewById(R.id.indicator_person);

        mIndicatorMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_box, mFragmentList.get(0));
                mFragmentTransaction.commit();
            }
        });
        mIndicatorType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_box, mFragmentList.get(1));
                mFragmentTransaction.commit();
            }
        });
        mIndicatorPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_box, mFragmentList.get(2));
                mFragmentTransaction.commit();
            }
        });
    }

    //实现Fragment事件监听
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
