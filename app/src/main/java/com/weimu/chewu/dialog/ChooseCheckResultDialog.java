package com.weimu.chewu.dialog;

import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.universalib.origin.view.diaog.BaseDialog;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;
import com.weimu.universalib.utils.WindowsUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:你需要一台永动机
 * Date:2018/5/1 14:15
 * Description:
 */
public class ChooseCheckResultDialog extends BaseDialog {
    @Override
    protected String getTagName() {
        return "chooseCheckResult";
    }

    private boolean isPass = false;

    @Override
    protected int getViewWidth() {
        return WindowsUtils.dip2px(266);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_choose_check_result;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemVie) {
        return new ViewHolder(itemVie);
    }


    class ViewHolder extends BaseRecyclerViewHolder {
        @OnClick(R.id.iv_close)
        public void clickToClose() {
            dismiss();
        }

        @BindView(R.id.tv_pass_ok)
        TextView tvPassOk;
        @BindView(R.id.tv_pass_no)
        TextView tvPassNo;

        @OnClick({R.id.tv_pass_ok, R.id.tv_pass_no})
        public void togglePass(View view) {
            switch (view.getId()) {
                case R.id.tv_pass_ok:
                    pass(true);
                    break;
                case R.id.tv_pass_no:
                    pass(false);
                    break;
            }
        }

        @OnClick(R.id.btn_confirm)
        public void clickToChoose() {
            dismiss();
            if (clickListener != null)
                clickListener.onClick(isPass);

        }

        private void pass(boolean isPassword) {
            ChooseCheckResultDialog.this.isPass = isPassword;

            if (isPassword) {
                tvPassOk.setActivated(true);
                tvPassNo.setActivated(false);
            } else {
                tvPassOk.setActivated(false);
                tvPassNo.setActivated(true);
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);

           pass(false);
        }
    }

    public OnClickListener clickListener;

    public interface OnClickListener {

        void onClick(boolean isPass);
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
