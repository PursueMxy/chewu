package com.weimu.chewu.module.order_detail_ing.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.OrderDetailB;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/4/30 12:38
 * Description:
 */
public class MessageAdapter extends BaseRecyclerAdapter<OrderDetailB.BackupBean> {

    public MessageAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_message_item;
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        OrderDetailB.BackupBean item = getItem(position);
        holder.tvTime.setText(item.getCreated_at());
        holder.tvMessage.setText(item.getContent());
    }

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.tv_message)
        TextView tvMessage;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
