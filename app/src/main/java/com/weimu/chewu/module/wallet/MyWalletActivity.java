package com.weimu.chewu.module.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.module.wallet.bank.WithdrawCashActivity;
import com.weimu.chewu.module.wallet.bank.money_detail.MoneyDetailActivity;
import com.weimu.chewu.module.wallet.withdraw.WithdrawContract;
import com.weimu.chewu.module.wallet.withdraw.WithdrawPresenterImpl;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWalletActivity extends BaseActivity implements WithdrawContract.View {
    @BindView(R.id.wallet_tv_money)
    TextView tv_money;
    @BindView(R.id.wallet_tv_freeze_money)
    TextView tv_freeze_money;
    @BindView(R.id.wallet_rl_bank_card)
    RelativeLayout rl_bank_card;
    @BindView(R.id.wallet_rl_money_detail)
    RelativeLayout rl_money_detail;
    @BindView(R.id.wallet_rl_withdraw_cash)
    RelativeLayout rl_withdraw_cash;
    private WithdrawContract.Presenter mPresenter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new WithdrawPresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getUserInfo();
//        fillData();
    }

    private void fillData() {
        UserB userB = UserCenter.getInstance().getUserShareP();
        tv_money.setText(userB.getCustomer().getBalance());
        tv_freeze_money.setText(userB.getCustomer().getFrozen_balance());
    }

    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setNavigationIcon(R.drawable.back)
                .setTitle("钱包");
    }

    @OnClick({R.id.wallet_rl_bank_card, R.id.wallet_rl_money_detail, R.id.wallet_rl_withdraw_cash})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.wallet_rl_bank_card://银行卡
                intent = new Intent(MyWalletActivity.this, ChooseBankActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_rl_money_detail://资金明细
                intent = new Intent(MyWalletActivity.this, MoneyDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_rl_withdraw_cash://我要提现
                intent = new Intent(MyWalletActivity.this, WithdrawCashActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void withdrawSuccess() {

    }

    @Override
    public void getUserInfoSuccess(UserB.CustomerBean customerBean) {
        UserB userB = UserCenter.getInstance().getUserShareP();
        userB.setCustomer(customerBean);
        UserCenter.getInstance().setUserShareP(userB);
        fillData();
    }
}
