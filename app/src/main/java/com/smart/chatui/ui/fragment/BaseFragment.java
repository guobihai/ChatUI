package com.smart.chatui.ui.fragment;


import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.smart.chatui.base.MyApplicationLike;
import com.smart.chatui.ui.views.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.bean.UnCliamMsg;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.interfaces.ResultCallBack;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment implements ResultCallBack {


    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    public void showErrorDialog(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getActivity()).setTitle("温馨提示")
                        .setMessage(message)
                        .setNegativeButton("确认", null).show();
            }
        });

    }

//    public ProgressDialog progressDialog;
    private LoadingDialog progressDialog;
    public LoadingDialog showProgressDialog() {
        progressDialog = new LoadingDialog(getActivity(),"加载中");
        progressDialog.show();
        return progressDialog;
    }

    public LoadingDialog showProgressDialog(String message) {
        progressDialog = new LoadingDialog(getActivity(),message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null ) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.close();
        }
    }

    /**
     * 播放声音
     */
    public void playMsgVoice() {
        MyApplicationLike.playMsg();
    }

    /**
     * 振动
     */
    public void verb() {
        playMsgVoice();
        boolean isVerb = PreferenceUtils.getBoolean(getActivity(), "verb_msg", false);
        if (!isVerb) return;
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    /**
     * 复制消息
     *
     * @param msg
     */
    public void copy(String msg) {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(msg);
        Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    /**
     * 登录成功
     *
     * @param msg
     */
    @Override
    public void onLoginSuccess(String msg) {

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
        LogUtils.sysout("====onResultHisContactListForJson======"+response);
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
