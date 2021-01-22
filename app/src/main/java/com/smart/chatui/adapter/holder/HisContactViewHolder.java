package com.smart.chatui.adapter.holder;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.smart.chatui.R;
import com.smart.chatui.adapter.HisContactAdapter;
import com.smart.chatui.util.MsgType;
import com.smart.chatui.util.TimeUtils;
import com.smart.chatui.widget.GifTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import trf.smt.com.netlibrary.enity.HisContact;

/**
 * Created by gbh on 2018/5/4  10:18.
 *
 * @describe
 */

public class HisContactViewHolder extends BaseViewHolder<HisContact> {
    @BindView(R.id.imgHeart)
    ImageView mImgHeart;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.personLayout)
    RelativeLayout mPersonLayout;
    @BindView(R.id.tvMsgTime)
    TextView mTvMsgTime;
    @BindView(R.id.tvHisMsg)
    GifTextView mTvHisMsg;
    @BindView(R.id.tvUnMsg)
    TextView mTvUnMsg;
    private HisContactAdapter.onItemClickListener mOnItemClickListener;
    private Handler mHandler;

    public HisContactViewHolder(ViewGroup parent, HisContactAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_his_msg_layout);
        ButterKnife.bind(this, itemView);
        this.mOnItemClickListener = onItemClickListener;
        this.mHandler = handler;
    }

    @Override
    public void setData(HisContact data) {
        mTvUserName.setText(TextUtils.isEmpty(data.getConRemark()) ? data.getNickName() : data.getConRemark());
        if (data.getMsgCount() != 0) {
            mTvUnMsg.setVisibility(View.VISIBLE);
            mTvUnMsg.setText(String.valueOf(data.getMsgCount()));
        } else {
            mTvUnMsg.setVisibility(View.GONE);
        }
        switch (data.getType()) {
            case MsgType.SEND_TYPE_TXT:
                mTvHisMsg.setSpanText(mHandler, data.getLastMsg(), false, 15);
                break;
            case MsgType.SEND_TYPE_EMOJI:
                mTvHisMsg.setText("[图片表情]");
                break;
            case MsgType.SEND_TYPE_IMG:
                mTvHisMsg.setText("[图片]");
                break;
            case MsgType.SEND_TYPE_VOICE:
                mTvHisMsg.setText("[语音]");
                break;
            case MsgType.SEND_TYPE_FILE:
                mTvHisMsg.setText("[文件]");
                break;
            case MsgType.SEND_TYPE_VIDEO:
                mTvHisMsg.setText("[视频]");
                break;
        }

        mTvMsgTime.setText(TimeUtils.longTimeFormat(data.getCreateTime()));
        if (!TextUtils.isEmpty(data.getIconUrl())) {
            Glide.with(getContext()).load(data.getIconUrl())
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.person_72).into(mImgHeart);
        } else {
            mImgHeart.setImageResource(R.drawable.person_72);
        }
        if (data.getUserName().equals("@chartRoom")) {
            mImgHeart.setImageResource(R.drawable.timg);
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
        mPersonLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null == mOnItemClickListener) return false;
                mOnItemClickListener.onLongItemClick(view, getDataPosition());
                return false;
            }
        });
    }
}
