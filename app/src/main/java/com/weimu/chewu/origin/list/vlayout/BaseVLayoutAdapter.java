package com.weimu.chewu.origin.list.vlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseVLayoutAdapter<T> extends DelegateAdapter.Adapter<BaseRecyclerViewHolder> {

    protected Context mContext;
    private List<T> dataList = new ArrayList<>();//不让子类直接使用
    protected OnItemClick onItemClick;


    public BaseVLayoutAdapter(Context mContext) {
        this.mContext = mContext;
    }


    //获取Viewholder
    protected abstract BaseRecyclerViewHolder getViewHolder(View contentView,int viewType);


    //获取布局
    protected abstract int getItemLayoutRes();

    //视图变化
    protected abstract void ItemViewChange(BaseRecyclerViewHolder viewHolder, final int position);



    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = null;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        contentView = mInflater.inflate(getItemLayoutRes(), parent, false);//防止item显示不全问题
        return getViewHolder(contentView,viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        //点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onClick(position);
                }

            }
        });
        ItemViewChange(holder, position);
    }


    public List<T> getDataList() {
        return dataList;
    }


    public void setDataToAdapter(List<T> data) {
        if (data == null) return;
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }



    public void addData(List<T> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void insertData(T item) {
        this.dataList.add(item);
        this.notifyDataSetChanged();
    }

    /**
     * 删除某个item【带有动画】
     */
    public void removeItem(int position) {
        dataList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 删除所有数据
     */
    public void clearAllData() {
        dataList.clear();
        notifyDataSetChanged();
    }


    public T getItem(int position) {
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        return dataList.get(position);
    }


    //item的点击接口
    public interface OnItemClick {
        void onClick(int position);
    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }



}
