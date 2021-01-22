package trf.smt.com.netlibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.AckMsg;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.HisMessageInfo;
import trf.smt.com.netlibrary.bean.ResBean;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.bean.UnCliamMsg;
import trf.smt.com.netlibrary.bean.UpInfo;
import trf.smt.com.netlibrary.enity.Cliams;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.enity.Person;
import trf.smt.com.netlibrary.enums.TagEnum;
import trf.smt.com.netlibrary.greendao.ctrls.HisContactCtrls;
import trf.smt.com.netlibrary.greendao.ctrls.MessageInfoCtrls;
import trf.smt.com.netlibrary.http.OkHttpUtils;
import trf.smt.com.netlibrary.interfaces.ActionInterface;
import trf.smt.com.netlibrary.interfaces.ResultCallBack;
import trf.smt.com.netlibrary.utils.CacheUtils;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.MsgType;
import trf.smt.com.netlibrary.utils.PraseJsonUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;
import trf.smt.com.netlibrary.utils.SystemUitls;
import trf.smt.com.netlibrary.utils.TimeTool;
import trf.smt.com.netlibrary.utils.UDPClient;
import trf.smt.com.netlibrary.utils.UdpActionUtils;

import static java.lang.Enum.valueOf;
import static trf.smt.com.netlibrary.enums.TagEnum.ACK;
import static trf.smt.com.netlibrary.enums.TagEnum.ACKMSG;
import static trf.smt.com.netlibrary.enums.TagEnum.CLAIM;
import static trf.smt.com.netlibrary.enums.TagEnum.MSG;
import static trf.smt.com.netlibrary.enums.TagEnum.UNCLAIM_MSG;
import static trf.smt.com.netlibrary.enums.TypeEnum.IMG;
import static trf.smt.com.netlibrary.enums.TypeEnum.TXT;
import static trf.smt.com.netlibrary.enums.TypeEnum.VOICE;

/**
 * Created by gbh on 2018/4/16  11:29.
 *
 * @describe UDP通信封装处理
 */

public final class UDPClientUtils implements ActionInterface {
    private String BASEURL = "http://113.106.222.250:9001";
    private static final String ILEAGLECLAIM = "-1";//认领失败返回标签
    private Handler mDelivery;
    private ResultCallBack mResultCallBack;
    private ExecutorService exec;//缓存线程池
    private UdpActionUtils mUdpActionUtils;
    private Context mContext;
    private String toDeviceId;//门店设备
    private String fromDeviceId;//本机设备
    private String shopId;//门店ID
    private final String chartRoom = "@chartRoom";
    private final String chatGroup = "@chatroom";//群聊
    private boolean isLogin;
    private String mUserId;//用户IDs
    private String curUserName = "";//当前聊天的wx用户

    private static UDPClientUtils sUdpActionUtils;

    private Send<AckValue> tagValueSend = null;
    private Map<String, Person> mPersonMap;
    private int page;//聊天室总页数

    public static UDPClientUtils getInstance(Context context) {
        if (null == sUdpActionUtils)
            sUdpActionUtils = new UDPClientUtils(context);
        return sUdpActionUtils;
    }

    /**
     * 初始化
     *
     * @param context    上下文
     * @param ip         通信IP
     * @param port       端口
     * @param toDeviceId 门店设备ID
     * @param shopId     门店ID
     * @param baesUrl
     * @return
     */
    public static UDPClientUtils getInstance(Context context, String ip, int port,
                                             String toDeviceId, String shopId, String baesUrl) {
        if (null == sUdpActionUtils)
            sUdpActionUtils = new UDPClientUtils(context, ip, port, toDeviceId, shopId, baesUrl);
        return sUdpActionUtils;
    }

    public UDPClientUtils(Context context) {
        this.mContext = context;
        if (null == mContext) throw new IllegalArgumentException("context不能为空");
        toDeviceId = PreferenceUtils.getString(mContext, "devicesId", "FFFFFFFF");
        String ip = PreferenceUtils.getString(mContext, "ip", "");
        if (TextUtils.isEmpty(ip)) throw new IllegalArgumentException("IP地址不能为空");
        init(ip, PreferenceUtils.getInt(mContext, "port", 0), toDeviceId, "", "");
    }

    /**
     * 初始化
     *
     * @param context    上下文
     * @param ip         通信IP
     * @param port       端口
     * @param toDeviceId 门店设备ID
     * @param baesUrl
     * @return
     */
    public UDPClientUtils(Context context, String ip, int port, String toDeviceId, String shopId, String baesUrl) {
        this.mContext = context;
        if (null == mContext) throw new IllegalArgumentException("context不能为空");
        init(ip, port, toDeviceId, shopId, baesUrl);
    }

