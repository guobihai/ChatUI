package com.smart.chatui.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.smart.chatui.base.MyApplicationLike;
import com.smart.chatui.ui.views.LoadingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.bean.UnCliamMsg;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.interfaces.ResultCallBack;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.LogUtils;
import trf.smt.com.netlibrary.utils.PreferenceUtils;

public class BaseActivity extends AppCompatActivity implements ResultCallBack {
    public static String BASEURL = "http://svr02.csosm.com:9001";
    //    public static final String BASEURL = "http://sos.csosm.com:9001";
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;

    private OnBooleanListener onPermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> pers = new ArrayList<>();
        pers.add(Manifest.permission.READ_PHONE_STATE);
        pers.add(Manifest.permission.ACCESS_NETWORK_STATE);
        pers.add(Manifest.permission.CAMERA);
        pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        pers.add(Manifest.permission.RECORD_AUDIO);
        pers.add(Manifest.permission.ACCESS_FINE_LOCATION);

        String str = PreferenceUtils.getString(this, "permissions", null);
        if (!TextUtils.isEmpty(str)) {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> permissions = JsonUtils.deserialize(str, type);
            for (String permission : permissions) {
                for (int i = 0; i < pers.size(); i++) {
                    if (pers.get(i).equals(permission)) {
                        pers.remove(i);
                    }
                }
            }
        }
        if (pers.size() > 0) {
            String[] arrays = new String[pers.size()];
            pers.toArray(arrays);
            ActivityCompat.requestPermissions(this, arrays,
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    List<String> data = new ArrayList<>();
                    String str = PreferenceUtils.getString(this, "permissions", null);
                    if (!TextUtils.isEmpty(str)) {
                        List<String> listsP = JsonUtils.deserialize(str, List.class);
                        data.addAll(listsP);
                    }
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            data.add(permissions[i]);
                        }
                    }
                    PreferenceUtils.putString(this, "permissions", JsonUtils.serialize(data));
                } else {
                    // functionality that depends on this permission.
//                    new AlertDialog.Builder(this).setTitle("警告")
//                            .setMessage("拒绝授权，会导致部分功能使用不了").setNegativeButton("确认", null).show();
                }
                return;
            }
        }
    }

    /**
     * 权限请求（这个方法也有坑，如果检测的时候权限是拒绝的，那就会回调false，但如果检测的时候权限是询问，
     * 然后让用户授权权限的时候选了拒绝，那么下面这个代码返回的依旧是true，所以需要搭配另外一个方法用以实时检测权限是否通过）
     *
     * @param permission        Manifest.permission.CAMERA
     * @param onBooleanListener 权限请求结果回调，true-通过  false-拒绝
     */
    public void permissionRequests(Activity activity, String permission, OnBooleanListener onBooleanListener) {
        onPermissionListener = onBooleanListener;
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS)) {
                //权限通过
                onPermissionListener.onClick(true);
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        1);
            }
        } else {
            //权限已有
            if (onPermissionListener != null) {
                onPermissionListener.onClick(true);
            }
        }
    }

    /**
     * 当此权限是必要的，而用户没有允许，允许弹出自定义的设置窗口
     *
     * @param activity
     * @param onBooleanListener
     * @param permissions
     */
    public void permissionCheck(Activity activity, OnBooleanListener onBooleanListener, String... permissions) {
        onPermissionListener = onBooleanListener;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                //没有权限，申请一下
                onPermissionListener.onClick(false);
                break;
            } else {
                if (permission.equals(permissions[permissions.length - 1])) {
                    onPermissionListener.onClick(true);
                }
            }
        }
    }

    public void showErrorDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(BaseActivity.this).setTitle("温馨提示")
                        .setMessage(message)
                        .setNegativeButton("确认", null).show();
            }
        });

    }

    public LoadingDialog progressDialog;

    public LoadingDialog showProgressDialog() {
        progressDialog = new LoadingDialog(this, "加载中...");
        progressDialog.show();
        return progressDialog;
    }

    public LoadingDialog showProgressDialog(String message) {
        progressDialog = new LoadingDialog(this, message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
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
        boolean isVerb = PreferenceUtils.getBoolean(this, "verb_msg", false);
        if (!isVerb) return;
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    /**
     * 复制文本
     *
     * @param msg
     */
    public void copy(String msg) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(msg);
        Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
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

    /**
     * 回调错误或者正确
     * Created by 她叫我小渝 on 2016/11/1.
     */

    public interface OnBooleanListener {
        void onClick(boolean bln);
    }

}
