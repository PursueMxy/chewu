package com.weimu.chewu.origin.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {


    private List<T> dataList = new ArrayList<>();//不让子类直接使用
    protected Context mContext;
    protected OnItemClick<T> onItemClick;

    private static final int TYPE_HEADER = 0, TYPE_ITEM = 1, TYPE_FOOT = 2, TYPE_EMPTY = 3;
    private View headView;
    private View footView;
    private int headViewSize = 0;
    private int footViewSize = 0;
    private boolean isAddFoot = false;
    private boolean isAddHead = false;


    private View emptyView;
    private int emptyViewSize = 0;
    private boolean isAddEmpty = false;


    public BaseRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isAddFoot() {
        return isAddFoot;
    }

    public boolean isAddHead() {
        return isAddHead;
    }

    public boolean isAddEmpty() {
        return isAddEmpty;
    }

    public void addEmptyView(View view) {
        emptyView = view;
        emptyViewSize = 1;
        isAddEmpty = true;
    }

    public void addHeadView(View view) {
        headView = view;
        headViewSize = 1;
        isAddHead = true;
    }


    public void addFootView(View view) {
        footView = view;
        footViewSize = 1;
        isAddFoot = true;
    }

    public void removeFootView() {
        footView = null;
        footViewSize = 0;
        isAddFoot = false;
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_ITEM;
        if (headViewSize == 1 && position == 0) {
            type = TYPE_HEADER;
        } else if (emptyViewSize == 1 && position == getItemCount() - 1 - footViewSize) {
            type = TYPE_EMPTY;
        } else if (footViewSize == 1 && position == getItemCount() - 1) {
            //最后一个位置
            type = TYPE_FOOT;
        }
        return type;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View targetView = null;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                return new BaseRecyclerViewHolder(headView);
            case TYPE_ITEM:
                targetView = mInflater.inflate(getItemLayoutRes(), parent, false);//防止item显示不全问题
                return getViewHolder(targetView);
            case TYPE_EMPTY:
                return new BaseRecyclerViewHolder(emptyView);
            case TYPE_FOOT:
                return new BaseRecyclerViewHolder(footView);
        }
        return null;
    }

    //获取Viewholder
    protected abstract BaseRecyclerViewHolder getViewHolder(View itemView);


    //获取布局
    protected abstract int getItemLayoutRes();

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM:
                initOnClickListener(viewHolder, position - (headViewSize + emptyViewSize));
                ItemViewChange(viewHolder, position - (headViewSize + emptyViewSize));
                break;
        }
    }

    //初始化Item点击事件
    private void initOnClickListener(BaseRecyclerViewHolder viewHolder, final int position) {
        //点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onClick(getItem(position), position);
                }

            }
        });
    }


    protected abstract void ItemViewChange(BaseRecyclerViewHolder viewHolder, final int position);

    @Override
    public int getItemCount() {
        return dataList.size() + headViewSize + footViewSize + emptyViewSize;
    }

    public List<T> getDataList() {
        return dataList;
    }



    public void setDataToAdapter(List<T> data) {
        if (data == null) return;
        dataList.clear();
        notifyDataSetChanged();
        if (data.size() > 0) {
            addData(data);
        }
    }


    public int getDataListCount() {
        return dataList.size();
    }


    public void addData(List<T> data) {
        if (data == null) {
            return;
        }
        int start = getItemCount() - 1;
        int count = data.size();
        dataList.addAll(data);
        notifyItemRangeInserted(start, count);
    }

    public void addData(T data) {
        if (data == null) return;
        int start = getItemCount() - 1;
        int count = 1;
        dataList.add(data);
        notifyDataSetChanged();
    }


    public T getItem(int position) {
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        return dataList.get(position);
    }


    //item的点击接口
    public interface OnItemClick<E> {
        void onClick(E item, int position);

    }

    public void setOnItemClickListener(OnItemClick<T> onItemClick) {
        this.onItemClick = onItemClick;
    }

}

