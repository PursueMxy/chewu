package com.weimu.chewu.module.city;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.CityB;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.local.CityListCase;
import com.weimu.chewu.interfaces.MyTextWathcher;
import com.weimu.chewu.module.city.adapter.CityAdapter;
import com.weimu.chewu.module.city.adapter.CitySearchListAdapter;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.popupwindow.CitySearchPopUpWindow;
import com.weimu.chewu.widget.WMProgressBar;
import com.weimu.universalib.utils.AssetsUtils;
import com.weimu.universalib.utils.EditTextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

//城市列表选择
public class CityListActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_city_list;
    }


    public static Intent newIntent(Context context, String currentCity) {
        Intent intent = new Intent(context, CityListActivity.class);
        intent.putExtra("currentCity", currentCity);
        return intent;
    }

    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.tv_city)
    TextView tvCity;

    @BindView(R.id.recyclerview_city)
    RecyclerView mRecyclerView;

    @BindView(R.id.lin_query)
    LinearLayout linQuery;
    @BindView(R.id.recyclerview_query)
    RecyclerView recyclerViewQuery;

    private CityListCase mCase;
    private CityAdapter mAdapter;

    private CitySearchListAdapter queryAdapter;

    private String currentCity;


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mCase = new CityListCaseImpl();
        currentCity = getIntent().getStringExtra("currentCity");
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        tvCity.setText(currentCity);
        initList();
        initQuery();
        initEdit();
        loadCityJsonData();

    }

    private void initQuery() {
        queryAdapter = new CitySearchListAdapter(this);
        queryAdapter.setOnItemClick(new BaseRecyclerMVPAdapter.OnItemClick<CityB>() {
            @Override
            public void onClick(CityB item, int position) {
                chooseCity(item.getName());
            }
        });
        recyclerViewQuery.setItemAnimator(new DefaultItemAnimator());
        recyclerViewQuery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewQuery.setAdapter(queryAdapter);
    }


    private List<CityB> queryCity = new ArrayList<>();

    private void initEdit() {
        etCity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    // do some your things
                    showToast("刷新");
                }
                return false;
            }
        });

        etCity.addTextChangedListener(new MyTextWathcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                //匹配出列表
                if (!TextUtils.isEmpty(query)) {
                    queryCity.clear();

                    List<CityB> dataList = mAdapter.getDataList();
                    for (CityB cityB : dataList) {
                        if (cityB.getName().contains(query)) {
                            queryCity.add(cityB);
                        }

                    }
                    queryAdapter.setDataToAdapter(queryCity);
                    linQuery.setVisibility(View.VISIBLE);
                } else {
                    linQuery.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initList() {
        mAdapter = new CityAdapter(getContext());
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick<CityB>() {
            @Override
            public void onClick(CityB item, int position) {
                chooseCity(item.getName());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        //Important！！！
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);
    }

    @SuppressLint("CheckResult")
    private void loadCityJsonData() {
        WMProgressBar.showProgressDialog(getContext());
        mCase.getCityList(getContext()).subscribe(new Consumer<List<CityB>>() {
            @Override
            public void accept(List<CityB> cities) throws Exception {
                WMProgressBar.hideProgressDialog();
//                for (CityB city : cities) {
//                    Log.e("weimu", "城市=" + city.getName() + " 编码=" + city.getCode() + " 首字母拼音=" + city.getParentPinYin());
//                }
                //Logger.e("城市列表加载成功 ");

                mAdapter.setDataToAdapter(cities);

            }
        });
    }


    @OnClick(R.id.iv_arrow_back)
    public void clickToBack() {
        onBackPressed();
    }


    @OnClick(R.id.tv_cancel)
    public void clickToCancel() {
        etCity.setText("");
    }

    //选择城市
    public void chooseCity(String city) {
        Intent intent = new Intent();
        intent.putExtra("city", city);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

}
