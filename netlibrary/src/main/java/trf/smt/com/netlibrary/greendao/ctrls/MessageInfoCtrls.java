package trf.smt.com.netlibrary.greendao.ctrls;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.bean.UpInfo;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.greendao.MessageInfoDao;
import trf.smt.com.netlibrary.utils.Constants;

/**
 * Created by gbh on 2018/5/14  19:44.
 *
 * @describe
 */

public class MessageInfoCtrls {
    /**
     * 获取总数量
     *
     * @param wxUserId
     * @param loginUserId
     * @return
     */
    public static int getMessageInfoCount(String wxUserId, String loginUserId) {
        int count = 0;
        //查询总数
        String sqlCount = "select count(*)as num from MESSAGE_INFO where WX_USER_ID ='" + wxUserId + "' " +
                " and LOGIN_USER_ID = '" + loginUserId + "'";
        Cursor cur = DbAppliaction.getDaoSession().getDatabase().rawQuery(sqlCount, null);
        while (cur.moveToNext()) {
            count = cur.getInt(cur.getColumnIndex("num"));
        }
        cur.close();
        return count;
    }


    /**
     * 分页获取消息记录
     *
     * @param curPage          当前页数
     * @param pageCount        每页总数
     * @param wxUserId         微信ID
     * @param loginUserId      当前登录用户ID
     * @param chartUserIconUrl 头像的URL
     * @return
     */
    public static List<MessageInfo> getListMessageInfoByPage(int curPage, int pageCount, String wxUserId, String loginUserId, String chartUserIconUrl) {
        List<MessageInfo> messageInfoList = new ArrayList<>();
        String sql = "select * from (select * from MESSAGE_INFO  where WX_USER_ID ='" + wxUserId + "' " +
                " and LOGIN_USER_ID = '" + loginUserId + "' order by  TIME  desc limit " + (
                curPage * pageCount) + "," + pageCount + " )  order by TIME asc";
        Cursor cursor = DbAppliaction.getDaoSession().getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MessageInfo info = new MessageInfo();
            info.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            int type = cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.Type.columnName));
            info.setContent(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Content.columnName)));
            info.setMsgType(cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.MsgType.columnName)));
            info.setNickName(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.NickName.columnName)));
            info.setWxUserId(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.WxUserId.columnName)));
            info.setImageUrl(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.ImageUrl.columnName)));
            info.setTime(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Time.columnName)));
            if (type == Constants.CHAT_ITEM_TYPE_LEFT)
                info.setHeader(chartUserIconUrl);
            info.setIsGoup(false);
            info.setLoginUserId(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.LoginUserId.columnName)));
            info.setFromUser(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.FromUser.columnName)));
            info.setToUser(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.ToUser.columnName)));
            info.setId(cursor.getLong(cursor.getColumnIndex(MessageInfoDao.Properties.Id.columnName)));
            info.setVoiceTime(cursor.getLong(cursor.getColumnIndex(MessageInfoDao.Properties.VoiceTime.columnName)));
            info.setFilepath(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Filepath.columnName)));
            info.setIsSend(cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.IsSend.columnName)));
            info.setType(type);
            messageInfoList.add(info);
        }
        cursor.close();
        return messageInfoList;
    }


    /**
     * 获取聊天室总消息数量
     *
     * @param chrtRoom
     * @return
     */
    public static int getChatRoomMessageInfoCount(String chrtRoom) {
        int count = 0;
        //查询总数
        String sqlCount = "select count(*)as num from MESSAGE_INFO where WX_USER_ID ='" + chrtRoom + "' ";
        Cursor cur = DbAppliaction.getDaoSession().getDatabase().rawQuery(sqlCount, null);
        while (cur.moveToNext()) {
            count = cur.getInt(cur.getColumnIndex("num"));
        }
        cur.close();
        return count;
    }

    /**
     * @param curPage
     * @param pageCount
     * @param chrtRoom
     * @return
     */
    public static List<MessageInfo> getChatRoomListMessageInfoByPage(int curPage, int pageCount, String chrtRoom) {
        List<MessageInfo> messageInfoList = new ArrayList<>();
        String sql = "select * from (select * from MESSAGE_INFO  where WX_USER_ID ='" + chrtRoom + "' " +
                " order by  TIME  desc limit " + (curPage * pageCount) + "," + pageCount + " )  order by TIME asc";
        Cursor cursor = DbAppliaction.getDaoSession().getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MessageInfo info = new MessageInfo();
            info.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            int type = cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.Type.columnName));
            info.setContent(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Content.columnName)));
            info.setMsgType(cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.MsgType.columnName)));
            info.setNickName(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.NickName.columnName)));
            info.setWxUserId(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.WxUserId.columnName)));
            info.setImageUrl(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.ImageUrl.columnName)));
            info.setTime(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Time.columnName)));
            info.setHeader(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Header.columnName)));
            info.setIsGoup(false);
            info.setLoginUserId(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.LoginUserId.columnName)));
            info.setFromUser(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.FromUser.columnName)));
            info.setToUser(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.ToUser.columnName)));
            info.setId(cursor.getLong(cursor.getColumnIndex(MessageInfoDao.Properties.Id.columnName)));
            info.setVoiceTime(cursor.getLong(cursor.getColumnIndex(MessageInfoDao.Properties.VoiceTime.columnName)));
            info.setFilepath(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Filepath.columnName)));
            info.setIsSend(cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.IsSend.columnName)));
            info.setIsWx(true);
            info.setType(type);
            messageInfoList.add(info);
        }
        cursor.close();
        return messageInfoList;
    }

    /**
     * @param curPage
     * @param pageCount
     * @param chrtRoom
     * @return
     */
    public static List<UpInfo> getChatRoomListUpInfoInfoByPage(int curPage, int pageCount, String chrtRoom) {
        List<UpInfo> messageInfoList = new ArrayList<>();
        String sql = "select * from (select * from MESSAGE_INFO  where WX_USER_ID ='" + chrtRoom + "' " +
                " order by  TIME  desc limit " + (curPage * pageCount) + "," + pageCount + " )  order by TIME asc";
        Cursor cursor = DbAppliaction.getDaoSession().getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MessageInfo info = new MessageInfo();
            info.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            int type = cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.Type.columnName));
            info.setContent(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Content.columnName)));
            info.setMsgType(cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.MsgType.columnName)));
            info.setNickName(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.NickName.columnName)));
            info.setWxUserId(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.WxUserId.columnName)));
            info.setImageUrl(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.ImageUrl.columnName)));
            info.setTime(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Time.columnName)));
            info.setHeader(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Header.columnName)));
            info.setIsGoup(false);
            info.setLoginUserId(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.LoginUserId.columnName)));
            info.setFromUser(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.FromUser.columnName)));
            info.setToUser(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.ToUser.columnName)));
            info.setId(cursor.getLong(cursor.getColumnIndex(MessageInfoDao.Properties.Id.columnName)));
            info.setVoiceTime(cursor.getLong(cursor.getColumnIndex(MessageInfoDao.Properties.VoiceTime.columnName)));
            info.setFilepath(cursor.getString(cursor.getColumnIndex(MessageInfoDao.Properties.Filepath.columnName)));
            info.setIsSend(cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.IsSend.columnName)));
            info.setIsWx(cursor.getInt(cursor.getColumnIndex(MessageInfoDao.Properties.IsWx.columnName)) > 0 ? true : false);
            info.setType(type);
            UpInfo upInfo = UpInfo.messageInfoToUpInfo(info);
            messageInfoList.add(upInfo);
        }
        cursor.close();
        return messageInfoList;
    }
}
