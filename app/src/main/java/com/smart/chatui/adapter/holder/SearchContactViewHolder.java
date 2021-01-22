package com.smart.chatui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.smart.chatui.R;
import com.smart.chatui.adapter.SearchContactAdapter;
import trf.smt.com.netlibrary.enity.Contacts;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gbh on 2018/5/4  10:18.
 *
 * @describe
 */

public class SearchContactViewHolder extends BaseViewHolder<Contacts> {
    @BindView(R.id.imgHeart)
    ImageView mImgHeart;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.personLayout)
    RelativeLayout mPersonLayout;
    @BindView(R.id.checkbox)
    CheckBox mCheckbox;
    private SearchContactAdapter.onItemClickListener mOnItemClickListener;
    private boolean isEdit;

    public SearchContactViewHolder(ViewGroup parent, boolean isEdit, SearchContactAdapter.onItemClickListener onItemClickListener) {
        super(parent, R.layout.item_contact_person_layout);
        ButterKnife.bind(this, itemView);
        this.isEdit = isEdit;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setData(Contacts data) {
        mTvUserName.setText(TextUtils.isEmpty(data.getConRemark()) ? data.getNickName() : data.getConRemark());
        if (!TextUtils.isEmpty(data.getIconUrl())) {
            Glide.with(getContext()).load(data.getIconUrl())
                    .placeholder(R.drawable.ic_image_loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.person_72).into(mImgHeart);
        } else {
            mImgHeart.setImageResource(R.drawable.person_72);
        }
        mCheckbox.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        mCheckbox.setChecked(data.isSelect());
        mImgHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mOnItemClickListener) return;
                mOnItemClickListener.onHeaderClick(getDataPosition());
            }
        });
        mPersonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemClickListener) return;
                mOnItemClickListener.onItemClick(view, getDataPosition());
            }
        });
        mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemClickListener) return;
                mOnItemClickListener.onItemClick(view, getDataPosition());
            }
        });
    }


}
