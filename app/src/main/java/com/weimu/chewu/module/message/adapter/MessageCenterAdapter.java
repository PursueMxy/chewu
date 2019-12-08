package com.weimu.chewu.module.message.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.MessageCenterB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.module.contract_service.adapter.ContactServiceAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerWithFooterAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Created by huangjinfu on 18/5/14.
 */

public class MessageCenterAdapter extends BaseRecyclerWithFooterAdapter<BaseB, MessageCenterB> {

    public MessageCenterAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }


    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_message;
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        MessageCenterB item = getItem(position);
        holder.tvContent.setText(item.getData().getContent());
        holder.tv_time.setText(item.getCreated_at());
        if (item.getType().equals("withdraw")) {
        } else if (item.getType().equals("order_cancel")) {
            holder.tv_type.setText("【取消订单】");
        }

//        holder.tvContent.setText(item.getTitle());
    }

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.item_tv_content)
        TextView tvContent;
        @BindView(R.id.item_tv_type)
        TextView tv_type;
        @BindView(R.id.item_tv_time)
        TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
