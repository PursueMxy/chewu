package com.weimu.chewu.dialog;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.utils.ZXingUtils;
import com.weimu.universalib.origin.view.diaog.BaseDialog;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/1 00:51
 * Description:显示付款二维码
 */
public class ShowPayCodeDialog extends BaseDialog {
    private String url;
    private String money;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    protected String getTagName() {
        return "payCode";
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_pay_code;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemVie) {
        return new ViewHolder(itemVie);
    }


    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.dialog_iv_qr_code)
        ImageView iv_qr_code;
        @BindView(R.id.dialog_tv_money)
        TextView tv_money;

        public ViewHolder(View itemView) {
            super(itemView);
            Bitmap bitmap = ZXingUtils.createQRImage(url, 250, 250);
            iv_qr_code.setImageBitmap(bitmap);
            tv_money.setText("请出示给车主\n" +
                    "扫描二维码并支付订单,\n" +
                    "共计" + money + "元");
        }


    }
}
