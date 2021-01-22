package com.smart.chatui.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.smart.chatui.enity.UnReadMsgCount;
import com.smart.chatui.ui.activity.ChartActivity;
import com.smart.chatui.ui.activity.FullImageActivity;
import com.smart.chatui.ui.activity.UnCliamsActivity;
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

import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.bean.UnCliamMsg;
import trf.smt.com.netlibrary.bean.UpInfo;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.enity.Person;
import trf.smt.com.netlibrary.greendao.PersonDao;
import trf.smt.com.netlibrary.interfaces.ResultCallBack;
import trf.smt.com.netlibrary.utils.CacheUtils;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;

/**
 * 聊天室
 *
 * @author gbh
 */
public class ChatFragment extends BaseFragment implements ResultCallBack, View.OnClickListener {

    EasyRecyclerView chatList;
    ImageView emotionVoice;
    EditText editText;
    TextView voiceText;
    ImageView emotionButton;
    ImageView emotionAdd;
    StateButton emotionSend;
    NoScrollViewPager viewpager;
    RelativeLayout emotionLayout;
    TextView mShowClaimMsg;
    TextView mTvUnMsg;
    TextView mShowBirthdayMsg;
    TextView mTvBirthdayMsg;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<MessageInfo> messageInfos;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;
    private View rootView;
    private GlobalOnItemClickManagerUtils globalOnItemClickListener;

    private UDPClientUtils mUDPClientUtils;
    private String mUserName;//当前登录人员
    private boolean isOnPause;
    private final String chartRoom = "@chartRoom";
    private boolean isSayHi;
    private MessageInfo mMessageInfo;

    private int pageCount = 100;//每页数量
    private int page = 0;//总页数
    private int curPage = 0;//当前页数
    private int count;//总数量

