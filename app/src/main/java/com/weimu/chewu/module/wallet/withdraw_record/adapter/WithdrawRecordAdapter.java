package com.weimu.chewu.module.wallet.withdraw_record.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.WithdrawRecordB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.module.contract_service.adapter.ContactServiceAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerWithFooterAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Created by huangjinfu on 18/5/11.
 */

public class WithdrawRecordAdapter extends BaseRecyclerWithFooterAdapter<BaseB, WithdrawRecordB> {

    public WithdrawRecordAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }


    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_withdraw;
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        WithdrawRecordB item = getItem(position);
        holder.tv_time.setText(item.getCreated_at());
        holder.tv_remain_money.setText("余额" + item.getRemain_balance());
        holder.tv_content.setText(item.getBackup());
        if (item.getType().equals("expend")) {
            holder.tv_money.setTextColor(mContext.getResources().getColor(R.color.color_blue));
            holder.rtv_state_xian.setVisibility(View.VISIBLE);
            holder.rtv_state_shou.setVisibility(View.GONE);
            holder.tv_money.setText("-" + item.getBalance());
        } else {
            holder.rtv_state_xian.setVisibility(View.GONE);
            holder.rtv_state_shou.setVisibility(View.VISIBLE);
            holder.tv_money.setText("+" + item.getBalance());
            holder.tv_money.setTextColor(mContext.getResources().getColor(R.color.color_yellow));

        }
    }

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.item_rtv_state)
        RoundTextView rtv_state_shou;
        @BindView(R.id.item_tv_time)
        TextView tv_time;
        @BindView(R.id.item_tv_remain_money)
        TextView tv_remain_money;
        @BindView(R.id.item_tv_money)
        TextView tv_money;
        @BindView(R.id.item_rtv_state_1)
        RoundTextView rtv_state_xian;
        @BindView(R.id.item_tv_remain_content)
        TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
