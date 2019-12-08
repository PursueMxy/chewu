package com.weimu.chewu.module.station.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.StationListResultB;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/1 12:56
 * Description:
 */
public class StationListAdapter extends BaseRecyclerAdapter<StationListResultB.StationB> {

    public StationListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_station_item;
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        StationListResultB.StationB item = getItem(position);

        holder.tvName.setText(item.getName());
    }


    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
