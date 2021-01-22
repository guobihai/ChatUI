package trf.smt.com.netlibrary.interfaces;

import java.util.List;

import trf.smt.com.netlibrary.bean.UpInfo;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.enity.MessageInfo;

/**
 * Created by gbh on 2018/4/16  11:17.
 *
 * @describe
 */

public interface ActionInterface {
    /**
     * 设备登录
     *
     * @param userId 当前登录的ID
     */
    boolean login(String userId);

    /**
     * 设备登出
     */
//    boolean logout();

    /**
     * 发送文本信息到聊天室
     *
     * @param fromUserId
     * @param content
     */
    boolean sendChatRoomTxt(String fromUserId, String content);

    /**
     * 发送图片信息到聊天室
     *
     * @param fromUserId
     * @param imgeUrl
     */
    boolean sendChatRoomImg(String fromUserId, String imgeUrl);

    /**
     * 发送文本
     */
    boolean sendTxt(String fromUserId, String toUserId, String content);

    /**
     * 发送图片
     *
     * @param imageUrl 图片路径
     */
    boolean sendImg(String fromUserId, String toUserId, String imageUrl);

    /**
     * 发送语音
     *
     * @param imageUrl 图片路径
     */
    boolean sendVoice(String fromUserId, String toUserId, String imageUrl);

    /**
     * 群发发送文本
     */
    boolean sendGroupTxt(String fromUserId, String toUserId, String content);

    /**
     * 群发发送图片
     *
     * @param imageUrl 图片路径
     */
    boolean sendGroupImg(String fromUserId, String toUserId, String imageUrl);

    /**
     * 设置IP地址
     *
     * @param ip
     */
    void setIp(String ip);

    /**
     * 设置端口
     *
     * @param port
     */
    void setPort(String port);

    /**
     * 设置延时时间 秒为单位
     *
     * @param delayTime
     */
    void setHeartDelayTime(String delayTime);

    /**
     * 设置接收设备ID号
     *
     * @param devicesId
     */
    void setToDevicesId(String devicesId);

    /**
     * 设置http 服务端URL
     *
     * @param url
     */
    void setBaseUrl(String url);

    /**
     * 导购认领
     *
     * @param username
     * @param loginUserId
     * @return
     */
    boolean sendClaim(String username, String loginUserId);

    /**
     * 导购取消认领
     *
     * @param username
     * @param loginUserId
     * @return
     */
    boolean sendUnClaim(String username, String loginUserId);

    /**
     * 同步未领取微信好友信息
     *
     * @param loginUserId
     * @return
     */
    boolean sendLoadUnClaimMsg(String loginUserId);

    /**
     * 发送朋友圈信息
     *
     * @param content
     * @param fileUrl
     * @return
     */
    boolean sendFriendsMsg(String content, String fileUrl);

    /**
     * 获取最近消息列表
     *
     * @return
     */
    List<HisContact> loadNearMessageInfo();

    /**
     * 获取最近消息列表，返回json 数据格式
     *
     * @return
     */
    String loadNearMessageInfoToJson();

    /**
     * 获取聊天室总聊天记录
     *
     * @return 返回总记录
     */
    int getChartRoomMessageCount();

    /**
     * 获取聊天室历史消息
     *
     * @param curPage   当前页数
     * @param pageCount 每页总条数
     * @return 返回历史消息列表记录
     */
    List<MessageInfo> getHisChartRoomMessageInfo(int curPage, int pageCount);

    /**
     * 获取聊天室历史消息(h5专用)
     *
     * @param curPage   当前页数
     * @param pageCount 每页总条数
     * @return 返回历史消息列表记录
     */
    List<UpInfo> getHisChartRoomMessageInfoToUpInfo(int curPage, int pageCount);
    /**
     * 获取聊天室历史消息
     *
     * @param curPage   当前页数
     * @param pageCount 每页总条数
     * @return 返回历史消息列表记录
     */
    String getHisChartRoomMessageInfoToJson(int curPage, int pageCount);

}
