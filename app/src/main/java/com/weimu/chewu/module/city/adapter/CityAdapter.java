package com.weimu.chewu.module.city.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.CityB;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/4/25 23:00
 * Description:
 */
public class CityAdapter extends BaseRecyclerAdapter<CityB> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {


    public CityAdapter(Context mContext) {
        super(mContext);
    }

    //Header
    @Override
    public long getHeaderId(int position) {
        return getItem(position).getParentPinYin().hashCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_city_header, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        HeaderHolder viewHolder = (HeaderHolder) holder;
        CityB item = getItem(position);
        viewHolder.tvPinYin.setText(item.getPinYinFirst().substring(0, 1).toUpperCase());
    }

    //Body
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


    //Header
    class HeaderHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_pinyin)
        TextView tvPinYin;

        HeaderHolder(View view) {
            super(view);
        }
    }

    //Item
    class ItemViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_city)
        TextView tvCity;

        ItemViewHolder(View view) {
            super(view);
        }
    }


}
