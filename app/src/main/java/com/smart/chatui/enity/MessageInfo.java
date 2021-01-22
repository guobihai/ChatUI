//package com.smart.chatui.enity;
//
//import android.text.TextUtils;
//
//import com.smart.chatui.util.Constants;
//import com.smart.chatui.util.MediaUtils;
//import com.smart.chatui.util.MsgType;
//
//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Generated;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.Keep;
//
//import trf.smt.com.netlibrary.bean.UpInfo;
//
//@Entity
//public class MessageInfo implements Cloneable {
//    @Id(autoincrement = true)
//    private Long Id;
//    @Keep
//    private int type;//消息发送/接收类型
//    @Keep
//    private String content;//内容
//    @Keep
//    private String filepath;//文件路径
//    @Keep
//    private int sendState;//发送状态
//    @Keep
//    private String time;//消息时间
//    @Keep
//    private String header;//头像URL
//    @Keep
//    private String imageUrl;//图片路径
//    @Keep
//    private long voiceTime;//音频时间
//    @Keep
//    private String msgId;//消息id
//    @Keep
//    private int msgType;//消息类型
//
//    @Keep
//    private String loginUserId;//登录ID
//    @Keep
//    private boolean isGoup;//是否群发
//    @Keep
//    private String fromUser;//发送人 当前微信人
//    @Keep
//    private String toUser;//接收人
//    @Keep
//    private int isSend;//0 接收，1发送 类型
//    @Keep
//    private boolean isRead;//是否已读
//    @Keep
//    private String wxUserId;//微信用户id
//    @Keep
//    private String nickName;//昵称
//
//    //是否是微信用户
//    @Keep
//    private boolean isWx;
//
//
//    @Generated(hash = 469586917)
//    public MessageInfo(Long Id, int type, String content, String filepath, int sendState, String time, String header, String imageUrl,
//            long voiceTime, String msgId, int msgType, String loginUserId, boolean isGoup, String fromUser, String toUser, int isSend,
//            boolean isRead, String wxUserId, String nickName, boolean isWx) {
//        this.Id = Id;
//        this.type = type;
//        this.content = content;
//        this.filepath = filepath;
//        this.sendState = sendState;
//        this.time = time;
//        this.header = header;
//        this.imageUrl = imageUrl;
//        this.voiceTime = voiceTime;
//        this.msgId = msgId;
//        this.msgType = msgType;
//        this.loginUserId = loginUserId;
//        this.isGoup = isGoup;
//        this.fromUser = fromUser;
//        this.toUser = toUser;
//        this.isSend = isSend;
//        this.isRead = isRead;
//        this.wxUserId = wxUserId;
//        this.nickName = nickName;
//        this.isWx = isWx;
//    }
//
//    @Generated(hash = 1292770546)
//    public MessageInfo() {
//    }
//
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getFilepath() {
//        return filepath;
//    }
//
//    public void setFilepath(String filepath) {
//        this.filepath = filepath;
//    }
//
//    public int getSendState() {
//        return sendState;
//    }
//
//    public void setSendState(int sendState) {
//        this.sendState = sendState;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getHeader() {
//        return header;
//    }
//
//    public void setHeader(String header) {
//        this.header = header;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public long getVoiceTime() {
//        return voiceTime;
//    }
//
//    public void setVoiceTime(long voiceTime) {
//        this.voiceTime = voiceTime;
//    }
//
//    public String getMsgId() {
//        return msgId;
//    }
//
//    public void setMsgId(String msgId) {
//        this.msgId = msgId;
//    }
//
//    @Override
//    public String toString() {
//        return "MessageInfo{" +
//                "type=" + type +
//                ", content='" + content + '\'' +
//                ", filepath='" + filepath + '\'' +
//                ", sendState=" + sendState +
//                ", time='" + time + '\'' +
//                ", header='" + header + '\'' +
//                ", imageUrl='" + imageUrl + '\'' +
//                ", voiceTime=" + voiceTime +
//                ", msgId='" + msgId + '\'' +
//                '}';
//    }
//
//    public Long getId() {
//        return this.Id;
//    }
//
//    public void setId(Long Id) {
//        this.Id = Id;
//    }
//
//    public int getMsgType() {
//        return this.msgType;
//    }
//
//    public void setMsgType(int msgType) {
//        this.msgType = msgType;
//    }
//
//
//    /**
//     * 接收消息转换
//     *
//     * @param info
//     * @param loginUserId
//     * @return
//     */
//    public static MessageInfo upInfoToMessageInfo(UpInfo info, String loginUserId, boolean isRead) {
//        MessageInfo messageInfo = new MessageInfo();
//        messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
//        messageInfo.setLoginUserId(loginUserId);
//        messageInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT);
//        messageInfo.setIsGoup(info.isGoup());
//        messageInfo.setFromUser(info.getFromUser());
//        messageInfo.setToUser(info.getToUser());
//        messageInfo.setTime(info.getCreateTime());
//        messageInfo.setIsSend(info.getIsSend());//接收
//        messageInfo.setMsgType(info.getType());
//        messageInfo.setIsRead(isRead);
//        messageInfo.setWxUserId(info.getFromUser());
//        switch (info.getType()) {
//            case MsgType.SEND_TYPE_TXT:
//                messageInfo.setContent(info.getContent());
//                break;
//            case MsgType.SEND_TYPE_IMG:
//            case MsgType.SEND_TYPE_EMOJI:
//                messageInfo.setImageUrl(info.getContent());
//                break;
//            case MsgType.SEND_TYPE_VOICE:
//                messageInfo.setFilepath(info.getContent());
//                //音频长度
//                messageInfo.setVoiceTime(MediaUtils.getMediaDuration(info.getContent()));
//                break;
//            case MsgType.SEND_TYPE_VIDEO:
//                break;
//        }
//
//        return messageInfo;
//    }
//
//    /**
//     * 接收消息转换
//     *
//     * @param info
//     * @param loginUserId
//     * @return
//     */
//    public static MessageInfo upInfoToMessageInfoToChartRoom(UpInfo info, String loginUserId, boolean isRead) {
//        MessageInfo messageInfo = new MessageInfo();
//        messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
//        messageInfo.setLoginUserId(loginUserId);
//        messageInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT);
//        messageInfo.setIsGoup(info.isGoup());
//        messageInfo.setFromUser(info.getFromUser());
//        messageInfo.setToUser(info.getToUser());
//        messageInfo.setTime(info.getCreateTime());
//        messageInfo.setIsSend(info.getIsSend());//接收
//        messageInfo.setMsgType(info.getType());
//        messageInfo.setIsRead(isRead);
//        messageInfo.setWxUserId(loginUserId);
//        switch (info.getType()) {
//            case MsgType.SEND_TYPE_TXT:
//                messageInfo.setContent(info.getContent());
//                break;
//            case MsgType.SEND_TYPE_IMG:
//            case MsgType.SEND_TYPE_EMOJI:
//                messageInfo.setImageUrl(info.getContent());
//                break;
//            case MsgType.SEND_TYPE_VOICE:
//                messageInfo.setFilepath(info.getContent());
//                //音频长度
//                messageInfo.setVoiceTime(MediaUtils.getMediaDuration(info.getContent()));
//                break;
//            case MsgType.SEND_TYPE_VIDEO:
//                break;
//        }
//
//        return messageInfo;
//    }
//
//
////    /**
////     * 发送消息
////     *
////     * @param messageInfo
////     * @param person
////     * @param loginUserId
////     * @return
////     */
////    public static MessageInfo sendMessageInfo(MessageInfo messageInfo, Person person, String loginUserId) {
////        messageInfo.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
////        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
////        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
////        messageInfo.setFromUser(loginUserId);
////        messageInfo.setToUser(person.getUserName());
////        messageInfo.setIsRead(true);
////        messageInfo.setTime(String.valueOf(System.currentTimeMillis()));
////        messageInfo.setIsSend(1);
////        messageInfo.setIsGoup(false);
////        messageInfo.setLoginUserId(loginUserId);
////        messageInfo.setWxUserId(person.getUserName());
////        return messageInfo;
////    }
//
//
//    /**
//     * 发送消息到聊天室
//     *
//     * @param messageInfo
//     * @param loginUserId
//     * @return
//     */
//    public static MessageInfo sendMessageInfoToChartRoom(MessageInfo messageInfo, String fromUser, String toUser, String loginUserId) {
//        messageInfo.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
//        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
//        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
//        messageInfo.setFromUser(fromUser);
//        messageInfo.setToUser(toUser);
//        messageInfo.setIsRead(true);
//        messageInfo.setTime(String.valueOf(System.currentTimeMillis()));
//        messageInfo.setIsSend(1);
//        messageInfo.setIsGoup(false);
//        messageInfo.setLoginUserId(loginUserId);
//        messageInfo.setWxUserId(toUser);
//        return messageInfo;
//    }
//
//
//    public String getLoginUserId() {
//        return this.loginUserId;
//    }
//
//    public void setLoginUserId(String loginUserId) {
//        this.loginUserId = loginUserId;
//    }
//
//    public boolean getIsGoup() {
//        return this.isGoup;
//    }
//
//    public void setIsGoup(boolean isGoup) {
//        this.isGoup = isGoup;
//    }
//
//    public String getFromUser() {
//        return this.fromUser;
//    }
//
//    public void setFromUser(String fromUser) {
//        this.fromUser = fromUser;
//    }
//
//    public String getToUser() {
//        return this.toUser;
//    }
//
//    public void setToUser(String toUser) {
//        this.toUser = toUser;
//    }
//
//    public int getIsSend() {
//        return this.isSend;
//    }
//
//    public void setIsSend(int isSend) {
//        this.isSend = isSend;
//    }
//
//    public boolean getIsRead() {
//        return this.isRead;
//    }
//
//    public void setIsRead(boolean isRead) {
//        this.isRead = isRead;
//    }
//
//    public String getWxUserId() {
//        return this.wxUserId;
//    }
//
//    public void setWxUserId(String wxUserId) {
//        this.wxUserId = wxUserId;
//    }
//
//    public String getNickName() {
//        return TextUtils.isEmpty(this.nickName) ? "匿名" : this.nickName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }
//
//
//    @Override
//    public MessageInfo clone() throws CloneNotSupportedException {
//        return (MessageInfo) super.clone();
//    }
//
//    public boolean getIsWx() {
//        return this.isWx;
//    }
//
//    public void setIsWx(boolean isWx) {
//        this.isWx = isWx;
//    }
//}
