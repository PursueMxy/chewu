package com.weimu.chewu.module.order_detail_arrival.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.weimu.chewu.R;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import butterknife.BindView;

/**
 * Author:你需要一台永动机
 * Date:2018/5/1 01:13
 * Description:
 */
public class ImagesAdapter extends BaseRecyclerAdapter<String> {
    public ImagesAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new ItemViewHolder(itemView);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.grid_image_item;
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position) {
        String image = getItem(position);
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        Glide.with(mContext).load(image).apply(new RequestOptions().centerCrop()).into(holder.ivPreview);
    }


    public class ItemViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.iv_preview)
        ImageView ivPreview;

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
