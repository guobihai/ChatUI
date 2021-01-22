package com.smart.chatui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.smart.chatui.R;
import com.smart.chatui.adapter.AddImageAdapt;
import com.smart.chatui.enity.ImageBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gbh on 2018/5/4  10:18.
 *
 * @describe
 */

public class AddImageViewHolder extends BaseViewHolder<ImageBean> {


    @BindView(R.id.addImageView)
    ImageView mAddImageView;
    private AddImageAdapt.onItemClickListener mOnItemClickListener;

    public AddImageViewHolder(ViewGroup parent, AddImageAdapt.onItemClickListener onItemClickListener) {
        super(parent, R.layout.add_image_item_layout);
        ButterKnife.bind(this, itemView);
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setData(ImageBean data) {
        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnItemClickListener)
                    mOnItemClickListener.onItemClick(view, getDataPosition());
            }
        });
    }


}
