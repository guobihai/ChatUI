package com.smart.chatui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.smart.chatui.adapter.holder.AddImageViewHolder;
import com.smart.chatui.adapter.holder.ImageViewHolder;
import com.smart.chatui.enity.ImageBean;

/**
 * Created by gbh on 2018/5/28  16:47.
 *
 * @describe 图片显示
 */

public class AddImageAdapt extends RecyclerArrayAdapter<ImageBean> {
    private onItemClickListener onItemClickListener;

    public AddImageAdapt(Context context) {
        super(context);
    }

    public int getImageCount() {
        int count = 0;
        for (ImageBean bean : getAllData()) {
            if (bean.getType() == ImageBean.PHOTO)
                count++;
        }
        return count;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        if (viewType == ImageBean.PHOTO)
            baseViewHolder = new ImageViewHolder(parent, onItemClickListener);
        else
            baseViewHolder = new AddImageViewHolder(parent, onItemClickListener);
        return baseViewHolder;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public void addItemClickListener(AddImageAdapt.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position);

        void onDeleteItemImage(View view, int position);
    }
}