    /**
     * 获取设备门店ID
     *
     * @return
     */
    public String getToDeviceId() {
        return toDeviceId;
    }

    /**
     * 初始化
     *
     * @param ip
     * @param port
     * @param toDeviceId
     */
    private void init(String ip, int port, String toDeviceId, String shopId, String baesUrl) {
        isLogin = false;
        if (port > 65535 || port < 0)
            throw new IllegalArgumentException("端口越界，请确认端口是否正常:当前传参端口:" + port);
        if (TextUtils.isEmpty(ip)) throw new IllegalArgumentException("IP地址不能为空");
        if (TextUtils.isEmpty(toDeviceId)) throw new IllegalArgumentException("接收设备ID不能为空");
        mPersonMap = new HashMap<>();
        mDelivery = new Handler(Looper.getMainLooper());
        this.toDeviceId = toDeviceId;
        this.shopId = shopId;
        this.BASEURL = baesUrl;
        exec = Executors.newCachedThreadPool();
        UDPClient mUDPClient = new UDPClient(ip, port);
        exec.execute(mUDPClient);
        fromDeviceId = SystemUitls.getFromDevicesId(mContext);
        mUdpActionUtils = new UdpActionUtils(exec, mUDPClient, fromDeviceId, toDeviceId);
        mUdpActionUtils.setHeartDelayTime(PreferenceUtils.getInt(mContext, "heart_delay", 120));
        mUDPClient.setRevCallBackInterface(new UDPClient.RevCallBackInterface() {
            @Override
            public void onCallBack(String data) {
                onCallBackMsg(data);
            }
        });
        loadData(shopId);
    }

    public void setCurUserName(String curUserName) {
        this.curUserName = curUserName;
    }

    /**
     * 获取所有通讯录信息
     *
     * @return
     */
    public Map<String, Person> getPersonMap() {
        return mPersonMap;
    }

    /**
     * 获取所有通讯录信息
     *
     * @return
     */
    public String getPersonMapToJson() {
        return JsonUtils.serialize(mPersonMap);
    }


    /**
     * 根据username获取通讯录对象
     *
     * @param username
     * @return
     */
    public Person getContactInfo(String username) {
        return mPersonMap.get(username);
    }

    /**
     * 判断是否是微信用户
     *
     * @param username
     * @return
     */
    public boolean isWxUser(String username) {
        Person person = mPersonMap.get(username);
        if (null == person) return false;
        return person.isWxUser();
    }

    public void setResultCallBack(ResultCallBack resultCallBack) {
        mResultCallBack = resultCallBack;
    }

