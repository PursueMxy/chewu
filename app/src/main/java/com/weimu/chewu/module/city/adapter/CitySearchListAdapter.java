package com.weimu.chewu.module.city.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.CityB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.module.city.adapter.CityAdapter;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/9 23:28
 * Description:
 */
public class CitySearchListAdapter extends BaseRecyclerMVPAdapter<BaseB, CityB> {


    public CitySearchListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_city_item;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ItemViewHolder(itemView);
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) viewHolder;
        itemHolder.tvCity.setText(getItem(position).getName());
    }

    class ItemViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_city)
        TextView tvCity;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
