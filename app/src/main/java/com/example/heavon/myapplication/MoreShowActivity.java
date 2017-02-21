package com.example.heavon.myapplication;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.andview.refreshview.listener.OnBottomLoadMoreTime;
import com.andview.refreshview.listener.OnTopRefreshTime;
import com.example.heavon.adapter.MoreShowAdapter;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.fragment.TypeShowFragment;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.views.TypeShowContentView;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;

import java.util.List;
import java.util.Map;

public class MoreShowActivity extends BasicActivity {

    private final String PERPAGE = "2";
    private String mType = "分类";
    private int mCurPage = 1;
    private List<Show> mShowList;

    //UI reference.
    XRefreshView mShowListScrollView;
//    SwipeRefreshLayout mShowListSwipeView;

    private RequestQueue mQueue;
    private TextView mShowNone;
    private RecyclerView mShowRecyclerView;
    //adapter.
    MoreShowAdapter mMoreShowAdapter;
    private int lastVisibleItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_show);
        mQueue = Volley.newRequestQueue(this);

        initialize();
        initUI();
    }

    public void initialize() {
        Bundle bundle = this.getIntent().getExtras();
        String type = bundle.getString("type");
        if (type != null && !type.isEmpty()) {
            mType = type;
        }
    }

    public void initUI() {

        mShowNone = (TextView) findViewById(R.id.show_none);
        // Refresh scroll.
        mShowListScrollView = (XRefreshView) findViewById(R.id.show_list_scroll);
        mShowListScrollView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh show list.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mShowListScrollView.stopRefresh();
                    }
                }, 2000);
                super.onRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loadMoreShow();
                super.onLoadMore(isSilence);
            }
        });
        mShowListScrollView.setPinnedTime(1000);
//        mShowListScrollView.setMoveForHorizontal(true);
        mShowListScrollView.setPullLoadEnable(true);
        mShowListScrollView.setPullRefreshEnable(true);

//        mShowListSwipeView = (SwipeRefreshLayout) findViewById(R.id.show_list_swipe);
//        mShowListSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadMoreShow();
//            }
//        });
//        mShowListSwipeView.setProgressViewOffset(false, 0, (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                        .getDisplayMetrics()));

        // Show list.
        mShowRecyclerView = (RecyclerView) findViewById(R.id.show_recycler_view);
        mShowRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mShowRecyclerView.setLayoutManager(layoutManager);
///**---------------------**/
//        mShowRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == mMoreShowAdapter.getItemCount()) {
//                    mShowListSwipeView.setRefreshing(true);
//                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
////                    loadMoreShow();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//            }
//        });
///**---------------------**/

        initShowList();
        initToolBar(mType);
    }

    public void initShowList() {
        ShowDao dao = new ShowDao();
        ShowFilter filter = new ShowFilter("localization", mType);
        Log.e("moreShowLocalization", mType);
        filter.addFilter("perpage", PERPAGE);
        filter.addFilter("page", String.valueOf(getCurPage()));
        dao.initShowsByFilter(filter, mQueue, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //error
                    Toast.makeText(MoreShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("moreshowActivity", "showlist is null");
                        return;
                    }
                    mShowList = showList;
                    if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
                        mShowListScrollView.removeView(mShowNone);
//                        mShowListSwipeView.removeView(mShowNone);
                    }
                    mMoreShowAdapter = new MoreShowAdapter(showList, MoreShowActivity.this);
                    //当需要使用数据不满一屏时不显示点击加载更多的效果时，解注释下面的三行代码
                    //并注释掉第四行代码
//                    CustomerFooter customerFooter = new CustomerFooter(this);
//                    customerFooter.setRecyclerView(recyclerView);
//                    recyclerviewAdapter.setCustomLoadMoreView(customerFooter);
//                    mMoreShowAdapter.setCustomLoadMoreView(new XRefreshViewFooter(MoreShowActivity.this));

                    mShowRecyclerView.setAdapter(mMoreShowAdapter);
                }
            }
        });
    }

    public void loadMoreShow() {
        ShowDao dao = new ShowDao();
        ShowFilter filter = new ShowFilter("localization", mType);
        filter.addFilter("perpage", PERPAGE);
        filter.addFilter("page", String.valueOf(getCurPage() + 1));
        dao.initShowsByFilter(filter, mQueue, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if ((Boolean) result.get("error")) {
                    //error
                    Toast.makeText(MoreShowActivity.this, (String) result.get("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    List<Show> showList = (List<Show>) result.get("showList");
                    if (showList == null || showList.isEmpty()) {
                        Log.e("moreshowActivity", "showlist is null");
                        mShowListScrollView.setLoadComplete(true);
                        return;
                    } else {
                        mShowList.addAll(showList);
                        if (mShowNone != null && mShowNone.getVisibility() == View.VISIBLE) {
                            mShowListScrollView.removeView(mShowNone);
//                            mShowListSwipeView.removeView(mShowNone);
                        }
                        for (Show show : showList) {
                            mMoreShowAdapter.insert(show, mMoreShowAdapter.getAdapterItemCount());
                        }
                        // add page.
                        mCurPage++;
                        // 刷新完成必须调用此方法停止加载
                        mShowListScrollView.stopLoadMore();
//                        mShowListSwipeView.setRefreshing(false);
                    }
                }
            }
        });
    }

    /**
     * 获取当前页面号
     *
     * @return 当前页面
     */
    private int getCurPage() {
        int curPage = 1;

        return mCurPage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_list) {
            /**----------wait to modify-----------**/

            Toast.makeText(this, "change list", Toast.LENGTH_SHORT).show();

            /**----------wait to modify-----------**/

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
