package com.weimu.universalib.origin.view.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private Unbinder bind;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        bind = ButterKnife.bind(this, itemView);
    }

    public void unbind() {
        bind.unbind();
    }


}
