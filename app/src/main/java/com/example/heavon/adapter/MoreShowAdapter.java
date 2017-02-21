package com.example.heavon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.heavon.myapplication.R;
import com.example.heavon.utils.DensityUtil;
import com.example.heavon.vo.Show;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoreShowAdapter extends BaseRecyclerAdapter<MoreShowAdapter.MoreShowAdapterViewHolder> {
    private Context context;
    private List<Show> list;
    private int largeCardHeight, smallCardHeight;

    public MoreShowAdapter(List<Show> list, Context context) {
        this.list = list;
        this.context = context;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(final MoreShowAdapterViewHolder holder, int position, boolean isItem) {
        Show show = list.get(position);
        String thumb = "http://s16.sinaimg.cn/large/003gRgrCzy73OGZAV434f&690";
        if(show != null && show.getThumb() != null && !show.getThumb().isEmpty()){
            thumb = show.getThumb();
        }else{
            Log.e("MoreShowAdapter", "Show thumb error.");
        }
        Log.e("initShow", show.toString());

        Picasso.with(context).load(thumb).into(holder.thumbIv);
        holder.thumbIv.setTag(show.getId());
        holder.thumbIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------wait to modify-----------**/
                Toast.makeText(context, "click show "+holder.thumbIv.getTag(), Toast.LENGTH_SHORT).show();
                /**----------wait to modify-----------**/
            }
        });
        holder.investmentTv.setText(show.getInvestment_status());
        holder.nameTv.setText(show.getName());
        holder.castTv.setText(show.getCast());
        holder.favoriteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------wait to modify-----------**/
                Toast.makeText(context, "favorite", Toast.LENGTH_SHORT).show();
                /**----------wait to modify-----------**/
            }
        });
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            holder.rootView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public MoreShowAdapterViewHolder getViewHolder(View view) {
        return new MoreShowAdapterViewHolder(view, false);
    }

    public void setData(List<Show> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MoreShowAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_type_show_content, parent, false);
        v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        MoreShowAdapterViewHolder vh = new MoreShowAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(Show show, int position) {
        insert(list, show, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public class MoreShowAdapterViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public ImageView thumbIv;
        public ImageButton favoriteBt;
        public TextView investmentTv;
        public TextView nameTv;
        public TextView castTv;
        public int position;

        public MoreShowAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                thumbIv = (ImageView) itemView
                        .findViewById(R.id.thumb);
                favoriteBt = (ImageButton) itemView
                        .findViewById(R.id.bt_favorite);
                investmentTv = (TextView) itemView
                        .findViewById(R.id.investment_status);
                nameTv = (TextView) itemView
                        .findViewById(R.id.name);
                castTv = (TextView) itemView
                        .findViewById(R.id.cast);
                rootView = itemView
                        .findViewById(R.id.show_box);
            }

        }
    }

    public Show getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}