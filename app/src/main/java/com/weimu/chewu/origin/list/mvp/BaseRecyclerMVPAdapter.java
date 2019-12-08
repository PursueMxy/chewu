package com.weimu.chewu.origin.list.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerMVPAdapter<H, T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private static final int TYPE_HEADER = 0, TYPE_ITEM = 1, TYPE_FOOT = 2, TYPE_EMPTY = 3, TYPE_ERROR = 4;

    protected Context mContext;
    private List<T> dataList = new ArrayList<>();//不让子类直接使用
    private H headerData;

    protected OnHeaderClick<H> onHeaderClick;
    protected OnItemClick<T> onItemClick;
    protected OnItemLongClick<T> onItemLongClick;
    protected OnFooterClick onFooterClick;

    private int headViewSize = 0;
    private int emptyViewSize = 0;
    private int errorViewSize = 0;
    private int footViewSize = 0;


    private int emptyHeight = 0;
    private int errorHeight = 0;


    public BaseRecyclerMVPAdapter(Context mContext) {
        this.mContext = mContext;

        if (getHeaderLayoutRes() != -1) headViewSize = 1;
        if (getEmptyLayoutRes() != -1) emptyViewSize = 1;
        if (getErrorLayoutRes() != -1) errorViewSize = 1;
        if (getFooterLayoutRes() != -1) footViewSize = 1;
    }

    //---Layout---

    //获取Header的Layout
    protected int getHeaderLayoutRes() {
        return -1;
    }

    //获取EmptyView的Layout
    protected int getEmptyLayoutRes() {
        return -1;
    }

    //获取ErrorView的Layout
    protected int getErrorLayoutRes() {
        return -1;
    }

    //获取Body的Layout
    protected abstract int getItemLayoutRes();

    //获取Footer的Layout
    protected int getFooterLayoutRes() {
        return -1;
    }

    //---Holder---

    //获取Header的Holder
    protected BaseRecyclerViewHolder getHeaderHolder(View itemView) {
        return new BaseRecyclerViewHolder(itemView);
    }

    //获取Empty的Holder
    protected BaseRecyclerViewHolder getEmptyHolder(View itemView) {
        return new BaseRecyclerViewHolder(itemView);
    }

    //获取Error的Holder
    protected BaseRecyclerViewHolder getErrorHolder(View itemView) {
        return new BaseRecyclerViewHolder(itemView);
    }

    //获取Body的Holder
    protected abstract BaseRecyclerViewHolder getViewHolder(View itemView);

    //获取Footer的Holder
    protected BaseRecyclerViewHolder getFooterHolder(View itemView) {
        return new BaseRecyclerViewHolder(itemView);
    }

    //---View---

    //显示Header的视图变化
    protected void headerViewChange(BaseRecyclerViewHolder viewHolder) {
    }

    //显示Body的视图变化
    protected abstract void ItemViewChange(BaseRecyclerViewHolder viewHolder, int position);

    //显示Footer的视图变化
    protected void footerViewChange(BaseRecyclerViewHolder viewHolder) {
    }

    //显示空视图的变化
    protected void emptyViewChange(BaseRecyclerViewHolder viewHolder) {


        View itemView = viewHolder.itemView;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = (emptyHeight <= 0) ? 1 : emptyHeight;
        itemView.setLayoutParams(layoutParams);


        if (emptyHeight<=0){
            itemView.setVisibility(View.GONE);
        }else{
            itemView.setVisibility(View.VISIBLE);
        }
    }

    //显示错误视图的变化
    protected void errorViewChange(BaseRecyclerViewHolder viewHolder) {
        View itemView = viewHolder.itemView;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = (errorHeight <= 0) ? 1 : errorHeight;
        itemView.setLayoutParams(layoutParams);

        if (errorHeight<=0){
            itemView.setVisibility(View.GONE);
        }else{
            itemView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemViewType(int position) {
        int type = TYPE_ITEM;
        if (headViewSize == 1 && position == 0) {
            type = TYPE_HEADER;
        } else if (emptyViewSize == 1 && position == 0 + headViewSize) {
            type = TYPE_EMPTY;
        } else if (errorViewSize == 1 && position == 0 + headViewSize + errorViewSize) {
            type = TYPE_ERROR;
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
        int targetRes = -1;
        switch (viewType) {
            case TYPE_HEADER:
                targetView = mInflater.inflate(getHeaderLayoutRes(), parent, false);//防止item显示不全问题
                return getHeaderHolder(targetView);
            case TYPE_EMPTY:
                targetView = mInflater.inflate(getEmptyLayoutRes(), parent, false);//防止item显示不全问题
                return getEmptyHolder(targetView);
            case TYPE_ERROR:
                targetView = mInflater.inflate(getErrorLayoutRes(), parent, false);//防止item显示不全问题
                return getErrorHolder(targetView);
            case TYPE_ITEM:
                targetView = mInflater.inflate(getItemLayoutRes(), parent, false);//防止item显示不全问题
                return getViewHolder(targetView);
            case TYPE_FOOT:
                targetView = mInflater.inflate(getFooterLayoutRes(), parent, false);//防止item显示不全问题
                return getFooterHolder(targetView);

        }
        return new BaseRecyclerViewHolder(targetView);
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                if (onHeaderClick != null) onHeaderClick.onclick(headerData);
                headerViewChange(viewHolder);
                break;
            case TYPE_EMPTY:
                emptyViewChange(viewHolder);
                break;
            case TYPE_ERROR:
                errorViewChange(viewHolder);
                break;
            case TYPE_ITEM:
                final int index = position - (headViewSize + emptyViewSize + errorViewSize);
                //单点
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClick != null) onItemClick.onClick(getItem(index), index);

                    }
                });
                //长按
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onItemLongClick != null)
                            onItemLongClick.onClick(getItem(index), index);
                        return true;
                    }
                });

                ItemViewChange(viewHolder, index);
                break;
            case TYPE_FOOT:
                if (onFooterClick != null) onFooterClick.onClick();
                footerViewChange(viewHolder);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size() + headViewSize + footViewSize + emptyViewSize + errorViewSize;
    }


    //Header
    public void setHeaderDataToAdapter(H data) {
        this.headerData = data;
        if (headViewSize == 1) notifyItemChanged(0);
    }

    public void hideHeader() {
        if (headViewSize == 0) return;
        headViewSize = 0;
        notifyItemChanged(0);
    }

    //body
    public void setDataToAdapter(List<T> data) {
        dataList.clear();
        notifyDataSetChanged();//必须使用 否则会出错
        addItems(data);
    }


    public void addItems(List<T> data) {
        if (data == null && data.size() > 0) {
            notifyDataSetChanged();
            return;
        }
        int currentSize = getItemCount();
        dataList.addAll(data);
        int start = currentSize - footViewSize;
        int count = data.size() + footViewSize;
        notifyItemRangeInserted(start, count);
    }

    public void addItem(T item) {
        dataList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position == -1) return;
        dataList.remove(position);
        notifyItemRemoved(headViewSize + emptyViewSize + errorViewSize + position);
        notifyItemRangeChanged(0, getItemCount() - 1);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void clearDataList() {
        dataList.clear();
        notifyDataSetChanged();
    }

    //Empty
    public boolean showEmpty(int height) {
        if (emptyViewSize != 1) return false;
        emptyHeight = height;
        notifyItemChanged(0 + headViewSize);
        return true;
    }

    public boolean hideEmpty() {
        if (emptyViewSize != 1) return false;
        if (emptyHeight == 0) return true;
        emptyHeight = -1;
        notifyItemChanged(0 + headViewSize);
        return true;
    }

    //Error
    public boolean showError(int height) {
        if (errorViewSize != 1) return false;
        errorHeight = height;
        notifyItemChanged(0 + headViewSize + emptyViewSize);
        return true;
    }

    public boolean hideError() {
        if (errorViewSize != 1) return false;
        if (errorHeight == 0) return true;
        errorHeight = -1;
        notifyItemChanged(0 + headViewSize + emptyViewSize);
        return true;
    }

    //Footer
    public void showFooter() {
        if (footViewSize == 1) return;
        footViewSize = 1;
        notifyDataSetChanged();
    }

    public void hideFooter() {
        if (footViewSize == 0) return;
        footViewSize = 0;
        notifyDataSetChanged();
    }


    public T getItem(int position) {
        return dataList.get(position);
    }

    //header的点击接口
    public interface OnHeaderClick<E> {
        void onclick(E item);
    }

    //item的点击接口
    public interface OnItemClick<E> {
        void onClick(E item, int position);

    }

    public interface OnItemLongClick<E> {
        void onClick(E item, int position);

    }

    //Footer
    public interface OnFooterClick {

        void onClick();
    }


    public void setOnHeaderClick(OnHeaderClick<H> onHeaderClick) {
        this.onHeaderClick = onHeaderClick;
    }

    public void setOnItemClick(OnItemClick<T> onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnItemLongClick(OnItemLongClick<T> onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public void setOnFooterClick(OnFooterClick onFooterClick) {
        this.onFooterClick = onFooterClick;
    }
}

