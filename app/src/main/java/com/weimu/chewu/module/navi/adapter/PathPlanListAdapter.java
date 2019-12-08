package com.weimu.chewu.module.navi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.PathPlanItemB;
import com.weimu.chewu.backend.bean.base.BaseB;
import com.weimu.chewu.origin.list.mvp.BaseRecyclerMVPAdapter;
import com.weimu.chewu.type.MyNaviType;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/5 22:32
 * Description:
 */
public class PathPlanListAdapter extends BaseRecyclerMVPAdapter<BaseB, PathPlanItemB> {

    private int checkItemPosition = 0;


    private MyNaviType naviType = MyNaviType.DRIVE;


    public void setNaviType(MyNaviType naviType) {
        this.naviType = naviType;
    }

    public void setCheckItemPosition(int checkItemPosition) {
        this.checkItemPosition = checkItemPosition;
        notifyDataSetChanged();
    }

    private int checkColor = Color.rgb(251, 162, 72);
    private int unCheckColor = Color.rgb(66, 66, 66);

    public PathPlanListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_path_plan_item;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        PathPlanItemB item = getItem(position);

        if (position == 0) {
            holder.tvTip.setText("距离最短");
        } else {
            holder.tvTip.setText("备选方案" + (position + 1));
        }

        holder.tvTime.setText(item.getTime());

        holder.tvDistance.setText(item.getDistance() + "公里");

        if (position == checkItemPosition) {
            holder.tvTip.setTextColor(checkColor);
            holder.tvTime.setTextColor(checkColor);
            holder.tvDistance.setTextColor(checkColor);
        } else {
            holder.tvTip.setTextColor(unCheckColor);
            holder.tvTime.setTextColor(unCheckColor);
            holder.tvDistance.setTextColor(unCheckColor);
        }

    }


    class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_tip)
        TextView tvTip;

        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.tv_distance)
        TextView tvDistance;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public void setDataToAdapter(List<PathPlanItemB> data) {
        checkItemPosition=0;
        super.setDataToAdapter(data);
    }
}
