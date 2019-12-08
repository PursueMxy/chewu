package com.weimu.chewu.module.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.wallet.BankInfo;
import com.weimu.chewu.module.wallet.bank.add.AddCardContract;
import com.weimu.chewu.module.wallet.bank.add.AddCardPresenterImpl;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.chewu.widget.WMToast;

import butterknife.BindView;
import butterknife.OnClick;

public class AddCardActivity extends BaseActivity implements AddCardContract.View {
    @BindView(R.id.add_card_et_num)
    EditText et_num;
    @BindView(R.id.add_card_et_name)
    EditText et_name;
    @BindView(R.id.add_card_et_card_name)
    EditText et_card_name;
    private AddCardContract.Presenter mPresenter;
    private BankInfo bankInfo;
    private int type;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_card;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        mPresenter = new AddCardPresenterImpl(this);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        getData();
        initToolbar();
    }

    private void getData() {
        type = getIntent().getIntExtra("type", -1);
        bankInfo = (BankInfo) getIntent().getSerializableExtra("bankInfo");
        if (type == 1) {
            et_card_name.setText(bankInfo.getBank());
            et_name.setText(bankInfo.getCardholder());
            et_num.setText(bankInfo.getCard_no());
        }
    }

    private void initToolbar() {
        if (type == 1) {
            ToolBarManager.with(this, getContentView())
                    .setNavigationIcon(R.drawable.back)
                    .setTitle("修改银行卡");
        } else {
            ToolBarManager.with(this, getContentView())
                    .setNavigationIcon(R.drawable.back)
                    .setTitle("添加银行卡");
        }
    }

    @OnClick({R.id.add_card_btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_card_btn_save://保存

                String name = et_name.getText().toString();
                String cardNum = et_num.getText().toString();
                String cardName = et_card_name.getText().toString();
                if (type == 1) {
                    mPresenter.updateBankCard(bankInfo.getId()+"",name,cardNum,cardName);
                } else {
                    mPresenter.addBankCard(name, cardNum, cardName);
                }

                break;

        }
    }

    @Override
    public void addSuccess() {
        if (type == 1) {
            WMToast.show("添加成功");
        } else {
            WMToast.show("修改成功");
        }
        finish();
    }

    @Override
    public void addFailed(String message) {

    }
}
