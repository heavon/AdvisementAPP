package com.example.heavon.views;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heavon.myapplication.R;
import com.example.heavon.vo.Show;
import com.squareup.picasso.Picasso;

/**
 * Created by heavon on 2017/2/15.
 */

public class TypeShowContentView extends LinearLayout {

    private ImageView mThumbView;
    private ImageButton mFavoriteButton;
    private TextView mInvestmentStatusView;
    private TextView mNameView;
    private TextView mCastView;

    public TypeShowContentView(Context context) {
        super(context);
        initialize(context);
    }

    public TypeShowContentView(Context context, AttributeSet attr) {
        super(context, attr);
        initialize(context);
    }

    public TypeShowContentView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initialize(context);
    }

    private void initialize(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_type_show_content, this);

        mThumbView = (ImageView) findViewById(R.id.thumb);
        if(mThumbView== null) {
            Log.e("TypeShowContentView", "thumb view is null.");
            return;
        }
        mFavoriteButton = (ImageButton) findViewById(R.id.bt_favorite);

        mInvestmentStatusView = (TextView) findViewById(R.id.investment_status);
        mNameView = (TextView) findViewById(R.id.name);
        mCastView = (TextView) findViewById(R.id.cast);
    }

    public void initShow(Show show){
        String thumb;
        if(show == null){
            Log.e("TypeShowContentView", "Show Info is null.");
            return;
        } else if (show.getThumb() == null || show.getThumb().isEmpty()){
            Log.e("TypeShowContentView", "Show thumb is null.");
            thumb = "http://s16.sinaimg.cn/large/003gRgrCzy73OGZAV434f&690";
        } else {
            thumb = show.getThumb();
        }

        Log.e("initShow", thumb);
        Picasso.with(getContext()).load(thumb).into(mThumbView);
        mThumbView.setTag(show.getId());
        mThumbView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------wait to modify-----------**/
                Toast.makeText(getContext(), "click show "+view.getTag(), Toast.LENGTH_SHORT).show();
                /**----------wait to modify-----------**/
            }
        });

        mFavoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------wait to modify-----------**/
                Toast.makeText(getContext(), "favorite", Toast.LENGTH_SHORT).show();
                /**----------wait to modify-----------**/
            }
        });

        mInvestmentStatusView.setText(show.getInvestment_status());
        mNameView.setText(show.getName());
        mCastView.setText(show.getCast());
    }

}
