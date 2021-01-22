package com.smart.chatui.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.smart.chatui.R;
import com.smart.chatui.adapter.verbs.VerbInfoAdapter;
import com.smart.chatui.enity.VerbInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gbh on 2018/5/4  10:18.
 *
 * @describe
 */

public class VerbViewHolder extends BaseViewHolder<VerbInfo> {

    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.tvBirthdayMsg)
    TextView mTvBirthdayMsg;
    @BindView(R.id.verbLayout)
    RelativeLayout mVerbLayout;
    private VerbInfoAdapter.onItemClickListener mOnItemClickListener;

    public VerbViewHolder(ViewGroup parent, VerbInfoAdapter.onItemClickListener onItemClickListener) {
        super(parent, R.layout.item_verb_layout);
        ButterKnife.bind(this, itemView);
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void setData(VerbInfo data) {
        mTvUserName.setText(data.getName());
        if (data.getMsgCount() != 0) {
            mTvBirthdayMsg.setVisibility(View.VISIBLE);
            mTvBirthdayMsg.setText(String.valueOf(data.getMsgCount()));
        } else {
            mTvBirthdayMsg.setVisibility(View.GONE);
        }
        mVerbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemClickListener) return;
                mOnItemClickListener.onItemClick(view, getDataPosition());
            }
        });
    }


}
