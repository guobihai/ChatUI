package com.smart.chatui.adapter.holder;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.smart.chatui.R;
import com.smart.chatui.adapter.ChatAdapter;
import com.smart.chatui.util.MsgType;
import com.smart.chatui.util.TimeUtils;
import com.smart.chatui.util.ChatUtils;
import com.smart.chatui.widget.BubbleImageView;
import com.smart.chatui.widget.GifTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import trf.smt.com.netlibrary.enity.MessageInfo;


/**
 * 聊天室
 */
public class ChatRoomAcceptViewHolder extends BaseViewHolder<MessageInfo> {


    @BindView(R.id.chat_item_date)
    TextView chatItemDate;
    @BindView(R.id.chat_item_header)
    ImageView chatItemHeader;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.chat_item_content_image)
    BubbleImageView chatItemContentImage;
    @BindView(R.id.chat_item_content_text)
    GifTextView chatItemContentText;
    @BindView(R.id.chat_item_voice)
    ImageView chatItemVoice;
    @BindView(R.id.chat_item_layout_content)
    LinearLayout chatItemLayoutContent;
    @BindView(R.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;

    public ChatRoomAcceptViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_room_accept);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
    }

    @Override
    public void setData(MessageInfo data) {
        String time = TimeUtils.longTimeFormatItem(data.getTime());
        chatItemDate.setText(time);
        //获取上一条数据
        ChatAdapter adapter = (ChatAdapter) getOwnerRecyclerView().getAdapter();
        if (null != adapter) {
            if (getDataPosition() - 1 != -1) {
                MessageInfo lastData = adapter.getAllData().get(getDataPosition() - 1);
                if (null != lastData) {
                    String lastTime = TimeUtils.longTimeFormatItem(lastData.getTime());
                    if (time.equals(lastTime)) {
                        chatItemDate.setText("");
                        chatItemDate.setVisibility(View.GONE);
                    } else {
                        chatItemDate.setText(time);
                        chatItemDate.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
        if (data.getIsWx()) {
            mTvUserName.setText("系统提示:" + data.getNickName() + "发来消息，请跟进");
            chatItemHeader.setImageResource(R.drawable.ic_alarm_black_24dp);
        } else {
            mTvUserName.setText(data.getNickName());
            if (TextUtils.isEmpty(data.getHeader())) {
                chatItemHeader.setImageResource(R.drawable.person_72);
            } else {
                Glide.with(getContext()).load(data.getHeader())
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.person_72)
                        .into(chatItemHeader);
            }
        }
        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onHeaderClick(getDataPosition());
            }
        });
        switch (data.getMsgType()) {
            case MsgType.SEND_TYPE_TXT:
                chatItemContentText.setSpanText(handler, data.getContent(), true);
                chatItemVoice.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.VISIBLE);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemContentText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (null == onItemClickListener) return false;
                        onItemClickListener.onLongItemClick(view, getDataPosition());
                        return false;
                    }
                });
                break;
            case MsgType.SEND_TYPE_IMG:
            case MsgType.SEND_TYPE_EMOJI:
                chatItemVoice.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(data.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(chatItemContentImage);
                chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
                    }
                });
                break;
            case MsgType.SEND_TYPE_VOICE:
                chatItemVoice.setVisibility(View.VISIBLE);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.VISIBLE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemVoiceTime.setText(ChatUtils.formatTime(data.getVoiceTime()));
                chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onVoiceClick(chatItemVoice, getDataPosition());
                    }
                });
                break;
        }


    }
}
