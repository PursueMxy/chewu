package com.weimu.chewu.origin.list.mvp;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.origin.list.LoadingFooter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 01:17
 * Description:
 */
public abstract class BaseRecyclerWithFooterAdapter<H, E> extends BaseRecyclerMVPAdapter<H, E> {

    public BaseRecyclerWithFooterAdapter(Context mContext) {
        super(mContext);
    }

    private BaseRecyclerWithFooterAdapter.State mState = BaseRecyclerWithFooterAdapter.State.Idle;

    public enum State {
        Idle, Loading, TheEnd, Hide
    }

    public BaseRecyclerWithFooterAdapter.State getState() {
        return mState;
    }

    public void setState(BaseRecyclerWithFooterAdapter.State mState) {
        this.mState = mState;
        notifyDataSetChanged();
    }
    //Footer
    @Override
    protected int getFooterLayoutRes() {
        return R.layout.content_list_loading_footer;
    }


    @Override
    protected BaseRecyclerViewHolder getFooterHolder(View itemView) {
        return new FooterHolder(itemView);
    }

    @Override
    protected void footerViewChange(BaseRecyclerViewHolder viewHolder) {
        FooterHolder footerHolder = (FooterHolder) viewHolder;
        View view = viewHolder.itemView;
        switch (mState) {
            case Loading:
                view.setVisibility(View.VISIBLE);
                footerHolder.tvTip.setText(R.string.base_loading);
                footerHolder.tvTip.animate().withLayer().alpha(1f).setDuration(400);
                footerHolder.progressBar.setVisibility(View.VISIBLE);
                break;
            case TheEnd:
                view.setVisibility(View.VISIBLE);
                footerHolder.tvTip.setText(R.string.base_no_more);
                footerHolder.tvTip.animate().withLayer().alpha(1f).setDuration(400);
                footerHolder.progressBar.setVisibility(View.GONE);
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }

    class FooterHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        @BindView(R.id.tv_tip)
        TextView tvTip;


        public FooterHolder(View itemView) {
            super(itemView);
        }
    }
}
