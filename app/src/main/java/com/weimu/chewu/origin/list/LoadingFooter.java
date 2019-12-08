
package com.weimu.chewu.origin.list;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weimu.chewu.R;


/**
 * 列表加载footer
 */
public class LoadingFooter {
    private static final long mAnimationDuration = 400;

    private View mLoadingFooter;
    private TextView mLoadingText;
    private ProgressBar mProgress;

    private State mState = State.Idle;

    public enum State {
        Idle, Loading, TheEnd
    }

    private boolean isShow = true;

    public LoadingFooter(Context context) {
        mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.content_list_loading_footer, null);
        mLoadingFooter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 屏蔽点击
            }
        });
        mProgress = (ProgressBar) mLoadingFooter.findViewById(R.id.progressBar);
        mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.tv_tip);
        setState(State.Idle);
    }

    public View getView() {
        return mLoadingFooter;
    }

    public State getState() {
        return mState;
    }


    public void setState(State status) {
        if (!isShow) {
            return;
        }
        mState = status;
        mLoadingFooter.setVisibility(View.VISIBLE);

        switch (status) {
            case Loading:
                mLoadingText.setText("正在加载更多数据");
                mLoadingText.setVisibility(View.VISIBLE);
                mLoadingText.animate().withLayer().alpha(1).setDuration(mAnimationDuration);
                mProgress.setVisibility(View.VISIBLE);
                break;
            case TheEnd:
                mLoadingText.setText("没有更多了");
                mLoadingText.setVisibility(View.VISIBLE);
                mLoadingText.animate().withLayer().alpha(1).setDuration(mAnimationDuration);
                mProgress.setVisibility(View.GONE);
                break;
            default:
                mLoadingFooter.setVisibility(View.GONE);
                break;
        }
    }

    public void hide() {
        isShow = false;
        mLoadingFooter.setVisibility(View.INVISIBLE);
    }
}