    /**
     * 消息回调
     *
     * @param data
     */
    private void onCallBackMsg(final String data) {
        if (TextUtils.isEmpty(data) || null == mResultCallBack) return;
        LogUtils.sysout("onCallBackMsg:" + data);
        try {
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
            String tag = jsonObject.get("tag").getAsString().toUpperCase();
            TagEnum tagEnum = valueOf(TagEnum.class, tag);
            if (tagEnum == ACK) {
                Type type = new TypeToken<Send<AckValue>>() {
                }.getType();
                tagValueSend = JsonUtils.deserialize(data, type);
                //登录成功,启动心跳包
                if (tagEnum.equals(ACK) && null != tagValueSend) {
                    tagEnum = valueOf(TagEnum.class, tagValueSend.getValue().getTag().toUpperCase());
                    switch (tagEnum) {
                        case LOGIN:
                            mUdpActionUtils.setRun(false);
                            if (!isLogin) {
                                isLogin = true;
                                mUdpActionUtils.sendHeartData();
                                sendLoadUnClaimMsg(mUserId);
                                mDelivery.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mResultCallBack.onLoginSuccess(data);
                                    }
                                });
                            }
                            return;
                        case WXSTATE:
                            mDelivery.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (tagValueSend.getValue().getIds().equals("online")) {
                                        mResultCallBack.wxOnline();
                                    } else
                                        mResultCallBack.wxOffLine();
                                }
                            });
                            return;
                        case SINGLE_CONTACT://当条通讯录同步
                            String json = (String) tagValueSend.getValue().getIds();
                            if (!TextUtils.isEmpty(json)) {
                                JsonParser parser = new JsonParser();
                                JsonArray array = (JsonArray) parser.parse(json);
                                for (int i = 0; i < array.size(); i++) {
                                    JsonObject object = (JsonObject) array.get(i);
                                    String username = object.get("userName").getAsString();
                                    String nikeName = object.get("nickName").getAsString();
                                    String conRemark = object.get("conRemark").getAsString();
                                    String iconUrl = object.get("iconUrl").getAsString();
                                    int wxType = object.get("type").getAsInt();
                                    Person person = new Person(1L, username, nikeName, conRemark, iconUrl, wxType, "");
                                    mPersonMap.put(username, person);
                                }
                            }
                            break;
                    }
                }
            } else if (tagEnum == MSG) {//接收到消息应答回去
                Type type = new TypeToken<Send<UpInfo>>() {
                }.getType();
                final Send<UpInfo> send = JsonUtils.deserialize(data, type);
                if (null == send || null == send.getValue()) {
                    mResultCallBack.onFailure(new IllegalArgumentException("接收报文异常"));
                    return;
                }
                //消息应答
                Send<AckMsg> sendAck = new Send<>(ACKMSG.getTag(), new AckMsg(send.getFromDevicesId(),
                        send.getValue().getId() + ""), fromDeviceId, toDeviceId);
                mUdpActionUtils.send(sendAck.toString());
                final Person person = mPersonMap.get(send.getValue().getFromUser());
                //是否群消息
                if (send.getValue().isGoup()) {
                    String str = send.getValue().getContent();
                    if (!TextUtils.isEmpty(str) && str.contains(":")) {
                        String key = str.substring(0, str.indexOf(":"));
                        if (TextUtils.isEmpty(key)) return;
                        Person p = mPersonMap.get(key);
                        if (null == p) return;
                        if (send.isGroupChart()) {
                            key = str.substring(0, str.indexOf(":") + 1);
                            send.getValue().setContent(str.replace(key, p.getConRemark()));
                        } else {
                            key = str.substring(0, str.indexOf(":") + 2);
                            send.getValue().setContent(str.replace(key, ""));
                        }
                        send.getValue().setNickName(p.getConRemark());
                        send.getValue().setHeadUrl(p.getIconUrl());
                        send.getValue().setWx(p.isWxUser());
                    }
                } else {
                    if (null != person) {
                        send.getValue().setNickName(person.getConRemark());
                        send.getValue().setHeadUrl(person.getIconUrl());
                        send.getValue().setWx(person.isWxUser());
                    }
                }

                final String newData = JsonUtils.serialize(send);
                //增加计数器
                if (send.isGroupChart()) {
                    UpInfo upInfo = send.getValue();
                    if (null != person && person.isWxUser()) {
                        CacheUtils.putClaim(send.getValue().getFromUser(), "1");
                        List<HisContact> list = HisContactCtrls.getHisContactListByUserId(mUserId, upInfo.getFromUser());
                        if (list.size() > 0) {
                            HisContact updateHisContact = list.get(0);
                            updateHisContact.setLastMsg(upInfo.getContent());
                            updateHisContact.setMsgCount(Integer.parseInt(CacheUtils.getMsgCount(upInfo.getFromUser())));
                            updateHisContact.setIconUrl(person.getIconUrl());
                            updateHisContact.setNickName(person.getConRemark());
                            updateHisContact.setType(upInfo.getType());
                            updateHisContact.setMemberId(person.getMemberId());
                            updateHisContact.setCreateTime(HisContact.getTime());
                            HisContactCtrls.updateHisContact(updateHisContact);
                        } else {
                            HisContact insertHisContact = HisContact.personToHisContact(person, mUserId);
                            insertHisContact.setLastMsg(upInfo.getContent());
                            insertHisContact.setType(upInfo.getType());
                            insertHisContact.setMemberId(person.getMemberId());
                            insertHisContact.setMsgCount(Integer.parseInt(CacheUtils.getMsgCount(upInfo.getFromUser())));
                            long id = HisContactCtrls.insertHisContactData(insertHisContact);
                            LogUtils.sysout("insert id:" + id);
                        }
                        if (null != mResultCallBack)
                            mResultCallBack.onResultHisContactListForJson(loadNearMessageInfoToJson());
                    }
                    //保存聊天室消息记录
                    final String url = upInfo.getToUser().equals(chartRoom) ? "" : null == person ? "" : "" + person.getIconUrl();
                    final MessageInfo insertMessageInfo = MessageInfo.upInfoToMessageInfoToChartRoom(upInfo, chartRoom, true);
                    insertMessageInfo.setHeader(url);
                    insertMessageInfo.setIsWx(isWxUser(upInfo.getFromUser()));
                    insertMessageInfo.setNickName(null == person ? "匿名" : person.getConRemark());
                    DbAppliaction.getDaoSession().getMessageInfoDao().insertOrReplace(insertMessageInfo);
                } else if (send.isSingleChart()) {
                    UpInfo upInfo = send.getValue();
                    if (null != person && person.isWxUser()) {
                        if (null == upInfo) return;
                        if (upInfo.getToUser().equals(mUserId)) {
                            List<HisContact> list = HisContactCtrls.getHisContactListByUserId(mUserId, upInfo.getFromUser());
                            if (list.size() > 0) {
                                HisContact updateHisContact = list.get(0);
                                updateHisContact.setLastMsg(upInfo.getContent());
                                updateHisContact.setMsgCount(0);
                                updateHisContact.setIconUrl(person.getIconUrl());
                                updateHisContact.setNickName(person.getConRemark());
                                updateHisContact.setType(upInfo.getType());
                                updateHisContact.setMemberId(person.getMemberId());
                                updateHisContact.setCreateTime(HisContact.getTime());
                                HisContactCtrls.updateHisContact(updateHisContact);
                            }
                            if (null != mResultCallBack)
                                mResultCallBack.onResultHisContactListForJson(loadNearMessageInfoToJson());
                        }
                    }
                    if (null == upInfo) return;
                    boolean isMe = upInfo.getFromUser().equals(curUserName);
                    final String url = null == person ? "" : person.getIconUrl();
                    MessageInfo insertMessageInfo = MessageInfo.upInfoToMessageInfo(upInfo, mUserId, isMe);
                    insertMessageInfo.setHeader(url);
                    insertMessageInfo.setIsWx(true);
                    insertMessageInfo.setNickName(null == person ? "" : person.getConRemark());
                    DbAppliaction.getDaoSession().getMessageInfoDao().insertOrReplace(insertMessageInfo);
                }

                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (send.isSingleChart()) {
                            mResultCallBack.onSingleSuccess(newData);
                        } else {
                            mResultCallBack.onChatRoomSuccess(newData);
                        }
                        mResultCallBack.onSuccess(newData);
                    }
                });

            } else if (tagEnum == CLAIM) {//导购认领消息
                Type type = new TypeToken<Send<AckValue>>() {
                }.getType();
                final Send<AckValue> send = JsonUtils.deserialize(data, type);
                if (null == send) {
                    mResultCallBack.onFailure(new IllegalArgumentException("接收报文异常"));
                    return;
                }
                AckValue ackValue = send.getValue();
                if (null != ackValue) {
                    if (!ackValue.getTag().equals(ILEAGLECLAIM) && mUserId.equals(ackValue.getTag())) {
                        setCurUserName(String.valueOf(ackValue.getIds()));
                    } else {
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                mResultCallBack.onClaimErrMsg(send);
                            }
                        });
                    }
                    CacheUtils.removeClaim(String.valueOf(ackValue.getIds()));
                    HisContactCtrls.updateAllUnMsgState(String.valueOf(ackValue.getIds()), mUserId);
                    if (null != mResultCallBack)
                        mResultCallBack.onResultHisContactListForJson(loadNearMessageInfoToJson());
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            mResultCallBack.onClaim(send);
                        }
                    });
                }
            } else if (tagEnum == UNCLAIM_MSG) {//未认领消息
                Type type = new TypeToken<Send<UnCliamMsg>>() {
                }.getType();
                final Send<UnCliamMsg> send = JsonUtils.deserialize(data, type);
                if (null == send) {
                    mResultCallBack.onFailure(new IllegalArgumentException("接收报文异常"));
                    return;
                }
                //缓存未认领未读消息
                CacheUtils.putAllClaim(send.getValue().getListUserMapInfo());
                loadCliamsDataForNet();
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        mResultCallBack.onUnClaimMsg(send);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.sysout("====ex===" + data);
            mUdpActionUtils.sendErrorMsg("接收报文异常:" + e.toString());
            mResultCallBack.onFailure(e);
        }


    }

    /**
     * 保持最后一条消息
     *
     * @param fromUser
     * @param type     消息
     */
    private void saveLastHisContactData(String fromUser, String content, int type) {
        if (fromUser.equals(chartRoom)) return;
        final Person person = mPersonMap.get(fromUser);
        List<HisContact> list = HisContactCtrls.getHisContactListByUserId(mUserId, fromUser);
        if (null == person) return;
        if (list.size() > 0) {
            HisContact updateHisContact = list.get(0);
            updateHisContact.setLastMsg(content);
            updateHisContact.setMsgCount(Integer.parseInt(CacheUtils.getMsgCount(fromUser)));
            updateHisContact.setIconUrl(person.getIconUrl());
            updateHisContact.setNickName(person.getConRemark());
            updateHisContact.setType(type);
            updateHisContact.setMemberId(person.getMemberId());
            updateHisContact.setCreateTime(HisContact.getTime());
            HisContactCtrls.updateHisContact(updateHisContact);
        } else {
            HisContact insertHisContact = HisContact.personToHisContact(person, mUserId);
            insertHisContact.setLastMsg(content);
            insertHisContact.setType(type);
            insertHisContact.setMemberId(person.getMemberId());
            insertHisContact.setMsgCount(Integer.parseInt(CacheUtils.getMsgCount(fromUser)));
            long id = HisContactCtrls.insertHisContactData(insertHisContact);
            LogUtils.sysout("insert id:" + id);
        }
        if (null != mResultCallBack)
            mResultCallBack.onResultHisContactListForJson(loadNearMessageInfoToJson());
    }


    /**
     * 设备登录
     *
     * @param userId
     */
    @Override
    public boolean login(String userId) {
        if (TextUtils.isEmpty(userId)) return false;
        fromDeviceId = SystemUitls.getFromDevicesId(mContext);
        if (TextUtils.isEmpty(fromDeviceId)) {
            if (null == mResultCallBack) return false;
            mResultCallBack.onFailureMsg(10001, "IMEI获取失败，请授权获取");
            return false;
        }
        mUdpActionUtils.setFromDeviceId(fromDeviceId);
        mUserId = userId;
        return mUdpActionUtils.sendLoginData(userId);
    }

    /**
     * 设备登出
     * 建议延迟1.5 秒后调用
     */
    public boolean logout(final onLogoutInterface onLogoutInterface) {
        if (null == onLogoutInterface)
            throw new IllegalArgumentException("onLogoutInterface  is not null");
        mUdpActionUtils.sendLogoutData();
        onLogoutInterface.onStartLogout();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                close();
                onLogoutInterface.onFinishLogout();
            }
        }, 1500);

        return true;
    }

    /**
     * 发送文本信息到聊天室
     *
     * @param fromUserId
     * @param content
     */
    @Override
    public boolean sendChatRoomTxt(String fromUserId, String content) {
        return sendTxt(fromUserId, chartRoom, content);
    }

    /**
     * 发送图片信息到聊天室
     *
     * @param fromUserId
     * @param imgeUrl
     */
    @Override
    public boolean sendChatRoomImg(String fromUserId, String imgeUrl) {
        return sendImg(fromUserId, chartRoom, imgeUrl);
    }


    /**
     * 发送文本
     *
     * @param fromUserId 发送人ID
     * @param content    内容
     * @param toUserId   接收人ID
     */
    @Override
    public boolean sendTxt(String fromUserId, String toUserId, String content) {
        MessageInfo messageInfo = MessageInfo.sendMessageInfo(content, toUserId, mUserId);
        messageInfo.setMsgType(MsgType.SEND_TYPE_TXT);
        long msgId = DbAppliaction.getDaoSession().getMessageInfoDao().insert(messageInfo);
        if (msgId < 0) return false;
        UpInfo upInfo = new UpInfo(msgId, fromUserId, toUserId, content, String.valueOf(System.currentTimeMillis()), 1, TXT.getType());
        Send<UpInfo> send = new Send<>(MSG.getTag(), upInfo, fromDeviceId, toDeviceId);
        LogUtils.sysout("=====msg:" + JsonUtils.serialize(send));
        saveLastHisContactData(toUserId, content, TXT.getType());
        return mUdpActionUtils.send(send.toString());
    }

    /**
     * 发送图片
     *
     * @param fromUserId 发送人
     * @param toUserId   接收人
     * @param imageUrl   图片路径
     */
    @Override
    public boolean sendImg(String fromUserId, String toUserId, String imageUrl) {
        MessageInfo messageInfo = MessageInfo.sendMessageInfo(imageUrl, toUserId, mUserId);
        messageInfo.setMsgType(MsgType.SEND_TYPE_IMG);
        long msgId = DbAppliaction.getDaoSession().getMessageInfoDao().insert(messageInfo);
        if (msgId < 0) return false;
        UpInfo upInfo = new UpInfo(msgId, fromUserId, toUserId, imageUrl, String.valueOf(System.currentTimeMillis()), 1, IMG.getType());
        Send<UpInfo> send = new Send<>(MSG.getTag(), upInfo, fromDeviceId, toDeviceId);
        saveLastHisContactData(toUserId, imageUrl, IMG.getType());
        return mUdpActionUtils.send(send.toString());
    }

    /**
     * 发送语音
     *
     * @param fromUserId
     * @param toUserId
     * @param imageUrl   图片路径
     */
    @Override
    public boolean sendVoice(String fromUserId, String toUserId, String imageUrl) {
        MessageInfo messageInfo = MessageInfo.sendMessageInfo(imageUrl, toUserId, mUserId);
        messageInfo.setMsgType(MsgType.SEND_TYPE_VOICE);
        long msgId = DbAppliaction.getDaoSession().getMessageInfoDao().insert(messageInfo);
        if (msgId < 0) return false;
        UpInfo upInfo = new UpInfo(msgId, fromUserId, toUserId, imageUrl, String.valueOf(System.currentTimeMillis()), 1, VOICE.getType());
        Send<UpInfo> send = new Send<>(MSG.getTag(), upInfo, fromDeviceId, toDeviceId);
        saveLastHisContactData(toUserId, imageUrl, VOICE.getType());
        return mUdpActionUtils.send(send.toString());
    }

    /**
     * 群发发送文本
     *
     * @param fromUserId 发送人
     * @param toUserId   接收群人 ;号隔开
     * @param content    内容
     */
    @Override
    public boolean sendGroupTxt(String fromUserId, String toUserId, String content) {
        return sendTxt(fromUserId, toUserId, content);
    }

    /**
     * 群发发送图片
     *
     * @param fromUserId 发送人
     * @param toUserId   接收群人 ;号隔开
     * @param imageUrl   图片路径
     */
    @Override
    public boolean sendGroupImg(String fromUserId, String toUserId, String imageUrl) {
        return sendImg(fromUserId, toUserId, imageUrl);
    }

    /**
     * 设置IP地址
     *
     * @param ip
     */
    @Override
    public void setIp(String ip) {
        if (TextUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("IP地址不能为空");
        }
        PreferenceUtils.putString(mContext, "ip", ip);
    }

    /**
     * 设置端口
     *
     * @param port
     */
    @Override
    public void setPort(String port) {
        PreferenceUtils.putInt(mContext, "port", Integer.parseInt(port.trim()));
    }

    /**
     * 设置延时时间 秒为单位
     *
     * @param delayTime
     */
    @Override
    public void setHeartDelayTime(String delayTime) {
        PreferenceUtils.putInt(mContext, "heart_delay", Integer.parseInt(delayTime.trim()));
    }

    /**
     * 设置接收设备ID号
     *
     * @param devicesId
     */
    @Override
    public void setToDevicesId(String devicesId) {
        PreferenceUtils.putString(mContext, "devicesId", devicesId);
    }

    /**
     * 设置http 服务端URL
     *
     * @param url
     */
    @Override
    public void setBaseUrl(String url) {
        if (TextUtils.isEmpty(url)) return;
        PreferenceUtils.putString(mContext, "baseUrl", url);
    }

    /**
     * 导购认领
     *
     * @param loginUserId 当前登录导购
     * @param username    wx username
     * @return
     */
    @Override
    public boolean sendClaim(String loginUserId, String username) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(loginUserId)) return false;
        return mUdpActionUtils.sendClaim(loginUserId, username);
    }

    /**
     * 导购取消认领
     *
     * @param loginUserId 当前登录导购
     * @param username    wx username
     * @return
     */
    @Override
    public boolean sendUnClaim(String loginUserId, String username) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(loginUserId)) return false;
        return mUdpActionUtils.sendUnClaim(loginUserId, username);
    }

    /**
     * 同步未领取微信好友信息
     *
     * @param userId 当前登录的userId
     * @return
     */
    @Override
    public boolean sendLoadUnClaimMsg(String userId) {
        if (TextUtils.isEmpty(userId)) return false;
        return mUdpActionUtils.sendLoadUnClaimMsg(toDeviceId, userId);
    }

    /**
     * 发送朋友圈信息
     *
     * @param content
     * @param fileUrl
     * @return
     */
    @Override
    public boolean sendFriendsMsg(String content, String fileUrl) {
        if (TextUtils.isEmpty(content)) return false;
        return mUdpActionUtils.sendFriendsMsg(content, fileUrl);
    }

    /**
     * 获取最近消息列表
     *
     * @return
     */
    @Override
    public List<HisContact> loadNearMessageInfo() {
        List<HisContact> hisContactList = new ArrayList<>();
        List<HisContact> list = HisContactCtrls.loadNearByContactInfo(mUserId);
        //获取未读消息
        //chartRoom
        HisContact hisContact = HisContactCtrls.loadNearByChartRoomInfo(mUserId);
        hisContactList.add(hisContact);
        hisContactList.addAll(list);
        LogUtils.sysout("最近消息列表：" + JsonUtils.serialize(hisContactList));
        return hisContactList;
    }

    /**
     * 获取最近消息列表，返回json 数据格式
     *
     * @return
     */
    @Override
    public String loadNearMessageInfoToJson() {
        String str = JsonUtils.serialize(loadNearMessageInfo());
        LogUtils.sysout("最近消息列表：" + str);
        return str;
    }


    /**
     * 获取聊天室总聊天记录
     *
     * @return 返回总记录
     */
    @Override
    public int getChartRoomMessageCount() {
        return MessageInfoCtrls.getChatRoomMessageInfoCount(chartRoom);
    }

    /**
     * 获取聊天室历史消息
     *
     * @param curPage   当前页数
     * @param pageCount 每页总条数
     * @return 返回历史消息列表记录
     */
    @Override
    public List<MessageInfo> getHisChartRoomMessageInfo(int curPage, int pageCount) {
        int count = getChartRoomMessageCount();
        if (count == 0) return new ArrayList<>();
        //计算总页数
        if (count % pageCount == 0) {
            page = count / pageCount;
        } else {
            page = count / pageCount + 1;
        }
        if (curPage >= page) return new ArrayList<>();
        return MessageInfoCtrls.getChatRoomListMessageInfoByPage(curPage, pageCount, chartRoom);
    }

    /**
     * 转换使用，应付h5调用
     *
     * @param curPage   当前页数
     * @param pageCount 每页总条数
     * @return 返回历史消息列表记录
     */
    @Override
    public List<UpInfo> getHisChartRoomMessageInfoToUpInfo(int curPage, int pageCount) {
        int count = getChartRoomMessageCount();
        if (count == 0) return new ArrayList<>();
        //计算总页数
        if (count % pageCount == 0) {
            page = count / pageCount;
        } else {
            page = count / pageCount + 1;
        }
        if (curPage >= page) return new ArrayList<>();
        return MessageInfoCtrls.getChatRoomListUpInfoInfoByPage(curPage, pageCount, chartRoom);
    }

    /**
     * 获取聊天室历史消息
     *
     * @param curPage   当前页数
     * @param pageCount 每页总条数
     * @return 返回历史消息列表记录
     */
    @Override
    public String getHisChartRoomMessageInfoToJson(int curPage, int pageCount) {
        return JsonUtils.serialize(getHisChartRoomMessageInfoToUpInfo(curPage, pageCount));
    }


    /**
     * 加载门店下的通讯录
     *
     * @param shopId 门店ID
     */
    private void loadData(String shopId) {
        if (TextUtils.isEmpty(shopId)) return;
        String baseUrl = PreferenceUtils.getString(mContext, "baseUrl", BASEURL);
        String url = baseUrl + "/api/webchat/" + shopId + "/contacts.json";
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
//                LogUtils.sysout("root:" + response);
                try {
                    Type type = new TypeToken<ResBean<List<Person>>>() {

                    }.getType();
                    ResBean<List<Person>> listResBean = JsonUtils.deserialize(response, type);
                    if (null == listResBean || !listResBean.isSuccess() || null == listResBean.getData())
                        return;
                    if (listResBean.getData().size() == 0) return;
                    initMapPerson(listResBean.getData());
                    LogUtils.sysout("res contact:" + listResBean.getData().size());
                    loadCliamsDataForNet();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    /**
     * 加载指定微信用户未读消息列表信息
     */
    private void loadCliamsDataForNet() {
        String url = BASEURL + "/api/query/webchat/lastchatrecord/list.json?userId=" + mUserId + "";
        final Map<String, String> map = new HashMap<>();
        map.put("int_storeId", shopId);
        map.put("str_deviceId", toDeviceId);
        map.put("strs_weixinIds", CacheUtils.getGroupWxUserName());
        OkHttpUtils.post(url, JsonUtils.serialize(map), new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout("===loadCliamsDataForNet====" + response);
                try {
                    Cliams cliams = JsonUtils.deserialize(response, Cliams.class);
                    if (null == cliams) return;
                    if (cliams.isSuccess()) {
                        if (null == cliams.getData()) return;
                        for (Cliams.DataBean bean : cliams.getData()) {
                            bean.setMsgCount(CacheUtils.getMsgCount(bean.getFromuser()));
                            if (bean.getFromuser().endsWith(chatGroup)) {
                                String str = bean.getContent();
                                if (!TextUtils.isEmpty(str) && str.contains(":")) {
                                    String key = str.substring(0, str.indexOf(":"));
                                    if (TextUtils.isEmpty(key)) return;
                                    Person p = mPersonMap.get(key);
                                    if (null == p) continue;
                                    key = str.substring(0, str.indexOf(":") + 1);
                                    bean.setContent(str.replace(key, p.getConRemark()));
                                }
                            }
                            Person person = mPersonMap.get(bean.getFromuser());
                            if (null == person) continue;
                            List<HisContact> list = HisContactCtrls.getHisContactListByUserId(mUserId, bean.getFromuser());
                            if (list.size() > 0) {
                                HisContact updateHisContact = list.get(0);
                                updateHisContact.setLastMsg(bean.getContent());
                                updateHisContact.setMsgCount(Integer.parseInt(bean.getMsgCount()));
                                updateHisContact.setIconUrl(person.getIconUrl());
                                updateHisContact.setNickName(person.getConRemark());
                                updateHisContact.setType(bean.getMsgType());
                                updateHisContact.setMemberId(person.getMemberId());
                                updateHisContact.setCreateTime(bean.getRecieveTime());
                                LogUtils.sysout("====updateHisContact====getCreateTime======"+updateHisContact.getCreateTime());
                                HisContactCtrls.updateHisContact(updateHisContact);
                            } else {
                                HisContact insertHisContact = HisContact.personToHisContact(person, mUserId);
                                insertHisContact.setLastMsg(bean.getContent());
                                insertHisContact.setType(bean.getMsgType());
                                insertHisContact.setMsgCount(Integer.parseInt(bean.getMsgCount()));
                                insertHisContact.setMemberId(person.getMemberId());
                                insertHisContact.setCreateTime(bean.getRecieveTime());
                                long id = HisContactCtrls.insertHisContactData(insertHisContact);
                                LogUtils.sysout("insert id:" + id);
                            }
                        }
                        //返回列表信息
                        List<HisContact> hisContactList = loadNearMessageInfo();
                        if (null != mResultCallBack)
                            mResultCallBack.onResultHisContactListForJson(JsonUtils.serialize(hisContactList));
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (null != mResultCallBack)
                    mResultCallBack.onFailure(e);
            }
        });
    }

    /**
     * 发送当个心跳包，在onResume调用
     *
     * @return
     */
    public boolean onResumeOnLine() {
        return sendLoadUnClaimMsg(mUserId);
    }


    /**
     * 关闭后台线程
     */
    public void close() {
        mUdpActionUtils.close();
    }

    /**
     * 初始化通讯录
     */
    private void initMapPerson(List<Person> peopleList) {
        if (null == peopleList || peopleList.size() == 0) return;
        for (Person person : peopleList) {
            if (!TextUtils.isEmpty(person.getUserName()))
                mPersonMap.put(person.getUserName().trim(), person);
        }
    }


    /**
     * 分页获取聊天记录
     *
     * @param userName
     * @param curPage
     * @param pageCount
     */
    public void loadHistoryMsgByUserName(final String userName, final int curPage, int pageCount) {
        final String url = BASEURL + "/api/query/webchat/chathistory/pages.json?userId=" + mUserId;
        Map<String, String> map = new HashMap<>();
        map.put("str_date_time", String.valueOf(System.currentTimeMillis()));
        map.put("str_weixinId", userName);
        map.put("int_shoppingGuide", mUserId);
        map.put("pageNum", String.valueOf(curPage));
        map.put("pageSize", String.valueOf(pageCount));
        OkHttpUtils.post(url, JsonUtils.serialize(map), new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout("=====response======" + response);
                try {
                    Person person = mPersonMap.get(userName);
                    String chartUserIconUrl = null == person ? "" : person.getIconUrl();
                    HisMessageInfo hisMessageInfo = PraseJsonUtils.praseMsgInfo(response, chartUserIconUrl);
                    if (hisMessageInfo.isSuccess()) {
                        if (null == hisMessageInfo.getData() || null == hisMessageInfo.getData().getData() ||
                                hisMessageInfo.getData().getData().size() == 0) {
                            if (null != mResultCallBack) {
                                mResultCallBack.onHistoryMsgListForJson(JsonUtils.serialize(new ArrayList<MessageInfo>()));
                                mResultCallBack.onHistoryMsgListList(new ArrayList<MessageInfo>());
                            }
                            return;
                        }
                        //群消息
                        List<MessageInfo> messageInfoList = hisMessageInfo.getData().getData();
                        boolean isGroup = userName.endsWith("@chatroom");
                        if (isGroup) {
                            for (MessageInfo info : messageInfoList) {
                                String str = info.getContent();
                                if (str.contains(":")) {
                                    String key = str.substring(0, str.indexOf(":"));
                                    if (!TextUtils.isEmpty(key)) {
                                        Person p = mPersonMap.get(key);
                                        if (null != p) {
                                            key = str.substring(0, str.indexOf(":") + 2);
                                            info.setContent(str.replace(key, ""));
                                            info.setNickName(p.getConRemark());
                                            info.setHeader(p.getIconUrl());
                                            info.setIsGoup(true);
                                            switch (info.getMsgType()) {
                                                case MsgType.SEND_TYPE_IMG:
                                                case MsgType.SEND_TYPE_EMOJI:
                                                    info.setImageUrl(info.getContent());
                                                    break;
                                                case MsgType.SEND_TYPE_VOICE:
                                                    info.setFilepath(info.getContent());
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (null != mResultCallBack) {
                            mResultCallBack.onHistoryMsgListForJson(JsonUtils.serialize(messageInfoList));
                            mResultCallBack.onHistoryMsgListList(messageInfoList);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (null != mResultCallBack) {
                        mResultCallBack.onFailureMsg(9999, "获取历史记录失败");
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (null != mResultCallBack) {
                    mResultCallBack.onFailureMsg(9999, "获取历史记录失败");
                }
            }
        });
    }


    //退出接口，延时处理
    public interface onLogoutInterface {
        /**
         * 开始退出
         */
        void onStartLogout();

        /**
         * 结束退出
         */
        void onFinishLogout();
    }
}
