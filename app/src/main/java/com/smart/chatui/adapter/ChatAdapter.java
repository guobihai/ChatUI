package com.smart.chatui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.smart.chatui.adapter.holder.ChatAcceptViewHolder;
import com.smart.chatui.adapter.holder.ChatRoomAcceptViewHolder;
import com.smart.chatui.adapter.holder.ChatSendViewHolder;
import com.smart.chatui.util.Constants;

import trf.smt.com.netlibrary.enity.MessageInfo;

/**
 * 作者：Rance on 2016/11/29 10:46
 * 邮箱：rance935@163.com
 */
public class ChatAdapter extends RecyclerArrayAdapter<MessageInfo> {

    private onItemClickListener onItemClickListener;
    public Handler handler;
    private boolean isChartRoom;

    public ChatAdapter(Context context) {
        super(context);
        handler = new Handler();
    }

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public ChatAdapter(Context context, boolean isChartRoom) {
        super(context);
        this.handler = new Handler();
        this.isChartRoom = isChartRoom;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                if (isChartRoom)
                    viewHolder = new ChatRoomAcceptViewHolder(parent, onItemClickListener, handler);
                else
                    viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener, handler);
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent, onItemClickListener, handler);
                break;
        }
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
        void onHeaderClick(int position);

        void onImageClick(View view, int position);

        void onVoiceClick(ImageView imageView, int position);

        void onLongItemClick(View view,int position);
    }
}
