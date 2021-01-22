//package com.smart.chatui.greendao.ctrls;
//
//import android.database.Cursor;
//
//import com.smart.chatui.base.MyApplicationLike;
//import com.smart.chatui.enity.HisContact;
//import com.smart.chatui.enity.MessageInfo;
//import com.smart.chatui.greendao.MessageInfoDao;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by gbh on 2018/5/12  09:53.
// *
// * @describe
// */
//
//public class HisContactCtrls {
//
//    /**
//     * 获取最近联系人
//     *
//     * @param loginUserId
//     * @return
//     */
//    public static List<HisContact> loadNearByContactInfo(String loginUserId) {
//        List<HisContact> list = new ArrayList<>();
//        String sql = "select A.NICK_NAME as nickName,A.CON_REMARK as conRemark,A.USER_NAME as userName, A.ICON_URL as iconUrl, B.MSG_TYPE as msgType, B.TIME as time, B.CONTENT as content from HIS_CONTACT\n" +
//                "  as A left join (select * from MESSAGE_INFO  group by WX_USER_ID  order by TIME desc) \n" +
//                " as B  ON A.USER_NAME = B.WX_USER_ID where A.LOGIN_USER_ID = '" + loginUserId + "' group by A.USER_NAME  order by B.TIME desc";
//        Cursor cursor = MyApplicationLike.getDaoSession().getDatabase().rawQuery(sql, null);
//        while (cursor.moveToNext()) {
//            HisContact hisContact = new HisContact();
//            hisContact.setLoginUserId(loginUserId);
//            hisContact.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
//            hisContact.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
//            hisContact.setConRemark(cursor.getString(cursor.getColumnIndex("conRemark")));
//            hisContact.setIconUrl(cursor.getString(cursor.getColumnIndex("iconUrl")));
//            hisContact.setType(cursor.getInt(cursor.getColumnIndex("msgType")));
//            hisContact.setCreateTime(cursor.getString(cursor.getColumnIndex("time")));
//            hisContact.setLastMsg(cursor.getString(cursor.getColumnIndex("content")));
//            list.add(hisContact);
//        }
//        cursor.close();
//        return list;
//    }
//
//    /**
//     * 获取聊天室最后的一条聊天记录
//     *
//     * @param loginUserId
//     * @return
//     */
//    public static HisContact loadNearByChartRoomInfo(String loginUserId) {
//        String roomSql = "select MSG_TYPE as msgType, TIME as time ,CONTENT as content  from MESSAGE_INFO  where WX_USER_ID = '@chartRoom' group by WX_USER_ID order by TIME desc";
//        Cursor cursor1 = MyApplicationLike.getDaoSession().getDatabase().rawQuery(roomSql, null);
//        HisContact hisContact = HisContact.getDefaultChartRoom(loginUserId);
//        while (cursor1.moveToNext()) {
//            hisContact.setLoginUserId(loginUserId);
//            hisContact.setType(cursor1.getInt(cursor1.getColumnIndex("msgType")));
//            hisContact.setCreateTime(cursor1.getString(cursor1.getColumnIndex("time")));
//            hisContact.setLastMsg(cursor1.getString(cursor1.getColumnIndex("content")));
//        }
//        cursor1.close();
//        return hisContact;
//    }
//
//    /**
//     * 获取未读消息总数
//     *
//     * @return
//     */
//    public static List<MessageInfo> loadUnReadMessageInfoCount() {
//        List<MessageInfo> list = new ArrayList<>();
//        String sql = "select count(*) as num,WX_USER_ID from MESSAGE_INFO where IS_READ = 0 group by WX_USER_ID ";
//        Cursor cursor = MyApplicationLike.getDaoSession().getDatabase().rawQuery(sql, null);
//        while (cursor.moveToNext()) {
//            MessageInfo info = new MessageInfo();
//            info.setType(cursor.getInt(cursor.getColumnIndex("num")));
//            info.setWxUserId(cursor.getString(cursor.getColumnIndex("WX_USER_ID")));
//            list.add(info);
//        }
//        cursor.close();
//        return list;
//    }
//
//    /**
//     * 修改所有的聊天记录状态
//     * @param wxUserName
//     */
//    public static void updateAllUnMsgState(String wxUserName) {
//        List<MessageInfo> list = MyApplicationLike.getDaoSession().getMessageInfoDao()
//                .queryBuilder()
//                .where(MessageInfoDao.Properties.IsRead.eq(false))
//                .where(MessageInfoDao.Properties.WxUserId.eq(wxUserName))
//                .build().list();
//        for (MessageInfo info : list) {
//            info.setIsRead(true);
//        }
//        MyApplicationLike.getDaoSession().getMessageInfoDao().updateInTx(list);
//    }
//
//    /**
//     * 获取所有私聊人员未读消息
//     * @return
//     */
//    public static int getUnReadCountByFriends() {
//        int count = 0;
//        String sql = "select count(*) as num from MESSAGE_INFO where IS_READ = 0 and WX_USER_ID !='@chartRoom'";
//        Cursor cursor = MyApplicationLike.getDaoSession().getDatabase().rawQuery(sql, null);
//        while (cursor.moveToNext()) {
//            count = cursor.getInt(cursor.getColumnIndex("num"));
//        }
//        cursor.close();
//        return count;
//    }
//}
