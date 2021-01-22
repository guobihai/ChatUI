package trf.smt.com.netlibrary.greendao.ctrls;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import trf.smt.com.netlibrary.base.DbAppliaction;
import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.greendao.HisContactDao;
import trf.smt.com.netlibrary.utils.MsgType;
import trf.smt.com.netlibrary.utils.TimeTool;

/**
 * Created by gbh on 2018/5/12  09:53.
 *
 * @describe 历史消息db处理
 */

public class HisContactCtrls {


    /**
     * 判断该微信是否存在最近消息历史记录中
     *
     * @param userId     当前登录用户
     * @param wxUserName 微信username
     * @return
     */
    public static List<HisContact> getHisContactListByUserId(String userId, String wxUserName) {
        List<HisContact> list = DbAppliaction.getDaoSession().getHisContactDao().queryBuilder()
                .where(HisContactDao.Properties.UserName.eq(wxUserName))
                .where(HisContactDao.Properties.LoginUserId.eq(userId))
                .build().list();
        return list;
    }

    /**
     * 新增或修改最近消息记录
     *
     * @param hisContact 数据体
     * @return
     */
    public static void updateHisContact(HisContact hisContact) {
        if (null == hisContact) return;
//        hisContact.setCreateTime(HisContact.getTime());
        DbAppliaction.getDaoSession().getHisContactDao().update(hisContact);
    }

    /**
     * 添加一条最近消息记录
     *
     * @param hisContact
     * @return
     */
    public static long insertHisContactData(HisContact hisContact) {
        if (null == hisContact) return -1;
        return DbAppliaction.getDaoSession().getHisContactDao()
                .insertOrReplace(hisContact);
    }

    /**
     * 获取最近联系人
     *
     * @param loginUserId
     * @return
     */
    public static List<HisContact> loadNearByContactInfo(String loginUserId) {
        List<HisContact> list = new ArrayList<>();
//        String sql = "select A.NICK_NAME as nickName,A.CON_REMARK as conRemark,A.USER_NAME as userName, A.ICON_URL as iconUrl, B.MSG_TYPE as msgType, B.TIME as time, B.CONTENT as content from HIS_CONTACT\n" +
//                "  as A left join (select * from MESSAGE_INFO  group by WX_USER_ID  order by TIME desc) \n" +
//                " as B  ON A.USER_NAME = B.WX_USER_ID where A.LOGIN_USER_ID = '" + loginUserId + "' group by A.USER_NAME  order by " +
//                " A.CREATE_TIME desc";
        String sql = "select * from  HIS_CONTACT where LOGIN_USER_ID = '" + loginUserId + "' order by CREATE_TIME desc";
        Cursor cursor = DbAppliaction.getDaoSession().getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            HisContact hisContact = new HisContact();
            hisContact.setLoginUserId(loginUserId);
            hisContact.setUserName(cursor.getString(cursor.getColumnIndex("USER_NAME")));
            hisContact.setNickName(cursor.getString(cursor.getColumnIndex("NICK_NAME")));
            hisContact.setConRemark(cursor.getString(cursor.getColumnIndex("CON_REMARK")));
            hisContact.setIconUrl(cursor.getString(cursor.getColumnIndex("ICON_URL")));
            hisContact.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
            hisContact.setMemberId(cursor.getInt(cursor.getColumnIndex("MEMBER_ID")));
            String time = cursor.getString(cursor.getColumnIndex("CREATE_TIME"));
            hisContact.setCreateTime("" + TimeTool.timeTolongTime(time, "yyyy/MM/dd HH:mm:ss"));
            switch (hisContact.getType()) {
                case MsgType.SEND_TYPE_TXT:
                    hisContact.setLastMsg(cursor.getString(cursor.getColumnIndex("LAST_MSG")));
                    break;
                case MsgType.SEND_TYPE_IMG:
                    hisContact.setLastMsg("[图片]");
                    break;
                case MsgType.SEND_TYPE_EMOJI:
                    hisContact.setLastMsg("[图片表情]");
                    break;
                case MsgType.SEND_TYPE_FILE:
                    hisContact.setLastMsg("[文件]");
                    break;
                case MsgType.SEND_TYPE_VOICE:
                    hisContact.setLastMsg("[语音]");
                    break;
                case MsgType.SEND_TYPE_VIDEO:
                    hisContact.setLastMsg("[视频]");
                    break;
            }
            hisContact.setMsgCount(cursor.getInt(cursor.getColumnIndex("MSG_COUNT")));
            list.add(hisContact);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取聊天室最后的一条聊天记录
     *
     * @param loginUserId
     * @return
     */
    public static HisContact loadNearByChartRoomInfo(String loginUserId) {
        String roomSql = "select MSG_TYPE as msgType, TIME as time ,CONTENT as content  from MESSAGE_INFO  where WX_USER_ID = '@chartRoom' group by WX_USER_ID order by TIME desc";
        Cursor cursor1 = DbAppliaction.getDaoSession().getDatabase().rawQuery(roomSql, null);
        HisContact hisContact = HisContact.getDefaultChartRoom(loginUserId);
        while (cursor1.moveToNext()) {
            hisContact.setLoginUserId(loginUserId);
            hisContact.setType(cursor1.getInt(cursor1.getColumnIndex("msgType")));
            hisContact.setCreateTime(cursor1.getString(cursor1.getColumnIndex("time")));
            hisContact.setLastMsg(cursor1.getString(cursor1.getColumnIndex("content")));
        }
        cursor1.close();
        return hisContact;
    }

    /**
     * 获取未读消息总数
     *
     * @return
     */
    public static List<MessageInfo> loadUnReadMessageInfoCount() {
        List<MessageInfo> list = new ArrayList<>();
        String sql = "select count(*) as num,WX_USER_ID from MESSAGE_INFO where IS_READ = 0 group by WX_USER_ID ";
        Cursor cursor = DbAppliaction.getDaoSession().getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MessageInfo info = new MessageInfo();
            info.setType(cursor.getInt(cursor.getColumnIndex("num")));
            info.setWxUserId(cursor.getString(cursor.getColumnIndex("WX_USER_ID")));
            list.add(info);
        }
        cursor.close();
        return list;
    }

    /**
     * 修改所有的聊天记录状态
     *
     * @param wxUserName
     */
    public static void updateAllUnMsgState(String wxUserName,String userId) {
        List<HisContact> list = DbAppliaction.getDaoSession().getHisContactDao()
                .queryBuilder()
                .where(HisContactDao.Properties.UserName.eq(wxUserName))
                .where(HisContactDao.Properties.LoginUserId.eq(userId))
                .build().list();
        for (HisContact info : list) {
            info.setMsgCount(0);
        }
        DbAppliaction.getDaoSession().getHisContactDao().updateInTx(list);
    }

    /**
     * 获取所有私聊人员未读消息
     *
     * @return
     */
    public static int getUnReadCountByFriends() {
        int count = 0;
        String sql = "select count(*) as num from MESSAGE_INFO where IS_READ = 0 and WX_USER_ID !='@chartRoom'";
        Cursor cursor = DbAppliaction.getDaoSession().getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("num"));
        }
        cursor.close();
        return count;
    }

}
