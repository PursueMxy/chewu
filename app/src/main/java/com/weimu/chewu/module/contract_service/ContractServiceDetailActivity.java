package com.weimu.chewu.module.contract_service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

public class ContractServiceDetailActivity extends BaseActivity {
    @BindView(R.id.contract_service_detail_tv_content)
    TextView tv_content;
    @BindView(R.id.contract_service_detail_tv_title)
    TextView tv_title;
    private ContactServiceB contactServiceB;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_contract_service_detail;
    }

    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        getData();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        fillData();
        initToolBar();
    }

    private void getData() {
        contactServiceB = (ContactServiceB) getIntent().getSerializableExtra("contactServiceB");
    }

    private void fillData() {
        tv_title.setText(contactServiceB.getTitle());
        RichText.fromHtml(contactServiceB.getContent()).into(tv_content);
//        tv_content.setText(contactServiceB.getContent());
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("问题详情")
                .setNavigationIcon(R.drawable.back);
    }
}
