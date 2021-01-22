package com.smart.chatui.enity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gbh on 2018/5/24  11:11.
 *
 * @describe 代办信息
 */

public class VerbInfo {
    public static final int ACTION_MENBER = 1;
    public static final int ACTION_BIRTHDAY = 2;
    public static final int ACTION_90 = 3;
    public static final int ACTION_VERB = 4;
    public static final int ACTION_HOLIDAY = 5;
    public static final int ACTION_SHLEEP = 6;

    private String name;
    private int msgCount;
    private int type;

    public VerbInfo(String name, int msgCount, int type) {
        this.name = name;
        this.msgCount = msgCount;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 功能
     *
     * @return
     */
    public static List<VerbInfo> initListVerbInfo() {
        List<VerbInfo> list = new ArrayList<>();
        list.add(new VerbInfo("会员待回复", 22, ACTION_MENBER));
        list.add(new VerbInfo("生日待跟进", getRomdom(), ACTION_BIRTHDAY));
        list.add(new VerbInfo("90待跟进", getRomdom(), ACTION_90));
        list.add(new VerbInfo("回访待跟进", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("节日待祝福", getRomdom(), ACTION_HOLIDAY));
        list.add(new VerbInfo("沉睡待激活", getRomdom(), ACTION_SHLEEP));
        return list;
    }

    /**
     * 生日跟进
     *
     * @return
     */
    public static List<VerbInfo> initListBirthdayInfo() {
        List<VerbInfo> list = new ArrayList<>();
        list.add(new VerbInfo("今天过生日", getRomdom(), ACTION_MENBER));
        list.add(new VerbInfo("本周过生日", getRomdom(), ACTION_BIRTHDAY));
        list.add(new VerbInfo("本月过生日", getRomdom(), ACTION_90));
        list.add(new VerbInfo("下月过生日", getRomdom(), ACTION_VERB));
        return list;
    }

    /**
     * 90数据
     *
     * @return
     */
    public static List<VerbInfo> initList90dayInfo() {
        List<VerbInfo> list = new ArrayList<>();
        list.add(new VerbInfo("销售1小时后跟进", getRomdom(), ACTION_MENBER));
        list.add(new VerbInfo("销售一天后跟进", getRomdom(), ACTION_BIRTHDAY));
        list.add(new VerbInfo("销售三天后跟进", getRomdom(), ACTION_90));
        list.add(new VerbInfo("销售七天后跟进", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("销售15天后跟进", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("销售30天后跟进", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("销售60天后跟进", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("销售90天后跟进", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("销售90天后后跟进", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("所有应跟进", getRomdom(), ACTION_VERB));
        return list;
    }


    /**
     * 回访
     *
     * @return
     */
    public static List<VerbInfo> initListVerbUpInfo() {
        List<VerbInfo> list = new ArrayList<>();
        list.add(new VerbInfo("售后回访", getRomdom(), ACTION_MENBER));
        list.add(new VerbInfo("活动通知", getRomdom(), ACTION_BIRTHDAY));
        list.add(new VerbInfo("服务邀约", getRomdom(), ACTION_90));
        list.add(new VerbInfo("感恩回访", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("满意度调查", getRomdom(), ACTION_VERB));
        list.add(new VerbInfo("其它回访", getRomdom(), ACTION_VERB));
        return list;
    }


    private static int getRomdom() {
        Random random = new Random();
        return random.nextInt(100);
    }
}
