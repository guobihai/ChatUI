package com.smart.chatui.adapter.verbs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.smart.chatui.adapter.holder.VerbViewHolder;
import com.smart.chatui.enity.VerbInfo;


/**
 * 代办适配器
 */
public class VerbInfoAdapter extends RecyclerArrayAdapter<VerbInfo> {

    private onItemClickListener onItemClickListener;

    public VerbInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        viewHolder = new VerbViewHolder(parent, onItemClickListener);
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

        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);

    }
}
