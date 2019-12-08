package com.weimu.chewu.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;
import com.weimu.universalib.origin.view.diaog.BaseDialog;
import com.weimu.universalib.utils.WindowsUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:你需要一台永动机
 * Date:2018/4/27 21:14
 * Description:联系客户弹窗
 */
public class ContactClientDialog extends BaseDialog {
    @Override
    protected String getTagName() {
        return "contractClient";
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    protected int getViewWidth() {
        return WindowsUtils.dip2px(266);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_contract_clent;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemVie) {
        return new ViewHolder(itemVie);
    }


    private CountDownTimer cd;

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.btn_contract)
        TextView tvContract;
        @BindView(R.id.tv_tip)
        TextView tv_tip;


        @OnClick(R.id.iv_close)
        public void clickToClose() {
            dismiss();
            if (onOrderConfirmListener != null) onOrderConfirmListener.cancel();
        }


        @OnClick(R.id.btn_contract)
        public void clickOK() {
            dismiss();
            if (onOrderConfirmListener != null) onOrderConfirmListener.confirm();
        }

        @OnClick(R.id.btn_cancel)
        public void clickCancel() {
            dismiss();
            if (onOrderConfirmListener != null) onOrderConfirmListener.cancel();
        }

        public ViewHolder(View itemView) {
            super(itemView);

            cd = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    tv_tip.setText("请您在" + l / 1000 + "S内联系客户");
                }

                @Override
                public void onFinish() {
                    tv_tip.setText("请您在" + 0 + "S内联系客户");
                    dismiss();
                    if (onOrderConfirmListener != null) onOrderConfirmListener.cancel();
                }
            };
            cd.start();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (cd != null) {
            cd.cancel();
            cd = null;
        }
    }

    private OnOrderConfirmListener onOrderConfirmListener;

    public void setOnOrderConfirmListener(OnOrderConfirmListener onOrderConfirmListener) {
        this.onOrderConfirmListener = onOrderConfirmListener;
    }

    public interface OnOrderConfirmListener {
        void confirm();

        void cancel();
    }
}
