package trf.smt.com.netlibrary.interfaces;

import java.util.List;

import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.bean.UnCliamMsg;
import trf.smt.com.netlibrary.enity.MessageInfo;

/**
 * Created by gbh on 2018/4/16  11:31.
 *
 * @describe
 */

public interface ResultCallBack {
    /**
     * 登录成功
     *
     * @param msg
     */
    void onLoginSuccess(String msg);

    /**
     * 微信在线
     */
    void wxOnline();

    /**
     * 微信下线
     */
    void wxOffLine();

    /**
     * 收到消息
     *
     * @param msg
     */
    void onSuccess(String msg);

    /**
     * 聊天室信息
     *
     * @param msg
     */
    void onChatRoomSuccess(String msg);


    /**
     * 单聊信息
     *
     * @param msg
     */
    void onSingleSuccess(String msg);

    /**
     * 异常信息
     *
     * @param e
     */
    void onFailure(Exception e);

    /**
     * 错误信息
     *
     * @param code 错误代码
     * @param msg  错误信息
     */
    void onFailureMsg(int code, String msg);

    /**
     * 导购认领错误信息
     *
     * @param msg
     */
    void onClaimErrMsg(Send<AckValue> msg);

    /**
     * 导购认领成功信息
     *
     * @param msg
     */
    void onClaim(Send<AckValue> msg);

    /**
     * 导购未认领信息
     *
     * @param cliamMsgSend
     */
    void onUnClaimMsg(Send<UnCliamMsg> cliamMsgSend);

    /**
     * 实时回调最近联系人未读消息列表
     *
     * @param response 返回的消息
     */
    void onResultHisContactListForJson(String response);

    /**
     * 返回历史聊天信息
     * @param response
     */
    void onHistoryMsgListForJson(String response);

    /**
     * 返回历史聊天信息
     * @param messageInfoList
     */
    void onHistoryMsgListList(List<MessageInfo> messageInfoList);
}
