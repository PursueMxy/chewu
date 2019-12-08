package com.weimu.chewu.module.wallet.bank.choose;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.wallet.BankInfo;
import com.weimu.chewu.origin.list.BaseRecyclerAdapter;
import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

/**
 * Created by huangjinfu on 18/4/30.
 */

public class ChooseBankAdapter extends BaseRecyclerAdapter<BankInfo> {
    private OnItemViewClickListener onItemViewClickListener;

    public ChooseBankAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(View itemView) {
        return new MyViewHolder(itemView);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.item_bank_card;
    }

    @Override
    protected void ItemViewChange(BaseRecyclerViewHolder viewHolder, final int position) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.tv_bank_name.setText(getItem(position).getBank());
        holder.tv_name.setText(getItem(position).getCardholder());
        holder.tv_num.setText(getItem(position).getCard_no());
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemViewClickListener != null) {
                    onItemViewClickListener.onEditClick(position);
                }
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemViewClickListener != null) {
                    onItemViewClickListener.onDeleteClick(position);
                }
            }
        });

    }

    class MyViewHolder extends BaseRecyclerViewHolder {
        private ImageView iv_delete, iv_edit;
        private TextView tv_bank_name, tv_name, tv_num;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_bank_name = itemView.findViewById(R.id.item_tv_card_name);
            tv_name = itemView.findViewById(R.id.item_tv_name);
            tv_num = itemView.findViewById(R.id.item_tv_card_num);
            iv_delete = itemView.findViewById(R.id.item_iv_delete);
            iv_edit = itemView.findViewById(R.id.item_iv_edit);
        }
    }

    public interface OnItemViewClickListener {
        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

}
