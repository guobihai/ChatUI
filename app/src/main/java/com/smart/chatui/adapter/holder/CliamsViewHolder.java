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
import com.smart.chatui.adapter.CliamsAdapter;
import trf.smt.com.netlibrary.enity.Cliams;
import com.smart.chatui.util.MsgType;
import com.smart.chatui.util.TimeUtils;
import com.smart.chatui.widget.GifTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gbh on 2018/5/4  10:18.
 *
 * @describe
 */

public class CliamsViewHolder extends BaseViewHolder<Cliams.DataBean> {
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
    @BindView(R.id.tvMsgToast)
    TextView mTvMsgToast;
    private CliamsAdapter.onItemClickListener mOnItemClickListener;
    private Handler mHandler;

    public CliamsViewHolder(ViewGroup parent, CliamsAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_his_msg_layout);
        ButterKnife.bind(this, itemView);
        this.mOnItemClickListener = onItemClickListener;
        this.mHandler = handler;
    }

    @Override
    public void setData(Cliams.DataBean data) {
        mTvUserName.setText(data.getNikeName());
        mTvUnMsg.setVisibility(View.VISIBLE);
        mTvMsgToast.setVisibility(View.VISIBLE);
        mTvUnMsg.setText(data.getMsgCount());
        switch (data.getMsgType()) {
            case MsgType.SEND_TYPE_TXT:
                mTvHisMsg.setSpanText(mHandler, data.getContent(), false, 15);
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

        mTvMsgTime.setText(TimeUtils.stringTimeFormat(data.getRecieveTime()));
        if (!TextUtils.isEmpty(data.getIconUrl())) {
            Glide.with(getContext()).load(data.getIconUrl())
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.person_72).into(mImgHeart);
        } else {
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
