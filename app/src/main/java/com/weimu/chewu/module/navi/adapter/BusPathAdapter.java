package com.weimu.chewu.module.navi.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.PathPlanItemB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/15 23:20
 * Description:
 */
public class BusPathAdapter extends BaseRecyclerMVPAdapter<BaseB, PathPlanItemB> {

    public BusPathAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_bust_path_item;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ItemViewHolder(itemView);
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        PathPlanItemB item = getItem(position);
        //标签
        if (position == 0) {
            holder.tvTag.setVisibility(View.VISIBLE);
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }


        String busline = item.getBusPath().getSteps().get(0).getBusLines().get(0).getBusLineName();
        busline = busline.substring(0, busline.indexOf("("));

        int stationNum = item.getBusPath().getSteps().get(0).getBusLines().size();


        //时间
        holder.tvTime.setText(item.getTime());

        //步行上公交距离
        holder.tvFirstStepDistance.setText(item.getBusPath().getSteps().get(0).getWalk().getDistance() + "米");
        //价钱
        holder.tvMoney.setText("");

        //公交线路
        holder.tvLine.setText(busline);

        //上公交地方
        //holder.tvAddressUp.setText(destination.toString());

        //公交总数
        holder.tvStationSum.setText("共" + stationNum + "站");

        //公交线路
        holder.tvStationUp.setText(busline);

        //接下去几站到达
        if (stationNum == 1) {
            holder.tvStationNext.setText("即将到达");
        } else {
            holder.tvStationNext.setText((stationNum - 1) + "站后到达");
        }
    }


    class ItemViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_tag)
        TextView tvTag;

        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.tv_first_step_distance)
        TextView tvFirstStepDistance;

        @BindView(R.id.tv_money)
        TextView tvMoney;

        @BindView(R.id.tv_line)
        TextView tvLine;

        @BindView(R.id.tv_address_up)
        TextView tvAddressUp;


        @BindView(R.id.tv_station_sum)
        TextView tvStationSum;

        @BindView(R.id.tv_station_up)
        TextView tvStationUp;

        @BindView(R.id.tv_station_next)
        TextView tvStationNext;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
