package com.weimu.chewu.module.contract_service.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.ContactServiceB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerWithFooterAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 00:01
 * Description:
 */
public class ContactServiceAdapter extends BaseRecyclerWithFooterAdapter<BaseB, ContactServiceB> {


    public ContactServiceAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }


    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_conrract_service_item;
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        ContactServiceB item = getItem(position);
        holder.tvContent.setText(item.getTitle());
    }

    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.content)
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
