package com.smart.chatui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

public class ImageViewHolder extends BaseViewHolder<ImageBean> {


    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.imgDelete)
    ImageView mImgDelete;
    @BindView(R.id.imgShowBig)
    ImageView mImgShowBig;
    private AddImageAdapt.onItemClickListener mOnItemClickListener;

    public ImageViewHolder(ViewGroup parent, AddImageAdapt.onItemClickListener onItemClickListener) {
        super(parent, R.layout.image_item_layout);
        ButterKnife.bind(this, itemView);
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setData(ImageBean data) {
        Glide.with(getContext()).load(data.getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.person_72)
                .thumbnail(0.5f)
                .into(mImageView);
        mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnItemClickListener)
                    mOnItemClickListener.onDeleteItemImage(view, getDataPosition());
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnItemClickListener)
                    mOnItemClickListener.onItemClick(view, getDataPosition());
            }
        });
    }


}