    public static ChatFragment newInstance(String param) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("username", param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != this.getArguments()) {
            mUserName = this.getArguments().getString("username");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.chart_main, container, false);
            mUDPClientUtils = UDPClientUtils.getInstance(getActivity());
            mUDPClientUtils.setResultCallBack(this);
            initWidget(rootView);
//            mUDPClientUtils.sendLoadUnClaimMsg(mUserName);
        }
        return rootView;
    }


    private void initWidget(View rootView) {
        chatList = rootView.findViewById(R.id.chat_list);
        emotionVoice = rootView.findViewById(R.id.emotion_voice);
        editText = rootView.findViewById(R.id.edit_text);
        voiceText = rootView.findViewById(R.id.voice_text);
        emotionButton = rootView.findViewById(R.id.emotion_button);
        emotionAdd = rootView.findViewById(R.id.emotion_add);
        emotionSend = rootView.findViewById(R.id.emotion_send);
        viewpager = rootView.findViewById(R.id.viewpager);
        emotionLayout = rootView.findViewById(R.id.emotion_layout);
        mShowClaimMsg = rootView.findViewById(R.id.showClaimMsg);
        mTvUnMsg = rootView.findViewById(R.id.tvUnMsg);
        mShowBirthdayMsg = rootView.findViewById(R.id.showBirthdayMsg);
        mTvBirthdayMsg = rootView.findViewById(R.id.tvBirthdayMsg);

        mShowClaimMsg.setOnClickListener(this);
        mShowBirthdayMsg.setOnClickListener(this);
        messageInfos = new ArrayList<>();
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(getActivity())
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

        globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(getActivity());
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new ChatAdapter(getActivity(), true);
        chatAdapter.setHasStableIds(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
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
        chatList.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage++;
                LogUtils.sysout("=====curPage=====" + curPage);
                if (curPage >= page) {
                    curPage--;
                    chatList.setRefreshing(false);
                    return;
                }
                LoadData(curPage);
            }
        });
    }

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
//            Toast.makeText(getActivity(), "onHeaderClick", Toast.LENGTH_SHORT).show();
            MessageInfo info = chatAdapter.getAllData().get(position);
            if (null == info || info.getType() == Constants.CHAT_ITEM_TYPE_RIGHT
                    || TextUtils.isEmpty(info.getFromUser())) return;
            List<Person> list = DbAppliaction.getDaoSession().getPersonDao().queryBuilder()
                    .where(PersonDao.Properties.UserName.eq(info.getFromUser())).build().list();
            if (null == list || list.size() == 0) return;
            Person person = list.get(0);
            if (person.getType() == -1) return;
            showProgressDialog(getString(R.string.cliam_loading));
            mUDPClientUtils.sendClaim(mUserName, info.getFromUser());
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
            startActivity(new Intent(getActivity(), FullImageActivity.class));
            getActivity().overridePendingTransition(0, 0);
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

    /**
     * 构造聊天数据
     */
    private void LoadData(final int offet) {

        //获取聊天室总消息数量
        count = mUDPClientUtils.getChartRoomMessageCount();
        //计算总页数
        if (count % pageCount == 0) {
            page = count / pageCount;
        } else {
            page = count / pageCount + 1;
        }
        LogUtils.sysout("=====page=====" + page);
        List<MessageInfo> list = mUDPClientUtils.getHisChartRoomMessageInfo(offet, pageCount);
        if (null != list && list.size() > 0) {
            messageInfos.addAll(0, list);
            chatAdapter.insertAll(list, 0);
            chatAdapter.notifyDataSetChanged();
            if (offet == 0)
                chatList.scrollToPosition(chatAdapter.getCount() - 1);
            else
                ((LinearLayoutManager) chatList.getRecyclerView().getLayoutManager())
                        .scrollToPositionWithOffset(list.size() - 1, 0);

        } else {
            if (isSayHi) return;
            messageInfos.add(MessageInfo.sayHisMessageInfo());
            chatAdapter.addAll(messageInfos);
            isSayHi = true;
        }
        chatList.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(Object obj) {
        if (!getUserVisibleHint() || isOnPause) return;
        if (obj instanceof MessageInfo) {
            final MessageInfo tempMessageInfo = (MessageInfo) obj;
            final MessageInfo messageInfo = MessageInfo.sendMessageInfoToChartRoom(tempMessageInfo, mUserName, chartRoom, mUserName);
//            LogUtils.sysout("=======MessageInfo======" + JsonUtils.serialize(messageInfo));
            switch (messageInfo.getMsgType()) {
                case MsgType.SEND_TYPE_TXT:
                    messageInfos.add(messageInfo);
                    chatAdapter.add(messageInfo);
                    chatList.scrollToPosition(chatAdapter.getCount() - 1);
                    mUDPClientUtils.sendTxt(mUserName, chartRoom, messageInfo.getContent());
                    break;
                case MsgType.SEND_TYPE_VOICE:
                    mMessageInfo = messageInfo;
                    break;
                case MsgType.SEND_TYPE_EMOJI:
                case MsgType.SEND_TYPE_VIDEO:
                case MsgType.SEND_TYPE_IMG:
                    mMessageInfo = messageInfo;
                    messageInfos.add(messageInfo);
                    chatAdapter.add(messageInfo);
                    chatList.scrollToPosition(chatAdapter.getCount() - 1);
                    break;
            }
            if (messageInfo.getMsgType() != MsgType.SEND_TYPE_TXT) return;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                    chatAdapter.notifyDataSetChanged();
                }
            }, 500);
        } else if (obj instanceof SendFiles) {//发送图片
            final SendFiles sendFiles = (SendFiles) obj;
            if (TextUtils.isEmpty(sendFiles.getFileUrl()) || mMessageInfo == null)
                return;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!sendFiles.isSuccess()) {
                        mMessageInfo.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
                        chatAdapter.notifyDataSetChanged();
                        return;
                    }
                    mMessageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                    chatAdapter.notifyDataSetChanged();
                }
            }, 1000);
            switch (sendFiles.getMsgType()) {
                case MsgType.SEND_TYPE_IMG:
                    mUDPClientUtils.sendImg(mUserName, chartRoom, sendFiles.getFileUrl());
                    break;
                case MsgType.SEND_TYPE_VIDEO:
                    mUDPClientUtils.sendVoice(mUserName, chartRoom, sendFiles.getFileUrl());
                    break;
            }

        }

    }

    public boolean onBackPressed() {
        return mDetector.interceptBackPress();
    }

    /**
     * 获取微信通讯录头像
     *
     * @param userName
     * @return
     */
    private Person getIconUrl(String userName) {
        List<Person> list = DbAppliaction.getDaoSession().getPersonDao().queryBuilder().
                where(PersonDao.Properties.UserName.eq(userName)).build().list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        isOnPause = false;
        mUDPClientUtils.onResumeOnLine();
        mUDPClientUtils.setResultCallBack(this);
        globalOnItemClickListener.attachToEditText(editText);
        LogUtils.sysout("chart fragment onResume");
        curPage = 0;
        chatAdapter.clear();
        LoadData(curPage);
        reflashClaimCount();
    }


    @Override
    public void onPause() {
        super.onPause();
        isOnPause = true;
        LogUtils.sysout("chart fragment onPause");
    }


    /**
     * 收到消息
     *
     * @param msg
     */
    @Override
    public void onSuccess(String msg) {
//        LogUtils.sysout("=all==msg=========" + msg);
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
        //1、如果是3-3 则是私聊
        //2、2-1 则是公聊
        if (revMsg.isSingleChart()) {
            playMsgVoice();
            verb();
            Person person = getIconUrl(info.getFromUser());
            final String url = info.getToUser().equals(chartRoom) ? "" : null == person ? "" : person.getIconUrl();
            MessageInfo insertMessageInfo = MessageInfo.upInfoToMessageInfo(info, mUserName, false);
            insertMessageInfo.setHeader(url);
            insertMessageInfo.setNickName(null == person ? "" : person.getConRemark());
            EventBus.getDefault().post("HisContact");
            EventBus.getDefault().post(new UnReadMsgCount());
        } else if (revMsg.isGroupChart()) {//公聊
            playMsgVoice();
            verb();
            final MessageInfo insertMessageInfo = MessageInfo.upInfoToMessageInfoToChartRoom(info, chartRoom, true);
            reflashClaimCount();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageInfos.add(insertMessageInfo);
                    chatAdapter.add(insertMessageInfo);
                    chatList.scrollToPosition(chatAdapter.getCount() - 1);
                }
            });
        }

    }


    /**
     * 导购认领
     *
     * @param msg
     */
    @Override
    public void onClaim(Send<AckValue> msg) {
        LogUtils.sysout("===========onClaim==ChartFragment=======" + msg.toString());
        AckValue ackValue = msg.getValue();
        if (null == ackValue) return;
        reflashClaimCount();
        dismissProgressDialog();
        if (!getUserVisibleHint() || isOnPause) {
            EventBus.getDefault().post(ackValue);
            return;
        }
        List<Person> list = DbAppliaction.getDaoSession().getPersonDao().queryBuilder()
                .where(PersonDao.Properties.UserName.eq(ackValue.getIds())).build().list();
        if (null == list || list.size() == 0) return;
        HisContact hisContact = HisContact.personToHisContact(list.get(0), mUserName);
        if (ackValue.getTag().equals("-1")) {
            String username = TextUtils.isEmpty(hisContact.getConRemark())
                    ? hisContact.getNickName() : hisContact.getConRemark();
            String message = username + getString(R.string.hash_cliam);
            showErrorDialog(message);
        } else {
            //判断是否是自己认领的，如果不是则返回
            if (!ackValue.getTag().equals(mUserName)) return;
            startActivity(new Intent(getActivity(), ChartActivity.class)
                    .putExtra("contact", HisContact.hisContactToPerson(hisContact)));
        }

    }


    /**
     * 更新计数器
     */
    private void reflashClaimCount() {
        LogUtils.sysout("=========reflashClaimCount=======");
        EventBus.getDefault().post(new UnReadMsgCount());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mTvUnMsg) return;
                mTvUnMsg.setText(CacheUtils.getMsgCount());
            }
        });
    }

    /**
     * 导购未认领信息
     *
     * @param cliamMsgSend
     */
    @Override
    public void onUnClaimMsg(Send<UnCliamMsg> cliamMsgSend) {
        super.onUnClaimMsg(cliamMsgSend);
        if (null == cliamMsgSend) return;
        LogUtils.sysout("cliamMsgSend:" + cliamMsgSend.toString());
        mTvUnMsg.setText(cliamMsgSend.getValue().getMsgCount());
        EventBus.getDefault().post(new UnReadMsgCount());
    }

    @Override
    public void onResultHisContactListForJson(String response) {
        EventBus.getDefault().post("onResultHisContactListForJson");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showBirthdayMsg:
                showErrorDialog("没有会员生日待跟进");
                break;
            case R.id.showClaimMsg:
                if (!CacheUtils.isTrue()) {
                    showErrorDialog("没有待回复的会员消息");
                    return;
                }
                startActivity(new Intent(getActivity(), UnCliamsActivity.class));
                break;
        }
    }
}
