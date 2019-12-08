package com.weimu.chewu.module.order_detail_ing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.http.core.RequestBodyHelper;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.OrderDetailCase;
import com.weimu.chewu.interfaces.MyTextWathcher;
import com.weimu.chewu.origin.view.BaseActivity;
import com.weimu.chewu.widget.ToolBarManager;
import com.weimu.universalib.utils.EditTextUtils;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

/**
 * 添加备注
 */
public class AddRemarkActivity extends BaseActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_remark;
    }

    public static Intent newIntent(Context context, OrderItemB orderItemB) {
        Intent intent = new Intent(context, AddRemarkActivity.class);
        intent.putExtra("orderItem", orderItemB);
        return intent;
    }

    @BindView(R.id.et_remark)
    EditText etRemark;

    @BindView(R.id.tv_remark_number)
    TextView tvRemarkNumber;

    private OrderItemB orderItemB;
    private OrderDetailCase mCase;


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
        orderItemB = (OrderItemB) getIntent().getSerializableExtra("orderItem");
        mCase = new OrderDetailCaseImpl();
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        initToolBar();
        initEdit();
    }

    private void initEdit() {
        etRemark.addTextChangedListener(new MyTextWathcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.length() > 200) {
                    etRemark.setText(str.substring(0, 200));
                    EditTextUtils.setSeletionEnd(etRemark);
                } else {
                    tvRemarkNumber.setText(str.length() + "/200");
                }
            }
        });
    }

    //点击完成
    @OnClick(R.id.tv_ok)
    public void clickToSubmitRemark() {
        String content = EditTextUtils.getContent(etRemark);
        if (TextUtils.isEmpty(content)) {
            showToast(etRemark.getHint().toString());
            return;
        }
        RequestBody body = new RequestBodyHelper()
                .addRaw("order_id", orderItemB.getId())
                .addRaw("content", content)
                .builder();

        mCase.addRemark(orderItemB.getId(), content).subscribe(new OnRequestObserver<String>() {

            @Override
            protected boolean OnSucceed(String result) {
                showToast("添加备注成功");
                setResult(RESULT_OK);
                onBackPressed();
                return super.OnSucceed(result);
            }

        });
    }


    private void initToolBar() {
        ToolBarManager.with(this, getContentView())
                .setTitle("添加备注")
                .setNavigationIcon(R.drawable.back);
    }
}
