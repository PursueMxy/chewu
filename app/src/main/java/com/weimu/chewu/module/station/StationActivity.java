package com.weimu.chewu.module.station;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.StationListResultB;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.StationCase;
import com.weimu.chewu.module.station.adapter.StationListAdapter;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.universalib.origin.view.list.decoration.DividerItemDecoration;

import butterknife.BindView;

/**
 * 监测站列表
 */
public class StationActivity extends BaseActivity {


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_station;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, StationActivity.class);
        return intent;
    }

    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    private StationCase mCase;
    private StationListAdapter mAdapter;


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mCase = new StationCaseImpl();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initList();
        loadData();
    }


    private void initList() {
        mAdapter = new StationListAdapter(getContext());
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick<StationListResultB.StationB>() {
            @Override
            public void onClick(StationListResultB.StationB item, int position) {
                Intent intent = new Intent();
                intent.putExtra("station", item);
                setResult(RESULT_OK, intent);
                onBackPressed();

            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL, 1));
        recyclerview.setAdapter(mAdapter);

        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void loadData() {
        mCase.getStations().subscribe(new OnRequestObserver<StationListResultB>() {

            @Override
            protected boolean OnSucceed(StationListResultB result) {
                mAdapter.setDataToAdapter(result.getData());
                endRefreshAnimation();
                return super.OnSucceed(result);
            }
        });
    }


    //开始刷新动画
    public void beginRefreshAnimation() {
        refreshlayout.post(new Runnable() {
            @Override
            public void run() {
                refreshlayout.setRefreshing(true);
            }
        });
    }

    //结束刷新动画
    public void endRefreshAnimation() {
        refreshlayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshlayout != null)
                    refreshlayout.setRefreshing(false);
            }
        }, 0);
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("选择监测站")
                .setNavigationIcon(R.drawable.back);
    }

}



