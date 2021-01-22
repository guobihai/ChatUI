package com.smart.chatui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.smart.chatui.adapter.holder.CliamsViewHolder;
import trf.smt.com.netlibrary.enity.Cliams;


/**
 * 待认领适配器
 */
public class CliamsAdapter extends RecyclerArrayAdapter<Cliams.DataBean> {

    private onItemClickListener onItemClickListener;
    private Handler mHandler;

    public CliamsAdapter(Context context) {
        super(context);
        mHandler = new Handler();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        viewHolder = new CliamsViewHolder(parent, onItemClickListener, mHandler);
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return 1;
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onItemClick(View view, int position);

    }
}
