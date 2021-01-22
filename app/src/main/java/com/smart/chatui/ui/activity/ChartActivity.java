package com.smart.chatui.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.smart.chatui.R;
import com.smart.chatui.adapter.ChatAdapter;
import com.smart.chatui.adapter.CommonFragmentPagerAdapter;
import com.smart.chatui.enity.FullImageInfo;
import com.smart.chatui.enity.SendFiles;
import com.smart.chatui.ui.fragment.ChatEmotionFragment;
import com.smart.chatui.ui.fragment.ChatFunctionFragment;
import com.smart.chatui.util.Constants;
import com.smart.chatui.util.GlobalOnItemClickManagerUtils;
import com.smart.chatui.util.MediaManager;
import com.smart.chatui.util.MsgType;
import com.smart.chatui.widget.EmotionInputDetector;
import com.smart.chatui.widget.NoScrollViewPager;
import com.smart.chatui.widget.StateButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.bean.UpInfo;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.enity.Person;
import trf.smt.com.netlibrary.greendao.PersonDao;
import trf.smt.com.netlibrary.interfaces.ResultCallBack;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * 单聊
 */
public class ChartActivity extends BaseActivity implements ResultCallBack {

    @BindView(R.id.chat_list)
    EasyRecyclerView chatList;
    @BindView(R.id.emotion_voice)
    ImageView emotionVoice;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.voice_text)
    TextView voiceText;
    @BindView(R.id.emotion_button)
    ImageView emotionButton;
    @BindView(R.id.emotion_add)
    ImageView emotionAdd;
    @BindView(R.id.emotion_send)
    StateButton emotionSend;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.emotion_layout)
    RelativeLayout emotionLayout;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<MessageInfo> messageInfos;
    //录音相关
    private int animationRes = 0;
    private int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    private UDPClientUtils mUDPClientUtils;
    private String fromUserName;//发送人
    private Person person;
    private String chartUserIconUrl;
    private MessageInfo mMessageInfo;

    private int pageCount = 20;//每页数量
    private int curPage = 0;//当前页数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUDPClientUtils.sendUnClaim(fromUserName, person.getUserName());
                finish();
            }
        });
        EventBus.getDefault().register(this);
        person = (Person) this.getIntent().getExtras().getSerializable("contact");
        mTvTitle.setText(TextUtils.isEmpty(person.getConRemark()) ? person.getNickName() : person.getConRemark());
        fromUserName = PreferenceUtils.getString(this, "userId", "");
        List<Person> list = DbAppliaction.getDaoSession().getPersonDao().queryBuilder().
                where(PersonDao.Properties.UserName.eq(person.getUserName())).build().list();
        if (list.size() > 0) {
            chartUserIconUrl = list.get(0).getIconUrl();
        }
        initUdp();
        initWidget();
    }

    private void initUdp() {
        mUDPClientUtils = UDPClientUtils.getInstance(this);
        mUDPClientUtils.setResultCallBack(this);
        mUDPClientUtils.setCurUserName(person.getUserName());
    }

    private void initWidget() {
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .setOnScrollToByRecycleView(new EmotionInputDetector.OnScrollToByRecycleView() {
                    @Override
                    public void onScrollToPosition() {
                        chatList.scrollToPosition(chatAdapter.getCount() - 1);
                    }
                })
                .build();

        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new ChatAdapter(this);
        chatAdapter.setHasStableIds(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        messageInfos = new ArrayList<>();
        chatList.setRefreshingColor(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        chatList.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage++;
                mUDPClientUtils.loadHistoryMsgByUserName(person.getUserName(), curPage, pageCount);
            }
        });

        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        curPage = 1;
        mUDPClientUtils.loadHistoryMsgByUserName(person.getUserName(), curPage, pageCount);
    }

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
//            Toast.makeText(ChartActivity.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            FullImageInfo fullImageInfo = new FullImageInfo();
            fullImageInfo.setLocationX(location[0]);
            fullImageInfo.setLocationY(location[1]);
            fullImageInfo.setWidth(view.getWidth());
            fullImageInfo.setHeight(view.getHeight());
            fullImageInfo.setImageUrl(messageInfos.get(position).getImageUrl());
            EventBus.getDefault().postSticky(fullImageInfo);
            startActivity(new Intent(ChartActivity.this, FullImageActivity.class));
            overridePendingTransition(0, 0);
        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfos.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(messageInfos.get(position).getFilepath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }

        @Override
        public void onLongItemClick(View view, int position) {
            MessageInfo info = chatAdapter.getAllData().get(position);
            if (info.getMsgType() == MsgType.SEND_TYPE_TXT) {
                copy(info.getContent());
            }
        }
    };

    @Override
    public void onHistoryMsgListList(List<MessageInfo> messageInfoList) {
        super.onHistoryMsgListList(messageInfoList);
        chatList.setRefreshing(false);
        messageInfos.addAll(0, messageInfoList);
        chatAdapter.insertAll(messageInfoList, 0);
        chatAdapter.notifyDataSetChanged();
        if (curPage == 0)
            chatList.scrollToPosition(chatAdapter.getCount() - 1);
        else
            ((LinearLayoutManager) chatList.getRecyclerView().getLayoutManager())
                    .scrollToPositionWithOffset(messageInfoList.size() - 1, 0);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHistoryMsgListForJson(String response) {
        super.onHistoryMsgListForJson(response);
        LogUtils.sysout("====history data======"+response);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (obj instanceof MessageInfo) {
            MessageInfo tempmMssageInfo = (MessageInfo) obj;
            final MessageInfo messageInfo = MessageInfo.sendMessageInfo(tempmMssageInfo, person, fromUserName);
            messageInfos.add(messageInfo);
            chatAdapter.add(messageInfo);
            chatList.scrollToPosition(chatAdapter.getCount() - 1);
            switch (messageInfo.getMsgType()) {
                case MsgType.SEND_TYPE_TXT:
//                    DbAppliaction.getDaoSession().getMessageInfoDao().insertOrReplace(messageInfo);
                    mUDPClientUtils.sendTxt(fromUserName, person.getUserName(), messageInfo.getContent());
                    break;
                case MsgType.SEND_TYPE_VOICE:
                    mMessageInfo = messageInfo;
                    break;
                case MsgType.SEND_TYPE_EMOJI:
                case MsgType.SEND_TYPE_VIDEO:
                case MsgType.SEND_TYPE_IMG:
                    mMessageInfo = messageInfo;
//                    DbAppliaction.getDaoSession().getMessageInfoDao().insertOrReplace(messageInfo);
                    chatAdapter.notifyDataSetChanged();
                    break;
            }
            if (messageInfo.getMsgType() != MsgType.SEND_TYPE_TXT) return;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                    chatAdapter.notifyDataSetChanged();
                }
            }, 500);
        } else if (obj instanceof SendFiles) {
            SendFiles sendFiles = (SendFiles) obj;
            if (TextUtils.isEmpty(sendFiles.getFileUrl()) || mMessageInfo == null)
                return;
            if (!sendFiles.isSuccess()) {
                mMessageInfo.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
                chatAdapter.notifyDataSetChanged();
                return;
            }
            mMessageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            chatAdapter.notifyDataSetChanged();
            switch (mMessageInfo.getMsgType()) {
                case MsgType.SEND_TYPE_IMG:
                    mUDPClientUtils.sendImg(fromUserName, person.getUserName(), sendFiles.getFileUrl());
                    break;
                case MsgType.SEND_TYPE_VOICE:
                    mUDPClientUtils.sendVoice(fromUserName, person.getUserName(), sendFiles.getFileUrl());
                    break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mUDPClientUtils.onResumeOnLine();
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 收到消息
     *
     * @param msg
     */
    @Override
    public void onSuccess(String msg) {
        LogUtils.sysout("====single==rev=msg=====" + msg);
        Send<UpInfo> revMsg = null;
        try {
            Type type = new TypeToken<Send<UpInfo>>() {
            }.getType();
            revMsg = JsonUtils.deserialize(msg, type);
        } catch (JsonSyntaxException e) {
            revMsg = null;
        }
        if (null == revMsg || revMsg.getValue() == null) return;
        final UpInfo info = revMsg.getValue();
        //单聊
        if (revMsg.isSingleChart()) {
            boolean isMe = info.getFromUser().equals(person.getUserName());
            //如果不是自己的单聊数据，则丢弃
            if (!isMe) return;
            playMsgVoice();
            verb();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MessageInfo message = MessageInfo.upInfoToMessageInfo(info, fromUserName, true);
                    message.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                    message.setHeader(message.getIsGoup() ? TextUtils.isEmpty(info.getHeadUrl()) ?
                            chartUserIconUrl : info.getHeadUrl() : chartUserIconUrl);
                    messageInfos.add(message);
                    chatAdapter.add(message);
                    chatList.scrollToPosition(chatAdapter.getCount() - 1);
                }
            });
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mUDPClientUtils.sendUnClaim(fromUserName, person.getUserName());
            EventBus.getDefault().removeStickyEvent(this);
            EventBus.getDefault().unregister(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
