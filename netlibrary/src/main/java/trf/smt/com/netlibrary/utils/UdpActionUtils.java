package trf.smt.com.netlibrary.utils;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.FriendsInfo;
import trf.smt.com.netlibrary.bean.Send;

import static trf.smt.com.netlibrary.enums.TagEnum.CLAIM;
import static trf.smt.com.netlibrary.enums.TagEnum.ERROR;
import static trf.smt.com.netlibrary.enums.TagEnum.FRIENDS_VERB;
import static trf.smt.com.netlibrary.enums.TagEnum.HEART;
import static trf.smt.com.netlibrary.enums.TagEnum.LOGIN;
import static trf.smt.com.netlibrary.enums.TagEnum.LOGOUT;
import static trf.smt.com.netlibrary.enums.TagEnum.UNCLAIM;
import static trf.smt.com.netlibrary.enums.TagEnum.UNCLAIM_MSG;
import static trf.smt.com.netlibrary.enums.TagEnum.WXSTATE;

/**
 * Created by gbh on 2018/4/14  14:07.
 *
 * @describe
 */

public final class UdpActionUtils {
    private UDPClient mUDPClient;
    private boolean isRun;//登录
    private boolean isHeartRun;//心跳包
    private boolean wxOnLine = true;//微信上线
    private boolean wxOffLine = true;//微信下线
    private String fromDeviceId;
    private String toDeviceId;//大V手机
    private int heartDelayTime = 120;
    private ExecutorService exec;//缓存线程池

    public UdpActionUtils(ExecutorService exec, UDPClient UDPClient, String fromDeviceId, String toDeviceId) {
        this.mUDPClient = UDPClient;
        this.exec = exec;
        this.fromDeviceId = fromDeviceId;
        this.toDeviceId = toDeviceId;
        isHeartRun = true;
    }

    public void setHeartDelayTime(int heartDelayTime) {
        this.heartDelayTime = heartDelayTime;
    }

    public void setRun(boolean run) {
        isRun = run;
    }


    public void setFromDeviceId(String fromDeviceId) {
        this.fromDeviceId = fromDeviceId;
    }

    /**
     * 设备登陆包
     */
    public boolean sendLoginData(final String ids) {
        isRun = true;
        exec.execute(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    //心跳包
                    Send<AckValue> send = new Send<>(LOGIN.getTag(), new AckValue(LOGIN.getTag(), ids), fromDeviceId, toDeviceId);
                    LogUtils.sysout("正在请求登录中...");
                    try {
                        mUDPClient.send(send.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        return true;

    }


    /**
     * 微信登陆包
     */
    public void sendWxLoginData() {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                //心跳包
                if (wxOnLine) {
                    Send<AckValue> send = new Send<>(WXSTATE.getTag(), new AckValue("textuser", "online"), fromDeviceId, toDeviceId);
                    try {
                        mUDPClient.send(send.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    wxOnLine = false;
                    wxOffLine = true;
                }

            }
        });
    }

    /**
     * 登出
     */
    public void sendLogoutData() {
        Send<AckValue> send = new Send<>(LOGOUT.getTag(), new AckValue(LOGOUT.getTag(), "0"), fromDeviceId, toDeviceId);
        send(send.toString());
    }


    /**
     * 心跳包
     */
    public void sendHeartData() {
        isHeartRun = true;
        exec.execute(new Runnable() {
            @Override
            public void run() {
                int delay = heartDelayTime * 1000;
                try {
                    if (null != timer)
                        timer.cancel();
                    if (null != timerTask)
                        timerTask.cancel();
                    timerTask = null;
                    start();
                    //延时1s，每隔500毫秒执行一次run方法
                    timer.schedule(timerTask, 1000, delay);
                } catch (Exception e) {
                    timer.cancel();
                    timer = null;
                    if (null != timerTask)
                        timerTask.cancel();
                    timerTask = null;
                    timer = new Timer();
                    start();
                    timer.schedule(timerTask, 1000, delay);
                }
            }
        });
    }

    Timer timer = new Timer();
    TimerTask timerTask = null;

    private void start() {
        if (null != timerTask) return;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //心跳包
                sendHeart();

            }
        };
    }

    public boolean sendHeart() {
        Send<AckValue> send = new Send<>(HEART.getTag(), new AckValue(HEART.getTag(), "0"), fromDeviceId, toDeviceId);
        LogUtils.sysout("======发送定时心跳包=======" + send.toString());
        send(send.toString());
        return true;
    }


    /**
     * 发送错误信息
     */
    public void sendErrorMsg(final String errorMsg) {
        Send<AckValue> send = new Send<>(ERROR.getTag(), new AckValue(ERROR.getTag(), errorMsg), fromDeviceId, toDeviceId);
        send(send.toString());
    }


    /**
     * 发送消息
     *
     * @param txtMsg
     */
    public boolean send(final String txtMsg) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mUDPClient.send(txtMsg);
                    LogUtils.sysout("send msg:" + txtMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }


    /**
     * 导购认领微信
     *
     * @param loginUserId 当前登录导购ID
     * @param username    wx username
     * @return
     */
    public boolean sendClaim(String loginUserId, String username) {
        Send<AckValue> send = new Send<>(CLAIM.getTag(), new AckValue(loginUserId, username), fromDeviceId, toDeviceId);
        return send(send.toString());
    }


    /**
     * 导购取消认领微信
     *
     * @param loginUserId 当前登录导购ID
     * @param username    wx username
     * @return
     */
    public boolean sendUnClaim(String loginUserId, String username) {
        Send<AckValue> send = new Send<>(UNCLAIM.getTag(), new AckValue(loginUserId, username), fromDeviceId, toDeviceId);
        return send(send.toString());
    }

    /**
     * 同步未领取微信好友信息
     *
     * @param devicesId
     * @param loginUserId
     * @return
     */
    public boolean sendLoadUnClaimMsg(String devicesId, String loginUserId) {
        Send<AckValue> send = new Send<>(UNCLAIM_MSG.getTag(), new AckValue(devicesId, loginUserId), fromDeviceId, toDeviceId);
        return send(send.toString());
    }


    /**
     * 发送朋友圈信息
     *
     * @return
     */
    public boolean sendFriendsMsg(String content, String fileUrl) {
        Send<FriendsInfo> send = new Send<>(FRIENDS_VERB.getTag(),
                new FriendsInfo(content, fileUrl), fromDeviceId, toDeviceId);
        return send(send.toString());
    }

    /**
     * 关闭通信
     */
    public void close() {
        if (exec.isShutdown())
            exec.shutdownNow();
        isRun = false;
        isHeartRun = false;
        if (null != timer)
            timer.cancel();
        timer = null;
        if (null != timerTask)
            timerTask.cancel();
        timerTask = null;
        mUDPClient.close();
    }

}
