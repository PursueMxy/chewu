package com.weimu.chewu.module.wallet.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.bean.wallet.BankInfo;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.ChooseBankCase;
import com.weimu.chewu.module.wallet.ChooseBankActivity;
import com.weimu.chewu.module.wallet.withdraw.WithdrawContract;
import com.weimu.chewu.module.wallet.withdraw.WithdrawPresenterImpl;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WithdrawCashActivity extends BaseActivity implements WithdrawContract.View {
    @BindView(R.id.withdraw_cash_tv_bank_name)
    TextView tv_card_name;
    @BindView(R.id.withdraw_cash_tv_weihao)
    TextView tv_weihao;
    @BindView(R.id.withdraw_cash_et_money)
    EditText et_money;
    @BindView(R.id.withdraw_cash_tv_money)
    TextView tv_money;
    private BankInfo bankInfo;
    private WithdrawContract.Presenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_withdraw_cash;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new WithdrawPresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        getBankCardList();
        fillData();
        initToolBar();
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("提现");
    }

    @OnClick({R.id.withdraw_cash_btn_input, R.id.main_rl_bank_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdraw_cash_btn_input://提现申请
                UserB userB = UserCenter.getInstance().getUserShareP();
                String tixianMoney = et_money.getText().toString();

                Integer doubletixianMoney = Integer.parseInt(tixianMoney);
                Double doubleMoney = Double.parseDouble(userB.getCustomer().getBalance());
                if (doubleMoney < doubletixianMoney) {
                    WMToast.show("输入金额超过可提现金额，请重新输入！");
                } else if (bankInfo == null) {
                    WMToast.show("请先选择可提现银行卡");
                } else {
                    mPresenter.doWithdraw(bankInfo.getId(), doubletixianMoney);
//                    doWithdraw(bankInfo.getId(), doubletixianMoney);
                }
                break;
            case R.id.main_rl_bank_choose://银行卡选择
                Intent intent = new Intent(WithdrawCashActivity.this, ChooseBankActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == 1 && data != null) {
            bankInfo = (BankInfo) data.getSerializableExtra("bankInfo");
            tv_card_name.setText(bankInfo.getBank());
            String weihao = bankInfo.getCard_no().substring(bankInfo.getCard_no().length() - 4, bankInfo.getCard_no().length());
            tv_weihao.setText("尾号" + weihao);

        }
    }

    private void fillData() {
        UserB userB = UserCenter.getInstance().getUserShareP();
        tv_money.setText("可提现金额" + userB.getCustomer().getBalance());
    }

    private void getBankCardList() {
        RetrofitClient.getDefault()
                .create(ChooseBankCase.class)
                .getBankList()
                .compose(RxSchedulers.<NormalResponseB<List<BankInfo>>>toMain())
                .subscribe(new OnRequestObserver<List<BankInfo>>() {
                    @Override
                    protected boolean OnSucceed(List<BankInfo> result) {
                        if (result.size() == 0) {
                            tv_card_name.setText("暂无可提现银行卡");
                        } else {
                            bankInfo = result.get(0);
                            tv_card_name.setText(result.get(0).getBank());
                            String weihao = result.get(0).getCard_no().substring(result.get(0).getCard_no().length() - 4, result.get(0).getCard_no().length());
                            tv_weihao.setText("尾号" + weihao);
                        }
                        return super.OnSucceed(result);
                    }

                    @Override
                    protected boolean onFailure(String message) {
                        return super.onFailure(message);
                    }
                });

    }


    @Override
    public void withdrawSuccess() {
        mPresenter.getUserInfo();
    }

    @Override
    public void getUserInfoSuccess(UserB.CustomerBean customerBean) {
        UserB userB = UserCenter.getInstance().getUserShareP();
        userB.setCustomer(customerBean);
        UserCenter.getInstance().setUserShareP(userB);
        WMToast.show("申请提现成功，请耐心等待审核结果。");
        finish();
    }
}
