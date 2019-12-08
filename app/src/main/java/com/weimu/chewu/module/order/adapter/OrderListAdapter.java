package com.weimu.chewu.module.order.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.OrderItemB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerWithFooterAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;
import com.weimu.universalib.utils.SpannableUtils;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 16:46
 * Description:
 */
public class OrderListAdapter extends BaseRecyclerWithFooterAdapter<BaseB, OrderItemB> {

    public OrderListAdapter(Context mContext) {
        super(mContext);
    }

    //empty
    @Override
    protected int getEmptyLayoutRes() {
        return R.layout.empty_order;
    }

    //error
    @Override
    protected int getErrorLayoutRes() {
        return R.layout.error_order;
    }

    //body
    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_order_item;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        OrderItemB item = getItem(position);

        //type
        colorTextView(holder.tvType, "代办类型：" + item.getProject());
        //time
        colorTextView(holder.tvTime, "接单时间：" + item.getAccepted_at() + "");
        //location
        colorTextView(holder.tvLocation, "接车地点：" + item.getAddress());
        //fee
        colorTextView(holder.tvFee, "司机费用：" + item.getDriver_price() + "元");
        //result

        if (item.getStatus().equals("accepted")) {
            holder.tvResult.setText("检测结果：—");
        } else {
            if (item.getStatus().equals("succeed")) {
                if (item.getCheckout_result().equals("failed")) {
                    holder.tvResult.setText("检测结果：未通过");
                } else {
                    holder.tvResult.setText("检测结果：通过");
                }

            }
        }
    }

    private void colorTextView(TextView tv, String content) {
        tv.setText(content);
        SpannableUtils.colorNormal(tv, content.indexOf("："), content.length(), R.color.colorBlackPrimary);
    }

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_type)
        TextView tvType;

        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.tv_location)
        TextView tvLocation;

        @BindView(R.id.tv_fee)
        TextView tvFee;

        @BindView(R.id.tv_result)
        TextView tvResult;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
