package com.weimu.chewu.origin.list.vlayout;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.weimu.chewu.R;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/3/31 19:06
 * Description:V-Layout的尾部
 */
public class FooterAdapter extends DelegateAdapter.Adapter<BaseRecyclerViewHolder> {

    private State mState = State.Idle;

    public enum State {
        Idle, Loading, TheEnd
    }



    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_loading_footer, parent, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        switch (mState) {
            case Loading:
                viewHolder.itemView.setVisibility(View.VISIBLE);
                viewHolder.tvTip.setText(R.string.base_loading);
                viewHolder.tvTip.animate().withLayer().alpha(1).setDuration(400);
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
            case TheEnd:
                viewHolder.itemView.setVisibility(View.VISIBLE);
                viewHolder.tvTip.setText(R.string.base_no_more);
                viewHolder.tvTip.animate().withLayer().alpha(1).setDuration(400);
                viewHolder.progressBar.setVisibility(View.GONE);
                break;
            default:
                viewHolder.itemView.setVisibility(View.GONE);
                break;
        }
    }


    public class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        @BindView(R.id.tv_tip)
        TextView tvTip;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        return singleLayoutHelper;
    }

    public State getState() {
        return mState;
    }

    public void setState(State status) {
        mState = status;
        notifyDataSetChanged();
    }
}
