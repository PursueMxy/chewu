package com.weimu.chewu.popupwindow;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.CityB;
import com.weimu.chewu.module.city.adapter.CitySearchListAdapter;
import com.weimu.universalib.origin.view.popupwindow.BasePopupWindow;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/5/9 23:11
 * Description:
 */
public class CitySearchPopUpWindow extends BasePopupWindow {

    private CitySearchListAdapter mAdapter;

    public CitySearchPopUpWindow(Context context) {
        super(context);

        mAdapter = new CitySearchListAdapter(context);

        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_city_search_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        setContentView(view);
    }

    public void setDataTpAdapter(List<CityB> dataList) {
        mAdapter.setDataToAdapter(dataList);
    }


    public CitySearchPopUpWindow show(View view) {
        showAtLocation(view, Gravity.LEFT | Gravity.TOP, 0, 0);
        return this;
    }


}
