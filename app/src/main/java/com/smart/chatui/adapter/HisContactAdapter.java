package com.smart.chatui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.smart.chatui.adapter.holder.HisContactViewHolder;

import trf.smt.com.netlibrary.enity.HisContact;


/**
 * 通讯录适配器
 */
public class HisContactAdapter extends RecyclerArrayAdapter<HisContact> {

    private onItemClickListener onItemClickListener;
    private Handler mHandler;

    public HisContactAdapter(Context context) {
        super(context);
        mHandler = new android.os.Handler();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        viewHolder = new HisContactViewHolder(parent, onItemClickListener,mHandler);
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onItemClick(View view, int position);

        void onLongItemClick(View view,int position);

    }
}
