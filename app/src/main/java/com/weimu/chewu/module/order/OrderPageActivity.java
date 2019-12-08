package com.weimu.chewu.module.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.TabEntityB;
import com.weimu.chewu.module.loginx.LoginActivity;
import com.weimu.chewu.module.order.ed.OrderEdListFragment;
import com.weimu.chewu.module.order.ing.OrderIngListFragment;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.universalib.origin.view.pager.BaseFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderPageActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_page;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, OrderPageActivity.class);
        return intent;
    }

    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;


    private String[] mTitles = {"订单进行中", "已完成订单"};
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);


        fragmentList.add(OrderIngListFragment.newInstance());
        fragmentList.add(OrderEdListFragment.newInstance());

    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initViewPager();
    }

    private void initViewPager() {
        viewpager.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tablayout.setViewPager(viewpager, mTitles);

        tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //刷新当前tab的数据
        TabRefreshAction tabRefreshAction = (TabRefreshAction) fragmentList.get(tablayout.getCurrentTab());
        tabRefreshAction.onRefresh();
    }

    private void initToolBar() {

        ToolBarManager.with(this, getContentView())
                .setTitle("订单")
                .setNavigationIcon(R.drawable.back);
    }

}
