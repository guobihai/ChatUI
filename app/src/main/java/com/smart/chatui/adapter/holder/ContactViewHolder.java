package com.smart.chatui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.smart.chatui.R;
import com.smart.chatui.adapter.ContactAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import trf.smt.com.netlibrary.enity.Person;

/**
 * Created by gbh on 2018/5/4  10:18.
 *
 * @describe
 */

public class ContactViewHolder extends BaseViewHolder<Person> {
    @BindView(R.id.imgHeart)
    ImageView mImgHeart;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.personLayout)
    RelativeLayout mPersonLayout;
    private ContactAdapter.onItemClickListener mOnItemClickListener;

    public ContactViewHolder(ViewGroup parent, ContactAdapter.onItemClickListener onItemClickListener) {
        super(parent, R.layout.item_contact_person_layout);
        ButterKnife.bind(this, itemView);
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setData(Person data) {
        mTvUserName.setText(TextUtils.isEmpty(data.getConRemark()) ? data.getNickName() : data.getConRemark());
        if (!TextUtils.isEmpty(data.getIconUrl())) {
            Glide.with(getContext()).load(data.getIconUrl())
                    .placeholder(R.drawable.ic_image_loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.person_72).into(mImgHeart);
        }else{
            mImgHeart.setImageResource(R.drawable.person_72);
        }
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
    }


}
