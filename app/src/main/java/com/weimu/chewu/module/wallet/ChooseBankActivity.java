package com.weimu.chewu.module.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.wallet.BankInfo;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ChooseBankCase;
import com.weimu.chewu.module.wallet.bank.choose.ChooseBankAdapter;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.utils.FastScrollManager;
import com.weimu.chewu.widget.ToolBarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

//选择银行
public class ChooseBankActivity extends BaseActivity {
    @BindView(R.id.choose_bank_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.choose_bank_rv)
    RecyclerView recyclerView;
    private ChooseBankAdapter adapter;
    private ArrayList<BankInfo> bankInfoList = new ArrayList<>();
    private int type;
    @BindView(R.id.empty_bank)
    RelativeLayout rl_empty;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_choose_bank;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        getData();
        initToolbar();
        initRecycleView();
        bindEven();

    }

    private void getData() {
        type = getIntent().getIntExtra("type", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBankList();
    }

    private void initToolbar() {
        ToolBarManager.with(this, getContentView())
                .setLeftMenuIconRes(R.drawable.back)
                .setRightMenuIconRes(R.drawable.add_bank)
                .setTitle("选择银行");
    }

    /**
     * recyclerview配置
     */
    private void initRecycleView() {
        LinearLayoutManager layout = new FastScrollManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);//竖直放置
        adapter = new ChooseBankAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.addData(bankInfoList);
    }

    private void bindEven() {
        ToolBarManager.with(this, getContentView())
                .setRightMenuIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChooseBankActivity.this, AddCardActivity.class);
                        startActivity(intent);
                    }
                });
        ToolBarManager.with(this, getContentView())
                .setLeftMenuIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBankList();
            }
        });
        adapter.setOnItemViewClickListener(new ChooseBankAdapter.OnItemViewClickListener() {
            //修改点击
            @Override
            public void onEditClick(int position) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(ChooseBankActivity.this, AddCardActivity.class);
                bundle.putSerializable("bankInfo", adapter.getItem(position));
                bundle.putInt("type", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            //删除点击
            @Override
            public void onDeleteClick(int position) {
                deleteCardDialog(adapter.getItem(position).getId() + "", position);
            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick<BankInfo>() {
            @Override
            public void onClick(BankInfo item, int position) {
                if (type == 1) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bankInfo", adapter.getItem(position));
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    finish();
                }
            }
        });
    }

    private void getBankList() {
        RetrofitClient.getDefault()
                .create(ChooseBankCase.class)
                .getBankList()
                .compose(RxSchedulers.<NormalResponseB<List<BankInfo>>>toMain())
                .subscribe(new OnRequestObserver<List<BankInfo>>() {
                    @Override
                    protected boolean OnSucceed(List<BankInfo> result) {
                        if (result == null || result.size() == 0) {
                            rl_empty.setVisibility(View.VISIBLE);
                        } else {
                            rl_empty.setVisibility(View.GONE);
                        }
                        adapter.setDataToAdapter(result);
                        refreshLayout.setRefreshing(false);
                        return super.OnSucceed(result);
                    }

                    @Override
                    protected boolean onFailure(String message) {
                        refreshLayout.setRefreshing(false);
                        return super.onFailure(message);
                    }
                });
    }

    /**
     * 选择拍照弹窗
     */
    private void deleteCardDialog(final String id, final int position) {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
//        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_modify_phone_notify, null);
//        //初始化控件
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
//        //设置Dialog从窗体从中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
//        dialog.setCancelable(true);
        dialog.show();//显示对话框

        TextView tv_concel = inflate.findViewById(R.id.dialog_tv_cancel);
        TextView tv_confirm = inflate.findViewById(R.id.dialog_tv_confirm);
        TextView tv_nofify = inflate.findViewById(R.id.dialog_tv_notify);
        tv_nofify.setText("是否要删除该银行卡？");
        tv_concel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RetrofitClient.getDefault()
                        .create(ChooseBankCase.class)
                        .deleteBankCard(id)
                        .compose(RxSchedulers.<NormalResponseB<String>>toMain())
                        .subscribe(new Observer<NormalResponseB<String>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(NormalResponseB<String> stringNormalResponseB) {
                                adapter.getDataList().remove(position);
                                adapter.notifyDataSetChanged();
                                if (adapter.getDataList() == null || adapter.getDataList().size() == 0) {
                                    rl_empty.setVisibility(View.VISIBLE);
                                } else {
                                    rl_empty.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }
}
