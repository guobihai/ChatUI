package com.smart.chatui.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.smart.chatui.enity.Users;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import trf.smt.com.netlibrary.UDPClientUtils;
import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.bean.UnCliamMsg;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.interfaces.ResultCallBack;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * Created by gbh on 2018/5/21  14:15.
 *
 * @describe
 */

public class MsgServices extends Service implements ResultCallBack {
    private final String UDPBASEURL = "mess.csosm.com";
    public static final int UDPPORT = 62280;
    private UDPClientUtils mUDPClientUtils;
    //    private static final String FILEBASEURL = "http://file.csosm.com:7001/";
//    private static final String FILEBASEURL = "http://svr02.csosm.com:6001/";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Users.DataBean dataBean = (Users.DataBean) intent.getExtras().getSerializable("dataBean");
        mUDPClientUtils = UDPClientUtils.getInstance(this, dataBean.getIP_TABLE().getDomain(),
                dataBean.getIP_TABLE().getPort(), dataBean.getDEVICE_ID(), dataBean.getUSER_STORE_ID()
                        + "", dataBean.getIP_TABLE().getBaseUrl());
        mUDPClientUtils.setResultCallBack(this);
        mUDPClientUtils.setIp(TextUtils.isEmpty(dataBean.getIP_TABLE().getDomain()) ? UDPBASEURL : dataBean.getIP_TABLE().getDomain());
        mUDPClientUtils.setPort(dataBean.getIP_TABLE().getPort() + "");
        mUDPClientUtils.setToDevicesId(dataBean.getDEVICE_ID());
        mUDPClientUtils.login(String.valueOf(dataBean.getUSER_ID()));
        PreferenceUtils.putString(this, "filePath", dataBean.getIP_TABLE().getBaseUrl());

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 登录成功
     *
     * @param msg
     */
    @Override
    public void onLoginSuccess(String msg) {
        EventBus.getDefault().post("LoginSuccess");
    }

    /**
     * 微信在线
     */
    @Override
    public void wxOnline() {

    }

    /**
     * 微信下线
     */
    @Override
    public void wxOffLine() {

    }

    /**
     * 收到消息
     *
     * @param msg
     */
    @Override
    public void onSuccess(String msg) {

    }

    /**
     * 聊天室信息
     *
     * @param msg
     */
    @Override
    public void onChatRoomSuccess(String msg) {

    }

    /**
     * 单聊信息
     *
     * @param msg
     */
    @Override
    public void onSingleSuccess(String msg) {

    }

    /**
     * 异常信息
     *
     * @param e
     */
    @Override
    public void onFailure(Exception e) {

    }

    /**
     * 错误信息
     *
     * @param code 错误代码
     * @param msg  错误信息
     */
    @Override
    public void onFailureMsg(int code, String msg) {
        LogUtils.sysout("========msg======"+msg);
    }

    /**
     * 导购认领错误信息
     *
     * @param msg
     */
    @Override
    public void onClaimErrMsg(Send<AckValue> msg) {

    }

    /**
     * 导购认领
     *
     * @param msg
     */
    @Override
    public void onClaim(Send<AckValue> msg) {

    }

    /**
     * 导购未认领信息
     *
     * @param cliamMsgSend
     */
    @Override
    public void onUnClaimMsg(Send<UnCliamMsg> cliamMsgSend) {

    }

    /**
     * 返回最近联系人未读消息列表
     *
     * @param response 返回的消息
     */
    @Override
    public void onResultHisContactListForJson(String response) {

    }

    /**
     * 返回历史聊天信息
     *
     * @param response
     */
    @Override
    public void onHistoryMsgListForJson(String response) {

    }

    /**
     * 返回历史聊天信息
     *
     * @param messageInfoList
     */
    @Override
    public void onHistoryMsgListList(List<MessageInfo> messageInfoList) {

    }
}
