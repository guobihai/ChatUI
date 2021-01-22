package trf.smt.com.netlibrary.bean;


import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.MsgType;

/**
 * Created by gbh on 2018/4/2  15:52.
 *
 * @describe
 */

public class UpInfo {
    private String id;//自增长
    private String fromUser;//发送人 当前微信人
    private String toUser;//接收人
    private String content;//内容
    private String createTime;//发送时间
    private String headUrl;//头像
    private String nickName;//昵称
    private int isSend;//0 接收，1发送 类型
    private int type;//消息类型 1 txt 3 img  ,
    private int isUpload;//

    private boolean isGoup;//是否群发
    private boolean isWx;//是否微信用户


    public UpInfo(String toUserId, String content, String createTime, int isSend, int type) {
        this.toUser = toUserId;
        this.content = content;
        this.createTime = createTime;
        this.isSend = isSend;
        this.type = type;
        this.isGoup = false;
    }

    public UpInfo(long id, String fromUserId, String toUserId, String content, String createTime, int isSend, int type) {
        this.id = String.valueOf(id);
        this.fromUser = fromUserId;
        this.toUser = toUserId;
        this.content = content;
        this.createTime = createTime;
        this.isSend = isSend;
        this.type = type;
        this.isGoup = false;
    }

    public UpInfo(String fromUserId, String toUserId, String content, String createTime, int isSend, int type) {
        this.fromUser = fromUserId;
        this.toUser = toUserId;
        this.content = content;
        this.createTime = createTime;
        this.isSend = isSend;
        this.type = type;
        this.isGoup = false;
    }


    public String getContent() {
        return content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public int getIsSend() {
        return isSend;
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }

    public String getId() {
        return (String) this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    public boolean isGoup() {
        return isGoup;
    }

    public void setGoup(boolean goup) {
        isGoup = goup;
    }

    public boolean isWx() {
        return isWx;
    }

    public void setWx(boolean wx) {
        isWx = wx;
    }

    public static UpInfo messageInfoToUpInfo(MessageInfo messageInfo) {
        if (null == messageInfo) throw new IllegalArgumentException("messageinfo is null ");

        String content = "";
        switch (messageInfo.getMsgType()) {
            case MsgType.SEND_TYPE_TXT:
                content = messageInfo.getContent();
                break;
            case MsgType.SEND_TYPE_IMG:
            case MsgType.SEND_TYPE_EMOJI:
                content = messageInfo.getImageUrl();
                break;
            case MsgType.SEND_TYPE_VOICE:
            case MsgType.SEND_TYPE_VIDEO:
                content = messageInfo.getFilepath();
                break;
        }
        UpInfo upInfo = new UpInfo(messageInfo.getId(), messageInfo.getFromUser(),
                messageInfo.getToUser(), content, messageInfo.getTime(), messageInfo.getIsSend(), messageInfo.getMsgType());

        upInfo.setNickName(messageInfo.getNickName());
        upInfo.setWx(messageInfo.getIsWx());
        upInfo.setHeadUrl(messageInfo.getHeader());
        return upInfo;
    }
}
